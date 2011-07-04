package org.rascalmpl.parser.gtd.stack.filter.follow;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;

public class MultiCharFollowRestriction implements ICompletionFilter{
	private final char[][] characters;
	
	public MultiCharFollowRestriction(char[][] characters){
		super();

		this.characters = characters;
	}
	
	public boolean isFiltered(char[] input, int start, int end, PositionStore positionStore){
		if((end + characters.length - 1) >= input.length) return false;
		
		OUTER : for(int i = characters.length - 1; i >= 0; --i){
			char next = input[end + i];
			
			char[] alternatives = characters[i];
			for(int j = alternatives.length - 1; j >= 0; --j){
				if(next == alternatives[j]){
					continue OUTER;
				}
			}
			return false;
		}
		
		return true;
	}
}
