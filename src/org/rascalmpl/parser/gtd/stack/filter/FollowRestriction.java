package org.rascalmpl.parser.gtd.stack.filter;

public class FollowRestriction implements IReductionFilter{
	private final char[] restricted;
	
	public FollowRestriction(char[] restricted){
		super();
		
		this.restricted = restricted;
	}
	
	public boolean isFiltered(char[] input, int start, int end){
		if((end + restricted.length) <= input.length){
			for(int i = restricted.length - 1; i >= 0; --i){
				if(input[end + i] != restricted[i]) return false;
			}
			return true;
		}
		
		return false;
	}
}
