@doc{
  This module implements the semantic of > in Rascal's syntax definition formalism.
  The factorize function translates the > into a hierarchy of related non-terminals
  which encode the relative priority of productions.
}
module rascal::parser::Priority

import rascal::parser::Grammar;
import rascal::parser::Normalization;
import ParseTree;
import List;
import IO;

rule bottom level(Symbol a, int x) => a when x <= 0;
rule label  level(label(str l, Symbol s), int x) => label(l, level(s, x));

public Grammar factorize(Grammar g) {
  g.productions = factorize(g.productions);
  return g;
}

set[Production] factorize(set[Production] productions) {
  return { factorize(p) | p <- productions };
}

@doc{
  Introduce new non-terminals to encode priority and associativity relations.
  
  The semantics of this function depends highly on the normalization rules in rascal::parser::Normalization
}
public set[Production] factorize(Production p) {
   if (choice(Symbol s, {set[Production] unordered, first(s, list[Production] prods)}) := p) {
       int len = size(prods);
   
      // first we give each level a number
      levels = for ([list[Production] prefix, Production elem, list[Production] postfix] := prods)
                 append setToLevel(elem, s, size(postfix)); 
          
           
      // then we define each level by itself and copying all the productions from lower levels
      return { choice(level(s, l), {elem, removeOrderings(unordered), toSet(redefine(prefix, level(s, l)))}) 
             | [list[Production] prefix, Production elem, list[Production] postfix] := levels, int l := size(postfix)}; 
   }
   else if (first(Symbol s, list[Production] prods) := p) { 
      int len = size(prods);
   
      // first we give each level a number
      levels = for ([list[Production] prefix, Production elem, list[Production] postfix] := prods)
                 append setToLevel(elem, s, size(postfix)); 
          
           
      // then we define each level by itself and copying all the productions from lower levels
      return { choice(level(s, l), {elem, toSet(redefine(prefix, level(s, l)))}) 
             | [list[Production] prefix, Production elem, list[Production] postfix] := levels, int l := size(postfix)}; 
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
       
@doc{
Redefines some list of productions to produce a new symbol; it deals with all production combinators
}  
public list[Production] redefine(list[Production] prods, Symbol s) {
  return visit (prods) {
    case prod(list[Symbol] lhs, _, Attributes a) => prod(lhs, s, a)
    case choice(_, set[Production] alts) => choice(s, alts)
    case \assoc(_, Associativity a, Production p) => \assoc(s, a, p)
    case \diff(_, Production p, set[Production] alts) => \diff(s, p, alts)
    case \restrict(_, Production language, list[CharClass] restrictions) => restrict(s, language, restrictions)
     // TODO: add cases for choice, assoc, diff and follow
  }
}

@doc{
Recognizes left-most and right-most recursive productions.
It replaces each recursive non-terminal, if left-most or right-most, with a wrapped (level) non-terminal
Associativity rules are applied by increasing the level number on selected non-terminals.
}
public Production setToLevel(Production p, Symbol s, int l) {
  return visit (p) {
    case prod([s1, list[Symbol] middle, s2],s,Attributes a:attrs([list[Attr] a1, \assoc(\left()),list[Attr] a2])) => 
         prod([level(s1,l), middle, level(s2,l+1)],level(s,l),a)
      when checkSymbol(s1, s), checkSymbol(s2,s)
    case prod([s1, list[Symbol] middle, s],s,Attributes a:attrs([list[Attr] a1, \assoc(\right()),list[Attr] a2])) => 
         prod([level(s1,l+1), middle, level(s2,l)],level(s,l),a)
      when checkSymbol(s1, s), checkSymbol(s2,s)   
    case prod([s1, list[Symbol] middle, s],s2,Attributes a:attrs([list[Attr] a1, \assoc(\assoc()),list[Attr] a2])) => 
         prod([level(s1, l), middle, level(s2, l+1)],level(s,l),a)
      when checkSymbol(s1, s), checkSymbol(s2,s)      
    case prod([s1, list[Symbol] middle, s],s2,Attributes a:attrs([list[Attr] a1, \assoc(\non-assoc()),list[Attr] a2])) => 
         prod([level(s, l+1), middle, level(s, l+1)],level(s,l),a)
      when checkSymbol(s1, s), checkSymbol(s2,s)    
    case prod([s1, list[Symbol] middle, s2],s,Attributes a) => 
         prod([level(s, l), middle, level(s, l)],level(s,l),a)
      when checkSymbol(s1, s), checkSymbol(s2,s)   
    case prod([s1, list[Symbol] tail],s,Attributes a)      => 
         prod([level(s, l), tail], level(s, l), a)
      when checkSymbol(s1, s)    
    case prod([list[Symbol] front, s1], s, Attributes a)   => 
         prod([front, level(s, l)], level(s, l), a) 
      when checkSymbol(s1, s)    
    case prod(list[Symbol] lhs, s, Attributes a)          => 
         prod(lhs, level(s, l), a)
    case choice(s, set[Production] alts) => choice(level(s, l), alts)
    case \assoc(s, Associativity a, Production p) => \assoc(level(s, l), a, p)
    case \diff(s, Production p, set[Production] alts) => \diff(level(s, l), p, alts)
    case \restrict(s, Production language, list[CharClass] restrictions) => restrict(level(s,l), language, restrictions)  
  }
}   

private bool checkSymbol(Symbol checked, Symbol referenced) {
  return referenced == checked || label(_, referenced) := checked;
}

private Attributes add(Attributes attrs, Attr a) {
  switch(attrs) {
    case \no-attrs() : return attrs([a]);
    case attrs(list[Attr] as) : return attrs([as, a]);
    default: throw "missed a case <attrs>";
  }
}