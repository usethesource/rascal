module Grammar

/* Very preliminary LR parser generator

data Symbol = t(str text) | nt(str name);
data Rule   = rule(str name, list[Symbol] symbols);
data Grammar= grammar(str start, list[Rule] rules);


// ------------ Items ------------------------------
data Item = item(str name, list[Symbol] left, list[Symbol] right]);

private Item makeItem(Rule rule){
	return item(rule,name, rule.symbols, []);
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


public ItemSet closure(ItemSet items){

    solve {
        for(Item item <- items){
            if(atNonTerminal(item)){
        	   nonterm = getNonTerminal(item);
        	   for(Rule rule <- definition[nonterm])
        		   items = items + makeITem(rule);
        	   }
            }
        }
    }
    return items;
}

alias StateId = int;

map[ItemSet, StateId] itemsets = ();
rel[str, list[Symbol]) definition = {};
set[Symbol] symbols = {};
int stateCnt = 0;

rel[StateId, Symbol, StateId] transitions;

public void generateLR(Grammar G){

	for(Rule rule <- G.rules){
		definition = definition + <rule.name, rule.symbols>;
		for(Symbol sym <- rule.symbols){
			symbols = symbols + sym;
		}
	}
	
	items(makeItem(rule("START", [nt(G.start)])));
}

public ItemSet goto(ItemSet items, Symbol sym){

     return closure({moveRight(it) | Item it <- items, atSymbol(it, sym)});
}

public StateId expand(ItemSet items){

      if(itemsets[items]?)
      	return itemsets[items];
      
      int this = stateCnt;
      stateCnt = stateCnt + 1;
      itemsets[this] = items;
      
      for(Item it <- items){
          if(!atEnd(dr)){
             localTransitions = localTranstions + <getSymbol(dr), moveRight(dr)>;
          }
      }
      for(Symbol sym <- domain(localTransitions)){
          next = expand(closure(localTransitions[sym]));
          transitions = transitions + <this, sym, next>;
      }
}