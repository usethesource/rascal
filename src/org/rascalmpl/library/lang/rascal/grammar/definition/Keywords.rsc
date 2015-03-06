@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@doc{
Keyword definitions in Rascal grammars are a collection of non-terminals with a specific 
semantics. Namely, statically they can not have recursion or lists, so they generate finite languages.
The \ operator and !>> and !<< will accept them because they are finite. Generally the definitions
are use to define (open) sets of reserved keywords, hence the name "keyword". This module
flattens all definitions into one big regular expression for further processing.
}
module lang::rascal::grammar::definition::Keywords

import Grammar;
import ParseTree;
import lang::rascal::grammar::definition::Symbols;
import lang::rascal::grammar::definition::Productions;
import IO;

Grammar expandKeywords(Grammar g)  
  = top-down-break visit(g) {
    case Symbol s => expandKeywords(s, g)
  };
  
Symbol expandKeywords(Symbol s, Grammar g) 
  = top-down-break visit(s) {
     case \if(c, s) => \if(c, expandKeywords(s, g))
     case \ifElse(c, Symbol i, Symbol t) => \ifElse(c, expandKeywords(i, g), expandKeywords(t, g))
     case \when(s, c) => \when(expandKeywords(s, g), c)
     case \do(s, b) => \do(expandKeywords(s, g), b)
     case \while(c, s) => \while(c, expandKeywords(s, g))
     case \conditional(sym, conds) => \conditional(sym, expandKeywords(g, conds))
  };  

@memo
bool isFinite(Grammar g, \lit(str _)) = true;

@memo
bool isFinite(Grammar g, \cilit(str _)) = true;

@memo
bool isFinite(Grammar g, \char-class(list[CharRange] _)) = true;

@memo default 
bool isFinite(Grammar g, Symbol s) = (true | it && isFinite(g,e) | /prod(_,[e],_) := g.rules[s]); 

set[Condition] expandKeywords(Grammar g, set[Condition] conds) {
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

set[Production] getKeywords(Grammar g) {
  return {g.rules[s] | s:keywords(_) <- g.rules}; 
}
