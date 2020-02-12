module lang::rascalcore::compile::Examples::Tst1

//int f(int n, int(int) g) = g(n);

list[&U] mapper(tuple[list[&T] lst, &U (&T) fun] t) = [ t.fun(elem) | elem <- t.lst ];

//import Type;
//
////import lang::rascal::checker::TTL::Library;
//extend lang::rascal::checker::TTL::TTLsyntax;
//
//alias SymbolPair = tuple[Symbol l, Symbol r];
//
//set[SymbolPair] rlub(Symbol::\real()) = { <\int(), \real()>, <\real(), \int()> };