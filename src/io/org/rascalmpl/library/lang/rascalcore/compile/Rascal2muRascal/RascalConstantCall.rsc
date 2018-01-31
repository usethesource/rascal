module lang::rascalcore::compile::Rascal2muRascal::RascalConstantCall

import lang::rascalcore::compile::muRascal::AST;
import ParseTree;
import String;
import List;
import Set;

MuExp translateConstantCall(str name, list[MuExp] args) =
	tcc(name, args);

// String
private MuExp tcc("size", [muCon(str s)]) = muCon(size(s));

// List
private MuExp tcc("size", [muCon(list[value] lst)]) = muCon(size(lst));
private MuExp tcc("isEmpty", [muCon(list[value] lst)]) = muCon(isEmpty(lst));
 
// Set 
private MuExp tcc("size", [muCon(set[value] st)]) = muCon(size(st));
private MuExp tcc("isEmpty", [muCon(set[value] st)]) = muCon(isEmpty(st));

// Type
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
private MuExp tcc("cons", [muCon(Symbol \adt), muCon(str name), muCon(list[Symbol] parameters)]) = muCon(\cons(\adt, name, parameters));
private MuExp tcc("alias", [muCon(str name), muCon(list[Symbol] parameters), muCon(Symbol aliased)]) = muCon(\alias(name, parameters, aliased));
private MuExp tcc("func", [muCon(Symbol ret), muCon(list[Symbol] parameters)]) = muCon(\func(ret, parameters, []));
//TODO: TC gives duplicate function error on next definition:
//MuExp tcc("var-func", [muCon(Symbol ret), muCon(list[Symbol] parameters), Symbol varArg]) = muCon(\var-func(ret, parameters, varArg));
private MuExp tcc("reified", [muCon(Symbol symbol)]) = muCon(\reified(symbol));

private MuExp tcc("parameter", [muCon(str name), muCon(Symbol bound)]) = muCon(\parameter(name, bound));

private MuExp tcc("cons", [muCon(Symbol \def), muCon(list[Symbol] symbols), muCon(list[Symbol] kwTypes), muCon(set[Attr] attributes)]) = 
			muCon(\cons(\def, symbols, kwTypes, attributes));
private MuExp tcc("func", [muCon(Symbol def), muCon(list[Symbol] symbols), muCon(list[Symbol] kwTypes), muCon(set[Attr] attributes)]) = 
			muCon(\cons(def, symbols, kwTypes, attributes));
 
private MuExp tcc("choice", [muCon(Symbol def), muCon(set[Production] alternatives)]) = 
			muCon(\choice(def, alternatives));
			
private MuExp tcc("subtype", [muCon(Symbol lhs), muCon(Symbol rhs)]) = muCon(subtype(lhs, rhs));
private MuExp tcc("lub", [muCon(Symbol lhs), muCon(Symbol rhs)]) = muCon(lub(lhs, rhs));
private MuExp tcc("glb", [muCon(Symbol lhs), muCon(Symbol rhs)]) = muCon(glb(lhs, rhs));
 
// Tree

private MuExp tcc("appl", [muCon(Production prod), muCon(list[Tree] args)]) = muCon(appl(prod, args));
private MuExp tcc("cycle", [muCon(Symbol symbol), muCon(int cycleLength)]) = muCon(cycle(symbol, cycleLength));
private MuExp tcc("char", [muCon(int character)]) = muCon(char(character));

// Production

private MuExp tcc("prod", [muCon(Symbol def), muCon(list[Symbol] symbols), muCon(set[Attr] attributes)]) = muCon(prod(def,symbols,attributes));
private MuExp tcc("regular", [muCon(Symbol def)]) = muCon(regular(def));
private MuExp tcc("error", [muCon(Production prod), muCon(int dot)]) = muCon(error(prod, dot));
private MuExp tcc("skipped", []) = muCon(skipped());

private MuExp tcc("priority", [muCon(Symbol def), muCon(list[Production] choices)]) = muCon(\priority(def, choices));
private MuExp tcc("associativity", [muCon(Symbol def), muCon(Associativity \assoc), muCon(set[Production] alternatives)]) = muCon(\associativity(def, \assoc, alternatives));
private MuExp tcc("others", [muCon(Symbol def)]) = muCon(others(def));
private MuExp tcc("reference", [muCon(Symbol def), muCon(str cons)]) = muCon(reference(def, cons));

