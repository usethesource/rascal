@license{Copyright (c) 2025, NWO-I Centrum Wiskunde & Informatica (CWI) 
All rights reserved. 
  
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
  
1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
  
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
  
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
}
@synopsis{Conversion of Cytoscape graphs to DOT graphs}
@description{Although Cytoscape is very powerful and flexible, in some situations Graphviz is the superior solution to render graphs.
For instance, graphviz is much better in routing edges around nodes in layered/hierarchical graphs.
The easiest way to use this module is to use the "dotLayout()" layout in vis::Graphs when using `graphServer()` to visualize your graph.
Graphs are then automatically converted to dot and rendered using the wasm version of graphviz.
Note that conversion from Cytoscape styles to DOT styles is done on a "best effort" basis. Cytoscape has more
(and different) styling options than can be found in DOT so you might encounter a few surprises in this area.
Feel free to contribute if you improve the styling conversion in this module.
}
module vis::CytoDot

import lang::dot::\syntax::Dot;
import vis::Graphs;
import Node;
import String;
import Content;
import ValueIO;
import lang::html::IO;
import lang::html::AST;
import util::IDEServices;

private alias DotAttr = tuple[str,str];
private alias DotAttrs = list[DotAttr];
private alias DotAttrTable = map[str,str];

str ATTR_URL = "URL";
str ATTR_TOOLTIP = "tooltip";

@description{
    Convert a Cytoscape graph to a DOT parse tree. The resulting tree is ready to be converted to a string and
    fed to graphviz for rendering. `vis::Graphs::graphServer()` can do this automatically for you if you use
    the `dotLayout()` layout.
}
public DOT cytoToDot(Cytoscape cytoscape, DotAttrs graphAttrs=[]) {
    // Cytoscape.layout must be dotLayout
    graphAttrs = graphAttrs + <"ranksep","<cytoscape.\layout.rankSep>">;
    graphAttrs = graphAttrs + <"rankdir",cytoscape.\layout.rankDir>;
    graphAttrs = graphAttrs + <"bgcolor", cytoscape.\layout.bgColor>;

    AttrList graphAttrList = generateAttrList(graphAttrs);

    DotAttrs nodeAttrs = cytoStylesToDotAttrs([style | cytoStyleOf(selector=\node(), style=style) <- cytoscape.style]);
    DotAttrs edgeAttrs = cytoStylesToDotAttrs([style | cytoStyleOf(selector=\edge(), style=style) <- cytoscape.style]);
    AttrList nodeAttrList = generateAttrList(nodeAttrs);
    AttrList edgeAttrList = generateAttrList(edgeAttrs);

    StatementList initialStats = (StatementList)`graph <AttrList graphAttrList>
    '  node <AttrList nodeAttrList> 
    '  edge <AttrList edgeAttrList>`;

    
    StatementList stats = generateStatements(cytoscape, toAttrTable(nodeAttrs), toAttrTable(edgeAttrs));
    stats = mergeStatements(initialStats, stats);

    return (DOT) `digraph G {
    '  <StatementList stats>
    '}`;
}

private DotAttrTable toAttrTable(DotAttrs attrs) = (name : val | <name,val> <- attrs);

private DotAttrs filterDefaultAttrs(DotAttrs attrs, DotAttrTable defaults) = [<name,val> | <name,val> <- attrs, !((name in defaults) && defaults[name] == val)];

private AttrList generateAttrList(DotAttrs attrs) {
    AttrList result = (AttrList) `[]`;
    for (<str name, str val> <- attrs) {
        result = addAttribute(result, name, val);
    }
    return result;
}

private AttrList addAttribute((AttrList) `[<Attribute* attrs1>]`, str name, str val) {
    Id nameId = [Id] toAttrValue(name);
    Id valId = [Id] toAttrValue(val);
    return (AttrList) `[<Attribute* attrs1> <Id nameId> = <Id valId>]`;
}

private str toAttrValue(str s) = "\"" + escape(s, ("\\": "\\\\", "\"": "\\\"")) + "\"";

private StatementList generateStatements(Cytoscape cytoscape, DotAttrTable defaultNodeAttrs, DotAttrTable defaultEdgeAttrs) {
    StatementList stats = (StatementList)``;
    for (CytoData element <- cytoscape.elements) {
        list[CytoStyle] styles = gatherStyles(element, cytoscape.style);
        stats = addStatement(stats, generateStatement(element.\data, styles, defaultNodeAttrs, defaultEdgeAttrs));
    }
    return stats;
}

private list[CytoStyle] gatherStyles(CytoData \data, list[CytoStyleOf] styles) {
    list[CytoStyle] matchingStyles = [];
    for (CytoStyleOf style <- styles) {
        if (styleMatches(\data, style.selector)) {
            matchingStyles += style.style;
        }
    }
    return matchingStyles;
}

private bool styleMatches(CytoData \data, CytoSelector selector) {
    switch (selector) {
        case \node(): return \node(_) := \data.\data;
        case \edge(): return \edge(_, _) := \data.\data;
        case \id(str id): return getStringField(\data.\data, "id") == id;
        case \and(list[CytoSelector] conjuncts): return allMatches(\data, conjuncts);
        case \or(list[CytoSelector] disjuncts): return anyMatches(\data, disjuncts);
        case \equal(str field, str \value): return getStringField(\data.\data, field) == \value;
        case \equal(str field, int \value): return getIntField(\data.\data, field) == \value;
        case \greater(str field, int \value): return getIntField(\data.\data, field) > \value;
        case \less(str field, int \value): return getIntField(\data.\data, field) < \value;
        case \greaterEqual(str field, int \value): return getIntField(\data.\data, field) >= \value;
        case \lessEqual(str field, int \value): return getIntField(\data.\data, field) <= \value;
        case \className(str name): return name in \data.classes;
    }
    fail;
}

