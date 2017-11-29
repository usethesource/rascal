module lang::rascalcore::check::Test4


import lang::rascal::grammar::definition::Modules;
import lang::rascal::grammar::definition::Productions;
import Grammar;
import ParseTree;
import Set;
import IO;

public Grammar expandRegularSymbols(Grammar G) {
  for (Symbol def <- G.rules) {
    if (choice(def, {regular(def)}) := G.rules[def]) { 
      Production init = choice(def,{});
      
      for (p <- expand(def)) {
        G.rules[p.def] = choice(p.def, {p, G.rules[p.def]?\init});
      }
    }
  }
  return G;
}

public set[Production] expand(Symbol s);

public Grammar makeRegularStubs(Grammar g) {
  prods = {g.rules[nont] | Symbol nont <- g.rules};
  stubs = makeRegularStubs(prods);
  return compose(g, grammar({},stubs));
}

public set[Production] makeRegularStubs(set[Production] prods) {
  return {regular(reg) | /Production p:prod(_,_,_) <- prods, sym <- p.symbols, reg <- getRegular(sym) };
}

private set[Symbol] getRegular(Symbol s) = { t | /Symbol t := s, isRegular(t) }; 

public default bool isRegular(Symbol s) = false;
public bool isRegular(opt(Symbol _)) = true;
public bool isRegular(iter(Symbol _)) = true;
public bool isRegular(\iter-star(Symbol _)) = true;
public bool isRegular(\iter-seps(Symbol _, list[Symbol] _)) = true;
public bool isRegular(\iter-star-seps(Symbol _, list[Symbol] _)) = true;
public bool isRegular(alt(set[Symbol] _)) = true;
public bool isRegular(seq(list[Symbol] _)) = true;
public bool isRegular(empty()) = true;



////import lang::rascal::grammar::definition::Modules;
////import lang::rascal::grammar::definition::Productions;
//import Grammar;
//import ParseTree;
////import Set;
////import IO;
//
////data Grammar 
////  = \grammar(set[Symbol] starts, map[Symbol sort, Production def] rules)
////  ;
//  
////data Symbol;
////
////data Production 
////     = prod(Symbol def, list[Symbol] symbols, set[Attr] attributes) // <1>
////     | regular(Symbol def) // <2>
////     | error(Production prod, int dot) // <3>
////     | skipped() // <4>
////     ;
////data Production 
////     = \priority(Symbol def, list[Production] choices) // <5>
////     | \associativity(Symbol def, Associativity \assoc, set[Production] alternatives) // <6>
////     | \others(Symbol def) // <7>
////     | \reference(Symbol def, str cons) // <8>
////    ;
////data Production
////     = \cons(Symbol def, list[Symbol] symbols, list[Symbol] kwTypes, set[Attr] attributes)
////     | \func(Symbol def, list[Symbol] symbols, list[Symbol] kwTypes, set[Attr] attributes /*, str code = "", map[str,value] bindings = (), loc cpe = |unknown:///|*/)
////     | \choice(Symbol def, set[Production] alternatives)
////     | \composition(Production lhs, Production rhs)
////     ;
//     
////public Production choice(Symbol s, {*Production a, others(Symbol t)}) {
////  if (a == {})
////    return others(t);
////  else
////    return choice(s, a);
////}
//
//public Grammar expandRegularSymbols(Grammar G) {
//  for (Symbol def <- G.rules) {
//    if (choice(def, {regular(def)}) := G.rules[def]) { 
//      //Production init = choice(def,{});
//      
//      for (p <- expand(def)) {
//        ;//G.rules[p.def] = choice(p.def, {p, G.rules[p.def]?\init});
//      }
//    }
//  }
//  return G;
//}
//
//public set[Production] expand(Symbol s); 