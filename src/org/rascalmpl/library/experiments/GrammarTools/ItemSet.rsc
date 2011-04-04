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
module experiments::GrammarTools::ItemSet

import experiments::GrammarTools::Grammar;
import experiments::GrammarTools::Grammars; // for testing

import List;
import Set;
import IO;

// ------------ Items ------------------------------
data Item = item(Symbol lhs, list[Symbol] left, list[Symbol] right);

public Item makeItem(Rule r){
	//println("makeItem(<r>)");
	return item(r.lhs, [], r.symbols);
}

public bool canMove(Item item, Symbol sym){
   return !isEmpty(item.right) && head(item.right) == sym;
}

public Item moveRight(Item itm){
   return item(itm.lhs, itm.left + [head(itm.right)], tail(itm.right));
}

public Symbol getSymbol(Item itm){
   return head(itm.right);
}

public bool atEnd(Item itm){
   return isEmpty(itm.right);
}

public bool atNonTerminal(Item itm){
	bool res = !isEmpty(itm.right) && isNonTermSymbol(head(itm.right));
	//println("atNonTerminal(<itm>) => <res>");
	return res;
}
	
public bool atTerminal(Item itm){
	return !isEmpty(itm.right) && isTermSymbol(head(itm.right));
}

public bool atSymbol(Item itm, Symbol sym){
	return !isEmpty(itm.right) && sym == head(itm.right);
}

private bool isEmpty(Item itm){
   return isEmpty(itm.left) &&  isEmpty(itm.right);
}

//-------- ItemSets ---------------------------------------

alias ItemSet = set[Item];

// Tests

private Item IT = item(nt("E"),[],[nt("E"),t("*"),nt("B")]);
    
test getSymbol(IT) == nt("E");
test atNonTerminal(IT);
test !atTerminal(IT);
test canMove(IT, nt("E"));
test !atEnd(IT);
    
private Item IT1 = item(nt("E"),[nt("E")], [t("*"),nt("B")]);

test moveRight(IT) == IT1;
    
test getSymbol(IT1) == t("*");
test !atNonTerminal(IT1);
test atTerminal(IT1);
test canMove(IT1, t("*"));
test !atEnd(IT1);
    
private Item IT2 = moveRight(IT1);
private Item IT3 = moveRight(IT2);
    
test !atNonTerminal(IT3);
test !atTerminal(IT3);
test !canMove(IT3, nt("B"));
test atEnd(IT3);

