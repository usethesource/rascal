package org.rascalmpl.parser.gtd.stack.filter;

import org.rascalmpl.parser.gtd.util.specific.PositionStore;

public class AtColumnRequirement implements IEnterFilter{
	private final int column;
	
	public AtColumnRequirement(int column){
		super();
		
		this.column = column;
	}
	
	public boolean isFiltered(char[] input, int start, PositionStore positionStore){
		return positionStore.isAtColumn(start, column);
	}
}
