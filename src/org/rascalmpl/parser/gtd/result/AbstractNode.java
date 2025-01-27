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
package org.rascalmpl.parser.gtd.result;

import io.usethesource.vallang.IConstructor;

/**
 * All nodes in the resulting tree are a subtype of this class.
 */
public abstract class AbstractNode{
	// Parse forest memoization during flattening
	// Note that the type should technically be a type parameter, but this
	// would entail more code changes than we are willing to make at this point.
	private IConstructor tree;

	public AbstractNode(){
		super();
	}
	
	/**
	 * Returns a unique identifier, indicating the type of result node.
	 */
	public abstract int getTypeIdentifier();
	
	/**
	 * Checks whether or not this node is an epsilon node.
	 */
	public boolean isEpsilon(){
		return false;
	}
	
	/**
	 * Check whether or not this node is empty (where empty means, that it is
	 * associated with a zero length location in the input string).
	 */
	public abstract boolean isEmpty();
	
	/**
	 * Checks whether or not this node represents a non-terminal separator.
	 */
	public abstract boolean isNonterminalSeparator();

	public <T> void setTree(T tree) {
		this.tree = (IConstructor) tree;
	}

	@SuppressWarnings("unchecked")
	public  <T> T getTree() {
		return (T) tree;
	}
}
