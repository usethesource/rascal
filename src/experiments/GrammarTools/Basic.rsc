module experiments::GrammarTools::Basic

import experiments::GrammarTools::BNF
import List;
import Set;
import IO;
import UnitTest;

/* Data structure for representing the grammar */

//public data Symbol      = t(str text) | nt(str name) | epsilon();

public data Symbol      = sym(Terminal | nt(str name) | epsilon();

public alias Rule       = tuple[str name, list[Symbol] symbols];
public data Grammar     = grammar(str start, set[Rule] rules);

Grammar importGrammar(BNF bnfGrammar){
    if([|grammar <NonTerminal Start> rules <Rule+ Rules>){
       rules = {};
       for([|<NonTerminal L> ::= <Symbol* Symbols> ; |] <- Rules){
            rules += <L, [convert(S) | Symbol S <- Symbols]>;
       }
       return grammar(Start, rules); 
    }
}

// Convert a BNF Element to a Symbol value
Symbol convert(Element E){
   switch(E){
     case Terminal _: return t("<E>");
     case NonTerminal _: return n("<E>");
   }
}


// First and follow

public set[Symbol] firstNonEmpty(list[Symbol] symbols, map[Symbol, set[Symbol]] FIRST){
    set[Symbol] result = {};
	for(Symbol sym <- symbols){
	    switch(sym){
	    case t(_):
	    	return result + {sym};
	    case nt(str name): {
	            nonterm= nt(name);
	    		f = FIRST[nonterm] ? {};
	 			if(epsilon() notin f)
					return (result + f) - {epsilon()};
				else
				    result = result + f;
			}
		}
	}
	return result;
}

public map[Symbol, set[Symbol]] first(Grammar G){
	gsymbols = symbols(G);
	
	map[Symbol, set[Symbol]] FIRST = ();
	
	solve (FIRST) {
		for(Symbol sym <- gsymbols){
			println("sym = <sym>");
		
		    switch(sym){
		    case t(_):
		    	FIRST[sym] = {sym};
		    case nt(str name):
		    	{
	        		nonterm = nt(name);
	        		if(!FIRST[nonterm]?)
	        			FIRST[nonterm] = {};
					for(list[Symbol] symbols <- G.rules[name]){
					    if(isEmpty(symbols))
					    	FIRST[nonterm] = FIRST[nonterm] + {epsilon()};
						FIRST[nonterm] = FIRST[nonterm] + firstNonEmpty(symbols, FIRST);
					}
				}
			}
		}
	}	
	return FIRST;
}



public Grammar G1 = grammar("E",
{
<"E", [nt("E"), t("*"), nt("B")]>,
<"E", [nt("E"), t("+"), nt("B")]>,
<"E", [nt("B")]>,
<"B", [t("0")]>,
<"B", [t("1")]>
});

BNF G1a = [|grammar E
            E ::= E "*" B;
            E ::= E "+" B;
            E ::= B;
            B ::= "0";
            B ::= "1";|]

public Grammar G2 = grammar( "E",
{
<"E",  [nt("T"), nt("E1")]>,
<"E1", [t("+"), nt("T"), nt("E1")]>,
<"E1", []>,
<"T",  [nt("F"), nt("T1")]>,
<"T1", [t("*"), nt("F"), nt("T1")]>,
<"T1", []>,
<"F",  [t("("), nt("E"), t(")")]>,
<"F",  [t("id")]>
});

public bool test(){
    println(importGrammar(G1a))

    assertEqual(first(G2), (nt("T1"):{epsilon(),t("*")},
                            t("*"):{t("*")},t("id"):{t("id")},t("+"):{t("+")},t("("):{t("(")},t(")"):{t(")")},
                            nt("E1"):{epsilon(),t("+")},
                            nt("E"):{t("id"),t("(")},
                            nt("T"):{t("id"),t("(")},
                            nt("F"):{t("id"),t("(")}));    
   
	return report("GrammarTools::Basic");
}

