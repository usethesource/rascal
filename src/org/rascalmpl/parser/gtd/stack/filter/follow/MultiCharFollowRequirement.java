package org.rascalmpl.parser.gtd.stack.filter.follow;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;

/**
 * A filter that requires the indicated substring to be followed by any of the,
 * with this filter associated, series of characters.
 */
public class MultiCharFollowRequirement implements ICompletionFilter{
	private final char[][] characters;
	
	public MultiCharFollowRequirement(char[][] characters){
		super();

		this.characters = characters;
	}
	
	public boolean isFiltered(char[] input, int start, int end, PositionStore positionStore){
		if((end + characters.length - 1) >= input.length) return true;
		
		OUTER : for(int i = characters.length - 1; i >= 0; --i){
			char next = input[end + i];
			
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
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter){
		if(!(otherCompletionFilter instanceof MultiCharFollowRequirement)) return false;
		
		MultiCharFollowRequirement otherMultiCharFollowFilter = (MultiCharFollowRequirement) otherCompletionFilter;
		
		char[][] otherCharacters = otherMultiCharFollowFilter.characters;
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
