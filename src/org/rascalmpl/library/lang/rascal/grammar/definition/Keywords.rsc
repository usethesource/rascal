module lang::rascal::grammar::definition::Keywords

import Grammar;
import ParseTree;
import lang::rascal::grammar::definition::Symbols;
import lang::rascal::grammar::definition::Productions;
import IO;

public set[Condition] expandKeywords(Grammar g, set[Condition] conds) {
  done = {};
  
  // find any condition defined by a keyword sort
  // we use '/' to skip over 'meta' wrappers
  while ({other*, cond} := conds, cond has symbol, keywords(name) := cond.symbol || meta(keywords(name)) := cond.symbol) {
    if (name in done) 
      return conds; // failsafe for erroneous cyclic keywords definition! 
     
    // now look up the definition of the keyword sort and weave it in.
    conds = other + {cond[symbol=s] | choice(_, alts) := g.rules[cond.symbol], prod([s],_,_) <- alts};
  }
  
  return conds;  
}