@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module lang::rascalcore::grammar::definition::Productions
     
import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::grammar::definition::Characters;
import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Attributes;
import lang::rascalcore::grammar::definition::Names;

import lang::rascalcore::grammar::definition::Grammar;
import List; 
import Set;
import String;    
//import ParseTree;
import lang::rascalcore::check::AType;
import IO;  
import util::Math;
import util::Maybe;


// conversion functions

//public Grammar syntax2grammar(set[SyntaxDefinition] defs) {
//  set[Production] prods = {prod(AType::empty(),[]), prod(layouts("$default$"),[])};
//  set[AType] starts = {};
//  
//  for (sd <- defs) {
//    <ps,st> = rule2prod(sd);
//    prods += ps;
//    if (st is just)
//      starts += st.val;
//  }
//  
//  return grammar(starts, prods);
//}

//public tuple[set[Production] prods, Maybe[AType] \start] rule2prod(SyntaxDefinition sd) {  
//    switch (sd) {
//      case \layout(_, nonterminal(Nonterminal n), Prod p) : 
//        return <{prod2prod(\layouts("<n>"), p)},nothing()>;
//      case \language(present() /*start*/, nonterminal(Nonterminal n), Prod p) : 
//        return < {prod(\start(sort("<n>")),[label("top", sort("<n>"))])
//                ,prod2prod(sort("<n>"), p)}
//               ,just(\start(sort("<n>")))>;
//      case \language(absent(), parametrized(Nonterminal l, {Sym ","}+ syms), Prod p) : 
//        return <{prod2prod(\parameterized-sort("<l>",separgs2ATypes(syms)), p)}, nothing()>;
//      case \language(absent(), nonterminal(Nonterminal n), Prod p) : 
//        return <{prod2prod(\sort("<n>"), p)},nothing()>;
//      case \lexical(parametrized(Nonterminal l, {Sym ","}+ syms), Prod p) : 
//        return <{prod2prod(\parameterized-lex("<l>",separgs2ATypes(syms)), p)}, nothing()>;
//      case \lexical(nonterminal(Nonterminal n), Prod p) : 
//        return <{prod2prod(\lex("<n>"), p)}, nothing()>;
//      case \keyword(nonterminal(Nonterminal n), Prod p) : 
//        return <{prod2prod(keywords("<n>"), p)}, nothing()>;
//      default: { iprintln(sd); throw "unsupported kind of syntax definition? <sd> at <sd@\loc>"; }
//    }
//} 
   
public AProduction prod2prod(AType nt, Prod p, set[SyntaxKind] syntaxKind) {
  src = p@\loc;
  switch(p) {
    case labeled(ProdModifier* ms, Name n, Sym* args) : 
      if ([Sym x] := args.args, x is empty) {
        m2a = mods2attrs(ms);
        return isEmpty(m2a) ? prod(nt[label="<n>"], [], src=src, syntaxKind=syntaxKind) 
                            : prod(nt[label="<n>"], [], attributes=m2a, src=src, syntaxKind=syntaxKind);
      }
      else {
        m2a = mods2attrs(ms);
        return isEmpty(m2a) ? prod(nt[label=unescape("<n>")],args2ATypes(args), src=src, syntaxKind=syntaxKind) 
                            : prod(nt[label=unescape("<n>")],args2ATypes(args),attributes=m2a, src=src, syntaxKind=syntaxKind);
      }
    case unlabeled(ProdModifier* ms, Sym* args) :
      if ([Sym x] := args.args, x is empty) {
        m2a = mods2attrs(ms);
        return isEmpty(m2a) ? prod(nt, [], src=src, syntaxKind=syntaxKind) 
                            : prod(nt, [], attributes=m2a, src=src, syntaxKind=syntaxKind);
      }
      else {
        m2a = mods2attrs(ms);
        return isEmpty(m2a) ? prod(nt, args2ATypes(args), src=src, syntaxKind=syntaxKind) 
                            : prod(nt, args2ATypes(args), attributes=m2a, src=src, syntaxKind=syntaxKind);
      }     
    case \all(Prod l, Prod r) :
      return choice(nt,{prod2prod(nt, l, syntaxKind), prod2prod(nt, r, syntaxKind)});
    case \first(Prod l, Prod r) : 
      return priority(nt,[prod2prod(nt, l, syntaxKind), prod2prod(nt, r, syntaxKind)]);
    case associativityGroup(\left(), Prod q) :
      return associativity(nt, Associativity::\left(), {prod2prod(nt, q, syntaxKind)});
    case associativityGroup(\right(), Prod q) :
      return associativity(nt, Associativity::\right(), {prod2prod(nt, q, syntaxKind)});
    case associativityGroup(\nonAssociative(), Prod q) :      
      return associativity(nt, \non-assoc(), {prod2prod(nt, q, syntaxKind)});
    case associativityGroup(\associative(), Prod q) :      
      return associativity(nt, Associativity::\left(), {prod2prod(nt, q, syntaxKind)});
    case others(): return \others(nt);
    case reference(Name n): return \reference(nt, "<n>");
    default: throw "prod2prod, missed a case <p>";
  } 
}

@doc{"..." in a choice is a no-op}   
public AProduction choice(AType s, {*AProduction a, others(AType t)}) {
  if (a == {})
    return others(t);
  else
    return choice(s, a);
}

@doc{This implements the semantics of "..." under a priority group}
public AProduction choice(AType s, {*AProduction a, priority(AType t, [*AProduction b, others(AType u), *AProduction c])}) 
  = priority(s, b + [choice(s, a)] + c);
