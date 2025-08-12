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
    // first we parse the program
    start[Program] tree = parse(#start[Program], file);

    // then we apply an adaptable formatting style to every node
    Box box = toBox(tree);

    // then we solve the two-dimensional layout problem, and get a formatted result
    str formatted = format(box);

    // now we extract a list of exact differences from the old and a new parse tree
    start[Program] formattedTree = parse(#start[Program], formatted, file);
    list[TextEdit] edits = layoutDiff(tree, formattedTree);

    // finally we apply the differences to the original file
    executeFileSystemChanges([changed(file, edits)]);
}

@synopsis{Format a string that contains an entire Pico program}
str formatPicoString(str file) {
    // first we parse the program
    start[Program] tree = parse(#start[Program], file, |unknown:///|);

    // then we apply an adaptable formatting style to every node
    Box box = toBox(tree);

     // then we solve the two-dimensional layout problem, and get a formatted result
    str formatted = format(box);

    // now we extract a list of exact differences from the old and a new parse tree
    start[Program] formattedTree = parse(#start[Program], formatted, |unknown:///|);
    list[TextEdit] edits = layoutDiff(tree, formattedTree);

    // finally we apply the differences to the original contents
    return executeTextEdits(file, edits);
}

@synopsis{Pico Format function for use in an IDE}
list[FileSystemChange] formatPico(start[Program] file)
    = [changed(file@\loc, layoutDiff(file, parse(#start[Program], (format o toBox)(file), |unknown:///|)))];

@synopsis{Make sure while loops are formatted the way we want them to be.}
Box toBox((Statement) `while <Expression e> do <{Statement ";"}* block> od`, FO opts = fo())
    = V([
        H([L("while"), toBox(e, opts=opts), L("do")]),
        I([toBox(block, opts=opts)]),
        L("od")
    ]);