// Attr
private MuExp tcc("tag", [muCon(value tagVal)]) = muCon(\tag(tagVal));

private MuExp tcc("bracket", []) = muCon(\bracket());

// Associativity

private MuExp tcc("left", []) = muCon(\left());
private MuExp tcc("right", []) = muCon(\right());
private MuExp tcc("assoc", []) = muCon(\assoc());
private MuExp tcc("non-assoc", []) = muCon(\non-assoc());

// CharRange

private MuExp tcc("range", [muCon(int begin), muCon(int end)]) = muCon(range(begin, end));

// Symbol
private MuExp tcc("start", [muCon(Symbol symbol)]) = muCon(\start(symbol));
private MuExp tcc("sort", [muCon(str name)]) = muCon(sort(name));
private MuExp tcc("lex", [muCon(str name)]) = muCon(lex(name));
private MuExp tcc("layouts", [muCon(str name)]) = muCon(layouts(name));
private MuExp tcc("keywords", [muCon(str name)]) = muCon(keywords(name));
private MuExp tcc("parameterized-sort", [muCon(str name), muCon(list[Symbol] parameters)]) = muCon(\parameterized-sort(name, parameters));
private MuExp tcc("parameterized-lex", [muCon(str name), muCon(list[Symbol] parameters)]) = muCon(\parameterized-lex(name, parameters));

private MuExp tcc("lit", [muCon(str s)]) = muCon(lit(s));
private MuExp tcc("cilit", [muCon(str s)]) = muCon(cilit(s));
private MuExp tcc("char-class", [muCon(list[CharRange] ranges)]) = muCon(\char-class(ranges));

private MuExp tcc("empty", []) = muCon(empty());
private MuExp tcc("opt", [muCon(Symbol symbol)]) = muCon(opt(symbol));
private MuExp tcc("iter", [muCon(Symbol symbol)]) = muCon(iter(symbol));
private MuExp tcc("iter-star", [muCon(Symbol symbol)]) = muCon(\iter-star(symbol));
private MuExp tcc("iter-seps", [muCon(Symbol symbol), muCon(list[Symbol] separators)]) = muCon(\iter-seps(symbol, separators));
private MuExp tcc("iter-star-seps", [muCon(Symbol symbol), muCon(list[Symbol] separators)]) = muCon(\iter-star-seps(symbol, separators));
private MuExp tcc("alt", [muCon(set[Symbol] alternatives)]) = muCon(alt(alternatives));
private MuExp tcc("seq", [muCon(list[Symbol] symbols)]) = muCon(seq(symbols));

private MuExp tcc("conditional", [muCon(Symbol symbol), muCon(set[Condition] conditions)]) = muCon(conditional(symbol, conditions));

// Condition

private MuExp tcc("follow", [muCon(Symbol symbol)]) = muCon(follow(symbol));
private MuExp tcc("not-follow", [muCon(Symbol symbol)]) = muCon(\not-follow(symbol));
private MuExp tcc("precede", [muCon(Symbol symbol)]) = muCon(precede(symbol));
private MuExp tcc("not-precede", [muCon(Symbol symbol)]) = muCon(\not-precede(symbol));
private MuExp tcc("delete", [muCon(Symbol symbol)]) = muCon(delete(symbol));
private MuExp tcc("at-column", [muCon(int column)]) = muCon(\at-column(column));
private MuExp tcc("begin-of-line", []) = muCon(\begin-of-line());
private MuExp tcc("end-of-line", []) = muCon(\end-of-line());
private MuExp tcc("except", [muCon(str label)]) = muCon(\except(label));

private MuExp tcc("label", [muCon(str name), muCon(Symbol symbol)]) = muCon(label(name, symbol));
private MuExp tcc("label", [muCon(str name), muCon(Symbol symbol)]) = muCon(label(name, symbol));

default MuExp tcc(str name, list[MuExp] args) { throw "NotConstant"; }