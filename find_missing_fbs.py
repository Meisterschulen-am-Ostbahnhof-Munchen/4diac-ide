#!/usr/bin/env python3
"""
Find missing FBType (F_*.fbt) files for Java StandardFunctions.

This script compares static methods defined in StandardFunctions.java
with F_*.fbt files in the typelibrary and reports gaps.

It understands that generic/overloaded Java functions (like TO_*, TRUNC)
often have specific FBT variants (F_INT_TO_REAL, F_TRUNC) instead of
generic ones.
"""

import argparse
import re
import sys
from collections import defaultdict
from pathlib import Path


def parse_java_methods(java_path):
    """Extract all unique static method names from StandardFunctions.java."""
    content = Path(java_path).read_text(encoding='utf-8')
    pattern = re.compile(
        r'static\s+(?:<[^>]+>\s+)?(?:[\w.$]+\s+)+(\w+)\s*\(',
        re.MULTILINE
    )
    methods = set()
    for match in pattern.finditer(content):
        name = match.group(1)
        if name in ('return', 'throw', 'new', 'if', 'for', 'while', 'switch'):
            continue
        methods.add(name)
    return sorted(methods)


def collect_fbt_files(typelib_path):
    """Collect all F_*.fbt and F_*.fct basenames from the typelibrary."""
    fbt_files = set()
    p = Path(typelib_path)
    for fbt in p.rglob('F_*.fbt'):
        fbt_files.add(fbt.stem)
    for fct in p.rglob('F_*.fct'):
        fbt_files.add(fct.stem)
    return sorted(fbt_files)


def normalize_fbt_name(fbt_stem):
    """Strip F_ prefix from FBT name."""
    if fbt_stem.startswith('F_'):
        return fbt_stem[2:]
    return fbt_stem


def is_covered_by_specific(java_method, fbt_methods_normalized):
    """
    Check if a generic Java method is covered by specific FBT variants.
    
    Examples:
      - TO_REAL       -> covered by F_INT_TO_REAL, F_DINT_TO_REAL, etc.
      - TO_BCD_BYTE   -> covered by F_USINT_TO_BCD_BYTE, etc.
      - TRUNC         -> covered by F_TRUNC
      - TRUNC_DINT    -> covered by F_TRUNC
    """
    # Generic TO_* conversions: covered by any F_<SRC>_TO_<DST>
    if java_method.startswith('TO_') and '_TO_' not in java_method[3:]:
        target = java_method[3:]  # e.g., "REAL"
        suffix = '_TO_' + target
        for fbt in fbt_methods_normalized:
            if fbt.endswith(suffix):
                return True, f"covered by specific F_*{suffix} variants"
    
    # BCD: TO_BCD_BYTE -> covered by F_USINT_TO_BCD_BYTE, etc.
    if java_method.startswith('TO_BCD_') or java_method.startswith('BCD_TO_'):
        if java_method.startswith('TO_BCD_'):
            suffix = '_TO_' + java_method
            for fbt in fbt_methods_normalized:
                if fbt.endswith(suffix):
                    return True, f"covered by specific F_*{suffix}"
        elif java_method.startswith('BCD_TO_'):
            prefix = java_method + '_'
            for fbt in fbt_methods_normalized:
                if fbt.startswith(prefix):
                    return True, f"covered by specific F_{prefix}*"
    
    # TRUNC variants: covered by generic F_TRUNC
    if java_method == 'TRUNC' or java_method.startswith('TRUNC_'):
        if 'TRUNC' in fbt_methods_normalized:
            return True, "covered by F_TRUNC"
    
    # LREAL_TRUNC_*, REAL_TRUNC_* -> covered by F_TRUNC
    if java_method.startswith('LREAL_TRUNC_') or java_method.startswith('REAL_TRUNC_'):
        if 'TRUNC' in fbt_methods_normalized:
            return True, "covered by F_TRUNC"
    
    # Generic ADD, MUL, SUB, DIV (non-time) -> covered by F_ADD, F_MUL, etc.
    if java_method in ('ADD', 'MUL', 'SUB', 'DIV'):
        if java_method in fbt_methods_normalized:
            return True, f"covered by F_{java_method}"
    
    # Time arithmetic: MUL_TIME -> MULTIME, DIV_TIME -> DIVTIME
    if java_method == 'MUL_TIME' and 'MULTIME' in fbt_methods_normalized:
        return True, "covered by F_MULTIME"
    if java_method == 'DIV_TIME' and 'DIVTIME' in fbt_methods_normalized:
        return True, "covered by F_DIVTIME"
    
    # AS_STRING -> F_ANY_AS_STRING
    if java_method == 'AS_STRING' and 'ANY_AS_STRING' in fbt_methods_normalized:
        return True, "covered by F_ANY_AS_STRING"
    
    return False, None


