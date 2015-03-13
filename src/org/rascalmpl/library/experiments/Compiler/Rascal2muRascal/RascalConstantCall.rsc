module experiments::Compiler::Rascal2muRascal::RascalConstantCall

import experiments::Compiler::muRascal::AST;
import ParseTree;
import String;
import List;
import Set;
import Type;

// String
MuExp translateConstantCall("size", [muCon(str s)]) = muCon(size(s));

// List
MuExp translateConstantCall("size", [muCon(list[value] lst)]) = muCon(size(lst));
MuExp translateConstantCall("isEmpty", [muCon(list[value] lst)]) = muCon(isEmpty(lst));

// Set
MuExp translateConstantCall("size", [muCon(set[value] st)]) = muCon(size(st));
MuExp translateConstantCall("isEmpty", [muCon(set[value] st)]) = muCon(isEmpty(st));

// Type
MuExp translateConstantCall("int", []) = muCon(\int());
MuExp translateConstantCall("bool", []) = muCon(\bool());
MuExp translateConstantCall("real", []) = muCon(\real());
MuExp translateConstantCall("rat", []) = muCon(\rat());
MuExp translateConstantCall("str", []) = muCon(\str());
MuExp translateConstantCall("num", []) = muCon(\num());
MuExp translateConstantCall("node", []) = muCon(\node());
MuExp translateConstantCall("void", []) = muCon(\void());
MuExp translateConstantCall("value", []) = muCon(\value());
MuExp translateConstantCall("loc", []) = muCon(\loc());
MuExp translateConstantCall("datetime", []) = muCon(\datetime());

MuExp translateConstantCall("label", [muCon(str name), muCon(Symbol symbol)]) = muCon(\label(name, symbol));

MuExp translateConstantCall("set", [muCon(Symbol symbol)]) = muCon(\set(symbol));
MuExp translateConstantCall("rel", [muCon(list[Symbol] symbols)]) = muCon(\rel(symbols));
MuExp translateConstantCall("lrel", [muCon(list[Symbol] symbols)]) = muCon(\lrel(symbols));
MuExp translateConstantCall("tuple", [muCon(list[Symbol] symbols)]) = muCon(\tuple(symbols));
MuExp translateConstantCall("list", [muCon(Symbol symbol)]) = muCon(\list(symbol));
MuExp translateConstantCall("map", [muCon(Symbol from), muCon(Symbol to)]) = muCon(\map(from, to));
MuExp translateConstantCall("bag", [muCon(Symbol symbol)]) = muCon(\bag(symbol));
MuExp translateConstantCall("adt", [muCon(str name), muCon(list[Symbol] parameters)]) = muCon(\adt(name, parameters));
MuExp translateConstantCall("cons", [muCon(Symbol \adt), muCon(str name), muCon(list[Symbol] parameters)]) = muCon(\cons(\adt, name, parameters));
MuExp translateConstantCall("alias", [muCon(str name), muCon(list[Symbol] parameters), muCon(Symbol aliased)]) = muCon(\alias(name, parameters, aliased));
MuExp translateConstantCall("func", [muCon(Symbol ret), muCon(list[Symbol] parameters)]) = muCon(\func(ret, parameters));
MuExp translateConstantCall("var-func", [muCon(Symbol ret), muCon(list[Symbol] parameters), Symbol varArg]) = muCon(\func(ret, parameters, varArg));
MuExp translateConstantCall("reified", [muCon(Symbol symbol)]) = muCon(\reified(symbol));

MuExp translateConstantCall("parameter", [muCon(str name), muCon(Symbol bound)]) = muCon(\parameter(name, bound));

MuExp translateConstantCall("cons", [muCon(Symbol \adt), muCon(str name), muCon(list[Symbol] kwTypes), muCon(map[str, value(map[str,value])] kwDefaults), muCon(set[Attr] attributes)]) = 
			muCon(\cons(\adt, name, kwTypes, kwDefaults, attributes));
MuExp translateConstantCall("func", [muCon(Symbol def), muCon(list[Symbol] symbols), muCon(list[Symbol] kwTypes), muCon(map[str, value(map[str,value])] kwDefaults), muCon(set[Attr] attributes)]) = 
			muCon(\cons(def, symbols, kwTypes, kwDefaults, attributes));
 
MuExp translateConstantCall("choice", [muCon(Symbol def), muCon(list[Symbol] alternatives)]) = 
			muCon(\choice(def, alternatives));
			
MuExp translateConstantCall("subtype", [muCon(Symbol lhs), muCon(Symbol rhs)]) = muCon(subtype(lhs, rhs));
MuExp translateConstantCall("lub", [muCon(Symbol lhs), muCon(Symbol rhs)]) = muCon(lub(lhs, rhs));
MuExp translateConstantCall("glb", [muCon(Symbol lhs), muCon(Symbol rhs)]) = muCon(glb(lhs, rhs));
 
// Tree

MuExp translateConstantCall("appl", [muCon(Production prod), muCon(list[Tree] args)]) = muCon(appl(prod, args));
MuExp translateConstantCall("cycle", [muCon(Symbol symbol), muCon(int cycleLength)]) = muCon(cycle(symbol, cycleLength));
MuExp translateConstantCall("char", [muCon(int character)]) = muCon(char(character));

// Production

