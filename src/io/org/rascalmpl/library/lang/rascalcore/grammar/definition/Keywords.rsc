@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascalcore::grammar::definition::Keywords

import lang::rascalcore::grammar::definition::Grammar;
//import ParseTree;
import lang::rascalcore::check::AType;
import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Productions;
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
      
      if (cond has symbol, keywords(str name) := cond.symbol) {
        if (name notin names) {
        	names += {name};
        	todo += {cond[symbol=s] | choice(_, set[Production] alts) := g.rules[cond.symbol], prod(_,[s],_) <- alts};
      	}  
      } else {
        done += cond;
      }
    }
  }
  
  return done;  
}

public set[Production] getKeywords(Grammar g) {
  return {g.rules[s] | s:keywords(_) <- g.rules}; 
}