def find_missing(java_methods, fbt_files):
    """Compare Java methods with FBT files and report gaps."""
    fbt_methods = {normalize_fbt_name(f) for f in fbt_files}
    
    present = []
    missing = []
    covered_by_specific = []
    extra = []
    
    for method in java_methods:
        if method in fbt_methods:
            present.append(method)
        else:
            covered, reason = is_covered_by_specific(method, fbt_methods)
            if covered:
                covered_by_specific.append((method, reason))
            else:
                missing.append(method)
    
    # Build a set of FBT names that act as generic covers
    cover_fbt_names = set()
    for method, reason in covered_by_specific:
        if 'F_TRUNC' in reason:
            cover_fbt_names.add('TRUNC')
        if 'F_ADD' in reason:
            cover_fbt_names.add('ADD')
        if 'F_MUL' in reason:
            cover_fbt_names.add('MUL')
        if 'F_SUB' in reason:
            cover_fbt_names.add('SUB')
        if 'F_DIV' in reason:
            cover_fbt_names.add('DIV')
        if 'F_MULTIME' in reason:
            cover_fbt_names.add('MULTIME')
        if 'F_DIVTIME' in reason:
            cover_fbt_names.add('DIVTIME')
        if 'F_ANY_AS_STRING' in reason:
            cover_fbt_names.add('ANY_AS_STRING')
    
    for fbt in fbt_files:
        norm = normalize_fbt_name(fbt)
        if norm not in java_methods:
            # Check if it's a specific variant of a generic Java function
            is_variant = False
            for method, _ in covered_by_specific:
                if norm.endswith('_TO_' + method) or norm.startswith(method + '_'):
                    is_variant = True
                    break
                # Special cases: MULTIME covers MUL_TIME, DIVTIME covers DIV_TIME
                if method == 'MUL_TIME' and norm == 'MULTIME':
                    is_variant = True
                    break
                if method == 'DIV_TIME' and norm == 'DIVTIME':
                    is_variant = True
                    break
                if method == 'AS_STRING' and norm == 'ANY_AS_STRING':
                    is_variant = True
                    break
            # Check if this FBT is a generic cover itself
            if not is_variant and norm in cover_fbt_names:
                is_variant = True
            if not is_variant:
                extra.append(fbt)
    
    return present, missing, covered_by_specific, extra


def categorize(methods_or_tuples):
    """Group methods into rough categories based on name prefixes."""
    cats = defaultdict(list)
    for item in methods_or_tuples:
        if isinstance(item, tuple):
            m = item[0]
        else:
            m = item
            
        if m.startswith('CONCAT_') or m.startswith('SPLIT_'):
            cats['Time/Date (Concat/Split)'].append(item)
        elif m.startswith('ADD_') or m.startswith('SUB_') or m.startswith('MUL_') or m.startswith('DIV_'):
            cats['Time/Date (Arithmetic)'].append(item)
        elif '_TO_' in m and not m.startswith('TO_'):
            cats['Conversion'].append(item)
        elif m.startswith('TRUNC'):
            cats['TRUNC'].append(item)
        elif 'BCD' in m:
            cats['BCD'].append(item)
        elif 'CHAR' in m or 'WCHAR' in m or m.startswith('STRING_') or m.startswith('WSTRING_'):
            cats['CHAR/WCHAR'].append(item)
        elif m in ('IS_VALID', 'IS_VALID_BCD'):
            cats['Validation'].append(item)
        elif 'ENDIAN' in m:
            cats['Endian'].append(item)
        elif m in ('MUX', 'SEL', 'MOVE', 'MAX', 'MIN', 'LIMIT'):
            cats['Selection'].append(item)
        elif m in ('GT', 'GE', 'EQ', 'LE', 'LT', 'NE'):
            cats['Comparison'].append(item)
        elif m in ('ABS', 'SQRT', 'LN', 'LOG', 'EXP', 'SIN', 'COS', 'TAN', 'ASIN', 'ACOS', 'ATAN', 'ATAN2', 'EXPT', 'MOD'):
            cats['Math'].append(item)
        elif m in ('SHL', 'SHR', 'ROL', 'ROR', 'AND', 'OR', 'XOR', 'NOT'):
            cats['Bitwise'].append(item)
        elif m in ('LEN', 'LEFT', 'RIGHT', 'MID', 'CONCAT', 'INSERT', 'DELETE', 'REPLACE', 'FIND'):
            cats['String'].append(item)
        elif m in ('LOWER_BOUND', 'UPPER_BOUND'):
            cats['Array'].append(item)
        elif m in ('NOW', 'NOW_MONOTONIC', 'OVERRIDE_NOW', 'OVERRIDE_NOW_MONOTONIC', 'DAY_OF_WEEK'):
            cats['Time/Date (Misc)'].append(item)
        elif m.startswith('TO_'):
            cats['Generic TO_*'].append(item)
        elif m in ('ADD', 'MUL', 'SUB', 'DIV'):
            cats['Generic Arithmetic'].append(item)
        else:
            cats['Other'].append(item)
    return cats


