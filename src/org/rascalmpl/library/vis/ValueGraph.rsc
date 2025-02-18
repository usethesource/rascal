module vis::ValueGraph

import vis::Graphs;
import ParseTree;
import Node;
import Set;
import Map;
import List;
import String;
import IO;
import Type;

private value identity(value val) = val;
private bool success(_) = true;

str CLASS_CHAR = "char";
str CLASS_AMB = "amb";
str CLASS_CYCLE = "cycle";
str CLASS_TOKEN = "token";

CytoStyle defNodeStyle() = cytoNodeStyle(
        visibility        = "visible", /* hidden, collapse */
        opacity           = "1",
        width             = "label",
        height            = "label",
        padding           = "5pt",
        color             = "white",
        \text-opacity     = "1",
        \font-size        = "5pt",
        \font-weight      = bold(),
        \background-color = "red",
        label             = "data(label)",
        shape             = \round-rectangle(),
        \text-halign      = CytoHorizontalAlign::\center(),
        \text-valign      = CytoVerticalAlign::\center(),
        \text-wrap        = CytoTextWrap::none(),
        \text-justification = CytoHorizontalAlign::center(),
        \line-height      = 1
    );

CytoStyle defEdgeStyle() = cytoEdgeStyle(
        visibility          = "visible", /* hidden, collapse */
        opacity             = "1",
        \line-opacity       = "1",
        width               = 1,
        \line-style         = "solid", /* dotted, dashed */
        \color              = "black",
        \target-arrow-color = "black",
        \source-arrow-color = "black",
        \target-arrow-shape = CytoArrowHeadStyle::triangle(),
        \source-arrow-shape = CytoArrowHeadStyle::none(),
        \curve-style        = \bezier(),
        \source-text-offset = 1,
        \target-text-offset = 1,
        \label              = "data(label)"
    );

data ValueToGraphConfig = valueToGraphConfig(
    list[value](value n) childGetter = defaultGetChildren,
    bool(value n) valueFilter = success,
    value(value n) valueTransformer = identity,
    CytoData(value n, str id) nodeGenerator = defaultNodeGenerator,
    CytoData(value n, str id, value child, str childId) edgeGenerator = defaultEdgeGenerator,
    list[CytoStyleOf] styles = [cytoNodeStyleOf(defNodeStyle()), cytoEdgeStyleOf(defEdgeStyle())]
);

// An EdgeValue is used to bundle extra edge information with a value
private data EdgeValue = edge(value val, str edgeLabel="", list[str] classes=[]);
private data Token = token(str token, str tooltip, loc source);

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

private default list[value] getNodeChildren(node n) {
    map[str,value] kwParams = getKeywordParameters(n);
    list[value] kwEdges = [edge(kwParams[key], edgeLabel="<key>", classes=[]) | key <- kwParams];
    return getChildren(n) + kwEdges;
}

// Label edges if the corresponding symbol in the production has a label
private list[value] getParseTreeChildren(appl(prod, args)) = getApplChildren(prod, args);
private list[value] getApplChildren(prod(_,formals,_), actuals) {
    list[tuple[Symbol,Tree]] args = zip2(formals, actuals);
    return [ label(name, _) := formal ? edge(actual, edgeLabel=name) : actual | <formal, actual> <- args];
}
private default list[value] getApplChildren(_, list[value] args) = args;

private list[value] getParseTreeChildren(amb(set[Tree] alts)) = toList(alts);
private default list[value] getParseTreeChildren(t) = [];

private CytoData cytoNode(str id, str label, str editor="|nothing:///|", str tooltip="", list[str] classes=[]) = cytodata(\node(id, label=toCytoString(label), editor=editor, tooltip=tooltip), classes=classes);
private CytoData cytoEdge(str parentId, str childId, str label="", list[str] classes=[]) = cytodata(\edge(parentId, childId, label=toCytoString(label)), classes=classes);

