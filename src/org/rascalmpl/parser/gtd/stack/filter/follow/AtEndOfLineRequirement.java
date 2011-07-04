package org.rascalmpl.parser.gtd.stack.filter.follow;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;

public class AtEndOfLineRequirement implements ICompletionFilter{
	
	public AtEndOfLineRequirement(){
		super();
	}
	
	public boolean isFiltered(char[] input, int start, int end, PositionStore positionStore){
		return positionStore.endsLine(end);
	}
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter){
		return (otherCompletionFilter instanceof AtEndOfLineRequirement);
	}
}
