module vis::tests::ValueGraphTest

import lang::dot::\syntax::Dot;
import vis::CytoDot;
import vis::ValueGraph;
import vis::Graphs;
import ParseTree;
import IO;
import Content;
import lang::html::IO;
import lang::html::AST;

//syntax Ambiguous = A? | B?;
//syntax A = "a";
//syntax B = "a";

private Content showGraph(value v, str name, ValueToGraphConfig config = valueToGraphConfig()) {
    return content(name, graphServer(valueToGraph(v, config=config), pageTitle=name));
}

Content showNode() {
    node n = "f"("g"("h"()),"i"(42));
    return showGraph(n, "node");
}

data Person = person(str name="", int age=0, list[Person] children=[]);

Content showPeople() {
    Person josh = person(name="Josh", age=7);
    Person alice = person(name="Alice", age=31, children=[josh]);
    return showGraph(alice, "people");
}

Tree parseTree = parse(#start[DOT], "graph X { a=b c d-\>g }");

Content showParseTree1() {
    return showGraph(parseTree, "ParseTree-1", config=createParseTreeConfig());
}

Content showParseTree2() {
    return showGraph(parseTree, "ParseTree-2", config=createParseTreeConfig(collapseTokens=true));
}

Content showParseTree3() {
    return showGraph(parseTree, "ParseTree-3", config=createParseTreeConfig(collapseTokens=true, filterLayout=true, filterMissingOptionals=true));
}

Content showYieldParseTreeCompact() {
    return showGraph(parseTree, "ParseTree-3", config=createYieldParseTreeConfig());
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

    CytoData dllNodeGenerator(ListNode n, str id) {
        return cytoNode(id, "<n.id>");
    }

    ValueToGraphConfig config = valueToGraphConfig(
        childGetter=dllChildGetter(nodeStore),
        allowRecursion=true,
        nodeGenerator=dllNodeGenerator,
        \layout = defaultDotLayout(rankDir="LR")
    );

    return showGraph(n1, "DLL", config=config);
}