MuExp translateConstantCall("prod", [muCon(Symbol def), muCon(list[Symbol] symbols), muCon(set[Attr] attributes)]) = muCon(prod(def,symbols,attributes));
MuExp translateConstantCall("regular", [muCon(Symbol def)]) = muCon(regular(def));
MuExp translateConstantCall("error", [muCon(Production prod), muCon(int dot)]) = muCon(error(prod, dot));
MuExp translateConstantCall("skipped", []) = muCon(skipped());

MuExp translateConstantCall("priority", [muCon(Symbol def), muCon(list[Production] choices)]) = muCon(\priority(def, choices));
MuExp translateConstantCall("associativity", [muCon(Symbol def), muCon(Associativity \assoc), muCon(set[Production] alternatives)]) = muCon(\associativity(def, \assoc, alternatives));
MuExp translateConstantCall("others", [muCon(Symbol def)]) = muCon(others(def));
MuExp translateConstantCall("reference", [muCon(Symbol def), muCon(str cons)]) = muCon(reference(def, cons));

// Attr
MuExp translateConstantCall("tag", [muCon(value tagVal)]) = muCon(\tag(tagVal));

MuExp translateConstantCall("assoc", [muCon(Associativity a)]) = muCon(\assoc(a));
MuExp translateConstantCall("bracket", []) = muCon(\bracket());

// Associativity

MuExp translateConstantCall("left", []) = muCon(\left());
MuExp translateConstantCall("right", []) = muCon(\right());
MuExp translateConstantCall("assoc", []) = muCon(\assoc());
MuExp translateConstantCall("non-assoc", []) = muCon(\non-assoc());

// CharRange

MuExp translateConstantCall("range", [muCon(int begin), muCon(int end)]) = muCon(range(begin, end));

// Symbol
MuExp translateConstantCall("start", [muCon(Symbol symbol)]) = muCon(\start(symbol));
MuExp translateConstantCall("sort", [muCon(str name)]) = muCon(sort(name));
MuExp translateConstantCall("lex", [muCon(str name)]) = muCon(lex(name));
MuExp translateConstantCall("layouts", [muCon(str name)]) = muCon(layouts(name));
MuExp translateConstantCall("keywords", [muCon(str name)]) = muCon(keywords(name));
MuExp translateConstantCall("parameterized-sort", [muCon(str name), muCon(list[Symbol] parameters)]) = muCon(\parameterized-sort(name, parameters));
MuExp translateConstantCall("parameterized-lex", [muCon(str name), muCon(list[Symbol] parameters)]) = muCon(\parameterized-lex(name, parameters));

MuExp translateConstantCall("lit", [muCon(str s)]) = muCon(lit(s));
MuExp translateConstantCall("cilit", [muCon(str s)]) = muCon(cilit(s));
MuExp translateConstantCall("char-class", [muCon(list[CharRange] ranges)]) = muCon(\char-class(ranges));

MuExp translateConstantCall("empty", []) = muCon(empty());
MuExp translateConstantCall("opt", [muCon(Symbol symbol)]) = muCon(opt(symbol));
MuExp translateConstantCall("iter", [muCon(Symbol symbol)]) = muCon(iter(symbol));
MuExp translateConstantCall("iter-star", [muCon(Symbol symbol)]) = muCon(\iter-star(symbol));
MuExp translateConstantCall("iter-seps", [muCon(Symbol symbol), muCon(list[Symbol] separators)]) = muCon(\iter-seps(symbol, separators));
MuExp translateConstantCall("iter-star-seps", [muCon(Symbol symbol), muCon(list[Symbol] separators)]) = muCon(\iter-star-seps(symbol, separators));
MuExp translateConstantCall("alt", [muCon(set[Symbol] alternatives)]) = muCon(alt(alternatives));
MuExp translateConstantCall("seq", [muCon(list[Symbol] symbols)]) = muCon(seq(symbols));

MuExp translateConstantCall("conditional", [muCon(Symbol symbol), muCon(set[Condition] conditions)]) = muCon(conditional(symbol, conditions));

// Condition

MuExp translateConstantCall("follow", [muCon(Symbol symbol)]) = muCon(follow(symbol));
MuExp translateConstantCall("not-follow", [muCon(Symbol symbol)]) = muCon(\not-follow(symbol));
MuExp translateConstantCall("precede", [muCon(Symbol symbol)]) = muCon(precede(symbol));
MuExp translateConstantCall("not-precede", [muCon(Symbol symbol)]) = muCon(\not-precede(symbol));
MuExp translateConstantCall("delete", [muCon(Symbol symbol)]) = muCon(delete(symbol));
MuExp translateConstantCall("at-column", [muCon(int column)]) = muCon(\at-column(column));
MuExp translateConstantCall("begin-of-line", []) = muCon(\begin-of-line());
MuExp translateConstantCall("end-of-line", []) = muCon(\end-of-line());
MuExp translateConstantCall("except", [muCon(str label)]) = muCon(\except(label));

MuExp translateConstantCall("label", [muCon(str name), muCon(Symbol symbol)]) = muCon(label(name, symbol));
MuExp translateConstantCall("label", [muCon(str name), muCon(Symbol symbol)]) = muCon(label(name, symbol));

default MuExp translateConstantCall(_, _) { throw "NotConstant"; }