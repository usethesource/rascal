module demo::Parsing::Parsing

/* Very preliminary LR parser generator */

public data Terminal    = t(str text);
public data NonTerminal = nt(str name);
public data Symbol      = Terminal | NonTerminal | epsilon;

public alias Rule       = tuple[str name, list[value] symbols];  // value => Symbol
public data Grammar     = grammar(str start, set[Rule] rules);  

// First and follow

public map[NonTerminal, set[Symbol]] FIRST = ();

public set[Symbol] firstNonEmpty(Rule r){
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

private Item makeItem(Rule r){
	return item(r,name, [], r.symbols);
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

private bool atNonterminal(Item it){
	return !isEmpty(it.right) && nt(_) := head(it.right);
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

    with 
    	ItemSet items = {};
    solve {
        for(Item item <- I1){
            if(atNonTerminal(item)){
        	   nonterm = getNonTerminal(item);
        	   for(Rule r <- G.rules[nonterm]){
        		   items = items + makeItem(r);
        	   }
            }
        }
    }
    return items;
}

public ItemSet goto(Grammar G, ItemSet items, Symbol sym){
     return closure(G, {moveRight(it) | Item it <- items, atSymbol(it, sym)});
}

public ItemSet items(Grammar G){
	set[Symbol] symbols = { sym | Rule r <- G.rules, Symbol sym <- r.symbols};
	Rule startRule = <"START", [nt(G.start)]>;
	G.rules += {startRule};
	
	with 
		ItemSet C = { closure(G, {makeItem(startRule)}) };
	solve
		C += { G | ItemSet I <- C, Symbol sym <- symbols, ItemSet G := goto(G, I, X), !isEmpty(G)};
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
	items(G1);
	return true;
}

