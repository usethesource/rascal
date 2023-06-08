module lang::rascal::tutor::conversions::SplitDocTag

import util::FileSystem;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import String;

list[loc] findDocTags(loc root) 
    = [*findDocTags(parse(#start[Module], f).top) | loc f <- find(root, "rsc") ];

list[loc] findDocTags(Module m)
    = [ t.src | /t:(Tag) `@doc <TagString _>` := m];

void rewriteDocTags(loc root) {
    locs = findDocTags(root);

    for (l <- locs) {
        rewriteDocTag(l);
    }
}

void rewriteDocTag(loc t) {
    T = parse(#Tag, trim(readFile(t)));

    // drop the { } and the leading and trailing whitespace
    cleanContent = trim("<T.contents>"[1..-1]);
    newContent = "";

    while (/^#### <heading:[A-Z][a-z]+>\s*<body:.*>\n\s*<rest:####.*>\s*$/m := cleanContent) {
        if (heading == "Synopsis") {
            newContent = "@synopsis{<trim(body)>}<if (trim(body)[-1] != ".") {>.<}>
                         '";
        } else {
            newContent += "@<toLowerCase(heading)>{
                          '<body>
                       '}
                       '";
        }
        cleanContent = trim(rest);
    }

    if (/^####\s*<heading:[A-Z][a-z]+>\s*\n<body:.*>$/ := cleanContent) {
        if (heading == "Synopsis") {
            newContent = "@synopsis{<trim(body)><if (trim(body)[-1] != ".") {>.<}>}
                         '";
        } else {
            println("doing <heading> with <body>");
            newContent += "@<toLowerCase(heading)>{
                          '<trim(body)>
                          '}
                          '";
        }
        cleanContent = "";
    }

    println("REPORTING <t>
            '  NEW : <newContent>
            '  LEFT: <cleanContent>");
}

public str example = 
    "#### Synopsis
    '
    'Syntax definition for S-Expressions, based on http://people.csail.mit.edu/rivest/Sexp.txt";

