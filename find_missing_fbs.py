#!/usr/bin/env python3
"""
Find missing FBType (F_*.fbt) files for Java StandardFunctions.

This script compares static methods defined in StandardFunctions.java
with F_*.fbt files in the typelibrary and reports gaps.
"""

import argparse
import os
import re
import sys
from collections import defaultdict
from pathlib import Path


def parse_java_methods(java_path):
    """Extract all unique static method names from StandardFunctions.java."""
    content = Path(java_path).read_text(encoding='utf-8')
    # Match: static <...> RETURN_TYPE METHOD_NAME(
    # Handles generics, annotations, comments, etc.
    pattern = re.compile(
        r'static\s+(?:<[^>]+>\s+)?(?:[\w.$]+\s+)+(\w+)\s*\(',
        re.MULTILINE
    )
    methods = set()
    for match in pattern.finditer(content):
        name = match.group(1)
        # Skip Java keywords that might accidentally match
        if name in ('return', 'throw', 'new', 'if', 'for', 'while', 'switch'):
            continue
        methods.add(name)
    return sorted(methods)


def collect_fbt_files(typelib_path):
    """Collect all F_*.fbt basenames from the typelibrary."""
    fbt_files = set()
    p = Path(typelib_path)
    for fbt in p.rglob('F_*.fbt'):
        fbt_files.add(fbt.stem)  # e.g., "F_ADD_TIME"
    return sorted(fbt_files)


def normalize_fbt_name(fbt_stem):
    """Strip F_ prefix from FBT name."""
    if fbt_stem.startswith('F_'):
        return fbt_stem[2:]
    return fbt_stem


def find_missing(java_methods, fbt_files):
    """Compare Java methods with FBT files and report gaps."""
    fbt_methods = {normalize_fbt_name(f) for f in fbt_files}

    missing = []
    present = []
    extra = []

    for method in java_methods:
        if method in fbt_methods:
            present.append(method)
        else:
            missing.append(method)

    for fbt in fbt_files:
        norm = normalize_fbt_name(fbt)
        if norm not in java_methods:
            extra.append(fbt)

    return present, missing, extra


def categorize(methods):
    """Group methods into rough categories based on name prefixes."""
    cats = defaultdict(list)
    for m in methods:
        if m.startswith('CONCAT_') or m.startswith('SPLIT_'):
            cats['Time/Date (Concat/Split)'].append(m)
        elif m.startswith('ADD_') or m.startswith('SUB_') or m.startswith('MUL_') or m.startswith('DIV_'):
            cats['Time/Date (Arithmetic)'].append(m)
        elif '_TO_' in m:
            cats['Conversion'].append(m)
        elif m.startswith('TRUNC'):
            cats['TRUNC'].append(m)
        elif 'BCD' in m:
            cats['BCD'].append(m)
        elif 'CHAR' in m or 'WCHAR' in m or m.startswith('STRING_') or m.startswith('WSTRING_'):
            cats['CHAR/WCHAR'].append(m)
        elif m in ('IS_VALID', 'IS_VALID_BCD'):
            cats['Validation'].append(m)
        elif 'ENDIAN' in m:
            cats['Endian'].append(m)
        elif m in ('MUX', 'SEL', 'MOVE', 'MAX', 'MIN', 'LIMIT'):
            cats['Selection'].append(m)
        elif m in ('GT', 'GE', 'EQ', 'LE', 'LT', 'NE'):
            cats['Comparison'].append(m)
        elif m in ('ABS', 'SQRT', 'LN', 'LOG', 'EXP', 'SIN', 'COS', 'TAN', 'ASIN', 'ACOS', 'ATAN', 'ATAN2', 'EXPT', 'MOD'):
            cats['Math'].append(m)
        elif m in ('SHL', 'SHR', 'ROL', 'ROR', 'AND', 'OR', 'XOR', 'NOT'):
            cats['Bitwise'].append(m)
        elif m in ('LEN', 'LEFT', 'RIGHT', 'MID', 'CONCAT', 'INSERT', 'DELETE', 'REPLACE', 'FIND'):
            cats['String'].append(m)
        elif m in ('LOWER_BOUND', 'UPPER_BOUND'):
            cats['Array'].append(m)
        elif m in ('NOW', 'NOW_MONOTONIC', 'OVERRIDE_NOW', 'OVERRIDE_NOW_MONOTONIC', 'DAY_OF_WEEK'):
            cats['Time/Date (Misc)'].append(m)
        elif m.startswith('TO_'):
            cats['Generic TO_*'].append(m)
        elif m in ('ADD', 'MUL', 'SUB', 'DIV'):
            cats['Generic Arithmetic'].append(m)
        else:
            cats['Other'].append(m)
    return cats


def print_report(present, missing, extra, fbt_files):
    """Print a human-readable report to stdout."""
    print(f"Java methods:   {len(present) + len(missing)}")
    print(f"Present in FBT: {len(present)}")
    print(f"Missing FBT:    {len(missing)}")
    print(f"Extra FBTs:     {len(extra)}")
    print()

    if missing:
        print("=" * 60)
        print("MISSING FBT FILES (grouped by category)")
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


def write_markdown(present, missing, extra, output_path):
    """Write a markdown checklist of missing FBTs."""
    lines = [
        "# Fehlende FBType-Dateien für StandardFunctions.java",
        "",
        f"Generiert am: {__import__('datetime').datetime.now().strftime('%Y-%m-%d %H:%M')}",
        "",
        f"- Java-Methoden gesamt: {len(present) + len(missing)}",
        f"- Vorhanden als FBT:    {len(present)}",
        f"- Fehlend:              {len(missing)}",
        f"- FBTs ohne Pendant:    {len(extra)}",
        "",
        "---",
        "",
    ]

    for cat, methods in sorted(categorize(missing).items()):
        lines.append(f"## {cat}")
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
        help="Write/update markdown checklist to this file"
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
    present, missing, extra = find_missing(java_methods, fbt_files)

    print_report(present, missing, extra, fbt_files)

    if not args.no_md:
        write_markdown(present, missing, extra, md_path)


if __name__ == "__main__":
    main()
