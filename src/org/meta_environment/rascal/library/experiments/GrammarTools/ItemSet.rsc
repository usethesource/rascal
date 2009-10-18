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

