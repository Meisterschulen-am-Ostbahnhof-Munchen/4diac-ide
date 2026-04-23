# Fehlende FBType-Dateien für StandardFunctions.java

Diese Liste enthält Java-Methoden aus `StandardFunctions.java`, für die noch keine entsprechende `F_*.fbt`-Datei in der Typenbibliothek existiert.

Stand: 2026-04-23

---

## Validierung
- [ ] `F_IS_VALID`
- [ ] `F_IS_VALID_BCD`

## Endian-Konvertierung
- [ ] `F_TO_BIG_ENDIAN`
- [ ] `F_TO_LITTLE_ENDIAN`
- [ ] `F_FROM_BIG_ENDIAN`
- [ ] `F_FROM_LITTLE_ENDIAN`

## Auswahl (Selection)
- [ ] `F_MUX` (generisch – es gibt nur `F_MUX_2`, `F_MUX_3`, `F_MUX_4`)

## Zeit/Datum – Arithmetik (ADD)
- [ ] `F_ADD_TIME`
- [ ] `F_ADD_LTIME`
- [ ] `F_ADD_TOD_TIME`
- [ ] `F_ADD_LTOD_LTIME`
- [ ] `F_ADD_DT_TIME`
- [ ] `F_ADD_LDT_LTIME`

## Zeit/Datum – Arithmetik (SUB)
- [ ] `F_SUB_TIME`
- [ ] `F_SUB_LTIME`
- [ ] `F_SUB_DATE_DATE`
- [ ] `F_SUB_LDATE_LDATE`
- [ ] `F_SUB_TOD_TIME`
- [ ] `F_SUB_LTOD_LTIME`
- [ ] `F_SUB_TOD_TOD`
- [ ] `F_SUB_LTOD_LTOD`
- [ ] `F_SUB_DT_TIME`
- [ ] `F_SUB_LDT_LTIME`
- [ ] `F_SUB_DT_DT`
- [ ] `F_SUB_LDT_LDT`

## Zeit/Datum – Arithmetik (MUL/DIV)
- [ ] `F_MUL_TIME`
- [ ] `F_MUL_LTIME`
- [ ] `F_DIV_TIME`
- [ ] `F_DIV_LTIME`

## Zeit/Datum – Sonstiges
- [ ] `F_DAY_OF_WEEK`
- [ ] `F_NOW`
- [ ] `F_NOW_MONOTONIC`

## Zeit/Datum – Typkonvertierungen
- [ ] `F_TIME_TO_LTIME`
- [ ] `F_LTIME_TO_TIME`
- [ ] `F_DT_TO_LDT`
- [ ] `F_LDT_TO_DT`
- [ ] `F_LDT_TO_DATE`
- [ ] `F_LDT_TO_LDATE`
- [ ] `F_LDT_TO_TOD`
- [ ] `F_LDT_TO_LTOD`
- [ ] `F_LDATE_TO_DATE`
- [ ] `F_DATE_TO_LDATE`
- [ ] `F_TOD_TO_LTOD`
- [ ] `F_LTOD_TO_TOD`
- [ ] `F_DT_TO_LDATE`
- [ ] `F_DT_TO_LTOD`

## Generische TO_* Konvertierungen (überladen)
- [ ] `F_TO_REAL`
- [ ] `F_TO_LREAL`
- [ ] `F_TO_SINT`
- [ ] `F_TO_INT`
- [ ] `F_TO_DINT`
- [ ] `F_TO_LINT`
- [ ] `F_TO_USINT`
- [ ] `F_TO_UINT`
- [ ] `F_TO_UDINT`
- [ ] `F_TO_ULINT`
- [ ] `F_TO_BYTE`
- [ ] `F_TO_WORD`
- [ ] `F_TO_DWORD`
- [ ] `F_TO_LWORD`
- [ ] `F_TO_TIME`
- [ ] `F_TO_LTIME`
- [ ] `F_TO_DATE`
- [ ] `F_TO_LDATE`
- [ ] `F_TO_TOD`
- [ ] `F_TO_LTOD`
- [ ] `F_TO_DT`
- [ ] `F_TO_LDT`
- [ ] `F_TO_STRING`
- [ ] `F_TO_WSTRING`
- [ ] `F_TO_CHAR`
- [ ] `F_TO_WCHAR`

