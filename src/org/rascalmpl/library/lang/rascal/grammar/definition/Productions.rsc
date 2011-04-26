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
module lang::rascal::grammar::Productions
     
import lang::rascal::syntax::RascalRascal;
import lang::rascal::grammar::Characters;
import lang::rascal::grammar::Symbols;

import Grammar;
import List; 
import String;    
import ParseTree;
import IO;  
import Integer;

// normalization rules
public Production choice(Symbol s, {set[Production] a, choice(Symbol t, set[Production] b)})
  = choice(s, a+b);
  
public Production priority(Symbol s, [list[Production] a, priority(Symbol t, list[Production] b),list[Production] c])
  = priority(s,a+b+c);
   
public Production associativity(Symbol s, Associativity as, {set[Production] a, choice(Symbol t, set[Production] b)}) 
  = associativity(s, as, a+b); 
  
public Production choice(Symbol s, {others(Symbol t)}) 
  = others(t);
             
public Production associativity(Symbol rhs, Associativity a, {associativity(Symbol rhs2, Associativity b, set[Production] alts), set[Production] rest}) 
  = associativity(rhs, a, rest + alts);

public Production associativity(Symbol s, Associativity as, {set[Production] a, priority(Symbol t, list[Production] b)}) 
  = associativity(s, as, a + { e | e <- b}); 
 
public Production choice(Symbol s, {set[Production] a, others(Symbol t)}) {
  if (t == s) 
    return choice(s, a);
  else 
    fail;
}

public Production choice(Symbol s, {set[Production] a, priority(Symbol t, [list[Production] b, others(Symbol u), list[Production] c])}) 
  = priority(s, b + [choice(s, a)] + c);
  
public Production  attrs([]) 
  = \no-attrs();

// conversion functions

public Grammar syntax2grammar(set[SyntaxDefinition] defs) {
  set[Production] prods = {};
  set[Symbol] starts = {};
  
  for (sd <- defs) {
    switch (sd) {
      case (SyntaxDefinition) `layout <Nonterminal n> = <Prod p>;` : {
        prods += prod2prod(\layouts("<n>"), p);
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
    case (Prod) `<ProdModifier* ms> <Name n> : <Sym* args>` :
      return prod(args2symbols(args), nt, mods2attrs(n, ms));
    case (Prod) `<ProdModifier* ms> <Sym* args>` :
      return prod(args2symbols(args), nt, mods2attrs(ms));
    case (Prod) `<Prod l> | <Prod r>` :
      return choice(nt,{prod2prod(nt, l), prod2prod(nt, r)});
    case (Prod) `<Prod l> > <Prod r>` : 
      return priority(nt,[prod2prod(nt, l), prod2prod(nt, r)]);
    case (Prod) `left (<Prod q>)` :
      return associativity(nt, \left(), {attribute(prod2prod(nt, q), \assoc(\left()))});
    case (Prod) `right (<Prod q>)` :
      return associativity(nt, \right(), {attribute(prod2prod(nt, q), \assoc(\right()))});
    case (Prod) `non-assoc (<Prod q>)` :
      return associativity(nt, \non-assoc(), {attribute(prod2prod(nt, q), \assoc(\non-assoc()))});
    case (Prod) `assoc(<Prod q>)` :
      return associativity(nt, \left(), {attribute(prod2prod(nt, q),\assoc(\assoc()))});
    case (Prod) `...`: return \others(nt);
    case (Prod) `: <Name n>`: return \reference(nt, "<n>");
    default: throw "missed a case <p>";
  } 
}

@doc{adds an attribute to all productions it can find}
private Production attribute(Production p, Attr a) {
  return visit (p) {
    case prod(lhs,rhs,\no-attrs()) => prod(lhs, rhs, attrs([a]))
    case prod(lhs,rhs,attrs(list[Attributes] l)) => prod(lhs, rhs, attrs([l, a]))
  }
}

private Attributes mods2attrs(Name name, ProdModifier* mods) {
  return attrs([term("cons"("<name>"))] + [ mod2attr(m) | m <- mods]);
}

private Attributes mods2attrs(ProdModifier* mods) {
  return attrs([mod2attr(m) | ProdModifier m <- mods]);
}
 
private Attr mod2attr(ProdModifier m) {
  switch (m) {
    case (ProdModifier) `left`: return \assoc(\left());
    case (ProdModifier) `right`: return \assoc(\right());
    case (ProdModifier) `non-assoc`: return \assoc(\non-assoc());
    case (ProdModifier) `assoc`: return \assoc(\assoc());
    case (ProdModifier) `bracket`: return \bracket();
    case (ProdModifier) `@ <Name n> = <StringConstant s>` : return \term("<n>"(unescape(s)));
    case (ProdModifier) `@ <Name n> = <Literal l>` : return \term("<n>"("<l>"));
    case (ProdModifier) `@ <Name n>` : return \term("<n>"());
    case (ProdModifier) `@ <Name n> <TagString s>` : return \term("<n>"("<s>"));
    default: throw "missed a case <m>";
  }
}