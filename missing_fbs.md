# Fehlende FBType-Dateien für StandardFunctions.java

Generiert am: 2026-04-24 00:41

- Java-Methoden gesamt:      496
- Exakter FBT-Match:         354
- Durch spezifische abgedeckt: 49
- Echt fehlend:              93
- FBTs ohne Pendant:         27

---

## Durch spezifische FBTs abgedeckt

### BCD

- [x] `TO_BCD_BYTE`  (covered by specific F_*_TO_BCD_BYTE variants)
- [x] `TO_BCD_DWORD`  (covered by specific F_*_TO_BCD_DWORD variants)
- [x] `TO_BCD_LWORD`  (covered by specific F_*_TO_BCD_LWORD variants)
- [x] `TO_BCD_WORD`  (covered by specific F_*_TO_BCD_WORD variants)

### Generic TO_*

- [x] `TO_BYTE`  (covered by specific F_*_TO_BYTE variants)
- [x] `TO_DATE`  (covered by specific F_*_TO_DATE variants)
- [x] `TO_DINT`  (covered by specific F_*_TO_DINT variants)
- [x] `TO_DWORD`  (covered by specific F_*_TO_DWORD variants)
- [x] `TO_INT`  (covered by specific F_*_TO_INT variants)
- [x] `TO_LINT`  (covered by specific F_*_TO_LINT variants)
- [x] `TO_LREAL`  (covered by specific F_*_TO_LREAL variants)
- [x] `TO_LWORD`  (covered by specific F_*_TO_LWORD variants)
- [x] `TO_REAL`  (covered by specific F_*_TO_REAL variants)
- [x] `TO_SINT`  (covered by specific F_*_TO_SINT variants)
- [x] `TO_STRING`  (covered by specific F_*_TO_STRING variants)
- [x] `TO_TOD`  (covered by specific F_*_TO_TOD variants)
- [x] `TO_UDINT`  (covered by specific F_*_TO_UDINT variants)
- [x] `TO_UINT`  (covered by specific F_*_TO_UINT variants)
- [x] `TO_ULINT`  (covered by specific F_*_TO_ULINT variants)
- [x] `TO_USINT`  (covered by specific F_*_TO_USINT variants)
- [x] `TO_WORD`  (covered by specific F_*_TO_WORD variants)
- [x] `TO_WSTRING`  (covered by specific F_*_TO_WSTRING variants)

### Other

- [x] `AS_STRING`  (covered by F_ANY_AS_STRING)
- [x] `LREAL_TRUNC_DINT`  (covered by F_TRUNC)
- [x] `LREAL_TRUNC_INT`  (covered by F_TRUNC)
- [x] `LREAL_TRUNC_LINT`  (covered by F_TRUNC)
- [x] `LREAL_TRUNC_SINT`  (covered by F_TRUNC)
- [x] `LREAL_TRUNC_UDINT`  (covered by F_TRUNC)
- [x] `LREAL_TRUNC_UINT`  (covered by F_TRUNC)
- [x] `LREAL_TRUNC_ULINT`  (covered by F_TRUNC)
- [x] `LREAL_TRUNC_USINT`  (covered by F_TRUNC)
- [x] `REAL_TRUNC_DINT`  (covered by F_TRUNC)
- [x] `REAL_TRUNC_INT`  (covered by F_TRUNC)
- [x] `REAL_TRUNC_LINT`  (covered by F_TRUNC)
- [x] `REAL_TRUNC_SINT`  (covered by F_TRUNC)
- [x] `REAL_TRUNC_UDINT`  (covered by F_TRUNC)
- [x] `REAL_TRUNC_UINT`  (covered by F_TRUNC)
- [x] `REAL_TRUNC_ULINT`  (covered by F_TRUNC)
- [x] `REAL_TRUNC_USINT`  (covered by F_TRUNC)

### TRUNC

- [x] `TRUNC_DINT`  (covered by F_TRUNC)
- [x] `TRUNC_INT`  (covered by F_TRUNC)
- [x] `TRUNC_LINT`  (covered by F_TRUNC)
- [x] `TRUNC_SINT`  (covered by F_TRUNC)
- [x] `TRUNC_UDINT`  (covered by F_TRUNC)
- [x] `TRUNC_UINT`  (covered by F_TRUNC)
- [x] `TRUNC_ULINT`  (covered by F_TRUNC)
- [x] `TRUNC_USINT`  (covered by F_TRUNC)

### Time/Date (Arithmetic)

- [x] `DIV_TIME`  (covered by F_DIVTIME)
- [x] `MUL_TIME`  (covered by F_MULTIME)

---

## Echt fehlende FBTs

### BCD

- [ ] `F_convertFromBCD`
- [ ] `F_convertToBCD`

### CHAR/WCHAR

- [ ] `F_STRING_AS_LTIME`
- [ ] `F_TO_CHAR`
- [ ] `F_TO_WCHAR`
- [ ] `F_WSTRING_AS_LTIME`

### Conversion

