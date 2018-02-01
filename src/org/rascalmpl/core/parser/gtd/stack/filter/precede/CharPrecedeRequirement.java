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
 * A filter that requires the indicated substring to be preceded by one of the
 * characters in the set of ranges.
 */
public class CharPrecedeRequirement implements IEnterFilter{
	private final int[][] ranges;
	
	public CharPrecedeRequirement(int[][] ranges){
		super();
		
		this.ranges = ranges;
	}
	
	public boolean isFiltered(int[] input, int location, PositionStore positionStore){
		if((location - 1) < 0) return true;
			
		int prev = input[location - 1];
		for(int i = ranges.length - 1; i >= 0; --i){
			int[] range = ranges[i];
			if(prev >= range[0] && prev <= range[1]){
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isEqual(IEnterFilter otherEnterFilter){
		if(!(otherEnterFilter instanceof CharPrecedeRequirement)) return false;
		
		CharPrecedeRequirement otherCharPrecedeFilter = (CharPrecedeRequirement) otherEnterFilter;
		
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
