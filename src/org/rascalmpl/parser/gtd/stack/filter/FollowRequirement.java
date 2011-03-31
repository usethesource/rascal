package org.rascalmpl.parser.gtd.stack.filter;

public class FollowRequirement implements IReductionFilter{
	private final char[] required;
	
	public FollowRequirement(char[] required){
		super();
		
		this.required = required;
	}
	
	public boolean isFiltered(char[] input, int start, int end){
		if((end + required.length) <= input.length){
			for(int i = required.length - 1; i >= 0; --i){
				if(input[end + i] != required[i]) return true;
			}
			return false;
		}
		
		return true;
	}
}
