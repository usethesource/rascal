package org.rascalmpl.parser.gtd.stack.filter;

import org.rascalmpl.parser.gtd.location.PositionStore;

public class CharFollowRestriction implements ICompletionFilter{
	private final char[][] ranges;
	
	public CharFollowRestriction(char[][] ranges){
		super();
		
		this.ranges = ranges;
	}
	
	public boolean isFiltered(char[] input, int start, int end, PositionStore positionStore){
		if((end + 1) <= input.length){
			char next = input[end];
			for(int i = ranges.length - 1; i >= 0; --i){
				char[] range = ranges[i];
				if(next >= range[0] && next <= range[1]){
					return true;
				}
			}
		}
		
		return false;
	}
}
