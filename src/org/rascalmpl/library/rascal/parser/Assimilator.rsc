@doc{
  This module provides functionality for merging the Rascal grammar and arbitrary user-defined grammars
}
module Assimilator

import ValueIO;
import rascal::parser::Normalization;

private Grammar rascalGrammar = grammar({},{});

@doc{
  It is assumed both the Rascal grammar and the object grammar have been normalized
}
public Grammar assimiliate(Grammar object) {
  if (rascalGrammar == grammar({},{})) {
    Grammar rascalGrammar = readValueFile(|stdlib:///org/rascalmpl/library/rascal/parser/Rascal.grammar|);
  }  
  
  Grammar result = rascalGrammar;
  set[Production] emptySet = {};

  // here we prima all symbols in the grammar to be distinct from the Rascal non-terminals  
  object = visit(object) {
    case Symbol s:\char-class(_) => s
    case Symbol s => prime(s, "meta", 1)
  }
  
  for (Symbol nont <- object.rules) {
    result.rules[sort("Expression")] += 
      { prod([lit("`"),nont,lit("`")],sort("Expression"),attrs([term("ToRascal")]))
      
      };
  }
  
  // finally we simply merge
  for (Symbol nont <- object.rules) {
    result.rules[nont]?emptySet += object.rules[nont];
  }
  
  return result;
}

Grammar prime(Grammar object) {
  return 
}

