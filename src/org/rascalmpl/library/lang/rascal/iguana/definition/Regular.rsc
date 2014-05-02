@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascal::iguana::definition::Regular

import lang::rascal::iguana::definition::Modules;
import lang::rascal::iguana::definition::Productions;
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

public set[Production] expand(Symbol s) {
  switch (s) {
    case \opt(t) : 
      return {choice(s,{prod(label("absent",s),[],{}),prod(label("present",s),[t],{})})};
    case \iter(t) : 
      return {choice(s,{prod(label("single",s),[t],{}),prod(label("multiple",s),[s,t],{})})};
    case \iter-star(t) : 
      return {choice(s,{prod(label("empty",s),[],{}),prod(label("nonEmpty",s),[\iter(t)],{})})} + expand(\iter(t));
    case \iter-seps(t,list[Symbol] seps) : 
      return {choice(s, {prod(label("single",s),[t],{}),prod(label("multiple",s),[s,*seps,t],{})})};
    case \iter-star-seps(t, list[Symbol] seps) : 
      return {choice(s,{prod(label("empty",s),[],{}),prod(label("nonEmpty",s),[\iter-seps(t,seps)],{})})} 
             + expand(\iter-seps(t,seps));
    case \alt(set[Symbol] alts) :
      return {choice(s, {prod(s,[a],{}) | a <- alts})};
    case \seq(list[Symbol] elems) :
      return {prod(s,elems, {})};
    case \empty() :
      return {prod(s,[],{})};
   }   

   throw "missed a case <s>";                   
}

public Grammar makeRegularStubs(Grammar g) {
  prods = {g.rules[nont] | Symbol nont <- g.rules, label(_,\token(_)) !:= nont, \token(_) !:= nont};
  stubs = makeRegularStubs(prods);
  return compose(g, iguana({},stubs, ()));
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
