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
 * A filter that requires the indicated substring to be followed by the string
 * associated with this filter.
 */
public class StringFollowRequirement implements ICompletionFilter{
	private final int[] string;
	
	public StringFollowRequirement(int[] string){
		super();
		
		this.string = string;
	}
	
	public boolean isFiltered(int[] input, int start, int end, PositionStore positionStore){
		if((end + string.length - 1) >= input.length) {
			return true;
		}
		
		for(int i = string.length - 1; i >= 0; --i){
			if(input[end + i] != string[i]) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter){
		if(!(otherCompletionFilter instanceof StringFollowRequirement)) {
			return false;
		}
		
		StringFollowRequirement otherStringFollowFilter = (StringFollowRequirement) otherCompletionFilter;
		
		int[] otherString = otherStringFollowFilter.string;
		if(string.length != otherString.length) {
			return false;
		}
		
		for(int i = string.length - 1; i >= 0; --i){
			if(string[i] != otherString[i]) {
				return false;
			}
		}
		
		return true;
	}
}
