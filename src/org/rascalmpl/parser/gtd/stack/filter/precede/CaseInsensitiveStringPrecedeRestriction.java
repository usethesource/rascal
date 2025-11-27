/*******************************************************************************
 * Copyright (c) 2009-2021 NWO-I CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Jurgen Vinju - Jurgen.Vinju@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.stack.filter.precede;

import java.util.Arrays;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

/**
 * A filter that restricts the indicated substring from being preceded by the
 * string associated with this filter, with a case-insensitive match.
 */
public class CaseInsensitiveStringPrecedeRestriction implements IEnterFilter{
	private final int[][] string;
	
	public CaseInsensitiveStringPrecedeRestriction(int[][] string){
		super();
		
		this.string = string;
		assert Arrays.stream(string).noneMatch(letter -> letter.length != 2);
	}
	
	public boolean isFiltered(int[] input, int start, PositionStore positionStore){
		int startLocation = start - string.length;
		if (startLocation < 0) {
			return false;
		}
		
		for (int i = string.length - 1; i >= 0; --i) {
			if (input[startLocation + i] != string[i][0] && input[startLocation + i] != string[i][1]) {
				return false;
			}
		}
		
		return true;	
	}
	
	public boolean isEqual(IEnterFilter otherEnterFilter){
		if(!(otherEnterFilter instanceof StringPrecedeRestriction)) return false;
		
		CaseInsensitiveStringPrecedeRestriction otherStringPrecedeFilter = (CaseInsensitiveStringPrecedeRestriction) otherEnterFilter;
		
		int[][] otherString = otherStringPrecedeFilter.string;
		if (string.length != otherString.length) {
			return false;
		}
		
		for (int i = string.length - 1; i >= 0; --i) {
			if (string[i][0] != otherString[i][0] || string[i][1] != otherString[i][1]) {
				return false;
			}
		}
		
		return true;
	}
}
