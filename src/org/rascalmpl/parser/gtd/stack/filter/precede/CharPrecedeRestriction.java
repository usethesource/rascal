package org.rascalmpl.parser.gtd.stack.filter.precede;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public class CharPrecedeRestriction implements IEnterFilter{
	private final char[][] ranges;
	
	public CharPrecedeRestriction(char[][] ranges){
		super();
		
		this.ranges = ranges;
	}
	
	public boolean isFiltered(char[] input, int location, PositionStore positionStore){
		if((location - 1) < 0) return false;
		
		char prev = input[location - 1];
		for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			if(prev >= range[0] && prev <= range[1]){
				return true;
			}
		}
		
		return false;
	}
}
