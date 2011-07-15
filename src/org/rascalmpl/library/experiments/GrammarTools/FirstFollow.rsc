@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module experiments::GrammarTools::FirstFollow

import experiments::GrammarTools::Grammar;
import experiments::GrammarTools::Grammars; // for testing
import List;
import Set;
import IO;

// First and follow

public set[Symbol] firstNonEmpty(list[Symbol] symbols, map[Symbol, set[Symbol]] FIRST){
    set[Symbol] result = {};
	for(Symbol sym <- symbols){
	    if(isTermSymbol(sym))
	    	return result + {sym};
	    else {
	        f = FIRST[sym] ? {};
	 		if(epsilon() notin f)
			   return (result + f) - {epsilon()};
			else
			   result += f;
		}
	}
	return result;
}

// Compute the first sets for a grammar

public map[Symbol, set[Symbol]] first(Grammar G){
    map[Symbol, set[Symbol]] FIRST = ();
    set[Symbol] ntSymbols = {};   // NB: removing type leads to error in += below
    
    for(Symbol sym <- symbols(G))
        if(isTermSymbol(sym))
		    FIRST[sym] = {sym};
		else {
		    FIRST[sym] = {};
		    ntSymbols += {sym};
		}
			
	solve (FIRST) {
		for(Symbol sym <- ntSymbols){
			for(list[Symbol] symbols <- G.rules[sym]){
				if(isEmpty(symbols))
				   FIRST[sym] += {epsilon()};
				else
				   FIRST[sym] += firstNonEmpty(symbols, FIRST);
			}
		}
	}	
	return FIRST;
}

// Compute the first set of a list of symbols for given FIRST map

public set[Symbol] first(list[Symbol] symbols, map[Symbol, set[Symbol]] FIRST){
  set[Symbol] result = {};
  for(Symbol sym <- symbols){
      f = FIRST[sym];
      result += f - {epsilon()};
      if(epsilon() notin f)
         return result;
  }
  return result + {epsilon()};
}

// Compute the follow sets for a grammar

public map[Symbol, set[Symbol]] follow(Grammar G, map[Symbol, set[Symbol]] FIRST){
	map[Symbol, set[Symbol]] FOLLOW = (); 
	
	for(Symbol sym <- symbols(G))  /* all non-terminals have empoty follow set */
	    if(isNonTermSymbol(sym))
	       FOLLOW[sym] = {};
	       
	FOLLOW[G.startSym] = {t("$")};   /* start symbol has eof marker in follow set */     
	
	solve (FOLLOW) {
	    for(/<Symbol A, list[Symbol] symbols> <- G){  /* A ::= alpha B beta; */
	        while(!isEmpty(symbols)){ 
	            B = head(symbols);
	            beta = tail(symbols);
	           
		        if(isNonTermSymbol(B)){
		           firstBeta =  first(beta, FIRST);
		           FOLLOW[B] += firstBeta - {epsilon()};
			       if(isEmpty(beta) || epsilon() in firstBeta)
			           FOLLOW[B] += FOLLOW[A];
			    }
			    symbols = beta;
			}
		}
	}	
	return FOLLOW;
}

    test first(G2) == 
                (nt("T1"):{epsilon(),t("*")},
                 t("*"):{t("*")},t("id"):{t("id")},t("+"):{t("+")},t("("):{t("(")},t(")"):{t(")")},
                 nt("E1"):{epsilon(),t("+")},
                 nt("E"):{t("id"),t("(")},
                 nt("T"):{t("id"),t("(")},
                 nt("F"):{t("id"),t("(")})
                ;    
     
     test first([nt("T1")], first(G2)) == {epsilon(),t("*")};
     
     test first([nt("F"), nt("T1")], first(G2)) == {t("id"),t("(")};
 
     test follow(G2, first(G2)) == 
                 (nt("E"):  {t(")"), t("$")},
                  nt("E1"): {t(")"), t("$")},
                  nt("T"):  {t("+"), t(")"), t("$")},
                  nt("T1"): {t("+"), t(")"), t("$")},
                  nt("F"):  {t("+"), t("*"), t(")"), t("$")})
                ;  

