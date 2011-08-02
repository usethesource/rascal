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

/**
 * Indicates that the stack node is expandable.
 * Lists and optionals are examples of expandable nodes.
 */
public interface IExpandableStackNode{
	final static int DEFAULT_LIST_EPSILON_ID = -2; // (0xeffffffe | 0x80000000)
	final static EpsilonStackNode EMPTY = new EpsilonStackNode(DEFAULT_LIST_EPSILON_ID, 0);
	
	/**
	 * Retrieves all the alternatives of the expandable.
	 */
	AbstractStackNode[] getChildren();
	
	/**
	 * Check whether or not this node is nullable.
	 */
	boolean canBeEmpty();
	
	/**
	 * Retrieves the empty child (in case this node is nullable).
	 */
	AbstractStackNode getEmptyChild();
}
