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

public Attrs DEFAULT_GRAPH_ATTRS = [<"ranksep","0.3">, <"bgcolor","0.482 0.1 1.0">];
public Attrs DEFAULT_NODE_ATTRS = [<"height","0.1">,<"style","filled">, <"fillcolor","white">];
public Attrs DEFAULT_EDGE_ATTRS = [];

data DotConfig = dotConfig(
    list[value](value n) childGetter = defaultGetChildren,
    bool allowRecursion = false,
    bool(value n) valueFilter = success,
    value(value n) valueTransformer = identity,
    Attrs graphAttrs = DEFAULT_GRAPH_ATTRS,
    Attrs nodeAttrs = DEFAULT_NODE_ATTRS,
    Attrs edgeAttrs = DEFAULT_EDGE_ATTRS,
    Statement(value n, int id) nodeGenerator = defaultNodeGenerator,
    Statement(value n, int id, value child, int childId) edgeGenerator = defaultEdgeGenerator
);

private value identity(value val) = val;
private bool success(_) = true;

// Compress the graph a little to be able to handle larger data structures

DOT valueToDot(Tree t, str name="Unknown", DotConfig config=createParseTreeConfig()) = value2Dot(t, name, config);
default DOT valueToDot(value v, str name="Unknown", DotConfig config=dotConfig()) = value2Dot(v, name, config);

private DOT value2Dot(value v, str name, DotConfig config) {
    Id graphName = [Id] toAttrValue(name);
    nextId = 1;

    AttrList graphAttrList = generateAttrList(config.graphAttrs);
    AttrList nodeAttrList = generateAttrList(config.nodeAttrs);
    AttrList edgeAttrList = generateAttrList(config.edgeAttrs);

    StatementList initialStats = (StatementList)`graph <AttrList graphAttrList>
    '  node <AttrList nodeAttrList> 
    '  edge <AttrList edgeAttrList>`;

    StatementList stats = config.valueFilter(v) ? generateStatements(config, v, 0, (v:0)) : (StatementList)``;
    stats = mergeStatements(initialStats, stats);

    return (DOT) `digraph <Id graphName> {
    '  <StatementList stats>
    '}`;
}

// Some attribute constants
str SHAPE = "shape";
tuple[str,str] SHAPE_RECT = <SHAPE, "rect">;
tuple[str,str] SHAPE_DOUBLE_OCTAGON = <SHAPE, "doubleoctagon">;
tuple[str,str] SHAPE_HEXACON = <SHAPE, "hexagon">;
tuple[str,str]  SHAPE_HOUSE = <SHAPE, "house">;
tuple[str,str]  SHAPE_ELLIPSE = <SHAPE, "ellipse">;
tuple[str,str]  SHAPE_CIRCLE = <SHAPE, "circle">;

str STYLE = "style";
tuple[str,str] STYLE_ROUNDED = <STYLE, "filled,rounded">;

tuple[str,str] COLLECTION_SHAPE = SHAPE_DOUBLE_OCTAGON;
tuple[str,str] TUPLE_SHAPE = SHAPE_HEXACON;
tuple[str,str] LOCATION_SHAPE = SHAPE_HOUSE;
tuple[str,str] ATOM_SHAPE = SHAPE_ELLIPSE;
tuple[str,str] BOOL_SHAPE = SHAPE_CIRCLE;

str COLOR = "color";
tuple[str,str] COLOR_ORANGE = <COLOR, "orange">;
tuple[str,str] COLOR_DARK_BLUE = <COLOR, "darkblue">;

str TOOLTIP = "tooltip";
str LABEL = "label";
str URL = "URL";
str WIDTH = "width";


// A EdgeValue is used to bundle extra edge information with a value
private data EdgeValue = edge(value val, str label="", Attrs attrs=[]);
private data SubgraphValue = subGraph(value val, str label="", Attrs attrs=[]);

alias Attrs = list[tuple[str,str]];

list[value] defaultGetChildren(node n) = getNodeChildren(n);
list[value] defaultGetChildren(list[value] l) = l;
list[value] defaultGetChildren(set[value] s) = toList(s);
list[value] defaultGetChildren(map[value, value] m) = getMapChildren(m);
list[value] defaultGetChildren(<v1>) = [v1];
list[value] defaultGetChildren(<v1,v2>) = [v1,v2];
list[value] defaultGetChildren(<v1,v2,v3>) = [v1,v2,v3];
list[value] defaultGetChildren(<v1,v2,v3,v4>) = [v1,v2,v3,v4];
list[value] defaultGetChildren(<v1,v2,v3,v4,v5>) = [v1,v2,v3,v4,v5];
default list[value] defaultGetChildren(v) = [];

