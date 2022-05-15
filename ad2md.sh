#!/usr/bin/env bash
# Requires asciidoctor, iconv, rename, pandoc to be installed

courseDir=./target/classes/boot/courses/
imgDir=./target/classes/boot/images/

find $courseDir -maxdepth 2 -type f -name "*.adoc" -exec sh -c "asciidoctor -b docbook {}" \;

find $courseDir -maxdepth 2 -type f -name "*.xml" -exec sh -c 'iconv -t utf-8 $0 | pandoc -f docbook -t gfm --columns=120 | iconv -f utf-8 | tee $0.md' {} \;

find $courseDir -type f -name "*.xml.md" -exec rename -f "s/.xml.md/.md/g" {} \;

find $courseDir -not -name "*.md" -not -name "*.png" -not -name "*.jpg" -not -name "*.rsc" -type f -delete

mkdir -p $imgDir
find $courseDir -type f -name "*.png" -exec mv {} $imgDir \;
find $courseDir -type f -name "*.jpg" -exec mv {} $imgDir \;

find $courseDir -type d -empty -delete

## TODO: automatically replace all IMG links with image/$image.png or image/$image.jpg
## TODO: move all concept123/concept123.md files to toplevel ./concept123.md
## TODO: add header block all .md files:
##  ---
##  label: "Concept 123"
##  sidebar_position: 2
##  ---
