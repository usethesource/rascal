/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.stack;

import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

/**
 * Empty is different from epsilon; it is the () symbol which is one of the 'regular' expressions.
 * it produces a tree!
 */
public final class EmptyStackNode<P> extends AbstractExpandableStackNode<P>{
	private final P production;
	private final String name;

	private final AbstractStackNode<P> emptyChild;
	private static final AbstractStackNode<?>[] children = new AbstractStackNode[0];
	
	public EmptyStackNode(int id, int dot, P production){
		super(id, dot);
		
		this.production = production;
		this.name = "empty"+id; // Add the id to make it unique.
		
		this.emptyChild = generateEmptyChild();
	}
	
	public EmptyStackNode(int id, int dot, P production, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters) {
		super(id, dot, enterFilters, completionFilters);
		
		this.production = production;
		this.name = "empty"+id;
		this.emptyChild = generateEmptyChild(); 
	}
	
	private EmptyStackNode(EmptyStackNode<P> original, int startLocation){
		super(original, startLocation);
		
		production = original.production;
		name = original.name;

		emptyChild = original.emptyChild;
	}
	
	@SuppressWarnings("unchecked")
	private AbstractStackNode<P> generateEmptyChild(){
		AbstractStackNode<P> empty = (AbstractStackNode<P>) EMPTY.getCleanCopy(DEFAULT_START_LOCATION);
		empty.setAlternativeProduction(production);
		return empty;
	}
	
	public String getName(){
		return name;
	}
	
	public AbstractStackNode<P> getCleanCopy(int startLocation){
		return new EmptyStackNode<P>(this, startLocation);
	}
	
	@SuppressWarnings("unchecked")
	public AbstractStackNode<P>[] getChildren(){
		return (AbstractStackNode<P>[]) children;
	}
	
	public boolean canBeEmpty(){
		return true;
	}
	
	public AbstractStackNode<P> getEmptyChild(){
		return emptyChild;
	}

	public String toShortString() {
		return toString();
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
	
	public int hashCode(){
		return 1;
	}
	
	public boolean isEqual(AbstractStackNode<P> stackNode){
		if(!(stackNode instanceof EmptyStackNode)) return false;
		
		return hasEqualFilters(stackNode);
	}
}
