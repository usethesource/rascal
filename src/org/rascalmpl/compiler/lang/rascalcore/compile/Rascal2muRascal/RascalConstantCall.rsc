module lang::rascalcore::compile::Rascal2muRascal::RascalConstantCall

import lang::rascalcore::compile::muRascal::AST;
import ParseTree;
import String;
import Grammar;
import List;
import Set;
import Map;
import Node;
import Type;
import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;

/*
 *  Translate selected calls with constant arguments at compile time
 *  See lang::rascalcore::compile::muRascal::Primitives for constant folding of muPrimitives
 */

MuExp translateConstantCall(str name, list[MuExp] args) {
	return tcc(name, args);
}

private MuExp tcc("size", [muCon(value v)]){
    switch(v){
        case str s: return muCon(size(s));
        case list[value] lst: return muCon(size(lst));
        case set[value] st: return muCon(size(st));
        case map[value,value] m: return(muCon(size(m)));     
    };
     throw "NotConstant";
}

private MuExp tcc("isEmpty", [muCon(value v)]){
    switch(v){
        case str s: return muCon(isEmpty(s));
        case list[value] lst: return muCon(isEmpty(lst));
        case set[value] st: return muCon(isEmpty(st));
        case map[value,value] m: return(muCon(isEmpty(m)));
    }
    throw "NotConstant";
}

private MuExp tcc("reverse", [muCon(value v)]){
    switch(v){
        case str s: return muCon(reverse(s));
        case list[value] lst: return muCon(reverse(lst));
    }
    throw "NotConstant";
}

// List

private  MuExp tcc("index", [muCon(list[value] lst)]) = muCon(index(lst));

// Node
private MuExp tcc("getName", [muCon(node nd)]) = muCon(getName(nd));
private MuExp tcc("getChildren", [muCon(node nd)]) = muCon(getChildren(nd));
 
// Tree

private MuExp tcc("appl", [muCon(Production prod), muCon(list[Tree] args)]) = muCon(ParseTree::appl(prod, args));
private MuExp tcc("cycle", [muCon(Symbol symbol), muCon(int cycleLength)]) = muCon(ParseTree::cycle(symbol, cycleLength));
private MuExp tcc("char", [muCon(int character)]) = muCon(ParseTree::char(character));
private MuExp tcc("amb",  [muCon(set[Tree] alternatives)]) = muCon(ParseTree::amb(alternatives));

// Production

private MuExp tcc("prod", [muCon(Symbol def), muCon(list[Symbol] symbols), muCon(set[ParseTree::Attr] attributes)]) = muCon(ParseTree::prod(def, symbols, attributes));
private MuExp tcc("regular", [muCon(Symbol def)]) = muCon(ParseTree::regular(def));
private MuExp tcc("priority", [muCon(Symbol def), muCon(list[Production] choices)]) = muCon(ParseTree::priority(def, choices));
private MuExp tcc("associativity", [muCon(Symbol def), muCon(ParseTree::Associativity \assoc), muCon(set[Production] alternatives)]) = muCon(ParseTree::associativity(def, \assoc, alternatives));
private MuExp tcc("reference", [muCon(Symbol def), muCon(str cons)]) = muCon(ParseTree::reference(def, cons));
private MuExp tcc("choice", [muCon(Symbol def), muCon(set[Production] alternatives)]) = muCon(Type::\choice(def, alternatives));

// Attr

private MuExp tcc("tag", [muCon(value \tag)]) = muCon(Attr::\tag(\tag));
private MuExp tcc("bracket", []) = muCon(ParseTree::\bracket());
private MuExp tcc("assoc", [muCon(ParseTree::Associativity \assoc)]) = muCon(ParseTree::\assoc(\assoc));

// Associativity

private MuExp tcc("left", []) = muCon(ParseTree::\left());
private MuExp tcc("right", []) = muCon(ParseTree::\right());
private MuExp tcc("assoc", []) = muCon(ParseTree::\assoc());
private MuExp tcc("non-assoc", []) = muCon(ParseTree::\non-assoc());

// CharRange

private MuExp tcc("range", [muCon(begin), muCon(end)]) = muCon(ParseTree::range(begin, end));

// Symbols

private MuExp tcc("int", []) = muCon(\int());
private MuExp tcc("bool", []) = muCon(\bool());
private MuExp tcc("real", []) = muCon(\real());
private MuExp tcc("rat", []) = muCon(\rat());
private MuExp tcc("str", []) = muCon(\str());
private MuExp tcc("num", []) = muCon(\num());
private MuExp tcc("node", []) = muCon(\node());
private MuExp tcc("void", []) = muCon(\void());
private MuExp tcc("value", []) = muCon(\value());
private MuExp tcc("loc", []) = muCon(\loc());
private MuExp tcc("datetime", []) = muCon(\datetime());

 
private MuExp tcc("label", [muCon(str name), muCon(Symbol symbol)]) = muCon(\label(name, symbol));

