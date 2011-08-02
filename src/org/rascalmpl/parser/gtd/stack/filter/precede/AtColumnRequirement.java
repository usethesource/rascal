package org.rascalmpl.parser.gtd.stack.filter.precede;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

/**
 * A filter that requires the indicated substring to start at a certain column.
 */
public class AtColumnRequirement implements IEnterFilter{
	private final int column;
	
	public AtColumnRequirement(int column){
		super();
		
		this.column = column;
	}
	
	public boolean isFiltered(char[] input, int start, PositionStore positionStore){
		return positionStore.isAtColumn(start, column);
	}
	
	public boolean isEqual(IEnterFilter otherEnterFilter){
		if(!(otherEnterFilter instanceof AtColumnRequirement)) return false;
		
		AtColumnRequirement otherAtColumnFilter = (AtColumnRequirement) otherEnterFilter;
		
		return column != otherAtColumnFilter.column;
	}
}
