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
    return showGraph("f"("g"("h"()),"i"(42)), "ADT");
}

data Person = person(str name="", int age=0, list[Person] children=[]);

Content showPeople() {
    Person josh = person(name="Josh", age=7);
    Person alice = person(name="Alice", age=31, children=[josh]);
    return showGraph(alice, "People");
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
    return showGraph(t, "PT Full", config=createParseTreeConfig());
}

Content showParseTreeCompact() {
    Tree t = testTree();
    return showGraph(t, "PT compact", config=createParseTreeConfig(collapseTokens=true, filterLayout=true, filterMissingOptionals=true));
}

Content showYieldTreeFull() {
    Tree t = testTree();
    return showGraph(t, "PT yield full", config=createYieldParseTreeConfig());
}

Content showYieldTreeCompact() {
    Tree t = testTree();
    return showGraph(t, "PT yield compact", config=createYieldParseTreeConfig(collapseTokens=true, filterLayout=true, filterMissingOptionals=true));
}

void writeParseTreeFull() {
    Tree t = testTree();
    writeGraph(valueToDot(t, config=createParseTreeConfig()), "parsetree-full");
}

data ListNode = listNode(int id, int prev=0, int next=0);

Content showDLL() {
    ListNode n1 = listNode(1, next=2);
    ListNode n2 = listNode(2, prev=1, next=3);
    ListNode n3 = listNode(3, prev=2, next=4);
    ListNode n4 = listNode(4, prev=3);

    map[int, ListNode] nodeStore = (n1.id:n1, n2.id:n2, n3.id:n3, n4.id:n4);

    list[value](value n) dllChildGetter(map[int, ListNode] store) {
        list[value] getter(ListNode n) {
            list[value] children = [];
            if (n.prev != 0) {
                children += edge(store[n.prev], label="prev");
            }
            if (n.next != 0) {
                children += edge(store[n.next], label="next");
            }

            return children;
        }

        return getter;
    }

    Statement dllNodeGenerator(ListNode n, int id) {
        return nodeStatement("<n.id>", id);
    }

    DotConfig config = dotConfig(
        childGetter=dllChildGetter(nodeStore),
        allowRecursion=true,
        nodeGenerator=dllNodeGenerator,
        graphAttrs = DEFAULT_GRAPH_ATTRS + <"rankdir","LR">
    );

    return content("DLL", dotServer(valueToDot(n1, name="DLL", config=config)));
}

data Division = division(str name, list[str] branches);
data Company = company(str name, list[Division] divisions);

Content showSubgraph() {
    Division engineering = division("Engingeering", ["Engineering", "Research"]);
    Division products = division("Products", ["Product Management", "User Experience", "Marketing"]);
    Division sales = division("Sales", ["America", "Europe", "Asia Pacific"]);
    Division legal = division("Legal", ["General Counsel", "Corporate Development", "New Business Development"]);
    Division finance = division("Finance", ["Treaasureer", "Real Estate", "Financial Planning"]);
    Company google = company("Google", [engineering, products, sales, legal, finance]);

    list[value] subgraphChildGetter(Company company) {
        return [subGraph(div, label=div.name, attrs=[<"color", "blue">, <"bgcolor","0.482 0.2 1.0">]) | div <- company.divisions];
    }
    list[value] subgraphChildGetter(Division division) = division.branches;

    default list[value] subgraphChildGetter(value v) {
        return defaultGetChildren(v);
    }

    DotConfig config = dotConfig(
        childGetter=subgraphChildGetter,
        graphAttrs = DEFAULT_GRAPH_ATTRS + <"rankdir","LR">
    );

    return content("Subgraph", dotServer(valueToDot(google, name="Google", config=config)));
}
