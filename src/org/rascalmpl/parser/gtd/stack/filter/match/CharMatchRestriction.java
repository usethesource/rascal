package org.rascalmpl.parser.gtd.stack.filter.match;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;

public class CharMatchRestriction implements ICompletionFilter{
	private final char[][] ranges;
	
	public CharMatchRestriction(char[][] ranges){
		super();
		
		this.ranges = ranges;
	}
	
	public boolean isFiltered(char[] input, int start, int end, PositionStore positionStore){
		if((end - start) != 1) return false;
		
		char character = input[start];
		for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			if(character >= range[0] && character <= range[1]){
				return true;
			}
		}
		
		return true;
	}
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter){
		if(!(otherCompletionFilter instanceof CharMatchRestriction)) return false;
		
		CharMatchRestriction otherCharMatchFilter = (CharMatchRestriction) otherCompletionFilter;
		
		char[][] otherRanges = otherCharMatchFilter.ranges;
		
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
