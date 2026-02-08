import os
import re

def update_fbt(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    filename = os.path.basename(file_path)
    match = re.search(r'INIT_ARR_(\d+)_(\w+)\.fbt', filename)
    if not match:
        return
    
    size = int(match.group(1))
    dtype = match.group(2)
    
    # Update FBType Comment to include (generic FB) at the end
    new_comment = f'Comment="FB to initialize a {dtype} array of size {size} (generic FB)"'
    content = re.sub(r'Comment="FB to initialize a \w+ array of size \d+"', new_comment, content)
    # Also handle the initial state just in case some weren't updated or if I revert
    content = re.sub(r'Comment="FB to make an Array \(generic FB\)"', new_comment, content)

    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)

for file in os.listdir('.'):
    if file.endswith('.fbt'):
        update_fbt(file)
        print(f"Updated {file}")
