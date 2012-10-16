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
package org.rascalmpl.parser.gtd.stack.filter;

import org.rascalmpl.parser.gtd.location.PositionStore;

/**
 * A filter that is executed before reduction.
 */
public interface ICompletionFilter{
	/**
	 * Checks whether or not the indicated position in the input string matches
	 * this filter.
	 * 
	 * Caveat: it is tempting to add a parameter to refer to a stack node or a tree node here
	 * for inspection by the filter, but due to heavy sharing in both stacks and forests you woul
	 * almost certainly introduce bugs if you implement a feature based on such information.
	 */
	boolean isFiltered(int[] input, int start, int end, PositionStore positionStore);
	
	/**
	 * Checks filter equality.
	 */
	boolean isEqual(ICompletionFilter otherCompletionFilter);
}
