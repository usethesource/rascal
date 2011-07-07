package org.rascalmpl.parser.gtd.grammar.symbol;

import org.rascalmpl.parser.gtd.grammar.State;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public abstract class Symbol{
	protected final IEnterFilter[] enterFilters;
	protected final ICompletionFilter[] completionFilters;
	
	public Symbol(IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super();
		
		this.enterFilters = enterFilters;
		this.completionFilters = completionFilters;
	}
	
	public abstract AbstractStackNode buildNode(State state, int dot);
	
	public abstract boolean isEqual(Symbol symbol);
	
	public boolean hasEqualFilters(Symbol symbol){
		IEnterFilter[] otherEnterFilters = symbol.enterFilters;
		OUTER: for(int i = enterFilters.length - 1; i >= 0; --i){
			IEnterFilter enterFilter = enterFilters[i];
			for(int j = otherEnterFilters.length - 1; j >= 0; --j){
				if(enterFilter.isEqual(otherEnterFilters[j])) continue OUTER;
			}
			return false;
		}
		
		ICompletionFilter[] otherCompletionFilters = symbol.completionFilters;
		OUTER: for(int i = completionFilters.length - 1; i >= 0; --i){
			ICompletionFilter completionFilter = completionFilters[i];
			for(int j = otherCompletionFilters.length - 1; j >= 0; --j){
				if(completionFilter.isEqual(otherCompletionFilters[j])) continue OUTER;
			}
			return false;
		}
		
		return true;
	}
}
