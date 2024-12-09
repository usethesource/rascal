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
 * A filter that requires the indicated substring to start at a certain column.
 */
public class AtColumnRequirement implements IEnterFilter{
	private final int column;
	
	public AtColumnRequirement(int column){
		super();
		
		this.column = column;
	}
	
	public boolean isFiltered(int[] input, int start, PositionStore positionStore){
		return !positionStore.isAtColumn(start, column);
	}
	
	public boolean isEqual(IEnterFilter otherEnterFilter){
		if(!(otherEnterFilter instanceof AtColumnRequirement)) return false;
		
		AtColumnRequirement otherAtColumnFilter = (AtColumnRequirement) otherEnterFilter;
		
		return column != otherAtColumnFilter.column;
	}
}
