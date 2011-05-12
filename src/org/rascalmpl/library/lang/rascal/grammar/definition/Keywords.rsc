module lang::rascal::grammar::definition::Keywords

import Grammar;
import ParseTree;
import lang::rascal::grammar::definition::Symbols;
import IO;

public set[Condition] expandKeywords(Grammar g, set[Condition] conds) {
  done = {};
  
  // find any condition defined by a keyword sort
  while ({other*, cond} := conds, bprintln(cond), keywords(name) := cond.symbol) {
    if (name in done) 
      return conds; // failsafe for erroneous cyclic keywords definition! 
     
    // now look up the definition of the keyword sort and weave it in.
    conds = other + {cond[symbol=s] | choice(_, alts) := g.rules[keywords(name)], prod([s],_,_) <- alts};
  }
    
  return conds;  
}