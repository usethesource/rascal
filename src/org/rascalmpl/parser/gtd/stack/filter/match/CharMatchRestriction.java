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
package org.rascalmpl.parser.gtd.stack.filter.match;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;

/**
 * A filter that restricts the indicated part of the input string from matching
 * a specific set of character ranges.
 */
public class CharMatchRestriction implements ICompletionFilter{
	private final int[][] ranges;
	
	public CharMatchRestriction(int[][] ranges){
		super();
		
		this.ranges = ranges;
	}
	
	public boolean isFiltered(int[] input, int start, int end, PositionStore positionStore){
		if((end - start) != 1) return false;
		
		int character = input[start];
		for(int i = ranges.length - 1; i >= 0; --i){
			int[] range = ranges[i];
			if(character >= range[0] && character <= range[1]){
				return true;
			}
		}
		
		return true;
	}
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter){
		if(!(otherCompletionFilter instanceof CharMatchRestriction)) return false;
		
		CharMatchRestriction otherCharMatchFilter = (CharMatchRestriction) otherCompletionFilter;
		
		int[][] otherRanges = otherCharMatchFilter.ranges;
		
		OUTER: for(int i = ranges.length - 1; i >= 0; --i){
			int[] range = ranges[i];
			for(int j = otherRanges.length - 1; j >= 0; --j){
				int[] otherRange = otherRanges[j];
				if(range[0] == otherRange[0] && range[1] == otherRange[1]) continue OUTER;
			}
			return false; // Could not find a certain range.
		}
		// Found all ranges.
		
		return true;
	}
}
