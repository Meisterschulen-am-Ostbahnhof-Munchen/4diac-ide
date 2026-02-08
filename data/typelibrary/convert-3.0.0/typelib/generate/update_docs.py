import os
import re

def update_fbt(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # Get array size and type from filename
    # Example: INIT_ARR_0008_BYTE.fbt
    filename = os.path.basename(file_path)
    match = re.search(r'INIT_ARR_(\d+)_(\w+)\.fbt', filename)
    if not match:
        return
    
    size = int(match.group(1))
    dtype = match.group(2)
    
    # Generate example values
    if dtype == 'BYTE':
        example = '[16#01, 16#00, ...]'
    elif dtype == 'INT':
        example = '[1, 2, ...]'
    else:
        example = '[...]'

    new_description = (
        "Copyright (c) 2026 Andreas Demmler Fahrzeugbau&#10;"
        " &#10;"
        "This program and the accompanying materials are made&#10;"
        "available under the terms of the Eclipse Public License 2.0&#10;"
        "which is available at https://www.eclipse.org/legal/epl-2.0/&#10;"
        "&#10;"
        "SPDX-License-Identifier: EPL-2.0 &#10;"
        "&#10;"
        f"This generic Function Block defines an array of {size} {dtype} elements.&#10;"
        "Functionality:&#10;"
        "- On the INIT event, the FB reads the array 'D1', calculates its size, and triggers INITO.&#10;"
        "- The output 'u16DaSize' provides the number of elements in the array.&#10;"
        "- The 'D1' InOut variable serves as the array container and can be pre-filled with initial values.&#10;"
        "&#10;"
        f"Example for D1 initialization: {example}"
    )

    # Replace Description attribute
    content = re.sub(r'Description="[^"]*"', f'Description="{new_description}"', content)
    
    # Update FBType Comment if needed
    content = re.sub(r'Comment="FB to make an Array \(generic FB\)"', f'Comment="FB to initialize a {dtype} array of size {size}"', content)

    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)

for file in os.listdir('.'):
    if file.endswith('.fbt'):
        update_fbt(file)
        print(f"Updated {file}")
