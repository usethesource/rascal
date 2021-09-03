/*******************************************************************************
 * Copyright (c) 2009-2021 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Jurgen Vinju - Jurgen.Vinju@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.stack.filter.match;

import java.util.Arrays;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;

/**
 * A filter that restricts the indicated part of the input string from matching
 * a specific string, but ignoring cases (uppercase/lowercase)
 */
public class CaseInsensitiveStringMatchRestriction implements ICompletionFilter{
	// for example the Rascal literal 'AB' would be reduced to [Aa][Bb] by the normalizer
	// and then encoded as { {'A','a'}, {'B','b' }} by the parser generator (in Unicode codepoints).
	private final int[][] string; 
	
	public CaseInsensitiveStringMatchRestriction(int[][] chars){
		super();
		this.string = chars;
		assert Arrays.stream(chars).noneMatch(letter -> letter.length != 2);
	}
	
	public boolean isFiltered(int[] input, int start, int end, PositionStore positionStore){
		if ((end - start) != string.length) {
			return false;
		}
		
		for(int i = string.length - 1; i >= 0; --i){
			if (input[start + i] != string[i][0]
			 && input[start + i] != string[i][1]) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter){
		if(!(otherCompletionFilter instanceof CaseInsensitiveStringMatchRestriction)) return false;
		
		CaseInsensitiveStringMatchRestriction otherStringMatchFilter = (CaseInsensitiveStringMatchRestriction) otherCompletionFilter;
		
		int[][] otherString = otherStringMatchFilter.string;
		if (string.length != otherString.length) {
			return false;
		}
		
		for(int i = string.length - 1; i >= 0; --i){
			if(string[i][0] != otherString[i][0]
			|| string[i][1] != otherString[i][1]) {
				return false;
		}
		
		return true;
	}
}
