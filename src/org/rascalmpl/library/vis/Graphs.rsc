@license{
  Copyright (c) 2022 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - storm@cwi.nl - CWI}
@synopsis{Simple data visualization using graphs}
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

@synopsis{A graph plot from a binary list relation.}
@examples{
```rascal-shell
import vis::Graphs;
graph([<x,x+1> | x <- [1..100]] + [<100,1>])
graph([<x,x+1> | x <- [1..100]] + [<100,1>], \layout=\defaultCircleLayout())
```

Providing locations as node identities automatically transforms them to node links:
```rascal-shell
import vis::Graphs;
import IO;
d = [<|std:///|, e> | e <- |std:///|.ls];
d += [<e,f> | <_, e> <- d, isDirectory(e), f <- e.ls];
graph(d, \layout=defaultDagreLayout());
// here we adapt the node labeler to show only the last file name in the path of the location:
graph(d, \layout=defaultDagreLayout(), nodeLabeler=str (loc l) { return l.file; });
```
}
Content graph(lrel[&T x, &T y] v, NodeLinker[&T] nodeLinker=defaultNodeLinker, NodeLabeler[&T] nodeLabeler=defaultNodeLabeler, EdgeLabeler[&T] edgeLabeler=defaultEdgeLabeler, str title="Graph", CytoLayout \layout=defaultCoseLayout(), CytoStyle nodeStyle=defaultNodeStyle(), CytoStyle edgeStyle=defaultEdgeStyle()) 
    = content(title, graphServer(cytoscape(graphData(v, nodeLinker=nodeLinker, nodeLabeler=nodeLabeler, edgeLabeler=edgeLabeler), \layout=\layout, nodeStyle=nodeStyle, edgeStyle=edgeStyle)));

@synopsis{A graph plot from a ternary list relation where the middle column is the edge label.}
@examples{
```rascal-shell
import vis::Graphs;
graph([<x,2*x+1,x+1> | x <- [1..100]] + [<100,101,1>])
```
}
Content graph(lrel[&T x, &L edge, &T y] v, NodeLinker[&T] nodeLinker=defaultNodeLinker, NodeLabeler[&T] nodeLabeler=defaultNodeLabeler, str title="Graph", CytoLayout \layout=defaultCoseLayout(), CytoStyle nodeStyle=defaultNodeStyle(), CytoStyle edgeStyle=defaultEdgeStyle()) 
    = content(title, graphServer(cytoscape(graphData(v, nodeLinker=nodeLinker, nodeLabeler=nodeLabeler), \layout=\layout, nodeStyle=nodeStyle, edgeStyle=edgeStyle)));

@synopsis{A graph plot from a binary relation.}
@examples{
```rascal-shell
import vis::Graphs;
graph({<x,x+1> | x <- [1..100]} + {<100,1>})
```
}
Content graph(rel[&T x, &T y] v, NodeLinker[&T] nodeLinker=defaultNodeLinker, NodeLabeler[&T] nodeLabeler=defaultNodeLabeler, EdgeLabeler[&T] edgeLabeler=defaultEdgeLabeler, str title="Graph", CytoLayout \layout=defaultCoseLayout(), CytoStyle nodeStyle=defaultNodeStyle(), CytoStyle edgeStyle=defaultEdgeStyle()) 
    = content(title, graphServer(cytoscape(graphData(v, nodeLinker=nodeLinker, nodeLabeler=nodeLabeler, edgeLabeler=edgeLabeler), \layout=\layout, nodeStyle=nodeStyle, edgeStyle=edgeStyle)));

@synopsis{A graph plot from a ternary relation where the middle column is the edge label.}
@examples{
```rascal-shell
import vis::Graphs;
graph({<x,2*x+1,x+1> | x <- [1..100]} + {<100,101,1>})
```
}
Content graph(rel[&T x, &L edge, &T y] v, NodeLinker[&T] nodeLinker=defaultNodeLinker, NodeLabeler[&T] nodeLabeler=defaultNodeLabeler, str title="Graph", CytoLayout \layout=defaultCoseLayout(), CytoStyle nodeStyle=defaultNodeStyle(), CytoStyle edgeStyle=defaultEdgeStyle()) 
    = content(title, graphServer(cytoscape(graphData(v, nodeLinker=nodeLinker, nodeLabeler=nodeLabeler), \layout=\layout, nodeStyle=nodeStyle, edgeStyle=edgeStyle)));

