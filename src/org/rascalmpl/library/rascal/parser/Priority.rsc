module rascal::parser::Priority

import rascal::parser::Grammar;
import rascal::parser::Definition;
import ParseTree;
import List;

@doc{A Symbol constructor that can introduce levels for a certain non-terminal,
which can be used to implement priorities and associativity}
data Symbol = level(Symbol symbol, int level);

rule bottom level(Symbol a, int x) => a when x <= 0;

public Grammar factorize(Grammar g) {
  g.productions = factorize(g.productions);
  return g;
}

set[Production] factorize(set[Production] productions) {
  return { factorize(p) | p <- productions };
}

public set[Production] factorize(Production p) {
   if (first(Symbol s, list[Production] prods) := p) { 
      int len = size(prods);
      levels = [ setToLevel(p, s, size(postfix)) 
               | [list[Production] prefix, Production elem, list[Production] postfix] := prods];
      return { choice(level(s, level), {elem, toSet(redefine(prefix, level(s, level)))}) 
               | int level := length(postfix), [list[Production] prefix, Production elem, list[Production] postfix] := levels}; 
   }
   else {
     return {p};
   }
}

test factorize(first(sort("E"), [
       prod([sort("Id")],sort("E"),\no-attrs()),
       prod([sort("E"), lit("*"), sort("E")], sort("E"),\no-attrs()),
       prod([sort("E"), lit("+"), sort("E")], sort("E"),\no-attrs())
     ])) ==
     { choice(sort("E"), {
         prod([sort("Id")],sort("E"),\no-attrs()),
         prod([level(sort("E"),1), lit("*"), level(sort("E"),1)], sort("E"),\no-attrs()),
         prod([level(sort("E"),2), lit("+"), level(sort("E"),2)], sort("E"),\no-attrs())
             }),
        choice(sort("E"), {
         prod([level(sort("E"),1), lit("*"), level(sort("E"),1)], level(sort("E"),1),\no-attrs()),
         prod([level(sort("E"),2), lit("+"), level(sort("E"),2)], level(sort("E"),1),\no-attrs())
             }),
        choice(sort("E"), {
         prod([level(sort("E"),2), lit("+"), level(sort("E"),2)], level(sort("E"),2),\no-attrs())
             })             
     };
         
list[Production] redefine(list[Production] prods, Symbol s) {
  return visit (prods) {
    case prod(list[Symbol] lhs, _, Attributes a) => prod(lhs, s, a)
    case choice(_, set[Production] alts) => choice(s, alts)
    case \assoc(_, Associativity a, Production p) => \assoc(s, a, p)
    case \diff(_, Production p, set[Production] alts) => \diff(s, p, alts)
    case \restrict(_, Production language, list[CharClass] restrictions) => restrict(s, language, restrictions)
     // TODO: add cases for choice, assoc, diff and follow
  }
}

Production setToLevel(Production p, Symbol s, int level) {
  // This visit recognizes left-most and right-most recursive productions.
  // It replaces each recursive non-terminal, if left-most or right-most, with a wrapped (level) non-terminal
  // Associativity rules are applied by increasing the level number on selected non-terminals.
  
  return visit (p) {
    case prod([s, list[Symbol] middle, s],s,Attributes a:attrs([list[Attr] a1, \assoc(\left()),list[Attr] a2])) => 
         prod([level(s, level), middle, level(s, level+1)],level(s,level),a)
    case prod([s, list[Symbol] middle, s],s,Attributes a:attrs([list[Attr] a1, \assoc(\right()),list[Attr] a2])) => 
         prod([level(s, level+1), middle, level(s, level)],level(s,level),a)
    case prod([s, list[Symbol] middle, s],s,Attributes a:attrs([list[Attr] a1, \assoc(\assoc()),list[Attr] a2])) => 
         prod([level(s, level), middle, level(s, level+1)],level(s,level),a)
    case prod([s, list[Symbol] middle, s],s,Attributes a:attrs([list[Attr] a1, \assoc(\non-assoc()),list[Attr] a2])) => 
         prod([level(s, level+1), middle, level(s, level+1)],level(s,level),a)
    case prod([s, list[Symbol] middle, s],s,Attributes a) => 
         prod([level(s, level), middle, level(s, level)],level(s,level),a)
    case prod([s, list[Symbol] tail],s,Attributes a)      => 
         prod([level(s, level), tail], level(s, level), a)
    case prod([list[Symbol] front, s], s, Attributes a)   => 
         prod([front, level(s, level)], level(s, level), a) 
    case prod(list[Symbol] lhs, s, Attributes a)          => 
         prod(lhs, level(s, level), a)
    case choice(s, set[Production] alts) => choice(level(s, level), alts)
    case \assoc(s, Associativity a, Production p) => \assoc(level(s, level), a, p)
    case \diff(s, Production p, set[Production] alts) => \diff(level(s, level), p, alts)
    case \restrict(s, Production language, list[CharClass] restrictions) => restrict(level(s,level), language, restrictions)  
  }
}   

private Attributes add(Attributes attrs, Attr a) {
  switch(attrs) {
    case \no-attrs() : return attrs([a]);
    case attrs(list[Attr] as) : return attrs([as, a]);
    default: throw "missed a case <attrs>";
  }
}