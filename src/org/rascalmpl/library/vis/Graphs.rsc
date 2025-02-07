@license{
  Copyright (c) 2022 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - storm@cwi.nl - CWI}
@synopsis{Simple data visualization using graphs; based on cytoscape.js}
@description{
This modules provides a simple API to create graph visuals for Rascal
(relational) data, based on [Cytoscape.js](https://js.cytoscape.org/). 

This module is quite new and may undergo some tweaks in the coming time.
}

@benefits{
* Easy to use for basic graph layouts.
}
module vis::Graphs

import lang::html::IO;
import lang::html::AST;
import util::IDEServices;
import Content;
import ValueIO;
import Set;

@synopsis{Optional configuration attributes for graph style and graph layout}
@description{
These configuration options are used to map input graph data to layout properties
and style properties. 

* title - does what it says
* nodeLinker - makes nodes clickable by providing an editor location
* nodeLabeler - allows simplification or elaboration on node labels beyond their identity string
* nodeClassifier - labels nodes with classes in order to later select them for specific styling
* edgeLabeler - allows simplification or elaboration on edge labels 
* layout - defines and configured the graph layout algorithm
* nodeStyle - defines the default style for all nodes
* edgeStyle - defines the default style for all edges
* style - collects specific styles for specific ((CytoSelector)) edge/node selectors using ((CytoStyleOf)) tuples.

Typically the functions passed into this configuration are closures that capture and use the original
input data to find out about where to link and how to classify. The `&T` parameter reflects the type of
the original input `Graph[&T]`; so that is the type of the nodes. Often this would be `loc` or `str`.
}
@examples{

Let's experiment with a number of styling parameters based on the shape of a graph:
```rascal-shell
import vis::Graphs;
// let's play with the geneology of the "Simpsons"
g = {
    <"Abraham Simpson", "Homer Simpson">,
    <"Mona Simpson", "Homer Simpson">,
    <"Homer Simpson", "Bart Simpson">,
    <"Homer Simpson", "Lisa Simpson">,
    <"Homer Simpson", "Maggie Simpson">,
    <"Marge Simpson", "Bart Simpson">,
    <"Marge Simpson", "Lisa Simpson">,
    <"Marge Simpson", "Maggie Simpson">,
    <"Bart Simpson", "Rod Flanders">,
    <"Bart Simpson", "Todd Flanders">,
    <"Lisa Simpson", "Bart Simpson">,
    <"Abraham Simpson", "Patty Bouvier">,
    <"Abraham Simpson", "Selma Bouvier">,
    <"Mona Simpson", "Patty Bouvier">,
    <"Mona Simpson", "Selma Bouvier">
};
// visualizing this without styling:
graph(g);
// to style nodes, let's select some special nodes and "classify" them first. We reuse some generic graph analysis tools.
import analysis::graphs::Graph;
list[str] nodeClassifier(str simpson) = [
  *["top" | simpson in top(g)],
  *["bottom" | simpson in bottom(g)]
];
// once classified, we can style each node according to their assigned classes. Nodes can be in more than one class.
styles = [
    cytoStyleOf( 
        selector=or([\node(className("top")),\node(className("bottom"))]),
        style=defaultNodeStyle()[shape=CytoNodeShape::diamond()]
    )
];
// we pick a sensible layout
lyt = defaultDagreLayout();
// we wrap the styling information into a configuration wrapper:
cfg = cytoGraphConfig(nodeClassifier=nodeClassifier, styles=styles, \layout=lyt);
// and now we see the effect:
graph(g, cfg=cfg)
// now let's style some edges:
list[str] edgeClassifier(str from, str to) = ["grandparent" | <from, to > in g o g];
// add another styling element
styles += [
    cytoStyleOf( 
        selector=edge(className("grandparent")),
        style=defaultEdgeStyle()[\line-style="dashed"]
    )
];
// and draw again (while adding the grandparent edges too)
graph(g + (g o g), cytoGraphConfig(nodeClassifier=nodeClassifier, edgeClassifier=edgeClassifier, styles=styles, \layout=lyt))
```    
}
data CytoGraphConfig = cytoGraphConfig(
    str title="Graph", 

    NodeLinker[&T]     nodeLinker     = defaultNodeLinker, 
    NodeLabeler[&T]    nodeLabeler    = defaultNodeLabeler,  
    NodeClassifier[&T] nodeClassifier = defaultNodeClassifier, 
    EdgeLabeler[&T]    edgeLabeler    = defaultEdgeLabeler, 
    EdgeClassifier[&T] edgeClassifier = defaultEdgeClassifier,
    
    CytoLayout \layout  = defaultCoseLayout(), 

    CytoStyle nodeStyle      = defaultNodeStyle(), 
    CytoStyle edgeStyle      = defaultEdgeStyle(), 
    list[CytoStyleOf] styles = []
);

@synopsis{A NodeLinker maps node identities to a source location to link to}
alias NodeLinker[&T] = loc (&T _id1);

@synopsis{The default node linker assumes any loc found in the node identity is a proper link.}
loc defaultNodeLinker(/loc l) = l;
default loc defaultNodeLinker(&T _) = |nothing:///|;

@synopsis{A NodeLabeler maps node identies to descriptive node labels}
alias NodeLabeler[&T]= str (&T _id2);

@synopsis{The default node labeler searches for any `str`` in the identity, or otherwise a file name of a `loc`}
str defaultNodeLabeler(/str s) = s;
str defaultNodeLabeler(loc l)  = l.file != "" ? l.file : "<l>";
default str defaultNodeLabeler(&T v) = "<v>";

@synopsis{A NodeClassifier maps node identities to classes that are used later to select specific layout and coloring options.}
alias NodeClassifier[&T] = list[str] (&T _id3);

@synopsis{The default classifier produces no classes}
list[str] defaultNodeClassifier(&T _) = [];

@synopsis{An EdgeClassifier maps edge identities to classes that are used later to select specific layout and coloring options.}
alias EdgeClassifier[&T] = list[str] (&T _from, &T _to);

@synopsis{The default edge classifier produces no classes}
list[str] defaultEdgeClassifier(&T _, &T _) = [];

@synopsis{An EdgeLabeler maps edge identies to descriptive edge labels.}
alias EdgeLabeler[&T]= str (&T _source, &T _target);

@synopsis{The default edge labeler returns the empty label for all edges.}
str defaultEdgeLabeler(&T _source, &T _target)  = "";


@synopsis{A graph plot from a binary list relation.}
@examples{
```rascal-shell
import vis::Graphs;
graph([<x,x+1> | x <- [1..100]] + [<100,1>])
graph([<x,x+1> | x <- [1..100]] + [<100,1>], cfg=cytoGraphConfig(\layout=\defaultCircleLayout()))
```

Providing locations as node identities automatically transforms them to node links:
```rascal-shell
import vis::Graphs;
import IO;
d = [<|std:///|, e> | e <- |std:///|.ls];
d += [<e,f> | <_, e> <- d, isDirectory(e), f <- e.ls];
graph(d, \layout=defaultDagreLayout());
// here we adapt the node labeler to show only the last file name in the path of the location:
graph(d, \layout=defaultDagreLayout(), cfg=cytoGraphConfig(nodeLabeler=str (loc l) { return l.file; }));
```
}
Content graph(lrel[&T x, &T y] v, CytoGraphConfig cfg = cytoGraphConfig()) 
    = content(cfg.title, graphServer(cytoscape(graphData(v, cfg=cfg))));

@synopsis{A graph plot from a ternary list relation where the middle column is the edge label.}
@examples{
```rascal-shell
import vis::Graphs;
graph([<x,2*x+1,x+1> | x <- [1..100]] + [<100,101,1>])
```
}
Content graph(lrel[&T x, &L edge, &T y] v, CytoGraphConfig cfg=cytoGraphConfig()) 
    = content(cfg.title, graphServer(cytoscape(graphData(v, cfg=cfg), cfg=cfg)));

@synopsis{A graph plot from a binary relation.}
@examples{
```rascal-shell
import vis::Graphs;
graph({<x,x+1> | x <- [1..100]} + {<100,1>})
``` 
}
Content graph(rel[&T x, &T y] v, CytoGraphConfig cfg=cytoGraphConfig()) 
    = content(cfg.title, graphServer(cytoscape(graphData(v, cfg=cfg), cfg=cfg)));

@synopsis{A graph plot from a ternary relation where the middle column is the edge label.}
@examples{
```rascal-shell
import vis::Graphs;
graph({<x,2*x+1,x+1> | x <- [1..100]} + {<100,101,1>})
```
}
Content graph(rel[&T x, &L edge, &T y] v, CytoGraphConfig cfg=cytoGraphConfig()) 
    = content(cfg.title, graphServer(cytoscape(graphData(v, cfg=cfg), cfg=cfg)));

@synopsis{This core workhorse mixes the graph data with the configuration to obtain visualizable CytoScape.js data-structure.}
@description{
This data-structure is serialized to JSON and communicated directly to initialize cytoscape.js.
The serialization is done by the generic ((lang::json::IO)) library under the hood of a ((util::Webserver)).
}@synopsis{Produces an overall cytoscape.js wrapper which is sent as JSON to the client side.}
Cytoscape cytoscape(list[CytoData] \data, CytoGraphConfig cfg=cytoGraphConfig())
    = cytoscape(
        elements=\data,        
        style=[
            cytoNodeStyleOf(cfg.nodeStyle),
            cytoEdgeStyleOf(cfg.edgeStyle),
            *cfg.styles
        ],
        \layout=cfg.\layout
    );

@synopsis{Turns a `rel[loc from, loc to]` into a graph}
list[CytoData] graphData(rel[loc x, loc y] v, CytoGraphConfig cfg=cytoGraphConfig())
    = [cytodata(\node("<e>", label=cfg.nodeLabeler(e), editor="<cfg.nodeLinker(e)>"), classes=cfg.nodeClassifier(e)) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label=cfg.edgeLabeler(from, to)), classes=cfg.edgeClassifier(from,to)) | <from, to> <- v]
      ;

@synopsis{Turns any `rel[&T from, &T to]` into a graph}
default list[CytoData] graphData(rel[&T x, &T y] v, CytoGraphConfig cfg=cytoGraphConfig())
    = [cytodata(\node("<e>", label=cfg.nodeLabeler(e), editor="<cfg.nodeLinker(e)>"), classes=cfg.nodeClassifier(e)) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label=cfg.edgeLabeler(from, to)), classes=cfg.edgeClassifier(from,to)) | <from, to> <- v]
      ;

@synopsis{Turns any `lrel[loc from, &L edge, loc to]` into a graph}
list[CytoData] graphData(lrel[loc x, &L edge, loc y] v, CytoGraphConfig cfg=cytoGraphConfig())
    = [cytodata(\node("<e>", label=cfg.nodeLabeler(e), editor="<cfg.nodeLinker(e)>"), classes=cfg.nodeClassifier(e)) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label="<e>"), classes=cfg.edgeClassifier(from,to)) | <from, e, to> <- v]
      ;

@synopsis{Turns any `lrel[&T from, &L edge, &T to]` into a graph}
default list[CytoData] graphData(lrel[&T x, &L edge, &T y] v, CytoGraphConfig cfg=cytoGraphConfig())
    = [cytodata(\node("<e>", label=cfg.nodeLabeler(e), editor="<cfg.nodeLinker(e)>"), classes=cfg.nodeClassifier(e)) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label="<e>"), classes=cfg.edgeClassifier(from,to)) | <from, e, to> <- v]
      ;

@synopsis{Turns any `lrel[loc from, loc to]` into a graph}
list[CytoData] graphData(lrel[loc x, loc y] v, CytoGraphConfig cfg=cytoGraphConfig())
    = [cytodata(\node("<e>", label=cfg.nodeLabeler(e), editor="<cfg.nodeLinker(e)>"), classes=cfg.nodeClassifier(e)) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label=cfg.edgeLabeler(from, to)), classes=cfg.edgeClassifier(from,to)) | <from, to> <- v]
      ;

@synopsis{Turns any `lrel[&T from, &T to]` into a graph}
default list[CytoData] graphData(lrel[&T x, &T y] v, CytoGraphConfig cfg=cytoGraphConfig())
    = [cytodata(\node("<e>", label=cfg.nodeLabeler(e), editor="<cfg.nodeLinker(e)>"), classes=cfg.nodeClassifier(e)) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label=cfg.edgeLabeler(from, to)), classes=cfg.edgeClassifier(from,to)) | <from, to> <- v]
      ;

@synopsis{Turns any `rel[loc from, &L edge, loc to]` into a graph}
list[CytoData] graphData(rel[loc x, &L edge, loc y] v, CytoGraphConfig cfg=cytoGraphConfig())
    = [cytodata(\node("<e>", label=cfg.nodeLabeler(e), editor="<cfg.nodeLinker(e)>"), classes=cfg.nodeClassifier(e)) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label="<e>"), classes=cfg.edgeClassifier(from,to)) | <from, e, to> <- v]
      ;

@synopsis{Turns any `rel[&T from, &L edge, &T to]` into a graph}
default list[CytoData] graphData(rel[&T x, &L edge, &T y] v, CytoGraphConfig cfg=cytoGraphConfig())
    = [cytodata(\node("<e>", label=cfg.nodeLabeler(e), editor="<cfg.nodeLinker(e)>"), classes=cfg.nodeClassifier(e)) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label="<e>"), classes=cfg.edgeClassifier(from,to)) | <from, e, to> <- v]
      ;

data CytoNodeShape
    = \ellipse()
    | \triangle()
    | \round-triangle()
    | \rectangle()
    | \round-rectangle()
    | \bottom-round-rectangle()
    | \cut-rectangle()
    | \barrel()
    | \rhomboid()
    | \diamond()
    | \round-diamond()
    | \pentagon()
    | \round-pentagon()
    | \hexagon()
    | \round-hexagon()
    | \concave-hexagon()
    | \heptagon()
    | \round-heptagon()
    | \octagon()
    | \round-octagon()
    | \star()
    | \tag()
    | \round-tag()
    | \vee()
    | \polygon()
    ;

@synopsis{Overall cytoscape.js object for sending to the client side.}
data Cytoscape 
    = cytoscape(
        list[CytoData] elements = [],
        list[CytoStyleOf] style=[],
        CytoLayout \layout = cytolayout()
    );

data CytoData
  = cytodata(CytoElement \data, list[str] classes=[]);

data CytoElement
  = \node(str id, str label=id, str editor="|none:///|")
  | \edge(str source, str target, str id="<source>-<target>", str label="")
  ;

data CytoHorizontalAlign
    = left()
    | center()
    | right()
    | auto()
    ;

data CytoVerticalAlign
    = top()
    | center()
    | bottom()
    | auto()
    ;

data CytoArrowHeadStyle
    = triangle()
    | \triangle-tee()
    | \circle-triangle()
    | \triangle-cross()
    | \triangle-backcurve()
    | vee()
    | tee()
    | square()
    | circle()
    | diamond()
    | chevron()
    | none()
    ;

data CytoTextWrap
    = none()
    | wrap()
    | ellipses()
    ;

data CytoCurveStyle
    = bezier()
    | \unbundled-bezier()
    | straight()
    | segments()
    | \round-segments()
    | \straight-triangle()
    | taxi()
    | \round-taxi()
    | haystack()
    ;

data CytoStyleOf
    = cytoStyleOf(
        CytoSelector selector = \node(),
        CytoStyle style = cytoNodeStyle()
    )
    ;

CytoStyleOf cytoNodeStyleOf(CytoStyle style) = cytoStyleOf(selector=\node(), style=style);
CytoStyleOf cytoEdgeStyleOf(CytoStyle style) = cytoStyleOf(selector=\edge(), style=style);

@synopsis{Instantiates a default node style}
@description{
Because the JSON writer can not instantiate default values for keyword fields,
we have to do it manually here.
}
CytoStyle defaultNodeStyle()
    = cytoNodeStyle(
        visibility        = "visible", /* hidden, collapse */
        opacity           = "1",
        width             = "label",
        height            = "label",
        padding           = "10pt",
        \background-color = "blue",
        color             = "white",
        \font-size        = "20pt",
        \font-weight      = bold(),
        label             = "data(label)",
        shape             = \round-rectangle(),
        \text-halign      = CytoHorizontalAlign::\center(),
        \text-valign      = CytoVerticalAlign::\center()
    );

@synopsis{Instantiates a default edge style}
@description{
Because the JSON writer can not instantiate default values for keyword fields
we have to do it manually here.
}
CytoStyle defaultEdgeStyle()
    = cytoEdgeStyle(
        visibility          = "visible", /* hidden, collapse */
        opacity             = "1",
        \line-opacity       = "1",
        width               = 3,
        \line-style         = "solid", /* dotted, dashed */
        \color              = "red",
        \line-color         = "black",
        \target-arrow-color = "black",
        \source-arrow-color = "black",
        \target-arrow-shape = CytoArrowHeadStyle::triangle(),
        \source-arrow-shape = CytoArrowHeadStyle::none(),
        \curve-style        = \unbundled-bezier(),
        \label              = "data(label)"
    );

data CytoFontWeight
    = normal()
    | lighter()
    | bold()
    | bolder()
    ;

data CytoStyle
    = cytoNodeStyle(
        str visibility          = "visible", /* hidden, collapse */
        str opacity             = "1",
        str width               = "label",
        str height              = "label",
        str padding             = "10pt",
        str color               = "white",
        str \text-opacity       = "1",
        str \font-family        = "",
        str \font-size          = "12pt",
        str \font-style         = "",
        CytoFontWeight \font-weight = normal(),
        str \background-color   = "blue",
        str label               = "data(label)",
        CytoNodeShape shape     = CytoNodeShape::ellipse(),
        CytoHorizontalAlign \text-halign = CytoHorizontalAlign::center(),
        CytoVerticalAlign \text-valign = CytoVerticalAlign::\top(),
        CytoTextWrap \text-wrap = CytoTextWrap::none(),
        str \text-max-width     = "100px",
        CytoHorizontalAlign \text-justification = CytoHorizontalAlign::center(),
        int \line-height        = 1
    )
    | cytoEdgeStyle(
        str visibility          = "visible", /* hidden, collapse */
        str opacity             = "1",
        str \line-opacity       = "1",
        int width               = 3,
        str \line-color         = "black",
        str \line-style         = "solid", /* dotted, dashed */
        str color               = "red",
        str \target-arrow-color = "black",
        str \source-arrow-color = "black",
        CytoArrowHeadStyle \target-arrow-shape = CytoArrowHeadStyle::triangle(),
        CytoArrowHeadStyle \source-arrow-shape = CytoArrowHeadStyle::none(),
        CytoCurveStyle \curve-style            = CytoCurveStyle::\unbundled-bezier(),
        int \source-text-offset = 1,
        int \target-text-offset = 1,
        str label               = "data(label)"
    )
    ;
    
@synopsis{A combinator language that translates down to strings in JSON}
@description{
* For field names you can use the names, or the dot notation for array indices and fields of objects: `"labels.0"`, `"name.first"`.
* `and` and `or` can not be nested; this will lead to failure to select anything at all. The or must be outside and the and must be inside.
* `node()` selects all nodes
* `edge()` selects all edges
}    
data CytoSelector
    = \node()
    | \edge()
    | \id(str id)
    | \and(list[CytoSelector] conjuncts)
    | \or(list[CytoSelector] disjuncts)
    | \equal(str field, str \value)
    | \equal(str field, int limit)
    | \greater(str field, int limit)
    | \less(str field, int limit)
    | \greaterEqual(str field, int limit)
    | \lessEqual(str field, int limit) 
    | \className(str)
    ;

@synopsis{Short-hand for a node with a single condition}
CytoSelector \node(CytoSelector condition) = and([\node(), condition]);

@synopsis{Short-hand for a node with a single condition}
CytoSelector \edge(CytoSelector condition) = and([\edge(), condition]);

@synopsis{Utility to generate class attributes with multiple names consistently.}
str more(set[str] names) = "<for (str n <- sort(names)) {><n> <}>"[..-1];

@synopsis{Serialize a ((CytoSelector)) to string for client side expression.}
str formatCytoSelector(\node()) = "node";
str formatCytoSelector(\edge()) = "edge";
str formatCytoSelector(\id(str i)) = formatCytoSelector(equal("id", i));
str formatCytoSelector(and(list[CytoSelector] cjs)) = "<for (cj <- cjs) {><formatCytoSelector(cj)><}>";
str formatCytoSelector(or(list[CytoSelector] cjs)) = "<for (cj <- cjs) {><formatCytoSelector(cj)>,<}>"[..-1];
str formatCytoSelector(className(str class)) = ".<class>";
str formatCytoSelector(equal(str field, str val)) = "[<field> = \"<val>\"]";
str formatCytoSelector(equal(str field, int lim)) = "[<field> = <lim>]";
str formatCytoSelector(greater(str field, int lim)) = "[<field> \> <lim>]";
str formatCytoSelector(greaterEqual(str field, int lim)) = "[<field> \>= <lim>]";
str formatCytoSelector(lessEqual(str field, int lim)) = "[<field> \<= <lim>]";
str formatCytoSelector(less(str field, int lim)) = "[<field> \< <lim>]";

@synopsis{Choice of different node layout algorithms.}
@description{
The different algorithms use heuristics to find a layout
that shows the structure of a graph best. Different types
of graph data call for different algorithms:
* `grid` is best when there are very few edges or when edges are not important. The edge relation
is not used at all for deciding where each node will end up. Grid 
is typically used for an initial exploration of the graph. It is very fast.
* `circle` puts all nodes on the edge of a circle and draws edges between them. The order on the
circle is arbitrary. This layout fails on larger collections of nodes because the points on the 
circle will become really small and indistinguishable. However for graphs with less than 100 nodes 
it provides a quick and natural overview.
* `breadthfirst` computes a breadthfirst spanning tree, and uses path length to decide on which
layer each node will reside. Cross-edges (between branches) and back-edges are allowed but if there
are many the graph will be messy. So this layout is best when you have a mostly hierarchical graph.
Examples are flow charts and dependency graphs.
* `cose` is a so-called "force-directed" layout. The edges become springs that both push nodes
apart as well as pull them together. Nodes drag on the surface but have an initial momentum such
that they can find a spot on the plain. This layout is very natural for scale-free networks such
as biological organisms, friends graphs and software ecosystems.
}
data CytoLayoutName
    = grid()
    | circle()
    | breadthfirst()
    | cose()
    | dagre()
    | klay()
    ;

@synopsis{An alias for dagre layout for documentation purposes.}
@description{
Dagre is a hierarchical graph layout.
}
CytoLayoutName hierarchical() = dagre();

data CytoLayout(CytoLayoutName name = dagre(), bool animate=false)
    = cytolayout()
    | breadthfirstLayout(
        CytoLayoutName name = CytoLayoutName::breadthfirst(),
        num spacingFactor= 1,
        list[str] roots = [],
        bool circle=false,
        bool grid=!circle,
        bool directed=false
    )
    | gridLayout(
        CytoLayoutName name = CytoLayoutName::grid(),
        int rows=2,
        int cols=2,
        bool avoidOverlap=true,
        num spacingFactor=.1
    )
    | circleLayout(
        CytoLayoutName name = CytoLayoutName::circle(),
        bool avoidOverlap=true,
        num spacingFactor=.1
    )
    | coseLayout(
        CytoLayoutName name = cose()
    )
    | dagreLayout(
        CytoLayoutName name = dagre(),
        num spacingFactor = .1,
        num nodeSep = 1000,
        DagreRanker ranker = \network-simplex() // network-simples tight-tree, or longest-path
    )
    | klayLayout (
        CytoLayoutName name = klay()
    )
    ;

data DagreRanker
    = \network-simplex()
    | \tight-tree()
    | \longest-path()
    ;

CytoLayout defaultCoseLayout()
    = coseLayout(
        name=cose(),
        animate=false
    )
    ;

CytoLayout defaultCircleLayout(bool avoidOverlap=true, num spacingFactor=.1)
    = circleLayout(
        name = CytoLayoutName::circle(),
        animate=false,
        avoidOverlap=avoidOverlap,
        spacingFactor=spacingFactor
    );

CytoLayout defaultGridLayout(int rows=2, int cols=rows, bool avoidOverlap=true, num spacingFactor=.1)
    = gridLayout(
        name=CytoLayoutName::grid(),
        animate=false,
        rows=rows,
        cols=cols,
        avoidOverlap=avoidOverlap,
        spacingFactor=spacingFactor
    )
    ;

CytoLayout defaultBreadthfirstLayout(num spacingFactor=.1, bool circle=false, bool grid=!circle, bool directed=false)
    = 
    breadthfirstLayout(
        name=CytoLayoutName::breadthfirst(),
        animate=false,
        spacingFactor=spacingFactor,
        circle=circle,
        grid=grid,
        directed=directed
    );

CytoLayout defaultDagreLayout(num spacingFactor=1)
    = dagreLayout(
        name=CytoLayoutName::dagre(),
        animate=false,
        spacingFactor=spacingFactor,
        ranker=\network-simplex()
    );

CytoLayout defaultKlayLayout()
    = klayLayout(
        name=CytoLayoutName::klay()
    );

@synopsis{this is the main server generator for any graph value}
@description{
Given a Graph value this server captures the value and serves it
as a JSON value to the HTML client generated by ((vis::Graphs::plotHTML)).
}
Response (Request) graphServer(Cytoscape ch) {
    Response reply(get(/^\/editor/, parameters=pms)) {
        if (pms["src"]?) {
            edit(readTextValueString(#loc, pms["src"]));
            return response(writeHTMLString(text("done")));
        }

        return response(writeHTMLString(text("could not edit <pms>")));
    }

    Response reply(get(/^\/cytoscape/)) {
        return response(ch, formatCytoSelector);
    }

    // returns the main page that also contains the callbacks for retrieving data and configuration
    default Response reply(get(_)) {
        return response(writeHTMLString(plotHTML()));
    }

    return reply;
}

@synopsis{default HTML wrapper for a cytoscape.js graph}
@description{
This client features:
* cytoscape.js loading with cytoscape-dagre and dagre present.
* fetching of graph data via `http://localhost/cytoscape` URL
* clickable links in every node that has an 'editor' data field that holds a `loc`, via the `http://localhost/editor?src=loc` URL
* full screen graph view

This client mirrors the server defined by ((graphServer)).
}
private HTMLElement plotHTML()
    = html([
        head([ 
            script([], src="https://cdnjs.cloudflare.com/ajax/libs/cytoscape/3.28.1/cytoscape.umd.js"),
            script([], src="https://cdnjs.cloudflare.com/ajax/libs/dagre/0.8.5/dagre.min.js"),
            script([], src="https://cdn.jsdelivr.net/npm/cytoscape-dagre@2.5.0/cytoscape-dagre.min.js"),
            script([], src="https://cdn.jsdelivr.net/npm/klayjs@0.4.1/klay.min.js"),
            script([], src="https://cdn.jsdelivr.net/npm/cytoscape-klay@3.1.4/cytoscape-klay.min.js"),
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
                \data(
                    "fetch(\'/cytoscape\').then(resp =\> resp.json()).then(cs =\> {
                    '   cs.container = document.getElementById(\'visualization\');
                    '   const cy = cytoscape(cs);
                    '   cy.on(\'tap\', \'node\', function (evt) {
                    '       var n = evt.target;
                    '       if (n.data(\'editor\') !== undefined) {
                    '           fetch(\'/editor?\' + new URLSearchParams({
                    '                src: n.data(\'editor\')
                    '           })) ;
                    '       }
                    '   });
                    '});
                    '")
            ], \type="text/javascript")
        ])
    ]);
