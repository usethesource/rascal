package org.rascalmpl.parser.gtd.stack.filter.precede;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

/**
 * A filter that requires the indicated substring to start at the beginning of
 * a line.
 */
public class AtStartOfLineRequirement implements IEnterFilter{
	
	public AtStartOfLineRequirement(){
		super();
	}
	
	public boolean isFiltered(char[] input, int start, PositionStore positionStore){
		return positionStore.startsLine(start);
	}
	
	public boolean isEqual(IEnterFilter otherEnterFilter){
		return (otherEnterFilter instanceof AtStartOfLineRequirement);
	}
}
