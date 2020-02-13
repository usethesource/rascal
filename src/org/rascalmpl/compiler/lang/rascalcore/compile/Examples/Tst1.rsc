module lang::rascalcore::compile::Examples::Tst1

layout Whitespace = [\ \t\n]*;

start syntax D = "d";
start syntax DS = D+;

test bool DvarsTypedInsert4() = (DS)`d <D+ Xs>` := (DS)`d d` && (DS)`d <D+ Xs>` == (DS)`d d`;

//int f(int n, int(int) g) = g(n);

//list[&U] mapper(tuple[list[&T] lst, &U (&T) fun] t) = [ t.fun(elem) | elem <- t.lst ];

//data TYPESET = SET(str name) | SUBTYPES(TYPESET tset) | INTERSECT(set[TYPESET] tsets);
//
//bool testSimp(TYPESET ats, TYPESET (TYPESET  ts) aSimp) = true;

//import Type;
//
////import lang::rascal::checker::TTL::Library;
//extend lang::rascal::checker::TTL::TTLsyntax;
//
//alias SymbolPair = tuple[Symbol l, Symbol r];
//
//set[SymbolPair] rlub(Symbol::\real()) = { <\int(), \real()>, <\real(), \int()> };