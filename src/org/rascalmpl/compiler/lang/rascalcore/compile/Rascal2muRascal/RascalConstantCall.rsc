module lang::rascalcore::compile::Rascal2muRascal::RascalConstantCall

import lang::rascalcore::compile::muRascal::AST;
import ParseTree;
import String;
import List;
import Set;
import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;

MuExp translateConstantCall(str name, list[MuExp] args) =
	tcc(name, args);

//// String
//private MuExp tcc("size", [muCon(str s)]) = muCon(size(s));
//
//// List
//private MuExp tcc("size", [muCon(list[value] lst)]) = muCon(size(lst));
//private MuExp tcc("isEmpty", [muCon(list[value] lst)]) = muCon(isEmpty(lst));
// 
//// Set 
//private MuExp tcc("size", [muCon(set[value] st)]) = muCon(size(st));
//private MuExp tcc("isEmpty", [muCon(set[value] st)]) = muCon(isEmpty(st));
//
//// Type
//private MuExp tcc("int", []) = muCon(aint());
//private MuExp tcc("bool", []) = muCon(abool());
//private MuExp tcc("real", []) = muCon(areal());
//private MuExp tcc("rat", []) = muCon(arat());
//private MuExp tcc("str", []) = muCon(astr());
//private MuExp tcc("num", []) = muCon(anum());
//private MuExp tcc("node", []) = muCon(anode([]));
//private MuExp tcc("void", []) = muCon(avoid());
//private MuExp tcc("value", []) = muCon(avalue());
//private MuExp tcc("loc", []) = muCon(aloc());
//private MuExp tcc("datetime", []) = muCon(adatetime());
//
////private MuExp tcc("label", [muCon(str name), muCon(AType symbol)]) = muCon(\label(name, symbol));
//
//private MuExp tcc("set", [muCon(AType symbol)]) = muCon(aset(symbol));
//private MuExp tcc("rel", [muCon(list[AType] symbols)]) = muCon(arel(atypeList(symbols)));
//private MuExp tcc("lrel", [muCon(list[AType] symbols)]) = muCon(alrel(atypeList(symbols)));
//private MuExp tcc("tuple", [muCon(list[AType] symbols)]) = muCon(atuple(atypeList(symbols)));
//private MuExp tcc("list", [muCon(AType symbol)]) = muCon(alist(symbol));
//private MuExp tcc("map", [muCon(AType from), muCon(AType to)]) = muCon(amap(from, to));
//private MuExp tcc("bag", [muCon(AType symbol)]) = muCon(abag(symbol));
//private MuExp tcc("adt", [muCon(str name), muCon(list[AType] parameters)]) = muCon(\aadt(name, parameters, dataSyntax()));
////private MuExp tcc("cons", [muCon(AType \adt), muCon(list[AType] fields), muCon(list[Keyword] kwFields) = muCon(acons(\adt, fields, kwFields));
//private MuExp tcc("alias", [muCon(str name), muCon(list[AType] parameters), muCon(AType aliased)]) = muCon(aalias(name, parameters, aliased));
//private MuExp tcc("func", [muCon(AType ret), muCon(list[AType] parameters)]) = muCon(afunc(ret, parameters, []));
////TODO: TC gives duplicate function error on next definition:
////MuExp tcc("var-func", [muCon(AType ret), muCon(list[AType] parameters), AType varArg]) = muCon(\var-func(ret, parameters, varArg));
//private MuExp tcc("reified", [muCon(AType symbol)]) = muCon(areified(symbol));
//
//private MuExp tcc("parameter", [muCon(str name), muCon(AType bound)]) = muCon(aparameter(name, bound));
//
//private MuExp tcc("cons", [muCon(AType \def), muCon(list[AType] symbols), muCon(list[Keyword] kwTypes)]) = 
//			muCon(acons(\def, symbols, kwTypes));
//private MuExp tcc("func", [muCon(AType def), muCon(list[AType] symbols), muCon(list[AType] kwTypes)]) = 
//			muCon(acons(def, symbols, kwTypes));
// 
//private MuExp tcc("choice", [muCon(AType def), muCon(set[AProduction] alternatives)]) = 
//			muCon(\choice(def, alternatives));
//			
//private MuExp tcc("subtype", [muCon(AType lhs), muCon(AType rhs)]) = muCon(asubtype(lhs, rhs));
//private MuExp tcc("lub", [muCon(AType lhs), muCon(AType rhs)]) = muCon(alub(lhs, rhs));
//private MuExp tcc("glb", [muCon(AType lhs), muCon(AType rhs)]) = muCon(aglb(lhs, rhs));
// 
//// Tree
//
//private MuExp tcc("appl", [muCon(Production prod), muCon(list[Tree] args)]) = muCon(appl(prod, args));
//private MuExp tcc("cycle", [muCon(AType symbol), muCon(int cycleLength)]) = muCon(cycle(symbol, cycleLength));
//private MuExp tcc("char", [muCon(int character)]) = muCon(char(character));
//
//// Production
//
//private MuExp tcc("prod", [muCon(AType def), muCon(list[AType] symbols), muCon(set[Attr] attributes)]) = muCon(prod(def,symbols,attributes=attributes));
//private MuExp tcc("regular", [muCon(AType def)]) = muCon(regular(def));
//private MuExp tcc("error", [muCon(Production prod), muCon(int dot)]) = muCon(error(prod, dot));
//private MuExp tcc("skipped", []) = muCon(skipped());
//
//private MuExp tcc("priority", [muCon(AType def), muCon(list[AProduction] choices)]) = muCon(\priority(def, choices));
//private MuExp tcc("associativity", [muCon(AType def), muCon(Associativity \assoc), muCon(set[AProduction] alternatives)]) = muCon(\associativity(def, \assoc, alternatives));
//private MuExp tcc("reference", [muCon(AType def), muCon(str cons)]) = muCon(reference(def, cons));
//
//// Attr
//private MuExp tcc("tag", [muCon(value tagVal)]) = muCon(\tag(tagVal));
//
//private MuExp tcc("bracket", []) = muCon(\bracket());
//
//// Associativity
//
//private MuExp tcc("left", []) = muCon(\left());
//private MuExp tcc("right", []) = muCon(\right());
//private MuExp tcc("assoc", []) = muCon(\assoc());
//private MuExp tcc("non-assoc", []) = muCon(\non-assoc());
//
//// CharRange
//
//private MuExp tcc("range", [muCon(int begin), muCon(int end)]) = muCon(range(begin, end));
//
//// AType
//private MuExp tcc("start", [muCon(AType symbol)]) = muCon(\start(symbol));
//private MuExp tcc("sort", [muCon(str name)]) = muCon(sort(name));
//private MuExp tcc("lex", [muCon(str name)]) = muCon(lex(name));
//private MuExp tcc("layouts", [muCon(str name)]) = muCon(layouts(name));
//private MuExp tcc("keywords", [muCon(str name)]) = muCon(keywords(name));
//private MuExp tcc("parameterized-sort", [muCon(str name), muCon(list[AType] parameters)]) = muCon(\parameterized-sort(name, parameters));
//private MuExp tcc("parameterized-lex", [muCon(str name), muCon(list[AType] parameters)]) = muCon(\parameterized-lex(name, parameters));
//
//private MuExp tcc("lit", [muCon(str s)]) = muCon(lit(s));
//private MuExp tcc("cilit", [muCon(str s)]) = muCon(cilit(s));
//private MuExp tcc("char-class", [muCon(list[CharRange] ranges)]) = muCon(\char-class(ranges));
//
//private MuExp tcc("empty", []) = muCon(empty());
//private MuExp tcc("opt", [muCon(AType symbol)]) = muCon(opt(symbol));
//private MuExp tcc("iter", [muCon(AType symbol)]) = muCon(iter(symbol));
//private MuExp tcc("iter-star", [muCon(AType symbol)]) = muCon(\iter-star(symbol));
//private MuExp tcc("iter-seps", [muCon(AType symbol), muCon(list[AType] separators)]) = muCon(\iter-seps(symbol, separators));
//private MuExp tcc("iter-star-seps", [muCon(AType symbol), muCon(list[AType] separators)]) = muCon(\iter-star-seps(symbol, separators));
//private MuExp tcc("alt", [muCon(set[AType] alternatives)]) = muCon(alt(alternatives));
//private MuExp tcc("seq", [muCon(list[AType] symbols)]) = muCon(seq(symbols));
//
//private MuExp tcc("conditional", [muCon(AType symbol), muCon(set[ACondition] conditions)]) = muCon(conditional(symbol, conditions));
//
//// Condition
//
//private MuExp tcc("follow", [muCon(AType symbol)]) = muCon(follow(symbol));
//private MuExp tcc("not-follow", [muCon(AType symbol)]) = muCon(\not-follow(symbol));
//private MuExp tcc("precede", [muCon(AType symbol)]) = muCon(precede(symbol));
//private MuExp tcc("not-precede", [muCon(AType symbol)]) = muCon(\not-precede(symbol));
//private MuExp tcc("delete", [muCon(AType symbol)]) = muCon(delete(symbol));
//private MuExp tcc("at-column", [muCon(int column)]) = muCon(\at-column(column));
//private MuExp tcc("begin-of-line", []) = muCon(\begin-of-line());
//private MuExp tcc("end-of-line", []) = muCon(\end-of-line());
//private MuExp tcc("except", [muCon(str label)]) = muCon(\except(label));
//
//private MuExp tcc("label", [muCon(str name), muCon(AType symbol)]) = muCon(label(name, symbol));
//private MuExp tcc("label", [muCon(str name), muCon(AType symbol)]) = muCon(label(name, symbol));

default MuExp tcc(str name, list[MuExp] args) { throw "NotConstant"; }