private list[value] getMapChildren(map[value, value] m) = [edge(val, label="<key>") | <key,val> <- toList(m)];

private default list[value] getNodeChildren(node n) {
    map[str,value] kwParams = getKeywordParameters(n);
    list[value] kwEdges = [edge(kwParams[key], label="<key>", attrs=[COLOR_DARK_BLUE]) | key <- kwParams];
    return getChildren(n) + kwEdges;
}

Statement defaultNodeGenerator(edge(val), int id) = defaultNodeGenerator(val, id);
Statement defaultNodeGenerator(node n, int id) = nodeNodeGenerator(n, id);
Statement defaultNodeGenerator(list[value] l:[], int id) = nodeStatement("list[]", id, attrs=[COLLECTION_SHAPE]);
Statement defaultNodeGenerator(list[value] l:[_,*_], int id) = nodeStatement("<typeLabel(l)>", id, attrs=[COLLECTION_SHAPE]);
Statement defaultNodeGenerator(set[value] s, int id) = nodeStatement("<typeLabel(s)>", id, attrs=[COLLECTION_SHAPE]);
Statement defaultNodeGenerator(map[value,value] m, int id) = nodeStatement("<typeLabel(m)>", id, attrs=[COLLECTION_SHAPE]);
Statement defaultNodeGenerator(t:<_>, int id) = nodeStatement("<typeLabel(t)>", id, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(t:<_,_>, int id) = nodeStatement("<typeLabel(t)>", id, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(t:<_,_,_>, int id) = nodeStatement("<typeLabel(t)>", id, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(t:<_,_,_,_>, int id) = nodeStatement("<typeLabel(t)>", id, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(t:<_,_,_,_,_>, int id) = nodeStatement("<typeLabel(t)>", id, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(t:<_,_,_,_,_,_>, int id) = nodeStatement("<typeLabel(t)>", id, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(t:<_,_,_,_,_,_,_>, int id) = nodeStatement("<typeLabel(t)>", id, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(t:<_,_,_,_,_,_,_,_>, int id) = nodeStatement("<typeLabel(t)>", id, attrs=[TUPLE_SHAPE]);
Statement defaultNodeGenerator(loc l, int id) = nodeStatement(l.length == 1 ? "<l.begin.line>,<l.begin.column>" : "<l.begin.line>,<l.begin.column>-<l.end.line>,<l.end.column>", id, attrs=[LOCATION_SHAPE]);
Statement defaultNodeGenerator(num n, int id) = nodeStatement("<n>", id, attrs=[ATOM_SHAPE]);
Statement defaultNodeGenerator(str s, int id) = nodeStatement("\"<s>\"", id, attrs=[ATOM_SHAPE]);
Statement defaultNodeGenerator(bool b, int id) = nodeStatement(b ? "T" : "F", id, attrs=[BOOL_SHAPE,<"fontcolor",b ? "green" : "red">]);
default Statement defaultNodeGenerator(value v, int id) = nodeStatement("<v>", id);

private Statement nodeNodeGenerator(Tree tree, int id) = parseTreeNodeGenerator(tree, id);
private default Statement nodeNodeGenerator(node n, int id) = nodeStatement(getName(n), id);

Statement defaultEdgeGenerator(value _, int id, edge(val, label=label, attrs=attrs), int childId)
    = edgeStatement(id, childId, label="<label>", attrs=attrs);
default Statement defaultEdgeGenerator(value _, int id, value e, int childId) = edgeStatement(id, childId);

Statement nodeStatement(str label, int id, Attrs attrs=[]) {
    Id nodeId = [Id] "<id>";
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

private str toAttrValue(str s) = "\"" + escape(s, ("\\": "\\\\", "\"": "\\\"")) + "\"";
private str toLabelString(str s) = escape(s, ("\r": "\\r", "\n": "\\n"));

bool(value v) parseTreeValueFilter(bool filterLayout, bool filterMissingOptionals, bool filterEmptyYield) {
    bool valueFilter(t:appl(production, args)) {
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

        if (filterEmptyYield && "<t>" == "") {
            return false;
        }

        return true;
    }
    default bool valueFilter(_) = true;

    return valueFilter;
}

private data Token = token(str token, Attrs attrs);

value(value v) parseTreeValueTransformer(bool collapseTokens) {
    value valueTransformer(t: appl(prod,_)) {
        if (collapseTokens && isToken(t)) {
            return token("<t>", treeAttrs(t, [], tooltip=));
        }

        return t;
    }
    default value valueTransformer(value n) = n;

    return valueTransformer;
}

DotConfig createYieldParseTreeConfig(bool collapseTokens=true, bool filterLayout=true, bool filterMissingOptionals=true, bool filterEmptyYield=true) {
    return dotConfig(
        childGetter = getParseTreeChildren,
        valueFilter = parseTreeValueFilter(filterLayout, filterMissingOptionals, filterEmptyYield),
        valueTransformer = parseTreeValueTransformer(collapseTokens),
        nodeGenerator = yieldParseTreeNodeGenerator,
        nodeAttrs = DEFAULT_NODE_ATTRS + [<WIDTH, "0.5">]
    );
}

DotConfig createParseTreeConfig(bool collapseTokens=true, bool filterLayout=true, bool filterMissingOptionals=true, bool filterEmptyYield=true) {
    return dotConfig(
        childGetter = getParseTreeChildren,
        valueFilter = parseTreeValueFilter(filterLayout, filterMissingOptionals, filterEmptyYield),
        valueTransformer = parseTreeValueTransformer(collapseTokens),
        nodeGenerator = parseTreeNodeGenerator
    );
}

DotConfig createParseTreeConfigFull(bool collapseTokens=false, bool filterLayout=false, bool filterMissingOptionals=false, bool filterEmptyYield=false)
    = createParseTreeConfig(collapseTokens=collapseTokens, filterLayout=filterLayout, filterMissingOptionals=filterMissingOptionals, filterEmptyYield=filterEmptyYield);

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

int nextId = 1;
private StatementList generateStatements(DotConfig config, value v, int id, map[value,int] processed) {
    if (subGraph(val, label=label, attrs=attrs) := v) {
        Id clusterId = [Id] "cluster_<nextId>";
        nextId = nextId + 1;

        StatementList stats = generateStatements(config, val, id, processed);

        if (label != "") {
            attrs = attrs + <LABEL, label>;
        }

        for (<attrName,attrVal> <- attrs) {
            Id nameId = [Id] toAttrValue(attrName);
            Id valId =  [Id] toAttrValue(attrVal);
            stats = addStatement(stats, (Statement)`<Id nameId>=<Id valId>`);
        }

        AttrList attrList = generateAttrList(attrs);
        return (StatementList)`subgraph <Id clusterId> { <StatementList stats> }`;
    }

    v = config.valueTransformer(v);

    Statement stat = config.nodeGenerator(v, id);

    StatementList stats = (StatementList) `<Statement stat>`;

    for (value edgeChild <- config.childGetter(v)) {
        value child = edge(val) := edgeChild ? val : edgeChild;

        if (!config.valueFilter(child)) {
            continue;
        }

        int childId = 0;
        if (config.allowRecursion && child in processed) {
            childId = processed[child];
        } else {
            childId = nextId;
            nextId += 1;
            if (config.allowRecursion) {
                processed[child] = childId;
            }

            StatementList childStats = generateStatements(config, child, childId, processed);
            stats = mergeStatements(stats, childStats);
        }
        
        Statement edgeStat = config.edgeGenerator(v, id, edgeChild, childId);
        stats = addStatement(stats, edgeStat);
    }

    return stats;
}

str yield(Tree t, int maxLength) {
    str yield = "<t>";
    if (size(yield) > maxLength) {
        yield = substring(yield, 0, maxLength) + "...";
    }
    return yield;
}

Attrs treeAttrs(Tree t, Attrs baseAttrs, str tooltip="") {
    Attrs attrs = [];
    if (tooltip != "") {
        attrs = attrs + [<TOOLTIP, tooltip>] + baseAttrs;
    }

    if (t@\loc?) {
        loc editorLoc = |https://editor|;
        editorLoc.query = "src=<t@\loc>";
        str link = substring(editorLoc.uri, 8); // Remove https://
        attrs = attrs + [<URL, link>];
    }

    return attrs;
}

private list[value] getParseTreeChildren(amb(set[Tree] alts)) = toList(alts);
// Label edges if the corresponding symbol in the production has a label
private list[value] getParseTreeChildren(appl(prod, args)) = getApplChildren(prod, args);
private default list[value] getParseTreeChildren(t) = [];

private list[value] getApplChildren(prod(_,formals,_), actuals) {
    list[tuple[Symbol,Tree]] args = zip2(formals, actuals);
    return [ label(name, _) := formal ? edge(actual, label=name) : actual | <formal, actual> <- args];
}
private default list[value] getApplChildren(_, list[value] args) = args;

Statement yieldParseTreeNodeGenerator(t:appl(prod,args), int id) {
    str label = yield(t, 30);
    if (label == "") {
        label = " ";
    }
    return nodeStatement(label, id, attrs=treeAttrs(t, [SHAPE_RECT, STYLE_ROUNDED], tooltip=prodToString(prod)));
}
default Statement yieldParseTreeNodeGenerator(value v, int id) = parseTreeNodeGenerator(v, id);

Statement parseTreeNodeGenerator(t:appl(prod, args), int id) {
    Attrs attrs = treeAttrs(t, [SHAPE_RECT, STYLE_ROUNDED], tooltip=yield(t, 256));
    return nodeStatement(prodToString(prod), id, attrs=attrs);
}

Statement parseTreeNodeGenerator(t:amb(alts), int id) = nodeStatement("amb", id, attrs=treeAttrs(t, [SHAPE_ELLIPSE, COLOR_ORANGE]));
Statement parseTreeNodeGenerator(t:char(ch), int id) = nodeStatement("<stringChar(ch)>", id, attrs=treeAttrs(t, [SHAPE_CIRCLE]));
Statement parseTreeNodeGenerator(t:cycle(sym, length), int id) = nodeStatement("cycle(<sym>,<length>)", id, attrs=treeAttrs(t, [SHAPE_CIRCLE]));
Statement parseTreeNodeGenerator(token(str tok, Attrs attrs), int id) = nodeStatement(tok, id, attrs=attrs);
default Statement parseTreeNodeGenerator(value v, id) = defaultNodeGenerator(v, id);

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
private str symbolToString(opt(sym)) = "<parenthesizeSymbol(sym)>?";
private str symbolToString(\iter(sym)) = "<parenthesizeSymbol(sym)>+";
private str symbolToString(\iter-seps(sym, seps)) = "{ <symbolToString(sym)> <symbolsToString(seps)>}+";
private str symbolToString(\iter-star(sym)) = "<parenthesizeSymbol(sym)>*";
private str symbolToString(\iter-star-seps(sym, seps)) = "{ <symbolToString(sym)> <symbolsToString(seps)>}*";
private str symbolToString(\label(name,sym)) = symbolToString(sym);
private str symbolToString(\conditional(sym, _)) = symbolToString(sym);
private str symbolToString(\seq(symbols)) = symbolsToString(symbols);
private str symbolToString(\alt(alts)) = substring(("" | "<it> | <symbolToString(alt)>" | alt <- alts), 3);

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

private str parenthesizeSymbol(Symbol sym) = isSimpleSymbol(sym) ? symbolToString(sym) : "(<symbolToString(sym)>)";
private bool isSimpleSymbol(\sort(_)) = true;
private bool isSimpleSymbol(\lex(_)) = true;
private bool isSimpleSymbol(\lit(_)) = true;
private bool isSimpleSymbol(\cilit(_)) = true;
private bool isSimpleSymbol(\layouts(_)) = true;
private bool isSimpleSymbol(\label(_, sym)) = isSimpleSymbol(sym);
private bool isSimpleSymbol(\conditional(sym, _)) = isSimpleSymbol(sym);
private bool isSimpleSymbol(\start(sym)) = isSimpleSymbol(sym);
private default bool isSimpleSymbol(Symbol _) = false;

private default str rangesToString(list[CharRange] ranges) {
    if ([range(1, end1), range(begin2, 1114111)] := ranges && end1 < begin2) {
        return "[^<rangeToString(range(end1+1,begin2-1))>]";
    }

    str result = "[";
    for (CharRange range <- ranges) {
        result += rangeToString(range);
    }
    return result + "]";
}

private str rangeToString(range(begin, end)) {
    if (begin == end) {
        return charToString(begin);
    }

    if (begin+1 == end) {
        return charToString(begin) + charToString(end);
    }

    return charToString(begin) + "-" + charToString(end);
}

private str charToString(9) = "\\t";
private str charToString(32) = "\\ ";
private default str charToString(c) = stringChar(c);

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
        str title = "<dot.id>";

        // Remove double quotes around graph id
        title = substring(title, 1, size(title)-1);

        return response(writeHTMLString(plotHTML(title)));
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
private HTMLElement plotHTML(str pageTitle)
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
