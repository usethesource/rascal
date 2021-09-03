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

import java.util.Arrays;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;

/**
 * A filter that requires the indicated substring to be followed by the string
 * associated with this filter. the string is case-insensitive
 */
public class CaseInsensitiveStringFollowRequirement implements ICompletionFilter{
	private final int[][] string;
	
	public CaseInsensitiveStringFollowRequirement(int[][] string){
		super();
		
		this.string = string;
		assert Arrays.stream(string).noneMatch(letter -> letter.length != 2);
	}
	
	public boolean isFiltered(int[] input, int start, int end, PositionStore positionStore){
		if ((end + string.length - 1) >= input.length) {
			return true;
		}
		
		for (int i = string.length - 1; i >= 0; --i){
			if (input[end + i] != string[i][0]
			  && input[end + i] != string[i][1]) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter){
		if(!(otherCompletionFilter instanceof CaseInsensitiveStringFollowRequirement)) {
			return false;
		}
		
		CaseInsensitiveStringFollowRequirement otherStringFollowFilter = (CaseInsensitiveStringFollowRequirement) otherCompletionFilter;
		
		int[][] otherString = otherStringFollowFilter.string;
		if(string.length != otherString.length) {
			return false;
		}
		
		for(int i = string.length - 1; i >= 0; --i){
			if(string[i][0] != otherString[i][0]
			|| string[i][1] != otherString[i][1]) {
				return false;
			}
		}
		
		return true;
	}
}
