@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@doc{
  This module implements the support for parameterized syntax definitions
}
module lang::rascalcore::grammar::definition::Parameters

import lang::rascalcore::grammar::definition::Grammar;
//import ParseTree;
import lang::rascalcore::check::AType;
import List;
import Set;

public Grammar expandParameterizedSymbols(Grammar g) {
  return grammar(g.starts, expand({g.rules[nt] | nt <- g.rules}));
} 

private AType delabel(AType l) {
  return (label(x,m) := l) ? m : l;
}

set[Production] expand(set[Production] prods) {
  // First we collect all the parametrized definitions
  defs = { p | p <- prods, AType s := delabel(p.def),  s is \parameterized-sort || s is \parameterized-lex};
  result = prods - defs;
  
  // Then we collect all the uses of parameterized sorts in the other productions
  uses = { s | /AType s <- result, s is \parameterized-sort || s is \parameterized-lex};
  
  // Now we copy each definition for each use and rename the parameters
  // Note that we assume normalization will remove the duplicates we introduce by instantiating the a definition twice
  // with the same actual parameters.
  
  instantiated = {};
  while (uses != {}) {
    instances = {};
    for (u <- uses, def <- defs, def.def.name == u.name) {
       name = u.name;
       actuals = u.parameters;
       formals = def.def.parameters;
       instantiated += {u};
       substs = (formals[i]:actuals[i] | int i <- index(actuals) & index(formals));
       instances = {*instances, visit (def) {
         case AType par:\parameter(_,_) => substs[par]?par
       }}; 
    }
  
    // now, we may have created more uses of parameterized symbols, by instantiating nested parameterized symbols
    uses = { s | /AType s <- instances, s is \parameterized-sort || s is \parameterized-lex, s notin instantiated};
    result += instances;
  }
  
  return result;
}
