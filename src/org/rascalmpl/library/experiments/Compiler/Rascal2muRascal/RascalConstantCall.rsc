module experiments::Compiler::Rascal2muRascal::RascalConstantCall

import experiments::Compiler::muRascal::AST;
import ParseTree;
import String;
import List;
import Set;
import Type;

MuExp translateConstantCall(str name, list[MuExp] args) =
	tcc(name, args);

// String
MuExp tcc("size", [muCon(str s)]) = muCon(size(s));

// List
MuExp tcc("size", [muCon(list[value] lst)]) = muCon(size(lst));
MuExp tcc("isEmpty", [muCon(list[value] lst)]) = muCon(isEmpty(lst));

// Set
MuExp tcc("size", [muCon(set[value] st)]) = muCon(size(st));
MuExp tcc("isEmpty", [muCon(set[value] st)]) = muCon(isEmpty(st));

// Type
MuExp tcc("int", []) = muCon(\int());
MuExp tcc("bool", []) = muCon(\bool());
MuExp tcc("real", []) = muCon(\real());
MuExp tcc("rat", []) = muCon(\rat());
MuExp tcc("str", []) = muCon(\str());
MuExp tcc("num", []) = muCon(\num());
MuExp tcc("node", []) = muCon(\node());
MuExp tcc("void", []) = muCon(\void());
MuExp tcc("value", []) = muCon(\value());
MuExp tcc("loc", []) = muCon(\loc());
MuExp tcc("datetime", []) = muCon(\datetime());

MuExp tcc("label", [muCon(str name), muCon(Symbol symbol)]) = muCon(\label(name, symbol));

MuExp tcc("set", [muCon(Symbol symbol)]) = muCon(\set(symbol));
MuExp tcc("rel", [muCon(list[Symbol] symbols)]) = muCon(\rel(symbols));
MuExp tcc("lrel", [muCon(list[Symbol] symbols)]) = muCon(\lrel(symbols));
MuExp tcc("tuple", [muCon(list[Symbol] symbols)]) = muCon(\tuple(symbols));
MuExp tcc("list", [muCon(Symbol symbol)]) = muCon(\list(symbol));
MuExp tcc("map", [muCon(Symbol from), muCon(Symbol to)]) = muCon(\map(from, to));
MuExp tcc("bag", [muCon(Symbol symbol)]) = muCon(\bag(symbol));
MuExp tcc("adt", [muCon(str name), muCon(list[Symbol] parameters)]) = muCon(\adt(name, parameters));
MuExp tcc("cons", [muCon(Symbol \adt), muCon(str name), muCon(list[Symbol] parameters)]) = muCon(\cons(\adt, name, parameters));
MuExp tcc("alias", [muCon(str name), muCon(list[Symbol] parameters), muCon(Symbol aliased)]) = muCon(\alias(name, parameters, aliased));
MuExp tcc("func", [muCon(Symbol ret), muCon(list[Symbol] parameters)]) = muCon(\func(ret, parameters));
MuExp tcc("var-func", [muCon(Symbol ret), muCon(list[Symbol] parameters), Symbol varArg]) = muCon(\func(ret, parameters, varArg));
MuExp tcc("reified", [muCon(Symbol symbol)]) = muCon(\reified(symbol));

MuExp tcc("parameter", [muCon(str name), muCon(Symbol bound)]) = muCon(\parameter(name, bound));

MuExp tcc("cons", [muCon(Symbol \adt), muCon(str name), muCon(list[Symbol] kwTypes), muCon(map[str, value(map[str,value])] kwDefaults), muCon(set[Attr] attributes)]) = 
			muCon(\cons(\adt, name, kwTypes, kwDefaults, attributes));
MuExp tcc("func", [muCon(Symbol def), muCon(list[Symbol] symbols), muCon(list[Symbol] kwTypes), muCon(map[str, value(map[str,value])] kwDefaults), muCon(set[Attr] attributes)]) = 
			muCon(\cons(def, symbols, kwTypes, kwDefaults, attributes));
 
MuExp tcc("choice", [muCon(Symbol def), muCon(list[Symbol] alternatives)]) = 
			muCon(\choice(def, alternatives));
			
MuExp tcc("subtype", [muCon(Symbol lhs), muCon(Symbol rhs)]) = muCon(subtype(lhs, rhs));
MuExp tcc("lub", [muCon(Symbol lhs), muCon(Symbol rhs)]) = muCon(lub(lhs, rhs));
MuExp tcc("glb", [muCon(Symbol lhs), muCon(Symbol rhs)]) = muCon(glb(lhs, rhs));
 
// Tree

MuExp tcc("appl", [muCon(Production prod), muCon(list[Tree] args)]) = muCon(appl(prod, args));
MuExp tcc("cycle", [muCon(Symbol symbol), muCon(int cycleLength)]) = muCon(cycle(symbol, cycleLength));
MuExp tcc("char", [muCon(int character)]) = muCon(char(character));

