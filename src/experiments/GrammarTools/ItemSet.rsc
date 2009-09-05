module experiments::GrammarTools::ItemSet

import experiments::GrammarTools::Grammar;
import experiments::GrammarTools::Grammars; // for testing

import List;
import Set;
import IO;
import UnitTest;

// ------------ Items ------------------------------
data Item = item(Symbol lhs, list[Symbol] left, list[Symbol] right);

public Item makeItem(Rule r){
	//println("makeItem(<r>)");
	return item(r.lhs, [], r.symbols);
}

public bool canMove(Item it, Symbol sym){
   return !isEmpty(it.right) && head(it.right) == sym;
}

public Item moveRight(Item it){
   return item(it.lhs, it.left + [head(it.right)], tail(it.right));
}

public Symbol getSymbol(Item it){
   return head(it.right);
}

public bool atEnd(Item it){
   return isEmpty(it.right);
}

public bool atNonTerminal(Item it){
	bool res = !isEmpty(it.right) && isNonTermSymbol(head(it.right));
	//println("atNonTerminal(<it>) => <res>");
	return res;
}
	
public bool atTerminal(Item it){
	return !isEmpty(it.right) && isTermSymbol(head(it.right));
}

public bool atSymbol(Item it, Symbol sym){
	return !isEmpty(it.right) && sym == head(it.right);
}

private bool isEmpty(Item it){
   return isEmpty(it.left) &&  isEmpty(it.right);
}

//-------- ItemSets ---------------------------------------

alias ItemSet = set[Item];


public bool test(){

    IT = item(nt("E"),[],[nt("E"),t("*"),nt("B")]);
    
    assertEqual(getSymbol(IT), nt("E"));
    assertTrue(atNonTerminal(IT));
    assertTrue(!atTerminal(IT));
    assertTrue(canMove(IT, nt("E")));
    assertTrue(!atEnd(IT));
    
    IT1 = item(nt("E"),[nt("E")], [t("*"),nt("B")]);
    assertEqual(moveRight(IT), IT1);
    
    assertEqual(getSymbol(IT1), t("*"));
    assertTrue(!atNonTerminal(IT1));
    assertTrue(atTerminal(IT1));
    assertTrue(canMove(IT1, t("*")));
    assertTrue(!atEnd(IT1));
    
    IT2 = moveRight(IT1);
    IT3 = moveRight(IT2);
    
    assertTrue(!atNonTerminal(IT3));
    assertTrue(!atTerminal(IT3));
    assertTrue(!canMove(IT3, nt("B")));
    assertTrue(atEnd(IT3));
	
	return report("GrammarTools::ItemSet");
}

