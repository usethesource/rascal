module lang::rascal::tutor::conversions::Includes

import IO;
import List;
import util::FileSystem;
import String;

list[loc] roots = [|project://rascal/src/org/rascalmpl/library|, |project://rascal/src/org/rascalmpl/courses|];

list[loc] findModuleIncludes() {
    for (r <- roots, f <- find(r, "md"), l:/include::\{LibDir\}<path:[^\]]+>\[tags=module\]/ <- readFileLines(f)) {
        println("found: <path>");

        theInc = |project://rascal/src/org/rascalmpl/library| + path;


        if (!exists(theInc)) {
            println("WARNING: In <f> this include does not exist: <theInc>");
            continue;
        }

        theIncLines = readFileLines(theInc);

        if ([*str prelude,
            /^\s*\/\/\s+tag::module\[\]\s*$/,
            *str moduleContent,
            /^\s*\/\/\s+end::module\[\]\s*$/,
            *str rest
        ] := theIncLines) {
            println("<f> - <theInc>");
        }
        else {
            println("WARNING: <theInc> did not match any tags");
        }
    }

    return [];
}

void fixModuleIncludes() {
    for (r <- roots, f <- find(r, "md")) {
        fixModuleIncludes(f);
    }
}

void fixModuleIncludes(loc f) {
    lines = readFileLines(f);

    writeFile(f, intercalate("\n", fixIncludes(readFileLines(f) + [""])));
}

//  l:/include::\{LibDir\}<path:[^\]]+>\[tags=module\]/ <- readFileLines(f)) {

list[str] fixIncludes(["```rascal",  l:/include::\{LibDir\}<path:[^\]]+>\.rsc\[tags=module\]/, "```", *str tail])
    = ["```rascal-include",
        replaceAll(path, "/", "::"),
        "```",
        *fixIncludes(tail)
    ];

default list[str] fixIncludes([str head, *str tail]) = [head, *fixIncludes(tail)];
list[str] fixIncludes([]) = [];

