module vis::Dot

import lang::dot::\syntax::Dot;
import Node;
import analysis::text::search::Grammars;
import ParseTree;
import Set;
import Map;
import List;
import String;
import IO;
import ValueIO;
import Type;
import Content;
import lang::html::IO;
import lang::html::AST;
import util::IDEServices;

data DotConfig = dotConfig(
    list[value](value n) childGetter = defaultGetChildren,
    bool(value n) valueFilter = success,
    value(value n) valueTransformer = identity,
    Statement(value n, int id, str tooltip) nodeGenerator = defaultNodeGenerator,
    Statement(value n, int id, value child, int childId) edgeGenerator = defaultEdgeGenerator,
    str(value v, int id) tooltipGenerator = defaultTooltipGenerator
);

private value identity(value val) = val;
private bool success(_) = true;

// A EdgeValue is used to bundle extra edge information with a value
private data EdgeValue = edge(value val, str edgeLabel="", Attrs edgeAttributes=[]);

alias Attrs = list[tuple[str,str]];

private list[value] defaultGetChildren(node n) = getNodeChildren(n);
private list[value] defaultGetChildren(list[value] l) = l;
private list[value] defaultGetChildren(set[value] s) = toList(s);
private list[value] defaultGetChildren(map[value, value] m) = getMapChildren(m);
private list[value] defaultGetChildren(<v1>) = [v1];
private list[value] defaultGetChildren(<v1,v2>) = [v1,v2];
private list[value] defaultGetChildren(<v1,v2,v3>) = [v1,v2,v3];
private list[value] defaultGetChildren(<v1,v2,v3,v4>) = [v1,v2,v3,v4];
private list[value] defaultGetChildren(<v1,v2,v3,v4,v5>) = [v1,v2,v3,v4,v5];
private default list[value] defaultGetChildren(v) = [];

private list[value] getMapChildren(map[value, value] m) = [edge(val, edgeLabel="<key>") | <key,val> <- toList(m)];

private list[value] getNodeChildren(Tree t) = getParseTreeChildren(t);
private default list[value] getNodeChildren(node n) {
    map[str,value] kwParams = getKeywordParameters(n);
    list[value] kwEdges = [edge(kwParams[key], edgeLabel="<key>", edgeAttributes=[<"fontcolor","darkblue">]) | key <- kwParams];
    return getChildren(n) + kwEdges;
}

// Label edges if the corresponding symbol in the production has a label
private list[value] getParseTreeChildren(appl(prod, args)) = getApplChildren(prod, args);
private list[value] getApplChildren(prod(_,formals,_), actuals) {
    list[tuple[Symbol,Tree]] args = zip2(formals, actuals);
    return [ label(name, _) := formal ? edge(actual, edgeLabel=name, edgeAttributes=[<"fontcolor","darkblue">]) : actual | <formal, actual> <- args];
}
private default list[value] getApplChildren(_, list[value] args) = args;

private list[value] getParseTreeChildren(amb(set[Tree] alts)) = toList(alts);
private default list[value] getParseTreeChildren(t) = [];

str SHAPE = "shape";
tuple[str,str] SHAPE_RECT = <SHAPE, "rect">;
tuple[str,str] SHAPE_DOUBLE_OCTAGON = <SHAPE, "doubleoctagon">;
tuple[str,str] SHAPE_HEXACON = <SHAPE, "hexagon">;
tuple[str,str]  SHAPE_HOUSE = <SHAPE, "house">;
tuple[str,str]  SHAPE_ELLIPSE = <SHAPE, "ellipse">;
tuple[str,str]  SHAPE_CIRCLE = <SHAPE, "circle">;

str STYLE = "style";
tuple[str,str] STYLE_ROUNDED = <STYLE, "rounded">;

tuple[str,str] COLLECTION_SHAPE = SHAPE_DOUBLE_OCTAGON;
tuple[str,str] TUPLE_SHAPE = SHAPE_HEXACON;
tuple[str,str] LOCATION_SHAPE = SHAPE_HOUSE;
tuple[str,str] ATOM_SHAPE = SHAPE_ELLIPSE;
tuple[str,str] BOOL_SHAPE = SHAPE_CIRCLE;

str COLOR = "color";
tuple[str,str] COLOR_ORANGE = <COLOR, "orange">;

str TOOLTIP = "tooltip";
str LABEL = "label";
str URL = "URL";