- [ ] `F_BCD_TO_UDINT`
- [ ] `F_BCD_TO_UINT`
- [ ] `F_BCD_TO_ULINT`
- [ ] `F_BCD_TO_USINT`
- [ ] `F_BYTE_BCD_TO_UDINT`
- [ ] `F_BYTE_BCD_TO_UINT`
- [ ] `F_BYTE_BCD_TO_ULINT`
- [ ] `F_BYTE_TO_BOOL`
- [ ] `F_BYTE_TO_CHAR`
- [ ] `F_CHAR_TO_BYTE`
- [ ] `F_CHAR_TO_DWORD`
- [ ] `F_CHAR_TO_LWORD`
- [ ] `F_CHAR_TO_STRING`
- [ ] `F_CHAR_TO_USINT`
- [ ] `F_CHAR_TO_WCHAR`
- [ ] `F_CHAR_TO_WORD`
- [ ] `F_DATE_TO_LDATE`
- [ ] `F_DT_TO_LDATE`
- [ ] `F_DT_TO_LDT`
- [ ] `F_DT_TO_LTOD`
- [ ] `F_DWORD_BCD_TO_UINT`
- [ ] `F_DWORD_BCD_TO_ULINT`
- [ ] `F_DWORD_BCD_TO_USINT`
- [ ] `F_DWORD_TO_BOOL`
- [ ] `F_LDATE_TO_DATE`
- [ ] `F_LDT_TO_DATE`
- [ ] `F_LDT_TO_DT`
- [ ] `F_LDT_TO_LDATE`
- [ ] `F_LDT_TO_LTOD`
- [ ] `F_LDT_TO_TOD`
- [ ] `F_LTIME_TO_TIME`
- [ ] `F_LTOD_TO_TOD`
- [ ] `F_LWORD_BCD_TO_UDINT`
- [ ] `F_LWORD_BCD_TO_UINT`
- [ ] `F_LWORD_BCD_TO_USINT`
- [ ] `F_LWORD_TO_BOOL`
- [ ] `F_STRING_TO_CHAR`
- [ ] `F_TIME_TO_LTIME`
- [ ] `F_TOD_TO_LTOD`
- [ ] `F_UDINT_TO_BCD_BYTE`
- [ ] `F_UDINT_TO_BCD_LWORD`
- [ ] `F_UDINT_TO_BCD_WORD`
- [ ] `F_UINT_TO_BCD_BYTE`
- [ ] `F_UINT_TO_BCD_DWORD`
- [ ] `F_UINT_TO_BCD_LWORD`
- [ ] `F_ULINT_TO_BCD_BYTE`
- [ ] `F_ULINT_TO_BCD_DWORD`
- [ ] `F_ULINT_TO_BCD_WORD`
- [ ] `F_USINT_TO_BCD_DWORD`
- [ ] `F_USINT_TO_BCD_LWORD`
- [ ] `F_USINT_TO_BCD_WORD`
- [ ] `F_WCHAR_TO_CHAR`
- [ ] `F_WCHAR_TO_DWORD`
- [ ] `F_WCHAR_TO_LWORD`
- [ ] `F_WCHAR_TO_WORD`
- [ ] `F_WCHAR_TO_WSTRING`
- [ ] `F_WORD_BCD_TO_UDINT`
- [ ] `F_WORD_BCD_TO_ULINT`
- [ ] `F_WORD_BCD_TO_USINT`
- [ ] `F_WORD_TO_BOOL`
- [ ] `F_WORD_TO_WCHAR`
- [ ] `F_WSTRING_TO_WCHAR`

### Generic TO_*

- [ ] `F_TO_DT`
- [ ] `F_TO_LDATE`
- [ ] `F_TO_LDT`
- [ ] `F_TO_LTIME`
- [ ] `F_TO_LTOD`
- [ ] `F_TO_TIME`

### Other

- [ ] `F_LTIME_AS_STRING`
- [ ] `F_LTIME_AS_WSTRING`
- [ ] `F_apply`

### Selection

- [ ] `F_MUX`

### Time/Date (Arithmetic)

- [ ] `F_ADD_LDT_LTIME`
- [ ] `F_ADD_LTIME`
- [ ] `F_ADD_LTOD_LTIME`
- [ ] `F_ADD_TIME`
- [ ] `F_DIV_LTIME`
- [ ] `F_MUL_LTIME`
- [ ] `F_SUB_LDATE_LDATE`
- [ ] `F_SUB_LDT_LDT`
- [ ] `F_SUB_LDT_LTIME`
- [ ] `F_SUB_LTIME`
- [ ] `F_SUB_LTOD_LTIME`
- [ ] `F_SUB_LTOD_LTOD`
- [ ] `F_SUB_TIME`

### Time/Date (Misc)

- [ ] `F_OVERRIDE_NOW`
- [ ] `F_OVERRIDE_NOW_MONOTONIC`

---

## FBTs ohne direktes Java-Pendant

- `F_CONCAT_2`
- `F_CONCAT_3`
- `F_EQ_2`
- `F_EQ_3`
- `F_GE_2`
- `F_GE_3`
- `F_GT_2`
- `F_GT_3`
- `F_LEN_ARRAY`
- `F_LE_2`
- `F_LE_3`
- `F_LT_2`
- `F_LT_3`
- `F_MAX_2`
- `F_MAX_3`
- `F_MIN_2`
- `F_MIN_3`
- `F_MUL_2`
- `F_MUL_3`
- `F_MUX_2`
- `F_MUX_2_1`
- `F_MUX_2_2`
- `F_MUX_3`
- `F_MUX_4`
- `F_SEL_E_2`
- `F_SEL_E_3`
- `F_SEL_E_4`
