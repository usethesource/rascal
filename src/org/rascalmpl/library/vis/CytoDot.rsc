module vis::CytoDot

import lang::dot::\syntax::Dot;
import vis::Graphs;
import Node;
import String;
import Content;

private alias DotAttr = tuple[str,str];
private alias DotAttrs = list[DotAttr];

public DOT cytoToDot(Cytoscape cytoscape) {
    AttrList graphAttrList = generateAttrList([]);
    AttrList nodeAttrList = generateAttrList([]);
    AttrList edgeAttrList = generateAttrList([]);

    StatementList initialStats = (StatementList)`graph <AttrList graphAttrList>
    '  node <AttrList nodeAttrList> 
    '  edge <AttrList edgeAttrList>
    ' 1 -\> 2`;


    StatementList stats = (StatementList)``;
    stats = mergeStatements(initialStats, stats);

    return (DOT) `digraph G {
    '  <StatementList stats>
    '}`;

    /*
    for (CytoData elem <- cytoscape.elements) {
        list[CytoStyleOf] styles = gatherElementStyles(cytoscape, elem);
    }*/
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
private str getStringField(\edge(source, _), "source") = source;
private str getStringField(\edge(_, target), "target") = target;
private str getStringField(\edge(_, _, id=id), "id") = id;
private str getStringField(\edge(_, _, label=label), "label") = label;
private default str getStringField(_) = "";

private int getIntField(CytoElement elem, str field) = toInt(getStringField(elem, field));

private Statement generateStatement(\node(id, label=label), list[CytoStyle] styles) {
    DotAttrs styleAttrs = cytoStylesToDotAttrs(styles);
    styleAttrs += <"label",label>;
    Id nodeId = [Id] id;
    AttrList attrList = generateAttrList(styleAttrs);
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

private DotAttrs cytoStyleToDotAttrs(CytoStyle style) {
    return [
        <"color",style.color>
    ] 
    + dotShape(style.shape);
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

