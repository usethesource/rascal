module lang::rascal::grammar::definition::Keywords

import Grammar;
import ParseTree;
import lang::rascal::grammar::definition::Symbols;
import lang::rascal::grammar::definition::Productions;
import lang::rascal::grammar::Assimilator;
import IO;

public Grammar expandKeywords(Grammar g) {
  return visit(g) {
    case conditional(sym, conds) => conditional(sym, expandKeywords(g, conds)) 
  };
}

public set[Condition] expandKeywords(Grammar g, set[Condition] conds) {
  names = {};
  done = {};
  todo = conds;

  solve(todo) {  
    for (cond <- todo, !(cond in done)) {
      todo -= {cond};
      
      if (cond has symbol, keywords(str name) := cond.symbol || meta(keywords(str name)) := cond.symbol) {
        if (name in names) {
          continue;
        }
        names += {name};
        todo += {cond[symbol=s] | choice(_, set[Production] alts) := g.rules[cond.symbol], prod(_,[s],_) <- alts};
      }
      else {
        done += cond;
      }
    }
  }
  
  return done;  
}

public set[Production] getKeywords(Grammar g) {
  return {g.rules[s] | s:keywords(_) <- g.rules}; 
}
