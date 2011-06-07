@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascal::grammar::definition::Regular

import lang::rascal::grammar::definition::Modules;
import lang::rascal::grammar::definition::Productions;
import Grammar;
import ParseTree;
import Set;
import IO;

public Grammar expandRegularSymbols(Grammar G) {
  for (Symbol rhs <- G.rules) {
    if (choice(rhs, {regular(rhs, _)}) := G.rules[rhs]) { 
      set[Production] init = {};
      
      for (p <- expand(rhs)) {
        G.rules[p.rhs]?init += {p};
      }
    }
  }
  return G;
}

public set[Production] expand(Symbol s) {
  switch (s) {
    case \opt(t) : 
      return {choice(s,{prod([],s,\no-attrs()),prod([t],s,\no-attrs())})};
    case \iter(t) : 
      return {choice(s,{prod([t],s,\no-attrs()),prod([t,s],s,\no-attrs())})};
    case \iter-star(t) : 
      return {choice(s,{prod([],s,\no-attrs()),prod([iter(t)],s,\no-attrs())})} + expand(iter(t));
    case \iter-seps(t,list[Symbol] seps) : 
      return {choice(s, {prod([t],s,\no-attrs()),prod([t,seps,s],s,\no-attrs())})};
    case \iter-star-seps(t, list[Symbol] seps) : 
      return {choice(s,{prod([],s,\no-attrs()),prod([\iter-seps(t,seps)],s,\no-attrs())})} 
             + expand(\iter-seps(t,seps));
    case \alt(set[Symbol] alts) :
      return {choice(s, {prod([a],s,\no-attrs()) | a <- alts})};
    case \seq(list[Symbol] elems) :
      return {prod(elems, s, \no-attrs())};
    case \empty() :
      return {prod([],s,\no-attrs())};
   }   

   throw "missed a case <s>";                   
}

public Grammar makeRegularStubs(Grammar g) {
  prods = {g.rules[nont] | Symbol nont <- g.rules};
  stubs = makeRegularStubs(prods);
  return compose(g, grammar({},stubs));
}

public set[Production] makeRegularStubs(set[Production] prods) {
  return {regular(reg,\no-attrs()) | /Production p:prod(_,_,_) <- prods, sym <- p.lhs, reg <- regular(sym) };
}

private set[Symbol] regular(Symbol s) {
  result = {};
  visit (s) {
     case t:\opt(Symbol n) : 
       result += {t};
     case t:\iter(Symbol n) : 
       result += {t};
     case t:\iter-star(Symbol n) : 
       result += {t};
     case t:\iter-seps(Symbol n, list[Symbol] sep) : 
       result += {t};
     case t:\iter-star-seps(Symbol n,list[Symbol] sep) : 
       result += {t};
     case t:\alt(set[Symbol] alts):
       result += {t};
     case t:\seq(list[Symbol] elems):
       result += {t};
     case t:\empty():
       result += {t};  
  }
  return result;
}  