alias NodeLinker[&T] = loc (&T _id1);
loc defaultNodeLinker(loc l) = l;
default loc defaultNodeLinker(&T _) = |nothing:///|;

alias NodeLabeler[&T]= str (&T _id2);
str defaultNodeLabeler(&T v) = "<v>";

alias EdgeLabeler[&T]= str (&T _source, &T _target);
str defaultEdgeLabeler(&T _source, &T _target)  = "";


Cytoscape cytoscape(list[CytoData] \data, \CytoLayout \layout=\defaultCoseLayout(), CytoStyle nodeStyle=defaultNodeStyle(), CytoStyle edgeStyle=defaultEdgeStyle())
    = cytoscape(
        elements=\data,        
        style=[
            cytoNodeStyleOf(nodeStyle),
            cytoEdgeStyleOf(edgeStyle)
        ],
        \layout=\layout
    );

list[CytoData] graphData(rel[loc x, loc y] v, NodeLinker[loc] nodeLinker=defaultNodeLinker, NodeLabeler[loc] nodeLabeler=defaultNodeLabeler, EdgeLabeler[loc] edgeLabeler=defaultEdgeLabeler)
    = [cytodata(\node("<e>", label=nodeLabeler(e), editor="<nodeLinker(e)>")) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label=edgeLabeler(from, to))) | <from, to> <- v]
      ;

default list[CytoData] graphData(rel[&T x, &T y] v, NodeLinker[&T] nodeLinker=defaultNodeLinker,  NodeLabeler[&T] nodeLabeler=defaultNodeLabeler, EdgeLabeler[&T] edgeLabeler=defaultEdgeLabeler)
    = [cytodata(\node("<e>", label=nodeLabeler(e), editor="<nodeLinker(e)>")) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label=edgeLabeler(from, to))) | <from, to> <- v]
      ;

list[CytoData] graphData(lrel[loc x, &L edge, loc y] v, NodeLinker[&T] nodeLinker=defaultNodeLinker, NodeLabeler[&T] nodeLabeler=defaultNodeLabeler, EdgeLabeler[&T] edgeLabeler=defaultEdgeLabeler)
    = [cytodata(\node("<e>", label=nodeLabeler(e), editor="<nodeLinker(e)>")) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label="<e>")) | <from, e, to> <- v]
      ;

default list[CytoData] graphData(lrel[&T x, &L edge, &T y] v, NodeLinker[&T] nodeLinker=defaultNodeLinker, NodeLabeler[&T] nodeLabeler=defaultNodeLabeler, EdgeLabeler[&T] edgeLabeler=defaultEdgeLabeler)
    = [cytodata(\node("<e>", label=nodeLabeler(e), editor="<nodeLinker(e)>")) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label="<e>")) | <from, e, to> <- v]
      ;

list[CytoData] graphData(lrel[loc x, loc y] v, NodeLinker[loc] nodeLinker=defaultNodeLinker, NodeLabeler[loc] nodeLabeler=defaultNodeLabeler, EdgeLabeler[loc] edgeLabeler=defaultEdgeLabeler)
    = [cytodata(\node("<e>", label=nodeLabeler(e), editor="<nodeLinker(e)>")) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label=edgeLabeler(from, to))) | <from, to> <- v]
      ;

default list[CytoData] graphData(lrel[&T x, &T y] v, NodeLinker[&T] nodeLinker=defaultNodeLinker, NodeLabeler[&T] nodeLabeler=defaultNodeLabeler, EdgeLabeler[&T] edgeLabeler=defaultEdgeLabeler)
    = [cytodata(\node("<e>", label=nodeLabeler(e), editor="<nodeLinker(e)>")) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label=edgeLabeler(from, to))) | <from, to> <- v]
      ;

list[CytoData] graphData(rel[loc x, &L edge, loc y] v, NodeLinker[loc] nodeLinker=defaultNodeLinker, NodeLabeler[loc] nodeLabeler=defaultNodeLabeler, EdgeLabeler[&T] edgeLabeler=defaultEdgeLabeler)
    = [cytodata(\node("<e>", label=nodeLabeler(e), editor="<nodeLinker(e)>")) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label="<e>")) | <from, e, to> <- v]
      ;

