package org.rascalmpl.parser.gtd.experiments.grammar.symbol;

import org.rascalmpl.parser.gtd.experiments.grammar.State;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.CharStackNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public class CharSymbol extends Symbol{
	private char[][] ranges;
	
	public CharSymbol(char[][] ranges){
		this(ranges, null, null);
	}
	
	public CharSymbol(char[][] ranges, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(enterFilters, completionFilters);
		
		this.ranges = ranges;
	}
	
	public AbstractStackNode buildNode(State state, int dot){
		return new CharStackNode(state.getNextId(this), dot, ranges, enterFilters, completionFilters);
	}
	
	public boolean isEqual(Symbol symbol){
		if(!(symbol instanceof CharSymbol)) return false;
		
		CharSymbol otherSymbol = (CharSymbol) symbol;
		
		char[][] otherRanges = otherSymbol.ranges;
		if(ranges.length != otherRanges.length) return false;
		
		OUTER: for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			for(int j = otherRanges.length - 1; j >= 0; --j){
				char[] otherRange = otherRanges[j];
				if(range[0] == otherRange[0] && range[1] == otherRange[1]) continue OUTER;
			}
			return false; // Could not find a certain range.
		}
		// Found all ranges.
		
		return hasEqualFilters(otherSymbol);
	}
}
