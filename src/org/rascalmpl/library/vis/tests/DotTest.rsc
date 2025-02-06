module vis::tests::DotTest

import lang::dot::\syntax::Dot;
import vis::Dot;
import ParseTree;
import IO;
import util::ShellExec;
import lang::paths::Windows;
import Content;

private void writeGraph(DOT graph, str name) {
    str dotExe = "C:\\Program Files\\Graphviz\\bin\\dot.exe";
    str dotFile = "D:\\debug\\<name>.dot";
    str svgFile = "D:\\debug\\<name>.svg";

    loc dotExeLoc = parseWindowsPath(dotExe);
    loc dotFileLoc = parseWindowsPath(dotFile);

    writeFile(dotFileLoc, "<graph>");

    <output, code> = execWithCode(dotExeLoc, args=["-Tsvg", dotFile, "-o", svgFile]);

    if (code != 0) {
        throw "dot command failed: <code>";
    }
}

private Content showGraph(value v, str name, DotConfig config = dotConfig()) {
    return content(name, dotServer(valueToDot(v, name=name, config=config)));
}

void testNode() {
    node n = "f"("g"("h"()),"i"(42));
    DOT graph = valueToDot(n);
    writeGraph(graph, "node");
}

Content showNode() {
    return showGraph("f"("g"("h"()),"i"(42)), "showNode");
}

data Person = person(str name="", int age=0, list[Person] children=[]);

Content showPeople() {
    Person josh = person(name="Josh", age=7);
    Person alice = person(name="Alice", age=31, children=[josh]);
    return showGraph(alice, "People");
}

void testADT2() {
    str source = readFile(|std:///lang/pico/examples/P1.pico|);
    CFGraph cfGraph = cflowProgram(source);
    DOT graph = valueToDot(cfGraph);
    writeGraph(graph, "adt2");
}

Tree testTree() = parse(#start[DOT], |std:///lang/dot/examples/example1.dot|);

void testParseTree() {
    Tree t = testTree();

    DOT graph = valueToDot(t, config=createParseTreeConfig());
    writeGraph(graph, "dotParseTree-1");
    graph = valueToDot(t, config=createParseTreeConfig(collapseTokens=true));
    writeGraph(graph, "dotParseTree-2");
    graph = valueToDot(t, config=createParseTreeConfig(collapseTokens=true, filterLayout=true, filterMissingOptionals=true));
    writeGraph(graph, "dotParseTree-3");
}

Content showParseTreeFull() {
    Tree t = testTree();
    return showGraph(t, "showNode", config=createParseTreeConfig());
}

Content showParseTreeCompact() {
    Tree t = testTree();
    return showGraph(t, "showNode", config=createParseTreeConfig(collapseTokens=true, filterLayout=true, filterMissingOptionals=true));
}
