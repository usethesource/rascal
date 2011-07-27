package org.rascalmpl.parser.gtd.experiments.grammar.symbol;

import org.rascalmpl.parser.gtd.experiments.grammar.State;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public class SortSymbol extends Symbol{
	private final String name;
	
	public SortSymbol(String name){
		this(name, null, null);
	}
	
	public SortSymbol(String name, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(enterFilters, completionFilters);
		
		this.name = name;
	}
	
	public AbstractStackNode buildNode(State state, int dot){
		return new NonTerminalStackNode(state.getNextId(this), dot, name, enterFilters, completionFilters);
	}
	
	public boolean isEqual(Symbol symbol){
		if(!(symbol instanceof SortSymbol)) return false;
		
		SortSymbol otherSymbol = (SortSymbol) symbol;

		if(!name.equals(otherSymbol.name)) return false;
		
		return hasEqualFilters(otherSymbol);
	}
}