## TRUNC Konvertierungen
- [ ] `F_TRUNC_SINT`
- [ ] `F_TRUNC_INT`
- [ ] `F_TRUNC_DINT`
- [ ] `F_TRUNC_LINT`
- [ ] `F_TRUNC_USINT`
- [ ] `F_TRUNC_UINT`
- [ ] `F_TRUNC_UDINT`
- [ ] `F_TRUNC_ULINT`
- [ ] `F_LREAL_TRUNC_SINT`
- [ ] `F_LREAL_TRUNC_INT`
- [ ] `F_LREAL_TRUNC_DINT`
- [ ] `F_LREAL_TRUNC_LINT`
- [ ] `F_LREAL_TRUNC_USINT`
- [ ] `F_LREAL_TRUNC_UINT`
- [ ] `F_LREAL_TRUNC_UDINT`
- [ ] `F_LREAL_TRUNC_ULINT`
- [ ] `F_REAL_TRUNC_SINT`
- [ ] `F_REAL_TRUNC_INT`
- [ ] `F_REAL_TRUNC_DINT`
- [ ] `F_REAL_TRUNC_LINT`
- [ ] `F_REAL_TRUNC_USINT`
- [ ] `F_REAL_TRUNC_UINT`
- [ ] `F_REAL_TRUNC_UDINT`
- [ ] `F_REAL_TRUNC_ULINT`

## BCD Konvertierungen
- [ ] `F_BCD_TO_USINT`
- [ ] `F_BCD_TO_UINT`
- [ ] `F_BCD_TO_UDINT`
- [ ] `F_BCD_TO_ULINT`
- [ ] `F_BYTE_BCD_TO_UINT`
- [ ] `F_BYTE_BCD_TO_ULINT`
- [ ] `F_BYTE_BCD_TO_USINT`
- [ ] `F_DWORD_BCD_TO_UINT`
- [ ] `F_DWORD_BCD_TO_ULINT`
- [ ] `F_DWORD_BCD_TO_USINT`
- [ ] `F_LWORD_BCD_TO_UDINT`
- [ ] `F_LWORD_BCD_TO_UINT`
- [ ] `F_LWORD_BCD_TO_USINT`
- [ ] `F_WORD_BCD_TO_UDINT`
- [ ] `F_WORD_BCD_TO_ULINT`
- [ ] `F_WORD_BCD_TO_USINT`
- [ ] `F_USINT_TO_BCD_WORD`
- [ ] `F_USINT_TO_BCD_DWORD`
- [ ] `F_USINT_TO_BCD_LWORD`

## CHAR / WCHAR Konvertierungen
- [ ] `F_BYTE_TO_CHAR`
- [ ] `F_CHAR_TO_BYTE`
- [ ] `F_CHAR_TO_WORD`
- [ ] `F_CHAR_TO_DWORD`
- [ ] `F_CHAR_TO_LWORD`
- [ ] `F_CHAR_TO_STRING`
- [ ] `F_CHAR_TO_USINT`
- [ ] `F_CHAR_TO_WCHAR`
- [ ] `F_WCHAR_TO_CHAR`
- [ ] `F_WCHAR_TO_WORD`
- [ ] `F_WCHAR_TO_DWORD`
- [ ] `F_WCHAR_TO_LWORD`
- [ ] `F_WCHAR_TO_WSTRING`
- [ ] `F_WORD_TO_WCHAR`
- [ ] `F_STRING_TO_CHAR`
- [ ] `F_WSTRING_TO_WCHAR`

## String <-> LTIME Konvertierungen
- [ ] `F_LTIME_AS_STRING`
- [ ] `F_LTIME_AS_WSTRING`
- [ ] `F_STRING_AS_LTIME`
- [ ] `F_WSTRING_AS_LTIME`

## Sonstige 4diac-spezifische Funktionen
- [ ] `F_OVERRIDE_NOW`
- [ ] `F_OVERRIDE_NOW_MONOTONIC`

---

## Hinweis: FBTs ohne direktes Java-Pendant
Folgende FBT-Dateien existieren, haben aber keinen exakt gleichnamigen Methodennamen in `StandardFunctions.java` (oft Varianten von varargs-Funktionen oder leicht abweichende Namen):
- `F_ANY_AS_STRING` (Java: `AS_STRING`)
- `F_CONCAT_2`, `F_CONCAT_3`
- `F_DIVTIME`, `F_MULTIME`
- `F_LEN_ARRAY`
- `F_MAX_2`, `F_MAX_3`, `F_MIN_2`, `F_MIN_3`
- `F_MUL_2`, `F_MUL_3`
- `F_MUX_2`, `F_MUX_2_1`, `F_MUX_2_2`, `F_MUX_3`, `F_MUX_4`
- `F_SEL_E_2`, `F_SEL_E_3`, `F_SEL_E_4`
- `F_TRUNC`
