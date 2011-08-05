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
 * A filter that requires the indicated substring to be followed by one of the
 * characters in the set of ranges.
 */
public class CharFollowRequirement implements ICompletionFilter{
	private final char[][] ranges;
	
	public CharFollowRequirement(char[][] ranges){
		super();
		
		this.ranges = ranges;
	}
	
	public boolean isFiltered(char[] input, int start, int end, PositionStore positionStore){
		if(end >= input.length) return true;
		
		char next = input[end];
		for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			if(next >= range[0] && next <= range[1]){
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter){
		if(!(otherCompletionFilter instanceof CharFollowRequirement)) return false;
		
		CharFollowRequirement otherCharFollowFilter = (CharFollowRequirement) otherCompletionFilter;
		
		char[][] otherRanges = otherCharFollowFilter.ranges;
		
		OUTER: for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			for(int j = otherRanges.length - 1; j >= 0; --j){
				char[] otherRange = otherRanges[j];
				if(range[0] == otherRange[0] && range[1] == otherRange[1]) continue OUTER;
			}
			return false; // Could not find a certain range.
		}
		// Found all ranges.
		
		return true;
	}
}
