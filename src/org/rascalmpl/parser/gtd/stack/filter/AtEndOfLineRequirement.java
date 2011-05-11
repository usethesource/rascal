package org.rascalmpl.parser.gtd.stack.filter;

import org.rascalmpl.parser.gtd.util.specific.PositionStore;

public class AtEndOfLineRequirement implements ICompletionFilter{
	
	public AtEndOfLineRequirement(){
		super();
	}
	
	public boolean isFiltered(char[] input, int start, int end, PositionStore positionStore){
		return positionStore.endsLine(end);
	}
}
