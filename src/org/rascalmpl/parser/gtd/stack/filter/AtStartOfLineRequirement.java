package org.rascalmpl.parser.gtd.stack.filter;

import org.rascalmpl.parser.gtd.location.PositionStore;

public class AtStartOfLineRequirement implements IEnterFilter{
	
	public AtStartOfLineRequirement(){
		super();
	}
	
	public boolean isFiltered(char[] input, int start, PositionStore positionStore){
		return positionStore.startsLine(start);
	}
}