def print_report(present, missing, covered, extra):
    """Print a human-readable report to stdout."""
    print(f"Java methods:              {len(present) + len(missing) + len(covered)}")
    print(f"Exact FBT match:           {len(present)}")
    print(f"Covered by specific FBT:   {len(covered)}")
    print(f"Truly missing FBT:         {len(missing)}")
    print(f"Extra FBTs (no pendant):   {len(extra)}")
    print()
    
    if covered:
        print("=" * 60)
        print("COVERED BY SPECIFIC FBT VARIANTS")
        print("=" * 60)
        for cat, items in sorted(categorize(covered).items()):
            print(f"\n## {cat}")
            for m, reason in items:
                print(f"  - {m}  ({reason})")
    
    if missing:
        print("\n" + "=" * 60)
        print("TRULY MISSING FBT FILES")
        print("=" * 60)
        for cat, methods in sorted(categorize(missing).items()):
            print(f"\n## {cat}")
            for m in methods:
                print(f"  - F_{m}")
    
    if extra:
        print("\n" + "=" * 60)
        print("FBT FILES WITHOUT EXACT JAVA PENDANT")
        print("=" * 60)
        for f in sorted(extra):
            print(f"  - {f}")
    
    print()


def write_markdown(present, missing, covered, extra, output_path):
    """Write a markdown report."""
    lines = [
        "# Fehlende FBType-Dateien für StandardFunctions.java",
        "",
        f"Generiert am: {__import__('datetime').datetime.now().strftime('%Y-%m-%d %H:%M')}",
        "",
        f"- Java-Methoden gesamt:      {len(present) + len(missing) + len(covered)}",
        f"- Exakter FBT-Match:         {len(present)}",
        f"- Durch spezifische abgedeckt: {len(covered)}",
        f"- Echt fehlend:              {len(missing)}",
        f"- FBTs ohne Pendant:         {len(extra)}",
        "",
        "---",
        "",
    ]
    
    if covered:
        lines.append("## Durch spezifische FBTs abgedeckt")
        lines.append("")
        for cat, items in sorted(categorize(covered).items()):
            lines.append(f"### {cat}")
            lines.append("")
            for m, reason in items:
                lines.append(f"- [x] `{m}`  ({reason})")
            lines.append("")
    
    if missing:
        lines.append("---")
        lines.append("")
        lines.append("## Echt fehlende FBTs")
        lines.append("")
        for cat, methods in sorted(categorize(missing).items()):
            lines.append(f"### {cat}")
            lines.append("")
            for m in methods:
                lines.append(f"- [ ] `F_{m}`")
            lines.append("")
    
    if extra:
        lines.append("---")
        lines.append("")
        lines.append("## FBTs ohne direktes Java-Pendant")
        lines.append("")
        for f in sorted(extra):
            lines.append(f"- `{f}`")
        lines.append("")
    
    Path(output_path).write_text('\n'.join(lines), encoding='utf-8')
    print(f"Markdown written to: {output_path}")


def main():
    parser = argparse.ArgumentParser(
        description="Compare StandardFunctions.java with typelibrary FBTs"
    )
    parser.add_argument(
        "--java",
        default="plugins/org.eclipse.fordiac.ide.model.eval/src/org/eclipse/fordiac/ide/model/eval/function/StandardFunctions.java",
        help="Path to StandardFunctions.java (relative to repo root)"
    )
    parser.add_argument(
        "--typelib",
        default="data/typelibrary",
        help="Path to typelibrary root (relative to repo root)"
    )
    parser.add_argument(
        "--repo",
        default=".",
        help="Repository root directory"
    )
    parser.add_argument(
        "--md",
        default="missing_fbs.md",
        help="Write/update markdown report to this file"
    )
    parser.add_argument(
        "--no-md",
        action="store_true",
        help="Skip markdown generation"
    )
    args = parser.parse_args()
    
    repo = Path(args.repo).resolve()
    java_path = repo / args.java
    typelib_path = repo / args.typelib
    md_path = repo / args.md
    
    if not java_path.exists():
        print(f"ERROR: Java file not found: {java_path}", file=sys.stderr)
        sys.exit(1)
    
    if not typelib_path.exists():
        print(f"ERROR: Typelibrary not found: {typelib_path}", file=sys.stderr)
        sys.exit(1)
    
    java_methods = parse_java_methods(java_path)
    fbt_files = collect_fbt_files(typelib_path)
    present, missing, covered, extra = find_missing(java_methods, fbt_files)
    
    print_report(present, missing, covered, extra)
    
    if not args.no_md:
        write_markdown(present, missing, covered, extra, md_path)


if __name__ == "__main__":
    main()
