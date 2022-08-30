module lang::rascal::tutor::conversions::ConvertSections

import IO;
import util::FileSystem;

void convertAllSections(loc dir) {
    set[loc] files = find(dir, isConceptFile);

    for (loc f <- files) {
        writeFile(f, "<for (l <- convertSections(f)) {><l>
                     '<}>");
    }
}

bool isConceptFile(loc f) = (f.extension) in {"md", "concept", "rsc"};

bool isImageFile(loc f) = f.extension in {"png", "jpg", "svg", "jpeg"};

list[str] convertSections(loc file) {
    return convertSections(readFileLines(file));
}

list[str] convertSections([str first:/^\s*\[source,rascal<rest1:.*>]\s*$/, /---/, *str block, /---/, *str rest2])
    = [
        "```rascal<removeQuotesThing(rest1)>",
        *block,
        "```",
        *convertSections(rest2)
    ];

list[str] convertSections([]) 
    = [];

default list[str] convertSections([str first, *str last]) 
    = [first, *convertSections(last)];

str removeQuotesThing(/^<prefix:.*>,subs=\"quotes\"<postfix:.*>/)
    = "<prefix><postfix>";

default str removeQuotesThing(str x) = x;