Statement defaultNodeGenerator(edge(val), int id, str tooltip) = defaultNodeGenerator(val, id, tooltip);
Statement defaultNodeGenerator(node n, int id, str tooltip) = nodeNodeGenerator(n, id, tooltip);
Statement defaultNodeGenerator(list[value] l:[], int id, str tooltip) = nodeStatement("list[]", id, tooltip, attrs=[COLLECTION_SHAPE]);
Statement defaultNodeGenerator(list[value] l:[_,*_], int id, str tooltip) = nodeStatement("<typeLabel(l)>", id, tooltip, attrs=[COLLECTION_SHAPE]);
Statement defaultNodeGenerator(set[value] s, int id, str tooltip) = nodeStatement("<typeLabel(s)>", id, tooltip, attrs=[COLLECTION_SHAPE]);
Statement defaultNodeGenerator(map[value,value] m, int id, str tooltip) = nodeStatement("<typeLabel(m)>", id, tooltip, attrs=[COLLECTION_SHAPE]);
Statement defaultNodeGenerator(t:<_>, int id, str tooltip) = nodeStatement("<typeLabel(t)>", id, tooltip, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(t:<_,_>, int id, str tooltip) = nodeStatement("<typeLabel(t)>", id, tooltip, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(t:<_,_,_>, int id, str tooltip) = nodeStatement("<typeLabel(t)>", id, tooltip, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(t:<_,_,_,_>, int id, str tooltip) = nodeStatement("<typeLabel(t)>", id, tooltip, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(t:<_,_,_,_,_>, int id, str tooltip) = nodeStatement("<typeLabel(t)>", id, tooltip, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(t:<_,_,_,_,_,_>, int id, str tooltip) = nodeStatement("<typeLabel(t)>", id, tooltip, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(t:<_,_,_,_,_,_,_>, int id, str tooltip) = nodeStatement("<typeLabel(t)>", id, tooltip, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(t:<_,_,_,_,_,_,_,_>, int id, str tooltip) = nodeStatement("<typeLabel(t)>", id, tooltip, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(loc l, int id, str tooltip) = nodeStatement(l.length == 1 ? "<l.begin.line>,<l.begin.column>" : "<l.begin.line>,<l.begin.column>-<l.end.line>,<l.end.column>", id, tooltip, attrs=[LOCATION_SHAPE]);
Statement defaultNodeGenerator(num n, int id, str tooltip) = nodeStatement("<n>", id, tooltip, attrs=[ATOM_SHAPE]);
Statement defaultNodeGenerator(str s, int id, str tooltip) = nodeStatement("\"<s>\"", id, tooltip, attrs=[ATOM_SHAPE]);
Statement defaultNodeGenerator(bool b, int id, str tooltip) = nodeStatement(b ? "T" : "F", id, tooltip, attrs=[BOOL_SHAPE,<"fontcolor",b ? "green" : "red">]);
default Statement defaultNodeGenerator(value v, int id, str tooltip) = nodeStatement("<v>", id, tooltip);

private Statement nodeNodeGenerator(Tree tree, int id, str tooltip) = parseTreeNodeGenerator(tree, id, tooltip);
private default Statement nodeNodeGenerator(node n, int id, str tooltip) = nodeStatement(getName(n), id, tooltip);

Statement defaultEdgeGenerator(value _, int id, edge(val, edgeLabel=edgeLabel, edgeAttributes=attrs), int childId)
    = edgeStatement(id, childId, label="<edgeLabel>", attrs=attrs);
default Statement defaultEdgeGenerator(value _, int id, value e, int childId) = edgeStatement(id, childId);

private Statement nodeStatement(str label, int id, str tooltip, Attrs attrs=[], str uri="") {
    Id nodeId = [Id] "<id>";
    if (uri != "") {
        attrs = attrs + <URL, uri>;
    }
    if (tooltip != "") {
        attrs = attrs + <TOOLTIP, tooltip>;
    }
    AttrList attrList = generateAttrList(attrs + <LABEL, toLabelString(label)>);
    return (Statement) `<Id nodeId> <AttrList attrList>;`;
}

Statement edgeStatement(int parentId, int childId, str label="", Attrs attrs=[]) {
    if (label != "") {
        attrs = attrs + <LABEL, toLabelString(label)>;
    }

    Id fromId = [Id] "<parentId>";
    Id toId = [Id] "<childId>";
    if (attrs == []) {
        return (Statement) `<Id fromId> -\> <Id toId>;`;
    }

    AttrList attrList = generateAttrList(attrs);

    return (Statement) `<Id fromId> -\> <Id toId> <AttrList attrList>;`;
}

str defaultTooltipGenerator(value v, int id) {
    str yield = "<v>";
    if (size(yield) > 100) {
        yield = substring(yield, 0, 100) + "...";
    }

    return yield;
}

private str toAttrValue(str s) = "\"" + escape(s, ("\"": "\\\"")) + "\"";
private str toLabelString(str s) = escape(s, ("\r": "\\\\r", "\n": "\\\\n"));

bool(value v) parseTreeValueFilter(bool filterLayout, bool filterMissingOptionals) {
    bool valueFilter(appl(production, args)) {
        Symbol getSymbol(prod(label(_, sym), _, _)) = sym;
        Symbol getSymbol(regular(def)) = def;
        // TODO: error productions
        default Symbol getSymbol(prod(sym, _, _)) = sym;

        if (filterLayout) {
            if (\layouts(_) := getSymbol(production)) {
                return false;
            }
        }

        if (filterMissingOptionals) {
            if (\opt(_) := getSymbol(production) && args == []) {
                return false;
            }
        }

        return true;
    }
    default bool valueFilter(_) = true;

    return valueFilter;
}

value(value v) parseTreeValueTransformer(bool collapseTokens) {
    value valueTransformer(t: appl(_,_)) {
        if (collapseTokens && isToken(t)) {
            return "<t>";
        }

        return t;
    }
    default value valueTransformer(value n) = n;

    return valueTransformer;
}

DotConfig createParseTreeConfig(bool collapseTokens=false, bool filterLayout=false, bool filterMissingOptionals=false) {
    return dotConfig(
        valueFilter = parseTreeValueFilter(filterLayout, filterMissingOptionals),
        valueTransformer = parseTreeValueTransformer(collapseTokens)
    );
}

DotConfig createCompactParseTreeConfig() = createParseTreeConfig(collapseTokens=true, filterLayout=true, filterMissingOptionals=true);

int nextId = 1;

DOT valueToDot(Tree t, str name="Unknown", DotConfig config=createCompactParseTreeConfig()) = value2Dot(t, name=name, config=config);

default DOT valueToDot(value v, str name="Unknown", DotConfig config=dotConfig()) = value2Dot(v, name=name, config=config);

DOT value2Dot(value v, str name="Unknown", DotConfig config = dotConfig()) {
    Id graphName = [Id] toAttrValue(name);
    nextId = 1;

    // Compress the graph a little to be able to handle larger parse trees
    StatementList initialStats = (StatementList)`graph [ranksep="0.3"]; node [height="0.1"]`;

    StatementList stats = config.valueFilter(v) ? generateStatements(config, v, 0) : (StatementList)``;
    stats = mergeStatements(initialStats, stats);

    return (DOT) `digraph <Id graphName> {
    '  <StatementList stats>
    '}`;
}

private StatementList addStatement((StatementList) `<Statement* stats1>`, Statement stat) {
    return (StatementList) `<Statement* stats1>
    '  <Statement stat>`;
}

private StatementList mergeStatements((StatementList) `<Statement* stats1>`, (StatementList) `<Statement* stats2>`) {
    return (StatementList) `<Statement* stats1> <Statement* stats2>`;
}

private AttrList generateAttrList(Attrs attrs) {
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

private StatementList generateStatements(DotConfig config, value v, int id) {
    v = config.valueTransformer(v);

    Statement stat = config.nodeGenerator(v, id, config.tooltipGenerator(v, id));

    StatementList stats = (StatementList) `<Statement stat>`;

    for (value edgeChild <- config.childGetter(v)) {
        value child = edge(val) := edgeChild ? val : edgeChild;
        
        if (!config.valueFilter(child)) {
            continue;
        }

        int childId = nextId;
        nextId += 1;
        
        StatementList childStats = generateStatements(config, child, childId);
        stats = mergeStatements(stats, childStats);

        Statement edgeStat = config.edgeGenerator(v, id, edgeChild, childId);
        stats = addStatement(stats, edgeStat);
    }

    return stats;
}

Statement parseTreeNodeGenerator(t:appl(prod, args), int id, str tooltip) {
    str label = prodToString(prod);
    str uri = "";

    map[str,value] kwParams = getKeywordParameters(t);
    if ("src" in kwParams) {
        loc editorLoc = |https://editor|;
        editorLoc.query = "src=<kwParams["src"]>";
        uri = substring(editorLoc.uri, 8); // Remove https://
    }

    return nodeStatement(label, id, tooltip, attrs=[SHAPE_PARSE_NODE, STYLE_ROUNDED], uri=uri);
}


tuple[str,str] SHAPE_PARSE_NODE = SHAPE_RECT;

Statement parseTreeNodeGenerator(t:amb(alts), int id, str tooltip) = nodeStatement("amb", id, tooltip, attrs=[SHAPE_ELLIPSE, COLOR_ORANGE]);
Statement parseTreeNodeGenerator(t:char(ch), int id, str tooltip) = nodeStatement("<stringChar(ch)>", id, tooltip, attrs=[SHAPE_CIRCLE]);
Statement parseTreeNodeGenerator(t:cycle(sym, length), int id, str tooltip) = nodeStatement("cycle(<sym>,<length>)", id, tooltip, attrs=[SHAPE_CIRCLE]);
default Statement parseTreeNodeGenerator(n, id, str tooltip) = nodeStatement("<n>", id, tooltip);

private str prodToString(prod(def, symbols, attrs)) = "<symbolToString(def)> = <symbolsToString(symbols)>";
private str prodToString(regular(def)) = symbolToString(def);
private default str prodToString(Production prod) = "<prod>";

private str symbolToString(\start(sym)) = symbolToString(sym);
private str symbolToString(sort(name)) = name;
private str symbolToString(lex(name)) = name;
private str symbolToString(lit(string)) = "\"<string>\"";
private str symbolToString(cilit(string)) = "\"<string>\"";
private str symbolToString(\char-class(ranges)) = rangesToString(ranges);
private str symbolToString(layouts(name)) = "_";
private str symbolToString(opt(sym)) = "<symbolToString(sym)>?";
private str symbolToString(\iter(sym)) = "<symbolToString(sym)>+";
private str symbolToString(\iter-seps(sym, seps)) = "{ <symbolToString(sym)> <symbolsToString(seps)>}+";
private str symbolToString(\iter-star(sym)) = "<symbolToString(sym)>*";
private str symbolToString(\iter-star-seps(sym, seps)) = "{ <symbolToString(sym)> <symbolsToString(seps)>}*";
private str symbolToString(\label(name,sym)) = symbolToString(sym);
private str symbolToString(\conditional(sym, _)) = symbolToString(sym);
private str symbolToString(\seq(symbols)) = symbolsToString(symbols);

default str symbolToString(sym) {
    return "<sym>";
}

private str symbolsToString(list[Symbol] symbols) {
    str result = "";
    bool first = true;
    for (Symbol sym <- symbols) {
        if (first) {
            first = false;
        } else {
            result += " ";
        }
        result = result + symbolToString(sym);
    }

    return result;
}

private str rangesToString(list[CharRange] ranges) {
    str result = "[";
    for (range(begin, end) <- ranges) {
        result = result + stringChar(begin);
        if (begin != end) {
            result = result + "-" + stringChar(end);
        }
    }
    return result + "]";
}

private str typeLabel(value v) = typeToLabel(typeOf(v));

private str typeToLabel(\set(sym)) = "set[<typeToLabel(sym)>]";
private str typeToLabel(\list(sym)) = "list[<typeToLabel(sym)>]";
private str typeToLabel(\bag(sym)) = "bag[<typeToLabel(sym)>]";
private str typeToLabel(\rel(symbols)) = "rel[<typesToLabel(symbols)>]";
private str typeToLabel(\lrel(symbols)) = "lrel[<typesToLabel(symbols)>]";
private str typeToLabel(\tuple(symbols)) = "tuple[<typesToLabel(symbols)>]";
private str typeToLabel(\map(key,val)) = "map[<typeToLabel(key)>,<typeToLabel(val)>]";

private str typeToLabel(adt(name,[])) = name;
private str typeToLabel(adt(name,[arg1,*args])) = name + "[" + typesToLabel([arg1,*args]) + "]";

private default str typeToLabel(Symbol tp) = "<tp>";

private str typesToLabel(list[Symbol] types) = substring(("" | it + ",<typeToLabel(tp)>" | tp <-types), 1);

@synopsis{this is the main server generator for any dot graph}
@description{
Given a DOT parse tree this server captures the value and serves it
as a string to the HTML client generated by ((vis::Dot::plotHTML)).
}
Response (Request) dotServer(DOT dot) {
    Response reply(get(/^\/editor/, parameters=pms)) {
        if (pms["src"]?) {
            edit(readTextValueString(#loc, pms["src"]));
            return response(writeHTMLString(text("done")));
        }

        return response(writeHTMLString(text("could not edit <pms>")));
    }

    Response reply(get(/^\/dot/)) {
        return plain("<dot>");
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
