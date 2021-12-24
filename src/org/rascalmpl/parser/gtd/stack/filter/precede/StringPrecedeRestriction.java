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
package org.rascalmpl.parser.gtd.stack.filter.precede;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

/**
 * A filter that restricts the indicated substring from being preceded by the
 * string associated with this filter.
 */
public class StringPrecedeRestriction implements IEnterFilter{
	private final int[] string;
	
	public StringPrecedeRestriction(int[] string){
		super();
		
		this.string = string;
	}
	
	public boolean isFiltered(int[] input, int start, PositionStore positionStore){
		int startLocation = start - string.length;
		if(startLocation < 0) return false;
		
		for(int i = string.length - 1; i >= 0; --i){
			if(input[startLocation + i] != string[i]) return false;
		}
		
		return true;	
	}
	
	public boolean isEqual(IEnterFilter otherEnterFilter){
		if(!(otherEnterFilter instanceof StringPrecedeRestriction)) return false;
		
		StringPrecedeRestriction otherStringPrecedeFilter = (StringPrecedeRestriction) otherEnterFilter;
		
		int[] otherString = otherStringPrecedeFilter.string;
		if(string.length != otherString.length) return false;
		
		for(int i = string.length - 1; i >= 0; --i){
			if(string[i] != otherString[i]) return false;
		}
		
		return true;
	}
}