default list[CytoData] graphData(rel[&T x, &L edge, &T y] v, NodeLinker[&T] nodeLinker=defaultNodeLinker, NodeLabeler[&T] nodeLabeler=defaultNodeLabeler, EdgeLabeler[&T] edgeLabeler=defaultEdgeLabeler)
    = [cytodata(\node("<e>", label=nodeLabeler(e), editor="<nodeLinker(e)>")) | e <- {*v<x>, *v<y>}] +
      [cytodata(\edge("<from>", "<to>", label="<e>")) | <from, e, to> <- v]
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

data Cytoscape 
    = cytoscape(
        list[CytoData] elements = [],
        list[CytoStyleOf] style=[],
        CytoLayout \layout = cytolayout()
    );

data CytoData
  = cytodata(CytoElement \data);

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
    | \straight-triangle()
    | taxi()
    | haystack()
    ;

data CytoStyleOf
    = cytoNodeStyleOf(
        CytoSelector selector = \node(),
        CytoStyle style = cytoNodeStyle()
    )
    | cytoEdgeStyleOf(
        CytoSelector selector = \edge(),
        CytoStyle style = cytoEdgeStyle()
    );

CytoStyleOf cytoNodeStyleOf(CytoStyle style) = cytoNodeStyleOf(selector=\node(), style=style);
CytoStyleOf cytoEdgeStyleOf(CytoStyle style) = cytoEdgeStyleOf(selector=\edge(), style=style);

CytoStyle defaultNodeStyle()
    = cytoNodeStyle(
        width             = "label",
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

CytoStyle defaultEdgeStyle()
    = cytoEdgeStyle(
        width               = 3,
        \color              = "red",
        \line-color         = "black",
        \target-arrow-color = "black",
        \source-arrow-color = "black",
        \target-arrow-shape = triangle(),
        \source-arrow-shape = CytoArrowHeadStyle::none(),
        \curve-style        = bezier(),
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
        str width               = "label",
        str padding             = "10pt",
        str color               = "white",
        str \text-opacity       = "100%",
        str \font-family        = "",
        str \font-size          = "12pt",
        str \font-style         = "",
        CytoFontWeight \font-weight = normal(),
        str \background-color   = "blue",
        str label               = "data(label)",
        CytoNodeShape shape     = circle(),
        CytoHorizontalAlign \text-halign = center(),
        CytoVerticalAlign \text-valign = \top(),
        CytoTextWrap \text-wrap = CytoTextWrap::none(),
        str \text-max-width     = "100px",
        CytoHorizontalAlign \text-justification = CytoHorizontalAlign::center(),
        int \line-height        = 1
    )
    | cytoEdgeStyle(
        int width               = 3,
        str \line-color         = "black",
        str color               = "red",
        str \target-arrow-color = "black",
        str \source-arrow-color = "black",
        CytoArrowHeadStyle \target-arrow-shape = triangle(),
        CytoArrowHeadStyle \source-arrow-shape = none(),
        CytoCurveStyle \curve-style = bezier(),
        int \source-text-offset = 1,
        int \target-text-offset = 1,
        str label               = "data(label)"
    )
    ;
    
data CytoSelector
    = \node()
    | \edge()
    ; 

data CytoLayoutName
    = grid()
    | circle()
    | breadthfirst()
    | cose()
    | dagre()
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
        num spacingFactor = .1
    )
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
        spacingFactor=spacingFactor
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
        return response(ch);
    }

    // returns the main page that also contains the callbacks for retrieving data and configuration
    default Response reply(get(_)) {
        return response(writeHTMLString(plotHTML()));
    }

    return reply;
}

@synopsis{default HTML wrapper for a chart}
private HTMLElement plotHTML()
    = html([
        head([ 
            script([], src="https://cdnjs.cloudflare.com/ajax/libs/cytoscape/3.28.1/cytoscape.umd.js"),
            script([], src="https://cdnjs.cloudflare.com/ajax/libs/dagre/0.8.5/dagre.min.js"),
            script([], src="https://cdn.jsdelivr.net/npm/cytoscape-dagre@2.5.0/cytoscape-dagre.min.js"),
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
