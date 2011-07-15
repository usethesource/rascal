@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module experiments::Parsing::LRGen

import List;
import Set;
import IO;

/* Very simple LR parser generator */

public data Symbol      = t(str text) | nt(str name) | epsilon();

public alias Rule       = tuple[str name, list[Symbol] symbols];
public data Grammar     = grammar(str startSym, set[Rule] rules);  

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
	        		println("G.rules=<G.rules>");
	        		println(G.rules[name]);
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


// ------------ Items ------------------------------
data Item = item(str name, list[Symbol] left, list[Symbol] right);

public Item makeItem(Rule r){
	//println("makeItem(<r>)");
	return item(r.name, [], r.symbols);
}

private bool canMove(Item item, Symbol sym){
   return !isEmpty(item.right) && head(item.right) == sym;
}

private Item moveRight(Item item){
   return item(item.name, item.left + [head(item.right)], tail(item.right));
}

private Symbol getSymbol(Item item){
   return head(item.right);
}

private bool atEnd(Item item){
   return isEmpty(item.right);
}

private bool atNonTerminal(Item item){
	bool res = !isEmpty(item.right) && nt(_) := head(item.right);
	//println("atNonTerminal(<item>) => <res>");
	return res;
}

private str getNonTerminal(Item item){
	Symbol h = head(item.right);
	if(nt(str Name) := h)
		return Name;
}
	
private bool atTerminal(Item item){
	return !isEmpty(item.right) && t(_) := head(item.right);
}

private bool atSymbol(Item item, Symbol sym){
	return !isEmpty(item.right) && sym == head(item.right);
}

private bool isEmpty(Item item){
   return isEmpty(item.left) &&  isEmpty(item.right);
}


//-------- ItemSets ---------------------------------------

alias ItemSet = set[Item];

public ItemSet closure(Grammar G, ItemSet I){
	//println("closure(<G>, <I>)");
    ItemSet items = I;
    
    solve (items) {
        for(Item item <- items){
            if(atNonTerminal(item)){
        	   nonterm = getNonTerminal(item);
        	   for(list[Symbol] symbols <- G.rules[nonterm]){
        	       //println("symbols = <symbols>");
        		   items = items + makeItem(<nonterm, symbols>);
        	   }
            }
        }
    }
    return items;
}

public ItemSet goto(Grammar G, ItemSet I, Symbol sym){
	//println("goto(<G>, <I>, <sym>)");
    return closure(G, {moveRight(item) | Item item <- I, atSymbol(item, sym)});
}

public set[Symbol] symbols(Grammar G){
   return { sym | Rule r <- G.rules, Symbol sym <- r.symbols};
}

public set[ItemSet] items(Grammar G){
	// Extract the symbols from the grammar
	set[Symbol] symbols = symbols(G);
	
	// Add a new start rule
	Rule startRule = <"START", [nt(G.startSym)]>;
	G.rules = G.rules + {startRule};  // TODO += does not seem to work here

	set[ItemSet] C = {{ closure(G, {makeItem(startRule)}) }};  // TODO: {{ }} horror
	
	solve (C) {
		C += { {GT} | ItemSet I <- C, Symbol X <- symbols, ItemSet GT := goto(G, I, X), !isEmpty(GT)};
	}
	return C;      
}

public Grammar G1 = grammar("E",
{
<"E", [nt("E"), t("*"), nt("B")]>,
<"E", [nt("E"), t("+"), nt("B")]>,
<"E", [nt("B")]>,
<"B", [t("0")]>,
<"B", [t("1")]>
});

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

// Tests

test first(G2) == (nt("T1"):{epsilon(),t("*")},
                   t("*"):{t("*")},t("id"):{t("id")},t("+"):{t("+")},t("("):{t("(")},t(")"):{t(")")},
                   nt("E1"):{epsilon(),t("+")},
                   nt("E"):{t("id"),t("(")},
                   nt("T"):{t("id"),t("(")},
                   nt("F"):{t("id"),t("(")});    
    
test items(G1) ==
	{
	//0
	{item("B",[],[t("1")]),item("E",[],[nt("E"),t("*"),nt("B")]),item("E",[],[nt("E"),t("+"),nt("B")]), item("START",[],[nt("E")]),item("E",[],[nt("B")]),item("B",[],[t("0")])},
	//1
	{item("B",[t("0")],[])},
	//2
	{item("B",[t("1")],[])},
	//3
	{item("E",[nt("E")],[t("*"),nt("B")]),item("START",[nt("E")],[]),item("E",[nt("E")],[t("+"),nt("B")])},
	//4
	{item("E",[nt("B")],[])},
	//5
	{item("E",[nt("E"),t("*")],[nt("B")]),item("B",[],[t("1")]),item("B",[],[t("0")])},
	//6
	{item("B",[],[t("1")]),item("E",[nt("E"),t("+")],[nt("B")]),item("B",[],[t("0")])},
	//7
	{item("E",[nt("E"),t("*"),nt("B")],[])},
	//8
	{item("E",[nt("E"),t("+"),nt("B")],[])}
	};
