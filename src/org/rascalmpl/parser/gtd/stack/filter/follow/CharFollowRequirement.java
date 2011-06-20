package org.rascalmpl.parser.gtd.stack.filter.follow;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;

public class CharFollowRequirement implements ICompletionFilter{
	private final char[][] ranges;
	
	public CharFollowRequirement(char[][] ranges){
		super();
		
		this.ranges = ranges;
	}
	
	public boolean isFiltered(char[] input, int start, int end, PositionStore positionStore){
		if(end >= input.length) return true;
		
		char next = input[end];
		for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			if(next >= range[0] && next <= range[1]){
				return false;
			}
		}
		
		return true;
	}
}