private MuExp tcc("set", [muCon(Symbol symbol)]) = muCon(\set(symbol));
private MuExp tcc("rel", [muCon(list[Symbol] symbols)]) = muCon(\rel(symbols));
private MuExp tcc("lrel", [muCon(list[Symbol] symbols)]) = muCon(\lrel(symbols));
private MuExp tcc("tuple", [muCon(list[Symbol] symbols)]) = muCon(\tuple(symbols));
private MuExp tcc("list", [muCon(Symbol symbol)]) = muCon(\list(symbol));
private MuExp tcc("map", [muCon(Symbol from), muCon(Symbol to)]) = muCon(\map(from, to));
private MuExp tcc("bag", [muCon(Symbol symbol)]) = muCon(\bag(symbol));
private MuExp tcc("adt", [muCon(str name), muCon(list[Symbol] parameters)]) = muCon(\adt(name, parameters));
private MuExp tcc("cons", [muCon(Symbol adt), muCon(str name), muCon(list[Symbol] parameters)]) = muCon(cons(adt, name, parameters));
private MuExp tcc("alias", [muCon(str name), muCon(list[Symbol] parameters), muCon(Symbol aliased)]) = muCon(\alias(name, parameters, aliased));
private MuExp tcc("func", [muCon(Symbol ret), muCon(list[Symbol] parameters), list[Symbol] kwparameters]) = muCon(func(ret, parameters, kwparameters));

private MuExp tcc("parameter", [muCon(str name), muCon(Symbol bound)]) = muCon(\parameter(name, bound));

private MuExp tcc("start", [muCon(Symbol symbol)]) = muCon(ParseTree::\start(symbol));

private MuExp tcc("sort", [muCon(str name)]) = muCon(Symbol::sort(name));
private MuExp tcc("lex", [muCon(str name)]) = muCon(lex(name));
private MuExp tcc("layouts", [muCon(str name)]) = muCon(ParseTree::layouts(name));
private MuExp tcc("keywords", [muCon(str name)]) = muCon(ParseTree::keywords(name));
private MuExp tcc("parameterized-sort", [muCon(str name), muCon(list[Symbol] parameters)]) = muCon(\parameterized-sort(name, parameters));
private MuExp tcc("parameterized-lex", [muCon(str name), muCon(list[Symbol] parameters)]) = muCon(\parameterized-sort(name, parameters));

private MuExp tcc("lit", [muCon(str string)]) = muCon(Symbol::lit(string));
private MuExp tcc("cilit", [muCon(str string)]) = muCon(Symbol::cilit(string));
private MuExp tcc("char-class", [muCon(list[CharRange] ranges)]) = muCon(Symbol::\char-class(ranges));
private MuExp tcc("empty", []) = muCon(ParseTree::empty());
private MuExp tcc("opt", [muCon(Symbol symbol)]) = muCon(Symbol::opt(symbol));

private MuExp tcc("iter", [muCon(Symbol symbol)]) = muCon(Symbol::iter(symbol));
private MuExp tcc("iter-star", [muCon(Symbol symbol)]) = muCon(Symbol::\iter-star(symbol));
private MuExp tcc("iter-seps", [muCon(Symbol symbol), muCon(list[Symbol] separators)]) = muCon(ParseTree::\iter-seps(symbol, separators));
private MuExp tcc("iter-star-seps", [muCon(Symbol symbol), muCon(list[Symbol] separators)]) = muCon(ParseTree::\iter-star-seps(symbol, separators));
private MuExp tcc("alt", [muCon(set[Symbol] alternatives)]) = muCon(ParseTree::alt(alternatives));

private MuExp tcc("seq", [muCon(list[Symbol] symbols)]) = muCon(ParseTree::seq(symbols));
private MuExp tcc("conditional", [muCon(Symbol symbol), muCon(set[Condition] conditions)]) = muCon(ParseTree::conditional(symbol, conditions));
  
// Condition

private MuExp tcc("follow", [muCon(Symbol symbol)]) = muCon(ParseTree::follow(symbol));
private MuExp tcc("not-follow", [muCon(Symbol symbol)]) = muCon(ParseTree::\not-follow(symbol));
private MuExp tcc("precede", [muCon(Symbol symbol)]) = muCon(ParseTree::precede(symbol));
private MuExp tcc("not-precede", [muCon(Symbol symbol)]) = muCon(ParseTree::\not-precede(symbol));
private MuExp tcc("delete", [muCon(Symbol symbol)]) = muCon(ParseTree::delete(symbol));
private MuExp tcc("at-column", [muCon(int column)]) = muCon(ParseTree::\at-column(column));
private MuExp tcc("begin-of-line", []) = muCon(ParseTree::\begin-of-line());
private MuExp tcc("end-of-line", []) = muCon(ParseTree::\end-of-line());
private MuExp tcc("except", [muCon(str label)]) = muCon(ParseTree::except(label));

// Grammar

private MuExp tcc("grammar", [muCon(set[Symbol] starts), muCon(map[Symbol sort, Production def] rules)]) = muCon(Grammar::grammar(starts, rules));
  

default MuExp tcc(str name, list[MuExp] args) { 
    throw "NotConstant"; 
}