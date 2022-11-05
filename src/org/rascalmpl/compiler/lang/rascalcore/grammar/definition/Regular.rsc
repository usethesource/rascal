@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascalcore::grammar::definition::Regular

import lang::rascalcore::grammar::definition::Grammar;
import lang::rascalcore::check::AType;
import Set;
import IO;

public AGrammar expandRegularSymbols(AGrammar G) {
  for (AType def <- G.rules) {
    if (choice(def, {regular(def)}) := G.rules[def]) { 
      AProduction init = choice(def,{});
      
      for (p <- expand(def)) {
        G.rules[p.def] = choice(p.def, {p, G.rules[p.def]?\init});
      }
    }
  }
  return G;
}

public set[AProduction] expand(AType s) {
  switch (s) {
    case \opt(t) : 
      return {choice(s, {prod(s[label="absent"], []),prod(s[label="present"], [t])})};
    case \iter(t) : 
      return {choice(s, {prod(s[label="single"], [t]), prod(s[label="multiple"], [t,s])})};
    case \iter-star(t) : 
      return {choice(s, {prod(s[label="empty"], []), prod(s[label="nonEmpty"], [\iter(t)])})} + expand(\iter(t));
    case \iter-seps(t,list[AType] seps) : 
      return {choice(s, {prod(s[label="single"], [t]), prod(s[label="multiple"], [t, *seps, s])})};
    case \iter-star-seps(t, list[AType] seps) : 
      return {choice(s,{prod(s[label="empty"], []), prod(s[label="nonEmpty"], [\iter-seps(t,seps)])})} 
             + expand(\iter-seps(t,seps));
    case \alt(set[AType] alts) :
      return {choice(s, {prod(s,[a]) | a <- alts})};
    case \seq(list[AType] elems) :
      return {prod(s, elems)};
    case \empty() :
      return {prod(s, [])};
   }   

   throw "expand, missed a case <s>";                   
}

public AGrammar makeRegularStubs(AGrammar g) {
  prods = {g.rules[nont] | AType nont <- g.rules};
  stubs = makeRegularStubs(prods);
  return compose(g, grammar({},stubs));
}

public set[AProduction] makeRegularStubs(set[AProduction] prods) {
  return {regular(reg) | /AProduction p:prod(_,_) <- prods, sym <- p.atypes, reg <- getRegular(sym) };
}

private set[AType] getRegular(AType s) = { t | /AType t := s, isRegular(t) }; 

public default bool isRegular(AType s) = false;
public bool isRegular(opt(AType _)) = true;
public bool isRegular(iter(AType _)) = true;
public bool isRegular(\iter-star(AType _)) = true;
public bool isRegular(\iter-seps(AType _, list[AType] _)) = true;
public bool isRegular(\iter-star-seps(AType _, list[AType] _)) = true;
public bool isRegular(alt(set[AType] _)) = true;
public bool isRegular(seq(list[AType] _)) = true;
public bool isRegular(empty()) = true;
