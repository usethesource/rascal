module experiments::GrammarTools::LR0


import experiments::GrammarTools::Grammar;
import experiments::GrammarTools::ItemSet;
import experiments::GrammarTools::Grammars; // for testing

import List;
import Set;
import IO;
import UnitTest;

public ItemSet closure(Grammar G, ItemSet I){
	//println("closure(<G>, <I>)");
    ItemSet items = I;
    
    solve (items) {
        for(Item item <- items){
            if(atNonTerminal(item)){
        	   nonterm = getSymbol(item);
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
    return closure(G, {moveRight(it) | Item it <- I, atSymbol(it, sym)});
}

public set[ItemSet] items(Grammar G){
	// Extract the symbols from the grammar
	set[Symbol] symbols = symbols(G);
	
	// Add a new, unique, start rule
	Rule startRule = <nt("START"), [G.start]>;
	G.rules = G.rules + {startRule};  // TODO += does not seem to work here

	set[ItemSet] C = {{ closure(G, {makeItem(startRule)}) }};  // TODO: {{ }} horror
	
	solve (C) {
		C += { {GT} | ItemSet I <- C, Symbol X <- symbols, ItemSet GT := goto(G, I, X), !isEmpty(GT)};
	}
	return C;      
}

public bool test(){

	assertEqual(items(G1),
	{
	//0
	{item(nt("B"),[],[t("1")]),item(nt("E"),[],[nt("E"),t("*"),nt("B")]),item(nt("E"),[],[nt("E"),t("+"),nt("B")]), item(nt("START"),[],[nt("E")]),item(nt("E"),[],[nt("B")]),item(nt("B"),[],[t("0")])},
	//1
	{item(nt("B"),[t("0")],[])},
	//2
	{item(nt("B"),[t("1")],[])},
	//3
	{item(nt("E"),[nt("E")],[t("*"),nt("B")]),item(nt("START"),[nt("E")],[]),item(nt("E"),[nt("E")],[t("+"),nt("B")])},
	//4
	{item(nt("E"),[nt("B")],[])},
	//5
	{item(nt("E"),[nt("E"),t("*")],[nt("B")]),item(nt("B"),[],[t("1")]),item(nt("B"),[],[t("0")])},
	//6
	{item(nt("B"),[],[t("1")]),item(nt("E"),[nt("E"),t("+")],[nt("B")]),item(nt("B"),[],[t("0")])},
	//7
	{item(nt("E"),[nt("E"),t("*"),nt("B")],[])},
	//8
	{item(nt("E"),[nt("E"),t("+"),nt("B")],[])}
	});
	
	return report("GrammarTools::LR0");
}

