@synopsis{Demonstrates ((Tree2Box)), ((Box2Text)) and ((HiFiLayoutDiff)) for constructing a declarative and HiFi Pico formatting pipeline}
module lang::pico::format::Formatting

extend lang::box::util::Tree2Box;

import analysis::diff::edits::ExecuteTextEdits;
import analysis::diff::edits::HiFiLayoutDiff;
import lang::box::\syntax::Box;
import lang::box::util::Box2Text;
import lang::pico::\syntax::Main;
import ParseTree;

@synopsis{In-place formatting of an entire Pico file}
void formatPicoFile(loc file) {
    edits = formatPicoTree(parse(#start[Program], file));
    executeFileSystemChanges([changed(file, edits)]);
}

@synopsis{Format a string that contains an entire Pico program}
str formatPicoString(str file) {
    start[Program] tree = parse(#start[Program], file, |unknown:///|);
    return executeTextEdits(file, formatPico(tree)[0].edits);
}

@synopsis{Pico Format function for reuse in file, str or IDE-based formatting contexts}
list[TextEdit] formatPicoTree(start[Program] file) {
    formatted = format(toBox(file));
    return layoutDiff(file, parse(#start[Program], formatted, file@\loc.top));
}

@synopsis{Format while}
Box toBox((Statement) `while <Expression e> do <{Statement ";"}* block> od`, FO opts = fo())
    = V([
        H([L("while"), toBox(e, opts=opts), L("do")]),
        I([toBox(block, opts=opts)]),
        L("od")
    ]); 

@synopsis{Format if-then-else }
Box toBox((Statement) `if <Expression e> then <{Statement ";"}* thenPart> else <{Statement ";"}* elsePart> fi`, FO opts = fo())
    = V([
        H([L("if"), toBox(e, opts=opts), L("then")]),
            I([toBox(thenPart, opts=opts)]),
        L("else"),
            I([toBox(elsePart, opts=opts)]),
        L("fi")
    ]); 