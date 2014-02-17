@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::grammar::definition::Keywords

import Grammar;
import ParseTree;
import lang::rascal::grammar::definition::Symbols;
import lang::rascal::grammar::definition::Productions;
import IO;

public Grammar expandKeywords(Grammar g) {
  return visit(g) {
    case conditional(sym, conds) => conditional(sym, expandKeywords(g, conds)) 
  };
}

@memo
bool isFinite(Grammar g, \lit(str _)) = true;

@memo
bool isFinite(Grammar g, \cilit(str _)) = true;

@memo
bool isFinite(Grammar g, \char-class(list[CharRange] _)) = true;

@memo default 
bool isFinite(Grammar g, Symbol s) = (true | it && isFinite(g,e) | /prod(_,[e],_) := g.rules[s]); 

public set[Condition] expandKeywords(Grammar g, set[Condition] conds) {
  names = {};
  done = {};
  todo = conds;

  solve(todo) {  
    for (cond <- todo, !(cond in done)) {
      todo -= {cond};
      if (cond has symbol, cond.symbol is lex || cond.symbol is sort || cond.symbol is keywords, isFinite(g, cond.symbol)) {
        if (cond.symbol.name in names) {
          continue;
        }
        names += {cond.symbol.name};
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
