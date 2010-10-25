@doc{
  This module implements the support for parameterized syntax definitions
}
module rascal::syntax::Parameters

import rascal::syntax::Grammar;
import rascal::syntax::Normalization; // this module assumes normalized grammars
import ParseTree;
import List;
import Set;

public Grammar expandParameterizedSymbols(Grammar g) {
  g.rules = index(expand({ g.rules[s] | s <- g.rules }), Symbol (Production p) { return p.rhs; });
  return g;
} 
 
set[Production] expand(set[Production] prods) {
  // First we collect all the parametrized definitions
  defs = { p | p <- prods, \parameterized-sort(_,_) := p.rhs };
  result = prods - defs;
  
  // Then we collect all the uses of parameterized sorts in the other productions
  uses = { s | /Symbol s:\parameterized-sort(_,_) <- result};
  
  // Now we copy each definition for each use and rename the parameters
  // Note that we assume normalization will remove the duplicates we introduce by instantiating the a definition twice
  // with the same actual parameters.
  
  instantiated = {};
  while (uses != {}) {
    instances = {};
    for (\parameterized-sort(name,actuals) <- uses, def <- defs, \parameterized-sort(name,formals) := def.rhs) {
       instantiated += {\parameterized-sort(name,actuals)};
       substs = (formals[i]:actuals[i] | int i <- domain(actuals) & domain(formals));
       instances = {instances, visit (def) {
         case Symbol par:\parameter(_) => substs[par]?par
       }}; 
    }
  
    // now, we may have created more uses of parameterized symbols, by instantiating nested parameterized symbols
    uses = { s | /Symbol s:\parameterized-sort(_,_) <- instances, s notin instantiated};
    result += instances;
  }
  
  return result;
}