private bool allMatches(CytoData \data, list[CytoSelector] conjuncts) {
    for (CytoSelector conjunct <- conjuncts) {
        if (!styleMatches(\data, conjunct)) {
            return false;
        }
    }

    return true;
}

private bool anyMatches(CytoData \data, list[CytoSelector] disjuncts) {
    for (CytoSelector disjunct <- disjuncts) {
        if (styleMatches(\data, disjunct)) {
            return true;
        }
    }

    return false;
}

private str getStringField(\node(id), "id") = id;
private str getStringField(\node(_, label=label), "label") = label;
private str getStringField(\node(_, editor=editor), "editor") = editor;
private str getStringField(\node(_, tooltip=tooltip), "tooltip") = tooltip;
private str getStringField(\edge(source, _), "source") = source;
private str getStringField(\edge(_, target), "target") = target;
private str getStringField(\edge(_, _, id=id), "id") = id;
private str getStringField(\edge(_, _, label=label), "label") = label;
private default str getStringField(_) = "";

private int getIntField(CytoElement elem, str field) = toInt(getStringField(elem, field));

private Statement generateStatement(\node(id, label=label, editor=editLoc, tooltip=tooltip), list[CytoStyle] styles, DotAttrTable defaultNodeAttrs, DotAttrTable _) {
    DotAttrs nodeAttrs = cytoStylesToDotAttrs(styles);
    if (label != "") {
        nodeAttrs += <"label",label>;
    }

    if (editLoc != "|nothing:///|") {
        if (startsWith(editLoc, "|")) {
            // Should be a location
            loc editorLoc = |https://editor|;
            editorLoc.query = "src=<editLoc>";
            str url = substring(editorLoc.uri, 8); // Remove https://
            nodeAttrs = nodeAttrs + <ATTR_URL, url>;
        } else {
            // Expect a complete url
            nodeAttrs = nodeAttrs + <ATTR_URL, editLoc>;
        }
    }

    if (tooltip != "") {
        nodeAttrs = nodeAttrs + <ATTR_TOOLTIP, tooltip>;
    }

    nodeAttrs = filterDefaultAttrs(nodeAttrs, defaultNodeAttrs);

    Id nodeId = [Id] id;

    if (nodeAttrs == []) {
        return (Statement) `<Id nodeId>;`;
    } else {
        AttrList attrList = generateAttrList(nodeAttrs);
        return (Statement) `<Id nodeId> <AttrList attrList>;`;
    }
}

private Statement generateStatement(\edge(source, target, label=label), list[CytoStyle] styles, DotAttrTable _, DotAttrTable defaultEdgeAttrs) {
    DotAttrs edgeAttrs = cytoStylesToDotAttrs(styles);
    if (label != "") {
        edgeAttrs += <"label",label>;
    }
    edgeAttrs = filterDefaultAttrs(edgeAttrs, defaultEdgeAttrs);

    Id sourceId = [Id] source;
    Id targetId = [Id] target;

    if (edgeAttrs == []) {
        return (Statement) `<Id sourceId> -\> <Id targetId>;`;
    } else {
        AttrList attrList = generateAttrList(edgeAttrs);
        return (Statement) `<Id sourceId> -\> <Id targetId> <AttrList attrList>;`;
    }
}

private StatementList addStatement((StatementList) `<Statement* stats1>`, Statement stat) {
    return (StatementList) `<Statement* stats1>
    '  <Statement stat>`;
}

private StatementList mergeStatements((StatementList) `<Statement* stats1>`, (StatementList) `<Statement* stats2>`) {
    return (StatementList) `<Statement* stats1>
    '  <Statement* stats2>`;
}

private DotAttrs cytoStylesToDotAttrs(list[CytoStyle] styles) = ([] | it + cytoStyleToDotAttrs(style) | style <- styles);

private real toInches(str spec) {
    if (endsWith(spec, "pt")) {
        spec = substring(spec, 0, size(spec)-2);
    }

    return toReal(spec)/72;
}

private DotAttrs cytoStyleToDotAttrs(style: cytoNodeStyle()) {
    DotAttrs attrs = [];

    if (style has color) {
        attrs = attrs + <"fillcolor",style.color>;
    }
    if (style has \border-color) {
        attrs = attrs + <"color",style.\border-color>;
    }
    if (style has \border-width && style.\border-width != 1) {
        attrs = attrs + <"penwidth","<style.\border-width>">;
    }

    if (style has shape) {
        attrs = attrs + dotShape(style.shape);
    }

    if (style has visibility && style.visibility != "visible") {
        attrs = attrs + <"style", "invis">;
    }

    if (style has padding) {
        attrs = attrs + <"margin",  "<toInches(style.padding)>">;
    }

    if (style has height && style.height != "label") {
        attrs = attrs + <"height", "<toInches(style.height)>">;
    }

    return attrs;
}

private DotAttrs cytoStyleToDotAttrs(style: cytoEdgeStyle(\line-color=lineColor, width=width)) {
    DotAttrs attrs = [];
    if (lineColor != "black") {
        attrs = attrs + <"color",lineColor>;
    }
    if (width != 1) {
        attrs = attrs + <"penwidth","<style.\width>">;
    }

    return attrs;
}

private DotAttrs dotShape(CytoNodeShape shape) {
    // Might need some more work to map shapes here
    str dotShape = getName(shape);
    DotAttrs attrs = [];
    if (startsWith(dotShape, "round-")) {
        dotShape = substring(dotShape, 6);
        attrs = attrs + [<"style", "filled,rounded">];
    }
    return attrs + <"shape", dotShape>;
}

