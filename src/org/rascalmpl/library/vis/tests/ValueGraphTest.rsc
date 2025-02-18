module vis::tests::ValueGraphTest

import lang::dot::\syntax::Dot;
import vis::ValueGraph;
import vis::CytoDot;
import ParseTree;
import IO;
import Content;
import lang::html::IO;
import lang::html::AST;

//syntax Ambiguous = A? | B?;
//syntax A = "a";
//syntax B = "a";

private Content showGraph(value v, str name, ValueToGraphConfig config = valueToGraphConfig()) {
    //println("graph: <valueToGraph(v, config=config)>");
    return content(name, dotServer(valueToGraph(v, config=config)));
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

/*Content testParseTreeWithAmbiguities() {
    Tree t = parse(#Ambiguous, "a", allowAmbiguity=true);
    return showGraph(t, "ambParseTree", config=createParseTreeConfig(collapseTokens=true, filterLayout=true, filterMissingOptionals=true));
}
*/

Content testGraphviz() {
  return html(writeHTMLString(html([
    head([            
        style([\data("#visualization {
                         '  width: 100%;
                         '  height: 100%;
                         '  position: absolute;
                         '  top: 0px;
                         '  left: 0px;
                         '}")])
    ]),
    body([
        div([], id="visualization"),
        script([
            \data("
            'import { Graphviz } from \"https://cdn.jsdelivr.net/npm/@hpcc-js/wasm/dist/index.js\";
            'console.log(\"starting\");
            'if (Graphviz) {
            '    const graphviz = await Graphviz.load();
            '    const dot = \"digraph G { Hello -\> World }\";
            '    const svg = graphviz.layout(dot, \"svg\", \"dot\");
            '    document.getElementById(\"visualization\").innerHTML = svg;
            '} 
            '")
        ], \type="module")
    ])
  ])));

/*
const graphviz = await Graphviz.load();

const dot = \"digraph G { Hello -\> World }\";
const svg = graphviz.dot(dot);
");
*/
}

/*
    <div id="ESM"></div>
    <script type="module">
        import { Graphviz } from "https://cdn.jsdelivr.net/npm/@hpcc-js/wasm/dist/index.js";
        // import { Graphviz } from "./packages/wasm/dist/index.js";
        if (Graphviz) {
            const graphviz = await Graphviz.load();
            const svg = graphviz.layout(dot, "svg", "dot");
            document.getElementById("ESM").innerHTML = svg;
        } 
    </script>

*/