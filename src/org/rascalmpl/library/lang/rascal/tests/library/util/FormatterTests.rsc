module lang::rascal::tests::library::util::FormatterTests

import IO;
import List;
import ParseTree;
import String;
import lang::pico::format::Formatting;
import lang::pico::\syntax::Main;

private bool debug = true;

public str example = "begin declare a:natural, b:natural; if a - b then b := a; a := b fi; while a do a := a - 1 od; if b -a then a := b else b := a fi end"; 

test bool stringTest() {
    formatted = formatPicoString(example);
    if (debug) println("stringTest:\n<formatted>");

    return size(split("\n", formatted)) == 19;
}

test bool fileTest() {
    writeFile(|memory://FormatterTests/example1.pico|, example);
    formatPicoFile(|memory://FormatterTests/example1.pico|);
    formatted = readFileLines(|memory://FormatterTests/example1.pico|);
    if (debug) println("fileTest:\n<for (l <- formatted) {><l>\n<}>");
    return size(formatted) == 19;
}

test bool treeTest() {
    input = parse(#start[Program], example);
    edits = formatPicoTree(input);
    if (debug) { println("treeTest:"); iprintln(edits); }
    return size(edits) == 23;
}
