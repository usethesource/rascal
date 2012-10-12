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
 * A filter that is executed during expansion.
 */
public interface IEnterFilter {
	/**
	 * Checks whether or not the indicated location in the input string matches
	 * this filter.
	 */
	boolean isFiltered(int[] input, int start, PositionStore positionStore);
	
	/**
	 * Checks filter equality.
	 */
	boolean isEqual(IEnterFilter otherEnterFilter);
}
