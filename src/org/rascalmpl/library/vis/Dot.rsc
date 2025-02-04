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
import Type;

data DotConfig = dotConfig(
    list[value](value n) childGetter = defaultGetChildren,
    bool(value n) valueFilter = success,
    value(value n) valueTransformer = identity,
    Statement(value n, int id) nodeGenerator = defaultNodeGenerator,
    Statement(value n, int id, value child, int childId) edgeGenerator = defaultEdgeGenerator
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
    println("edges: <getChildren(n) + kwEdges>");
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

tuple[str,str] COLLECTION_SHAPE = <"shape","doubleoctagon">;
tuple[str,str] TUPLE_SHAPE = <"shape","hexagon">;
tuple[str,str] LOCATION_SHAPE = <"shape","house">;
tuple[str,str] ATOM_SHAPE = <"shape","rect">;
tuple[str,str] BOOL_SHAPE = <"shape","circle">;


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

Statement defaultEdgeGenerator(value _, int id, edge(val, edgeLabel=edgeLabel, edgeAttributes=attrs), int childId)
    = edgeStatement(id, childId, label="<edgeLabel>", attrs=attrs);
default Statement defaultEdgeGenerator(value _, int id, value e, int childId) = edgeStatement(id, childId);

private Statement nodeStatement(str label, int id, Attrs attrs=[]) {
    Id nodeId = [Id] "<id>";
    AttrList attrList = generateAttrList(attrs + <"label", label>);
    return (Statement) `<Id nodeId> <AttrList attrList>;`;
}

Statement edgeStatement(int parentId, int childId, str label="", Attrs attrs=[]) {
    if (label != "") {
        attrs = attrs + <"label", label>;
    }

    Id fromId = [Id] "<parentId>";
    Id toId = [Id] "<childId>";
    if (attrs == []) {
        return (Statement) `<Id fromId> -\> <Id toId>;`;
    }

    AttrList attrList = generateAttrList(attrs);
    return (Statement) `<Id fromId> -\> <Id toId> <AttrList attrList>;`;
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

private str toDotString(str s) = "\"" + escape(s, ("\"": "\\\"", "\n": "\\\\n")) + "\"";

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

int nextId = 1;

DOT value2Dot(value v, str name="Unknown", DotConfig config = dotConfig()) {
    Id graphName = [Id] name;
    nextId = 1;

    StatementList stats = config.valueFilter(v) ? generateStatements(config, v, 0) : (StatementList)``;
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
    Id nameId = [Id] toDotString(name);
    Id valId = [Id] toDotString(val);
    return (AttrList) `[<Attribute* attrs1> <Id nameId> = <Id valId>]`;
}

private StatementList generateStatements(DotConfig config, value v, int id) {
    println("value: <v>");
    println("type: <typeOf(v)>");

    v = config.valueTransformer(v);

    Statement stat = config.nodeGenerator(v, id);

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

Statement parseTreeNodeGenerator(appl(prod, args), int id) {
    str label = prodToString(prod);
    return nodeStatement(label, id);
}

Statement parseTreeNodeGenerator(amb(alts), id) = nodeStatement("amb", id, attrs=[<"shape", "oval">, <"color","orange">]);
Statement parseTreeNodeGenerator(char(ch), id) = nodeStatement("\'<stringChar(ch)>\'", id);
Statement parseTreeNodeGenerator(cycle(sym, length), id) = nodeStatement("cycle(<sym>,<length>)", id, attrs=[<"shape","round">]);
default Statement parseTreeNodeGenerator(n, id) = nodeStatement("<n>", id);

private str prodToString(prod(def, symbols, attrs)) = "<symbolToString(def)> -\> <symbolsToString(symbols)>";
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