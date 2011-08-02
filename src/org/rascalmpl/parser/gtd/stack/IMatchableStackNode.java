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
package org.rascalmpl.parser.gtd.stack;

import org.rascalmpl.parser.gtd.result.AbstractNode;

/**
 * Indicates that the stack node is matchable.
 * Literals and characters are examples of matchable nodes.
 */
public interface IMatchableStackNode{
	/**
	 * Matches the node to the input string and the indicated location and
	 * constructs the result in case the match was successful. Null will
	 * be returned otherwise. 
	 */
	AbstractNode match(char[] input, int location);
	
	/**
	 * Returns the length (in number of characters) of the matchable.
	 */
	int getLength();
}
