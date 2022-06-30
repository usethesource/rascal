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
package org.rascalmpl.parser.gtd.stack.filter.match;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;

/**
 * A filter that restricts the indicated part of the input string from matching
 * a specific string.
 */
public class StringMatchRestriction implements ICompletionFilter{
	private final int[] string;
	
	public StringMatchRestriction(int[] string){
		super();
		
		this.string = string;
	}
	
	public boolean isFiltered(int[] input, int start, int end, PositionStore positionStore){
		if ((end - start) != string.length) {
			return false;
		}
		
		for (int i = string.length - 1; i >= 0; --i) {
			if (input[start + i] != string[i]) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter){
		if(!(otherCompletionFilter instanceof StringMatchRestriction)) return false;
		
		StringMatchRestriction otherStringMatchFilter = (StringMatchRestriction) otherCompletionFilter;
		
		int[] otherString = otherStringMatchFilter.string;
		if(string.length != otherString.length) return false;
		
		for(int i = string.length - 1; i >= 0; --i){
			if(string[i] != otherString[i]) return false;
		}
		
		return true;
	}
}
