package org.rascalmpl.parser.gtd.stack.filter.follow;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;

public class CharFollowRestriction implements ICompletionFilter{
	private final char[][] ranges;
	
	public CharFollowRestriction(char[][] ranges){
		super();
		
		this.ranges = ranges;
	}
	
	public boolean isFiltered(char[] input, int start, int end, PositionStore positionStore){
		if(end >= input.length) return false;
		
		char next = input[end];
		for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			if(next >= range[0] && next <= range[1]){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter){
		if(!(otherCompletionFilter instanceof CharFollowRestriction)) return false;
		
		CharFollowRestriction otherCharFollowFilter = (CharFollowRestriction) otherCompletionFilter;
		
		char[][] otherRanges = otherCharFollowFilter.ranges;
		
		OUTER: for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			for(int j = otherRanges.length - 1; j >= 0; --j){
				char[] otherRange = otherRanges[j];
				if(range[0] == otherRange[0] && range[1] == otherRange[1]) continue OUTER;
			}
			return false; // Could not find a certain range.
		}
		// Found all ranges.
		
		return true;
	}
}
