module demo::Parsing::Parsing

import List;
import IO;

/* Very preliminary LR parser generator */

public data Symbol      = t(str text) | nt(str name) | epsilon;

public alias Rule       = tuple[str name, list[Symbol] symbols];
public data Grammar     = grammar(str start, set[Rule] rules);  

// First and follow

public map[Symbol, set[Symbol]] FIRST = ();

public set[Symbol] firstNonEmpty(Rule r){
    println("firstNonEmpty(<r>)");
	for(Symbol sym <- r.symbols){
	    switch(sym){
	    case Terminal _:
	    	return {sym};
	    case NonTerminal _:
	 		if(epsilon notin FIRST[sym])
				return FIRST[sym] - {epsilon};	
		}
	}
	return {};
}

public Symbol first(Symbol sym){
 	println("first(<sym>)");
	switch(sym){
	case t(_): return {sym};
	
	case n(str name):
		for(Rule r <- definition[name]){
			f = firstNonEmpty(r);
			if(isEmpty(f))
				FIRST[r.name] = FIRST[r.name] + epsilon;
			else
				FIRST[r.name] = FIRST[r.name] + f;
		}
	}
}


// ------------ Items ------------------------------
data Item = item(str name, list[Symbol] left, list[Symbol] right);

public Item makeItem(Rule r){
	//println("makeItem(<r>)");
	return item(r.name, [], r.symbols);
}

private bool canMove(Item it, Symbol sym){
   return !isEmpty(it.right) && head(it.right) == sym;
}

private Item moveRight(Item it){
   return item(it.left + [it.head], tail(it.right));
}

private Symbol getSymbol(Item it){
   return head(it.right);
}

private bool atEnd(Item it){
   return isEmpty(it.right);
}

private bool atNonTerminal(Item it){
	bool res = !isEmpty(it.right) && nt(_) := head(it.right);
	//println("atNonTerminal(<it>) => <res>");
	return res;
}

private str getNonTerminal(Item it){
	Symbol h = head(it.right);
	if(nt(str Name) := h)
		return Name;
}
	
private bool atTerminal(Item it){
	return !isEmpty(it.right) && t(_) := head(it.right);
}

private bool atSymbol(Item it, Symbol sym){
	return !isEmpty(it.right) && sym == head(it.right);
}

private bool isEmpty(Item it){
   return isEmpty(it.left) &&  isEmpty(it.right);
}


//-------- ItemSets ---------------------------------------

alias ItemSet = set[Item];

public ItemSet closure(Grammar G, ItemSet I){
	//println("closure(<G>, <I>)");
    with 
    	ItemSet items = I;
    solve {
        for(Item item <- items){
            if(atNonTerminal(item)){
        	   nonterm = getNonTerminal(item);
        	   for(list[Symbol] symbols <- G.rules[nonterm]){
        	       println("symbols = <symbols>");
        		   items = items + makeItem(<nonterm, symbols>);
        	   }
            }
        }
    }
    return items;
}

public ItemSet goto(Grammar G, ItemSet I, Symbol sym){
	println("goto(<G>, <I>, <sym>)");
    return closure(G, {moveRight(it) | Item it <- I, atSymbol(it, sym)});
}

public ItemSet items(Grammar G){
	println("items(<G>)");
	set[Symbol] symbols = { sym | Rule r <- G.rules, Symbol sym <- r.symbols};
	Rule startRule = <"START", [nt(G.start)]>;
	G.rules = G.rules + {startRule};  // TODO += does not seem to work here

	with 
		ItemSet C = { closure(G, {makeItem(startRule)}) };
	solve {
		C += { GT | ItemSet I <- C, Symbol X <- symbols, ItemSet GT := goto(G, I, X), !isEmpty(GT)};
		println("items: C = <C>");
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


public bool test(){
	C = items(G1);
	println("C = <C>");
	return true;
}

