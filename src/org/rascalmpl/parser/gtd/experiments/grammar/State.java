package org.rascalmpl.parser.gtd.experiments.grammar;

import org.rascalmpl.parser.gtd.experiments.grammar.symbol.Symbol;
import org.rascalmpl.parser.gtd.util.ArrayList;

public class State{
	private final ArrayList<Symbol> symbolMap;
	
	private int identifierCounter;
	
	public State(){
		super();
		
		symbolMap = new ArrayList<Symbol>();
		
		identifierCounter = -1;
	}
	
	public int getNextId(Symbol symbol){
		symbolMap.add(symbol);
		
		return ++identifierCounter;
	}
	
	public int getIdForSymbol(Symbol symbol){
		return symbolMap.findIndex(symbol);
	}
}
