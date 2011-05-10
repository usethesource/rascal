@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@bootstrapParser
module lang::rascal::grammar::definition::Productions
     
import lang::rascal::syntax::RascalRascal;
import lang::rascal::grammar::definition::Characters;
import lang::rascal::grammar::definition::Symbols;
import lang::rascal::grammar::definition::Attributes;
import lang::rascal::grammar::definition::Lexical;

import Grammar;
import List; 
import String;    
import ParseTree;
import IO;  
import Integer;


// conversion functions

public Grammar syntax2grammar(set[SyntaxDefinition] defs) {
  set[Production] prods = {};
  set[Symbol] starts = {};
  
  for (sd <- defs) {
    switch (sd) {
      case (SyntaxDefinition) `layout <Nonterminal n> = <Prod p>;` : {
        prods += attribute(prod2prod(\layouts("<n>"), p), visibility(\public()));
      }
      case (SyntaxDefinition) `start syntax <Nonterminal n> = <Prod p>;` : {
        Symbol top = sort("<n>");
        prods  += prod([top], start(top), \no-attrs()); 
        prods  += prod2prod(top, p);
      }
      case (SyntaxDefinition) `syntax <Nonterminal n> = <Prod p>;` : {
        prods += prod2prod(sort("<n>"), p);
      }
      case (SyntaxDefinition) `lexical <Nonterminal n> = <Prod p>;` : {
        prods += lexical(prod2prod(sort("<n>"), p));
      }
      case (SyntaxDefinition) `keyword <Nonterminal n> = <Prod p>;` : {
        prods += prod2prod(keywords("<n>"), p);
      }
    }
  }

  return grammar(starts, prods);
} 
   
private Production prod2prod(Symbol nt, Prod p) {
  switch(p) {
    case (Prod) `<ProdModifier* ms> <Name n> : ()` :
      return attribute(prod([], nt, mods2attrs(ms)), term("cons"("<n>")));
    case (Prod) `<ProdModifier* ms> ()` :
      return prod([], nt, mods2attrs(ms));
    case (Prod) `<ProdModifier* ms> <Name n> : <Sym* args>` :
      return attribute(prod(args2symbols(args), nt, mods2attrs(ms)), term("cons"("<n>")));
    case (Prod) `<ProdModifier* ms> <Sym* args>` :
      return prod(args2symbols(args), nt, mods2attrs(ms));
    case (Prod) `<Prod l> | <Prod r>` :
      return choice(nt,{prod2prod(nt, l), prod2prod(nt, r)});
    case (Prod) `<Prod l> > <Prod r>` : 
      return priority(nt,[prod2prod(nt, l), prod2prod(nt, r)]);
    case (Prod) `left (<Prod q>)` :
      return associativity(nt, \left(), {prod2prod(nt, q)});
    case (Prod) `right (<Prod q>)` :
      return associativity(nt, \right(), {prod2prod(nt, q)});
    case (Prod) `non-assoc (<Prod q>)` :
      return associativity(nt, \non-assoc(), {prod2prod(nt, q)});
    case (Prod) `assoc(<Prod q>)` :
      return associativity(nt, \left(), {prod2prod(nt, q)});
    case (Prod) `...`: return \others(nt);
    case (Prod) `: <Name n>`: return \reference(nt, "<n>");
    default: throw "missed a case <p>";
  } 
}

// normalization rules
public Production choice(Symbol s, {set[Production] a, choice(Symbol t, set[Production] b)})
  = choice(s, a+b);
  
public Production priority(Symbol s, [list[Production] a, priority(Symbol t, list[Production] b),list[Production] c])
  = priority(s,a+b+c);
   
public Production associativity(Symbol s, Associativity as, {set[Production] a, choice(Symbol t, set[Production] b)}) 
  = associativity(s, as, a+b); 
  

     
@doc{Nested equal associativity is flattened}             
public Production associativity(Symbol rhs, Associativity a, {associativity(Symbol rhs2, Associativity b, set[Production] alts), set[Production] rest}) {
  if (a == b)  
    return associativity(rhs, a, rest + alts) ;
  else
    fail;
}

@doc{Priority under an associativity group defaults to choice}
public Production associativity(Symbol s, Associativity as, {set[Production] a, priority(Symbol t, list[Production] b)}) 
  = associativity(s, as, a + { e | e <- b}); 
  
@doc{In a group a single production inherits the group's associativity, unless contradicted.}
public Production associativity(Symbol s, Associativity as, {set[Production] a, Production p:prod(_,_,_)}) { 
   if (!/\assoc(_) := p.attributes) 
     return associativity(s, as, {a, attribute(p, \assoc(as))});
   else
     fail; 
}
 
public Production choice(Symbol s, {set[Production] a, others(Symbol t)}) {
  if (a == {})
    return others(t);
  else
    return choice(s, a);
}

public Production choice(Symbol s, {set[Production] a, priority(Symbol t, [list[Production] b, others(Symbol u), list[Production] c])}) 
  = priority(s, b + [choice(s, a)] + c);
  
public Production  attrs([]) 
  = \no-attrs();
