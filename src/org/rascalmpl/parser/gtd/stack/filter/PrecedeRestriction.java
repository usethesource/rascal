package org.rascalmpl.parser.gtd.stack.filter;

public class PrecedeRestriction implements IExpansionFilter{
	private final char[] restricted;
	
	public PrecedeRestriction(char[] restricted){
		super();
		
		this.restricted = restricted;
	}
	
	public boolean isFiltered(char[] input, int location){
		if((location - restricted.length) >= 0){
			int startLocation = location - restricted.length;
			for(int i = restricted.length - 1; i >= 0; --i){
				if(input[startLocation + i] != restricted[i]) return false;
			}
			return true;
		}
		
		return false;
	}
}