CytoData defaultNodeGenerator(edge(val), str id) = defaultNodeGenerator(val, id);
CytoData defaultNodeGenerator(num n, str id) = cytoNode(id, "<n>");
CytoData defaultNodeGenerator(str s, str id) = cytoNode(id, "\"<s>\"");
CytoData defaultNodeGenerator(bool b, str id) = cytoNode(id, b ? "T" : "F", classes=[b ? "boolTrue" : "boolFalse"]);
CytoData defaultNodeGenerator(list[value] l, str id) = cytoNode(id, isEmpty(l) ? "list[]" : typeLabel(l), classes=["collection", "list"]);
CytoData defaultNodeGenerator(set[value] s, str id) = cytoNode(id, isEmpty(s) ? "set[]" : typeLabel(s), classes=["collection", "set"]);
CytoData defaultNodeGenerator(map[value,value] m, str id) = cytoNode(id, isEmpty(m) ? "map[,]" : typeLabel(m), classes=["collection", "map"]);
CytoData defaultNodeGenerator(t:<_>, str id) = cytoNode(id, typeLabel(t), classes=["tuple"]);
CytoData defaultNodeGenerator(t:<_,_>, str id) = cytoNode(id, typeLabel(t), classes=["tuple"]);
CytoData defaultNodeGenerator(t:<_,_,_>, str id) = cytoNode(id, typeLabel(t), classes=["tuple"]);
CytoData defaultNodeGenerator(t:<_,_,_,_>, str id) = cytoNode(id, typeLabel(t), classes=["tuple"]);
CytoData defaultNodeGenerator(t:<_,_,_,_,_>, str id) = cytoNode(id, typeLabel(t), classes=["tuple"]);
CytoData defaultNodeGenerator(t:<_,_,_,_,_,_>, str id) = cytoNode(id, typeLabel(t), classes=["tuple"]);
CytoData defaultNodeGenerator(t:<_,_,_,_,_,_,_>, str id) = cytoNode(id, typeLabel(t), classes=["tuple"]);
CytoData defaultNodeGenerator(t:<_,_,_,_,_,_,_,_>, str id) = cytoNode(id, typeLabel(t), classes=["tuple"]);
// TODO: link location
CytoData defaultNodeGenerator(loc l, str id) {
    str source = "<l>";
    if (l.length?) {
        if (l.length == 1) {
            return cytoNode(id, "<l.begin.line>,<l.begin.column>", editor=source, tooltip=source);
        }
        return cytoNode(id, "<l.begin.line>,<l.begin.column>-<l.end.line>,<l.end.column>", editor=source, tooltip=source);
    }

    return cytoNode(id, "loc", editor=source, tooltip=source);
}

str yield(Tree t, int maxLength) {
    str yield = "<t>";
    if (size(yield) > maxLength) {
        yield = substring(yield, 0, maxLength) + "...";
    }
    return yield;
}

str editor(Tree t) {
    if (t@\loc?) {
        return "<t@\loc>";
    }

    return "|unknown:///|";
}

CytoData defaultNodeGenerator(node n, str id) = nodeNodeGenerator(n, id);
default CytoData defaultNodeGenerator(value v, str id) = cytoNode(id, "<v>");

private CytoData nodeNodeGenerator(Tree tree, str id) = parseTreeNodeGenerator(tree, id);
private default CytoData nodeNodeGenerator(node n, str id) = cytoNode(id, getName(n));

CytoData parseTreeNodeGenerator(t:appl(prod, args), str id) = cytoNode(id, prodToString(prod), editor=editor(t), tooltip=yield(t, 100));
CytoData parseTreeNodeGenerator(t: amb(alts), str id) = cytoNode(id, "amb", classes=["ambiguity"], editor=editor(t), tooltip=yield(t, 100), classes=[CLASS_AMB]);
CytoData parseTreeNodeGenerator(t: char(ch), str id) = cytoNode(id, "\'<stringChar(ch)>\'", editor=editor(t), tooltip="char(<ch>)", classes=[CLASS_CHAR]);
CytoData parseTreeNodeGenerator(t: cycle(sym, length), id) = cytoNode(id, "cycle(<sym>,<length>)", editor=editor(t), tooltip="cycle", classes=[CLASS_CYCLE]);
CytoData parseTreeNodeGenerator(token(str tok, str tooltip, loc source), id) = cytoNode(id, tok, editor="<source>", tooltip=tooltip, classes=[CLASS_TOKEN]);
default CytoData parseTreeNodeGenerator(n, str id) = cytoNode(id, "<n>");

CytoData yieldParseTreeNodeGenerator(t:appl(prod,args), str id) {
    str label = yield(t, 30);
    if (label == "") {
        label = " ";
    }
    return cytoNode(id, label, editor=editor(t), tooltip=prodToString(prod));
}
default CytoData yieldParseTreeNodeGenerator(value v, str id) = parseTreeNodeGenerator(v, id);

CytoData defaultEdgeGenerator(value _, str parentId, edge(val, edgeLabel=edgeLabel, classes=classes), str childId)
    = cytoEdge(parentId, childId, label=edgeLabel, classes=classes);
