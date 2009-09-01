module experiments::GrammarTools::FirstFollow

import experiments::GrammarTools::Grammar;
import experiments::GrammarTools::BNF;
import List;
import Set;
import IO;
import UnitTest;

// First and follow

public set[Symbol] firstNonEmpty(list[Symbol] symbols, map[Symbol, set[Symbol]] FIRST){
    set[Symbol] result = {};
	for(Symbol sym <- symbols){
	    switch(sym){
	    case t(_):
	    	return result + {sym};
	    case nt(str name): {
	    		f = FIRST[sym] ? {};
	 			if(epsilon() notin f)
					return (result + f) - {epsilon()};
				else
				    result += f;
			}
		}
	}
	return result;
}

// Compute the first sets for a grammar

public map[Symbol, set[Symbol]] first(Grammar G){
	gsymbols = symbols(G);
	
	map[Symbol, set[Symbol]] FIRST = ();
	
	solve (FIRST) {
		for(Symbol sym <- gsymbols){
		    switch(sym){
		    case t(_):
		    	FIRST[sym] = {sym};
		    case nt(str name):
		    	{
	        		if(!FIRST[sym]?)
	        			FIRST[sym] = {};
					for(list[Symbol] symbols <- G.rules[sym]){
					    if(isEmpty(symbols))
					    	FIRST[sym] += {epsilon()};
					    else
						    FIRST[sym] += firstNonEmpty(symbols, FIRST);
					}
				}
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
  return result;
}

// Compute the follow sets for a grammar

public map[Symbol, set[Symbol]] follow(Grammar G, map[Symbol, set[Symbol]] FIRST){
	gsymbols = symbols(G);
	
	map[Symbol, set[Symbol]] FOLLOW = (G.start : {t("$")});
	
	/////  Work in progress
	
	solve (FOLLOW) {
		for(Symbol sym <- gsymbols){
		    switch(sym){
		    case t(_):
		    	FIRST[sym] = {sym};
		    case nt(str name):
		    	{
	        		if(!FIRST[sym]?)
	        			FIRST[sym] = {};
					for(list[Symbol] symbols <- G.rules[sym]){
					    if(isEmpty(symbols))
					    	FIRST[sym] += {epsilon()};
					    else
						    FIRST[sym] += firstNonEmpty(symbols, FIRST);
					}
				}
			}
		}
	}	
	return FIRST;
}


BNF G2 = `grammar E
          rules 
            E ::= T E1;
            E1 ::= '+' T E1;
            E1 ::= ;
            T  ::= F T1;
            T1 ::= '*' F T1;
            T1 ::= ;
            F  ::= '(' E ')';
            F  ::= 'id';
            `;  

public bool test(){

    assertEqual(first(importGrammar(G2)), 
                (nt("T1"):{epsilon(),t("*")},
                 t("*"):{t("*")},t("id"):{t("id")},t("+"):{t("+")},t("("):{t("(")},t(")"):{t(")")},
                 nt("E1"):{epsilon(),t("+")},
                 nt("E"):{t("id"),t("(")},
                 nt("T"):{t("id"),t("(")},
                 nt("F"):{t("id"),t("(")}));    
   
	return report("GrammarTools::FirstFollow");
}

