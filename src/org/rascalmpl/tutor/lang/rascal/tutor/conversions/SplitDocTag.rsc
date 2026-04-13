module lang::rascal::tutor::conversions::SplitDocTag

import util::FileSystem;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import String;
import analysis::diff::edits::TextEdits;
import util::IDEServices;

list[loc] findDocTags(loc root) 
    = [*findDocTags(parse(#start[Module], f).top) | loc f <- find(root, "rsc") ];

list[loc] findDocTags(Module m)
    = [ t.src | /t:(Tag) `@doc <TagString _>` := m];


void editLibrary(loc root) {
    for (loc f <- find(root, "rsc")) {
        editModule(parse(#start[Module], f).top);
    }
}

void editModule(loc example) = editModule(parse(#start[Module], example).top);
list[TextEdit] editsForModule(loc example) = rewriteDocTags(example);

void editModule(Module m) {
    edits = rewriteDocTags(m@\loc.top);
    executeDocumentEdits([changed(m@\loc.top, edits)]);
    return;
}

list[TextEdit] rewriteDocTags(loc root) 
    = [replace(l, rewriteDocTag(l)) | l <- findDocTags(root)];

void doSomething() {
    executeDocumentEdits([changed(|project://rascal/src/org/rascalmpl/library/Boolean.rsc|, [replace(
    |project://rascal/src/org/rascalmpl/library/Boolean.rsc|(1618,210,<108,0>,<125,1>),
    "\n@synopsis{\n\nConvert Boolean value to string.\n\n}\n@description{\n\nMaps `true` to `\"true\"` and `false` to `\"false\"`.\n\n}\n@examples{\n\n```rascal-shell\nimport Boolean;\ntoString(true);\ntoString(false);\n```\n}")])]);
}

str rewriteDocTag(loc t) {
    T = parse(#Tag, trim(readFile(t)));
    
    // drop the { } and the leading and trailing whitespace
    cleanContent = trim("<T.contents>"[1..-1]);
    newContent = "";

    if (/####/ !:= cleanContent) {
        // this is a synopsis
        return "@synopsis{<cleanContent>}";
    }

    output = for (line <- split("\n", cleanContent)) {
        println("line: <line>");
        switch (line) {
            case /^####\s+[Ss]ynopsis\s*$/: {
                println("SYN!");
                append "@synopsis{";
            }
            case /^####\s+<heading:[A-Z][a-z]+>\s*$/: {
                println("DESC! <heading>");
                append "}";
                append "@<toLowerCase(heading)>{";
            }
            default:
                {
                    // println("normal: <line>");
                append line;
                }
        }
    }

    return "
           '<for (l <- output) {><l>
           '<}>}";
}

void executeDocumentEdits(list[DocumentEdit] edits) {
    for (e <- edits) {
        executeDocumentEdit(e);
    }
}

void executeDocumentEdit(removed(loc f)) {
    remove(f.top);
}

void executeDocumentEdit(created(loc f)) {
    writeFile(f, "");
}

void executeDocumentEdit(renamed(loc from, loc to)) {
    move(from.top, to.top, overwrite=true);
}

void executeDocumentEdit(changed(loc file, list[TextEdit] edits)) {
    assert isSorted(edits, less=bool (TextEdit e1, TextEdit e2) { 
        return e1.range.offset < e2.range.offset; 
    });

    str content = readFile(file);

    for (replace(loc range, str repl) <- reverse(edits)) {
        assert range.top == file.top;
        content = "<content[..range.offset]><repl><content[range.offset+range.length..]>";
    }

    writeFile(file.top, content);
}

public str example = 
    "#### Synopsis
    '
    'Syntax definition for S-Expressions, based on http://people.csail.mit.edu/rivest/Sexp.txt";

