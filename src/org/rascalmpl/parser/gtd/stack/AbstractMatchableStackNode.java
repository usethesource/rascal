/*******************************************************************************
 * Copyright (c) 2011-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Anya Helene Bagge - anya@ii.uib.no
*******************************************************************************/
package org.rascalmpl.parser.gtd.stack;

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

/**
 * Indicates that the stack node is matchable.
 * Literals and characters are examples of matchable nodes.
 */
public abstract class AbstractMatchableStackNode<P> extends AbstractStackNode<P>{
	
	protected AbstractMatchableStackNode(int id, int dot){
		super(id, dot);
	}

	protected AbstractMatchableStackNode(int id, int dot, int startLocation){
		super(id, dot, startLocation);
	}

	protected AbstractMatchableStackNode(int id, int dot, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);
	}
	
	protected AbstractMatchableStackNode(AbstractMatchableStackNode<P> original, int startLocation){
		super(original, startLocation);
	}
	
	/**
	 * Matches the node to the input string and the indicated location and
	 * constructs the result in case the match was successful. Null will
	 * be returned otherwise. 
	 */
	public abstract AbstractNode match(int[] input, int location);
	
	/**
	 * Returns the length (in number of characters) of the matchable.
	 */
	public abstract int getLength();
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode<P>[] getChildren(){
		throw new UnsupportedOperationException();
	}
	
	public boolean canBeEmpty(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode<P> getEmptyChild(){
		throw new UnsupportedOperationException();
	}
	
	public final boolean isMatchable(){
		return true;
	}

}
