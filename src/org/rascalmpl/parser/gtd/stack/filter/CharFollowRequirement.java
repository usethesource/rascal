package org.rascalmpl.parser.gtd.stack.filter;

import org.rascalmpl.parser.gtd.util.specific.PositionStore;

public class CharFollowRequirement implements ICompletionFilter{
	private final char[][] ranges;
	
	public CharFollowRequirement(char[][] ranges){
		super();
		
		this.ranges = ranges;
	}
	
	public boolean isFiltered(char[] input, int start, int end, PositionStore positionStore){
		if((end + 1) <= input.length){
			char next = input[end];
			for(int i = ranges.length - 1; i >= 0; --i){
				char[] range = ranges[i];
				if(next >= range[0] && next <= range[1]){
					return false;
				}
			}
		}
		
		return true;
	}
}
