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
package org.rascalmpl.parser.gtd.stack.filter.precede;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

/**
 * A filter that restricts the indicated substring from being preceded by one
 * of the characters in the set of ranges.
 */
public class CharPrecedeRestriction implements IEnterFilter{
	private final int[][] ranges;
	
	public CharPrecedeRestriction(int[][] ranges){
		super();
		
		this.ranges = ranges;
	}
	
	public boolean isFiltered(int[] input, int location, PositionStore positionStore){
		if((location - 1) < 0) return false;
		
		int prev = input[location - 1];
		for(int i = ranges.length - 1; i >= 0; --i){
			int[] range = ranges[i];
			if(prev >= range[0] && prev <= range[1]){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isEqual(IEnterFilter otherEnterFilter){
		if(!(otherEnterFilter instanceof CharPrecedeRestriction)) return false;
		
		CharPrecedeRestriction otherCharPrecedeFilter = (CharPrecedeRestriction) otherEnterFilter;
		
		int[][] otherRanges = otherCharPrecedeFilter.ranges;
		
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
