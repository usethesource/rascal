package org.rascalmpl.parser.gtd.stack.filter.precede;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public class MultiCharPrecedeRequirement implements IEnterFilter{
	private final char[][] characters;
	
	public MultiCharPrecedeRequirement(char[][] characters){
		super();

		this.characters = characters;
	}
	
	public boolean isFiltered(char[] input, int start, PositionStore positionStore){
		int startLocation = start - characters.length;
		if(startLocation < 0) return true;
		
		OUTER : for(int i = characters.length - 1; i >= 0; --i){
			char next = input[startLocation + i];
			
			char[] alternatives = characters[i];
			for(int j = alternatives.length - 1; j >= 0; --j){
				if(next == alternatives[j]){
					continue OUTER;
				}
			}
			return true;
		}
		
		return false;
	}
}
