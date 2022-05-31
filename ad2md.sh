#!/usr/bin/env bash
# Requires asciidoctor, iconv, rename, pandoc (https://github.com/jgm/pandoc/releases), python3 to be installed

courseDir=./target/classes/boot/courses/
imgDir=./target/classes/boot/images/

# Execute AsciiDoctor
find $courseDir -maxdepth 2 -type f -name "*.adoc" -exec sh -c "asciidoctor -b docbook {}" \;
# Convert docbook XMLs to Markdown
find $courseDir -maxdepth 2 -type f -name "*.xml" -exec sh -c 'iconv -t utf-8 $0 | pandoc -f docbook -t gfm --columns=120 | iconv -f utf-8 | tee $0.md' {} \;
# rename output to .md (no magic in previous line for sake of simplicity)
find $courseDir -type f -name "*.xml.md" -exec rename -f "s/.xml.md/.md/g" {} \;

# Delete all non-markdown, non-image and non-rascal files
find $courseDir -not -name "*.md" -not -name "*.png" -not -name "*.jpg" -not -name "*.rsc" -type f -delete

# Collect all images in a central directory
mkdir -p $imgDir
find $courseDir -type f -name "*.png" -exec mv {} $imgDir \;
find $courseDir -type f -name "*.jpg" -exec mv {} $imgDir \;

# Collect all markdown files in the root directory
find $courseDir -type f -name "*.md" -exec mv {} $courseDir \;

# Delete empty directories in the tree
find $courseDir -type d -empty -delete

find $courseDir -type f -name "*.md" -exec ./add_header.py {} \;

## TODO: automatically replace all IMG links with /image/$image