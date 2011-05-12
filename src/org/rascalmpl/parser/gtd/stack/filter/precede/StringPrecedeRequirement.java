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
package org.rascalmpl.parser.gtd.stack.filter.precede;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public class StringPrecedeRequirement implements IEnterFilter{
	private final char[] string;
	
	public StringPrecedeRequirement(char[] string){
		super();
		
		this.string = string;
	}
	
	public boolean isFiltered(char[] input, int location, PositionStore positionStore){
		int startLocation = location - string.length;
		if(startLocation < 0) return true;
		
		for(int i = string.length - 1; i >= 0; --i){
			if(input[startLocation + i] != string[i]) return true;
		}
		
		return false;
	}
}
