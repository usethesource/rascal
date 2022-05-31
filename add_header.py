#!/usr/bin/env python3

import re
import sys
import os

# first argument -> last part
file_name = os.path.basename(sys.argv[1])
# remove extension
raw_name = file_name.replace('.md', '')
# add in some spaces
concept_name = re.sub(r'((?<=[a-z])[A-Z]|(?<!\A)[A-Z](?=[a-z]))', r' \1', raw_name)
# prepend this text
header = f"""---\nlabel: "{concept_name}"\n#sidebar_position: 2\n---\n"""
with open(file_name, 'r+') as file:
    content = file.read()
    file.seek(0)
    file.write(header)
    file.write(content)