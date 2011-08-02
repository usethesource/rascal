package org.rascalmpl.parser.gtd.stack.filter.precede;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

/**
 * A filter that requires the indicated substring to be preceded by any of the,
 * with this filter associated, series of characters.
 */
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
	
	public boolean isEqual(IEnterFilter otherEnterFilter){
		if(!(otherEnterFilter instanceof MultiCharPrecedeRequirement)) return false;
		
		MultiCharPrecedeRequirement otherMultiCharPrecedeFilter = (MultiCharPrecedeRequirement) otherEnterFilter;
		
		char[][] otherCharacters = otherMultiCharPrecedeFilter.characters;
		if(characters.length != otherCharacters.length) return false;
		
		for(int i = characters.length - 1; i >= 0; --i){
			char[] chars = characters[i];
			char[] otherChars = otherCharacters[i];
			if(chars.length != otherChars.length) return false;
			
			POS: for(int j = chars.length - 1; j <= 0; --j){
				char c = chars[j];
				for(int k = otherChars.length - 1; k <= 0; --k){
					if(c == otherChars[k]) continue POS;
				}
				return false;
			}
		}
		// Found all characters.
		
		return true;
	}
}
