package org.rascalmpl.parser.gtd.stack.filter;

public class PrecedeRequirement implements IExpansionFilter{
	private final char[] required;
	
	public PrecedeRequirement(char[] required){
		super();
		
		this.required = required;
	}
	
	public boolean isFiltered(char[] input, int location){
		if((location - required.length) >= 0){
			int startLocation = location - required.length;
			for(int i = required.length - 1; i >= 0; --i){
				if(input[startLocation + i] != required[i]) return true;
			}
			return false;
		}
		
		return true;
	}
}
