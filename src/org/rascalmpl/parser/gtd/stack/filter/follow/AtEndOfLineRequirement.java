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
 * A filter that requires the indicated substring to end at the end of a line.
 */
public class AtEndOfLineRequirement implements ICompletionFilter{
	
	public AtEndOfLineRequirement(){
		super();
	}
	
	public boolean isFiltered(int[] input, int start, int end, PositionStore positionStore){
		return !positionStore.endsLine(end);
	}
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter){
		return (otherCompletionFilter instanceof AtEndOfLineRequirement);
	}
}
