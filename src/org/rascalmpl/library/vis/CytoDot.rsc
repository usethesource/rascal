module vis::CytoDot

import lang::dot::\syntax::Dot;
import vis::Graphs;
import Node;
import String;
import util::Math;
import Content;
import ValueIO;
import lang::html::IO;
import lang::html::AST;
import util::IDEServices;


private alias DotAttr = tuple[str,str];
private alias DotAttrs = list[DotAttr];

str ATTR_URL = "URL";
str ATTR_TOOLTIP = "tooltip";

DotAttrs DEFAULT_GRAPH_ATTRS = [<"ranksep","0.3">, <"bgcolor","0.482 0.1 1.0">];

public DOT cytoToDot(Cytoscape cytoscape, DotAttrs graphAttrs=DEFAULT_GRAPH_ATTRS) {
    AttrList graphAttrList = generateAttrList(graphAttrs);

    AttrList nodeAttrList = generateAttrList(cytoStylesToDotAttrs([style | cytoStyleOf(selector=\node(), style=style) <- cytoscape.style]));
    AttrList edgeAttrList = generateAttrList(cytoStylesToDotAttrs([style | cytoStyleOf(selector=\edge(), style=style) <- cytoscape.style]));

    StatementList initialStats = (StatementList)`graph <AttrList graphAttrList>
    '  node <AttrList nodeAttrList> 
    '  edge <AttrList edgeAttrList>`;


    StatementList stats = generateStatements(cytoscape);
    stats = mergeStatements(initialStats, stats);

    return (DOT) `digraph G {
    '  <StatementList stats>
    '}`;
}

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

private StatementList generateStatements(Cytoscape cytoscape) {
    StatementList stats = (StatementList)``;
    for (CytoData element <- cytoscape.elements) {
        list[CytoStyle] styles = gatherStyles(element, cytoscape.style);
        stats = addStatement(stats, generateStatement(element.\data, styles));
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

private Statement generateStatement(\node(id, label=label, editor=editLoc, tooltip=tooltip), list[CytoStyle] styles) {
    DotAttrs attrs = cytoStylesToDotAttrs(styles);
    attrs += <"label",label>;

    if (editLoc != "|nothing:///|") {
        if (startsWith(editLoc, "|")) {
            // Should be a location
            loc editorLoc = |https://editor|;
            editorLoc.query = "src=<editLoc>";
            str url = substring(editorLoc.uri, 8); // Remove https://
            attrs = attrs + <ATTR_URL, url>;
        } else {
            // Expect a complete url
            attrs = attrs + <ATTR_URL, editLoc>;
        }
    }

    if (tooltip != "") {
        attrs = attrs + <ATTR_TOOLTIP, tooltip>;
    }

    Id nodeId = [Id] id;
    AttrList attrList = generateAttrList(attrs);
    return (Statement) `<Id nodeId> <AttrList attrList>;`;
}

private Statement generateStatement(\edge(source, target, label=label), list[CytoStyle] styles) {
    DotAttrs styleAttrs = cytoStylesToDotAttrs(styles);
    styleAttrs += <"label",label>;
    Id sourceId = [Id] source;
    Id targetId = [Id] target;
    AttrList attrList = generateAttrList(styleAttrs);
    return (Statement) `<Id sourceId> -\> <Id targetId> <AttrList attrList>;`;
}

private StatementList addStatement((StatementList) `<Statement* stats1>`, Statement stat) {
    return (StatementList) `<Statement* stats1>
    '  <Statement stat>`;
}

private StatementList mergeStatements((StatementList) `<Statement* stats1>`, (StatementList) `<Statement* stats2>`) {
    return (StatementList) `<Statement* stats1>
    '<Statement* stats2>`;
}

DotAttrs cytoStylesToDotAttrs(list[CytoStyle] styles) = ([] | it + cytoStyleToDotAttrs(style) | style <- styles);

real toInches(str spec) {
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

private DotAttrs cytoStyleToDotAttrs(style: cytoEdgeStyle()) {
    DotAttrs attrs = [];
    if (style has \line-color) {
        attrs = attrs + <"color",style.color>;
    }
    if (style has width && style.width != 1) {
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


@synopsis{this is the main server generator for rendering a Cytoscape graph using Dot}
@description{
Given a Cytoscape graph this server generates a dot graph and serves it as text 
to the HTML client generated by ((vis::Dot::plotDotHTML)).
}
Response (Request) dotServer(Cytoscape ch, str pageTitle="DOT Graph") {
    Response reply(get(/^\/editor/, parameters=pms)) {
        if (pms["src"]?) {
            edit(readTextValueString(#loc, pms["src"]));
            return response(writeHTMLString(text("done")));
        }

        return response(writeHTMLString(text("could not edit <pms>")));
    }

    Response reply(get(/^\/dot/)) {
        return plain("<cytoToDot(ch)>");
    }

    // returns the main page that also contains the callbacks for retrieving data and configuration
    default Response reply(get(_)) {
        return response(writeHTMLString(plotDotHTML(pageTitle)));
    }

    return reply;
}

private HTMLElement plotDotHTML(str pageTitle)
    = html([
        head([ 
            title([\data(pageTitle)]),
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
                    'const graphviz = await Graphviz.load();
                    'fetch(\'/dot\').then(resp =\> resp.text()).then(dotSource =\> {
                    '   const svg = graphviz.layout(dotSource, \"svg\", \"dot\");
                    '   document.getElementById(\'visualization\').innerHTML = svg;
                    '});
                    '")
            ], \type="module")
        ])
    ]);