default CytoData defaultEdgeGenerator(value _, str parentId, value e, str childId) = cytoEdge(parentId, childId);

private str toCytoString(str s) = escape(s, ("\n": "\\n"));

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

private default str symbolToString(sym) {
    println("default symbolToString: <sym>");
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

private bool(value v) parseTreeValueFilter(bool filterLayout, bool filterMissingOptionals, bool filterEmptyYield) {
    bool valueFilter(t: appl(production, args)) {
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

private value(value v) parseTreeValueTransformer(bool collapseTokens) {
    value valueTransformer(Tree t: appl(prod,_)) {
        if (collapseTokens && isToken(t)) {
            loc editorLoc = |nothing:///|;
            if (t@\loc?) {
                editorLoc = t@\loc;
            }
            return token("<t>", prodToString(prod), editorLoc);
        }

        return t;
    }
    default value valueTransformer(value n) = n;

    return valueTransformer;
}

private bool isTokenType(lit(_)) = true;
private bool isTokenType(cilit(_)) = true;    
private bool isTokenType(lex(_)) = true;  
private bool isTokenType(layouts(_)) = true;
private bool isTokenType(label(str _, Symbol s)) = isTokenType(s);
private default bool isTokenType(Symbol _) = false;
private bool isToken(appl(prod(Symbol s, _, _), _)) = true when isTokenType(s);
private bool isToken(char(_)) = true;
private default bool isToken(Tree _) = false;

private CytoStyleOf nodeStyleOf(str cls, CytoStyle style) = cytoStyleOf(selector=\node(\className(cls)), style=style);

list[CytoStyleOf] getParseTreeStyles() {
    CytoStyle ellipseStyle = defNodeStyle()[shape=\ellipse()];
    CytoStyleOf styleOfChar = nodeStyleOf(CLASS_CHAR, ellipseStyle);
    CytoStyleOf styleOfAmb = nodeStyleOf(CLASS_AMB, ellipseStyle[\border-color="orange"]);
    CytoStyleOf styleOfCycle = nodeStyleOf(CLASS_CYCLE, ellipseStyle);
    CytoStyleOf styleOfToken = nodeStyleOf(CLASS_TOKEN, defNodeStyle()[\border-color="darkblue"]);
    return [cytoNodeStyleOf(defNodeStyle()), cytoEdgeStyleOf(defEdgeStyle()),
            styleOfChar, styleOfAmb, styleOfCycle, styleOfToken];
}

ValueToGraphConfig createParseTreeConfig(bool collapseTokens=false, bool filterLayout=false, bool filterMissingOptionals=false, bool filterEmptyYield=true) {
    return valueToGraphConfig(
        childGetter = getParseTreeChildren,
        valueFilter = parseTreeValueFilter(filterLayout, filterMissingOptionals, filterEmptyYield),
        valueTransformer = parseTreeValueTransformer(collapseTokens),
        nodeGenerator = parseTreeNodeGenerator,
        styles = getParseTreeStyles());
}

ValueToGraphConfig createYieldParseTreeConfig(bool collapseTokens=true, bool filterLayout=true, bool filterMissingOptionals=true, bool filterEmptyYield=true) {
    ValueToGraphConfig config = createParseTreeConfig(collapseTokens=collapseTokens, filterLayout=filterLayout, filterMissingOptionals=filterMissingOptionals);
    config.nodeGenerator = yieldParseTreeNodeGenerator;
    return config;
}

int nextId = 0;

Cytoscape valueToGraph(value v, ValueToGraphConfig config = valueToGraphConfig()) {
    nextId = 1;
    return cytoscape(
        elements = generateElements(v, 0, config),
        style=config.styles,
        \layout=defaultDagreLayout(spacingFactor=0.5)
    );
}

private list[CytoData] generateElements(value v, int id, ValueToGraphConfig config) {
    v = config.valueTransformer(v);

    CytoData nodeData = config.nodeGenerator(v, "<id>");

    list[CytoData] elements = [nodeData];

    for (value edgeChild <- config.childGetter(v)) {
        value child = edge(val) := edgeChild ? val : edgeChild;
        
        if (!config.valueFilter(child)) {
            continue;
        }

        int childId = nextId;
        nextId += 1;
        
        list[CytoData] childElems = generateElements(child, childId, config);
        elements = elements + childElems;

        CytoData edgeData = config.edgeGenerator(v, "<id>", edgeChild, "<childId>");
        elements = elements + edgeData;
    }

    return elements;
}
