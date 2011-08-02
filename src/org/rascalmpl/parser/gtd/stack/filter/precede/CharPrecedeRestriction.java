package org.rascalmpl.parser.gtd.stack.filter.precede;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

/**
 * A filter that restricts the indicated substring from being preceded by one
 * of the characters in the set of ranges.
 */
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
	
	public boolean isEqual(IEnterFilter otherEnterFilter){
		if(!(otherEnterFilter instanceof CharPrecedeRestriction)) return false;
		
		CharPrecedeRestriction otherCharPrecedeFilter = (CharPrecedeRestriction) otherEnterFilter;
		
		char[][] otherRanges = otherCharPrecedeFilter.ranges;
		
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
