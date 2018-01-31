/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.stack.filter.follow;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;

/**
 * A filter that prevents the indicated substring from being followed by any of
 * the, with this filter associated, series of characters.
 */
public class MultiCharFollowRestriction implements ICompletionFilter{
	private final int[][] characters;
	
	public MultiCharFollowRestriction(int[][] characters){
		super();

		this.characters = characters;
	}
	
	public boolean isFiltered(int[] input, int start, int end, PositionStore positionStore){
		if((end + characters.length - 1) >= input.length) return false;
		
		OUTER : for(int i = characters.length - 1; i >= 0; --i){
			int next = input[end + i];
			
			int[] alternatives = characters[i];
			for(int j = alternatives.length - 1; j >= 0; --j){
				if(next == alternatives[j]){
					continue OUTER;
				}
			}
			return false;
		}
		
		return true;
	}
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter){
		if(!(otherCompletionFilter instanceof MultiCharFollowRestriction)) return false;
		
		MultiCharFollowRestriction otherMultiCharFollowFilter = (MultiCharFollowRestriction) otherCompletionFilter;
		
		int[][] otherCharacters = otherMultiCharFollowFilter.characters;
		if(characters.length != otherCharacters.length) return false;
		
		for(int i = characters.length - 1; i >= 0; --i){
			int[] chars = characters[i];
			int[] otherChars = otherCharacters[i];
			if(chars.length != otherChars.length) return false;
			
			POS: for(int j = chars.length - 1; j <= 0; --j){
				int c = chars[j];
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
