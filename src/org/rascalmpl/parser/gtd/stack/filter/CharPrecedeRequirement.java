package org.rascalmpl.parser.gtd.stack.filter;

import org.rascalmpl.parser.gtd.util.specific.PositionStore;

public class CharPrecedeRequirement implements IEnterFilter{
	private final char[][] ranges;
	
	public CharPrecedeRequirement(char[][] ranges){
		super();
		
		this.ranges = ranges;
	}
	
	public boolean isFiltered(char[] input, int location, PositionStore positionStore){
		if((location - 1) >= 0){
			char prev = input[location - 1];
			for(int i = ranges.length - 1; i >= 0; --i){
				char[] range = ranges[i];
				if(prev >= range[0] && prev <= range[1]){
					return false;
				}
			}
		}
		
		return true;
	}
}
