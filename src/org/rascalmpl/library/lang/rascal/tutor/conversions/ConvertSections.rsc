module lang::rascal::tutor::conversions::ConvertSections

import IO;
import String;
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

list[str] convertSections([str first:/^\s*\[source,rascal<rest1:.*>]\s*$/, /---/, *str block, /----*<postfix:[^\-]*>/, *str rest2])
    = [
        "```rascal<removeQuotesThing(rest1)>",
        *block,
        "```<postfix>",
        *convertSections(rest2)
    ];


/*

|               |                                                                                                    |
| --- | --- |
| *What*        | The pocket calculator language Calc; we already covered it ((A simple pocket calculator language)) |
| *Illustrates* | fact, define, use, requireEqual, calculate, getType, report |
| *Source*      | https://github.com/cwi-swat/typepal/tree/master/src/examples/calc |

*/
list[str] convertSections([
    str before,
    /^\s*\|====*\s*$/, 
    str firstLine,
    *str body,
    /^\s*\|====*\s*$/,
    *str rest
    ])
    = [ /^\s*\[[^\]*]*\]\s*$/ := before ? "" : before,
    //   emptyHeader(firstLine),
      completeBodyLine(firstLine),
      columnsLine(emptyHeader(firstLine)),
      *[completeBodyLine(b) | b <- body, trim(b) != ""],
      "",
      *convertSections(rest)
    ] when [*_, /\|====*/, *_] !:= body;

str emptyHeader(str firstLine) = visit(completeBodyLine(firstLine)) { case /[^\|]/ => " "};

str completeBodyLine(str body) = /\|\s*$/ := body ? body : "<body> |";

str columnsLine(/\|\s*\|<postfix:.*>$/) = "| --- <columnsLine("|<postfix>")>"; 
str columnsLine("|") = "|";

list[str] convertSections([]) 
    = [];

default list[str] convertSections([str first, *str last]) 
    = [first, *convertSections(last)];

str removeQuotesThing(/^<prefix:.*>,subs=\"quotes\"<postfix:.*>/)
    = "<prefix><postfix>";

default str removeQuotesThing(str x) = x;
