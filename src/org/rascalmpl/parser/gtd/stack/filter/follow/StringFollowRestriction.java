/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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
 * A filter that restricts the indicated substring from being followed by the
 * string associated with this filter.
 */
public class StringFollowRestriction implements ICompletionFilter{
	private final int[] string;
	
	public StringFollowRestriction(int[] string){
		super();
		
		this.string = string;
	}
	
	public boolean isFiltered(int[] input, int start, int end, PositionStore positionStore){
		if((end + string.length - 1) >= input.length) return false;
			
		for(int i = string.length - 1; i >= 0; --i){
			if(input[end + i] != string[i]) return false;
		}
		
		return true;
	}
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter){
		if(!(otherCompletionFilter instanceof StringFollowRestriction)) return false;
		
		StringFollowRestriction otherStringFollowFilter = (StringFollowRestriction) otherCompletionFilter;
		
		int[] otherString = otherStringFollowFilter.string;
		if(string.length != otherString.length) return false;
		
		for(int i = string.length - 1; i >= 0; --i){
			if(string[i] != otherString[i]) return false;
		}
		
		return true;
	}
}
