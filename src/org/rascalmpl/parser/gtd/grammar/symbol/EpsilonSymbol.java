package org.rascalmpl.parser.gtd.grammar.symbol;

import org.rascalmpl.parser.gtd.grammar.State;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.EpsilonStackNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public class EpsilonSymbol extends Symbol{
	
	public EpsilonSymbol(){
		super(null, null);
	}
	
	public EpsilonSymbol(IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(enterFilters, completionFilters);
	}
	
	public AbstractStackNode buildNode(State state, int dot){
		return new EpsilonStackNode(state.getNextId(this), dot, enterFilters, completionFilters);
	}
	
	public boolean isEqual(Symbol symbol){
		if(!(symbol instanceof EpsilonSymbol)) return false;
		
		return hasEqualFilters(symbol);
	}
}
