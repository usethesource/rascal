package org.rascalmpl.parser.gtd.stack.filter;

public class MatchRestriction implements IReductionFilter{
	private final char[] restricted;
	
	public MatchRestriction(char[] restricted){
		super();
		
		this.restricted = restricted;
	}
	
	public boolean isFiltered(char[] input, int start, int end){
		if((end - start) != restricted.length) return false;
		
		for(int i = restricted.length - 1; i >= 0; --i){
			if(input[start + i] != restricted[i]) return false;
		}
		
		return true;
	}
}