// Production

MuExp tcc("prod", [muCon(Symbol def), muCon(list[Symbol] symbols), muCon(set[Attr] attributes)]) = muCon(prod(def,symbols,attributes));
MuExp tcc("regular", [muCon(Symbol def)]) = muCon(regular(def));
MuExp tcc("error", [muCon(Production prod), muCon(int dot)]) = muCon(error(prod, dot));
MuExp tcc("skipped", []) = muCon(skipped());

MuExp tcc("priority", [muCon(Symbol def), muCon(list[Production] choices)]) = muCon(\priority(def, choices));
MuExp tcc("associativity", [muCon(Symbol def), muCon(Associativity \assoc), muCon(set[Production] alternatives)]) = muCon(\associativity(def, \assoc, alternatives));
MuExp tcc("others", [muCon(Symbol def)]) = muCon(others(def));
MuExp tcc("reference", [muCon(Symbol def), muCon(str cons)]) = muCon(reference(def, cons));

// Attr
MuExp tcc("tag", [muCon(value tagVal)]) = muCon(\tag(tagVal));

MuExp tcc("assoc", [muCon(Associativity a)]) = muCon(\assoc(a));
MuExp tcc("bracket", []) = muCon(\bracket());

// Associativity

MuExp tcc("left", []) = muCon(\left());
MuExp tcc("right", []) = muCon(\right());
MuExp tcc("assoc", []) = muCon(\assoc());
MuExp tcc("non-assoc", []) = muCon(\non-assoc());

// CharRange

MuExp tcc("range", [muCon(int begin), muCon(int end)]) = muCon(range(begin, end));

// Symbol
MuExp tcc("start", [muCon(Symbol symbol)]) = muCon(\start(symbol));
MuExp tcc("sort", [muCon(str name)]) = muCon(sort(name));
MuExp tcc("lex", [muCon(str name)]) = muCon(lex(name));
MuExp tcc("layouts", [muCon(str name)]) = muCon(layouts(name));
MuExp tcc("keywords", [muCon(str name)]) = muCon(keywords(name));
MuExp tcc("parameterized-sort", [muCon(str name), muCon(list[Symbol] parameters)]) = muCon(\parameterized-sort(name, parameters));
MuExp tcc("parameterized-lex", [muCon(str name), muCon(list[Symbol] parameters)]) = muCon(\parameterized-lex(name, parameters));

MuExp tcc("lit", [muCon(str s)]) = muCon(lit(s));
MuExp tcc("cilit", [muCon(str s)]) = muCon(cilit(s));
MuExp tcc("char-class", [muCon(list[CharRange] ranges)]) = muCon(\char-class(ranges));

MuExp tcc("empty", []) = muCon(empty());
MuExp tcc("opt", [muCon(Symbol symbol)]) = muCon(opt(symbol));
MuExp tcc("iter", [muCon(Symbol symbol)]) = muCon(iter(symbol));
MuExp tcc("iter-star", [muCon(Symbol symbol)]) = muCon(\iter-star(symbol));
MuExp tcc("iter-seps", [muCon(Symbol symbol), muCon(list[Symbol] separators)]) = muCon(\iter-seps(symbol, separators));
MuExp tcc("iter-star-seps", [muCon(Symbol symbol), muCon(list[Symbol] separators)]) = muCon(\iter-star-seps(symbol, separators));
MuExp tcc("alt", [muCon(set[Symbol] alternatives)]) = muCon(alt(alternatives));
MuExp tcc("seq", [muCon(list[Symbol] symbols)]) = muCon(seq(symbols));

MuExp tcc("conditional", [muCon(Symbol symbol), muCon(set[Condition] conditions)]) = muCon(conditional(symbol, conditions));

// Condition

MuExp tcc("follow", [muCon(Symbol symbol)]) = muCon(follow(symbol));
MuExp tcc("not-follow", [muCon(Symbol symbol)]) = muCon(\not-follow(symbol));
MuExp tcc("precede", [muCon(Symbol symbol)]) = muCon(precede(symbol));
MuExp tcc("not-precede", [muCon(Symbol symbol)]) = muCon(\not-precede(symbol));
MuExp tcc("delete", [muCon(Symbol symbol)]) = muCon(delete(symbol));
MuExp tcc("at-column", [muCon(int column)]) = muCon(\at-column(column));
MuExp tcc("begin-of-line", []) = muCon(\begin-of-line());
MuExp tcc("end-of-line", []) = muCon(\end-of-line());
MuExp tcc("except", [muCon(str label)]) = muCon(\except(label));

MuExp tcc("label", [muCon(str name), muCon(Symbol symbol)]) = muCon(label(name, symbol));
MuExp tcc("label", [muCon(str name), muCon(Symbol symbol)]) = muCon(label(name, symbol));

default MuExp tcc(_, _) { throw "NotConstant"; }