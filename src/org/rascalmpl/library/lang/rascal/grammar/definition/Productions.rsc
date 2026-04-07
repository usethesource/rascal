@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module lang::rascal::grammar::definition::Productions

import lang::rascal::\syntax::Rascal;
import lang::rascal::grammar::definition::Symbols;
import lang::rascal::grammar::definition::Attributes;
import lang::rascal::grammar::definition::Names;
extend Grammar;
extend ParseTree;   
import IO;  
import util::Maybe;


// conversion functions

public Grammar syntax2grammar(set[SyntaxDefinition] defs) {
  set[Production] prods = {prod(Symbol::empty(),[],{}), prod(layouts("$default$"),[],{})};
  set[Symbol] starts = {};
  
  for (sd <- defs) {
    <ps,st> = rule2prod(sd);
    prods += ps;
    if (st is just)
      starts += st.val;
  }
  
  return grammar(starts, prods);
}

public tuple[set[Production] prods, Maybe[Symbol] \start] rule2prod(SyntaxDefinition sd) {  
    switch (sd) {
      case \layout(_, nonterminal(Nonterminal n), Prod p) : 
        return <{prod2prod(\layouts("<n>"), p)},nothing()>;
      case \language(present() /*start*/, nonterminal(Nonterminal n), Prod p) : 
        return < {prod(\start(sort("<n>")),[label("top", sort("<n>"))],{})
                ,prod2prod(sort("<n>"), p)}
               ,just(\start(sort("<n>")))>;
      case \language(absent(), parametrized(Nonterminal l, {Sym ","}+ syms), Prod p) : 
        return <{prod2prod(\parameterized-sort("<l>",separgs2symbols(syms)), p)}, nothing()>;
      case \language(absent(), nonterminal(Nonterminal n), Prod p) : 
        return <{prod2prod(\sort("<n>"), p)},nothing()>;
      case \lexical(parametrized(Nonterminal l, {Sym ","}+ syms), Prod p) : 
        return <{prod2prod(\parameterized-lex("<l>",separgs2symbols(syms)), p)}, nothing()>;
      case \lexical(nonterminal(Nonterminal n), Prod p) : 
        return <{prod2prod(\lex("<n>"), p)}, nothing()>;
      case \keyword(nonterminal(Nonterminal n), Prod p) : 
        return <{prod2prod(keywords("<n>"), p)}, nothing()>;
      default: { iprintln(sd); throw "unsupported kind of syntax definition? <sd> at <sd.src>"; }
    }
} 
   
private Production prod2prod(Symbol nt, Prod p) {
  switch(p) {
    case labeled(ProdModifier* ms, Name n, Sym* args) : {
          if ([Sym x] := args.args, x is empty) {
            return associativity(nt, \mods2assoc(ms), prod(label(unescape("<n>"),nt), [], mods2attrs(ms)));
          }
          return associativity(nt, \mods2assoc(ms), prod(label(unescape("<n>"),nt), args2symbols(args), mods2attrs(ms)));
      }
    case unlabeled(ProdModifier* ms, Sym* args) : {
          if ([Sym x] := args.args, x is empty) {
            return associativity(nt, mods2assoc(ms), prod(nt, [], mods2attrs(ms)));
          }
          return associativity(nt, mods2assoc(ms), prod(nt,args2symbols(args),mods2attrs(ms)));
      }     
    case \all(Prod l, Prod r) :
      return choice(nt,{prod2prod(nt, l), prod2prod(nt, r)});
    case \first(Prod l, Prod r) : 
      return priority(nt,[prod2prod(nt, l), prod2prod(nt, r)]);
    case associativityGroup(\left(), Prod q) :
      return associativity(nt, Associativity::\left(), {prod2prod(nt, q)});
    case associativityGroup(\right(), Prod q) :
      return associativity(nt, Associativity::\right(), {prod2prod(nt, q)});
    case associativityGroup(\nonAssociative(), Prod q) :      
      return associativity(nt, \non-assoc(), {prod2prod(nt, q)});
    case associativityGroup(\associative(), Prod q) :      
      return associativity(nt, Associativity::\left(), {prod2prod(nt, q)});
    case reference(Name n): return \reference(nt, unescape("<n>"));
    default: throw "prod2prod, missed a case <p>"; 
  } 
}



private Production associativity(Symbol nt, nothing(), Production p) = p;
private default Production associativity(Symbol nt, just(Associativity a), Production p) = associativity(nt, a, {p});
