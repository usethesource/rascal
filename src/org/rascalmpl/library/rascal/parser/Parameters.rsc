@doc{
  This module implements the support for parameterized syntax definitions
}
module rascal::parser::Parameters

import rascal::parser::Grammar;
import rascal::parser::Normalization; // this module assumes normalized grammars
import ParseTree;
import List;

public Grammar expandParameterizedSymbols(Grammar g) {
  g.productions = expand(g.productions);
}

set[Production] expand(set[Production] prods) {
  // First we collect all the parametrized definitions
  defs = { p | p <- prods, \parameterized-sort(_,_) := sort(p) };
  result = prods - defs;
  
  // Then we collect all the uses of parameterized sorts in the other productions
  uses = { s | /Symbol s:\parametrized-sort(_,_) <- result};
  
  // Now we copy each definition for each use and rename the parameters
  // Note that we assume normalization will remove the duplicates we introduce by instantiating the a definition twice
  // with the same actual parameters.
  
  instantiated = {};
  while (uses != {}) {
    instances = {};
    for (\parametrized-sort(name,actuals) <- uses, def <- defs, \parametrized-sort(name,formals) := sort(def)) {
       instantiated += \parametrized-sort(name,actuals);
       substs = (formals[i]:actuals[i] | int i <- domain(actuals) & domain(formals));
       instances += visit (def) {
         case Symbol par:\sort(_) => substs[par]?par
       }; 
    }
  
    // now, we may have created more uses of parameterized symbols, by instantiating nested parameterized symbols
    uses = { s | /Symbol s:\parametrized-sort(_,_) <- instances, s notin instantiated};
    result += instances;
  }
  
  return result;
}