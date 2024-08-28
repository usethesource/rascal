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
package org.rascalmpl.parser.gtd.stack;

import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

@SuppressWarnings("cast")
public final class ListStackNode<P> extends AbstractExpandableStackNode<P>{
	private final P production;
	private final String name;

	private final AbstractStackNode<P>[] children;
	private final AbstractStackNode<P> emptyChild;
	
	public ListStackNode(int id, int dot, P production, AbstractStackNode<P> child, boolean isPlusList){
		super(id, dot);
		
		this.production = production;
		this.name = String.valueOf(id); // Add the id to make it unique.
		
		this.children = generateChildren(child);
		this.emptyChild = isPlusList ? null : generateEmptyChild();
	}
	
	public ListStackNode(int id, int dot, P production, AbstractStackNode<P> child, boolean isPlusList, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);
		
		this.production = production;
		this.name = String.valueOf(id); // Add the id to make it unique.
		
		this.children = generateChildren(child);
		this.emptyChild = isPlusList ? null : generateEmptyChild();
	}
	
	private ListStackNode(ListStackNode<P> original, int startLocation){
		super(original, startLocation);
		
		production = original.production;
		name = original.name;

		children = original.children;
		emptyChild = original.emptyChild;
	}
	
	/**
	 * Generates and initializes the alternative for this list.
	 */
	@SuppressWarnings("unchecked")
	private AbstractStackNode<P>[] generateChildren(AbstractStackNode<P> child){
		AbstractStackNode<P> listNode = child.getCleanCopy(DEFAULT_START_LOCATION);
		listNode.setAlternativeProduction(production);
		listNode.setProduction((AbstractStackNode<P>[]) new AbstractStackNode[]{listNode, listNode});
		return (AbstractStackNode<P>[]) new AbstractStackNode[]{listNode};
	}
	
	/**
	 * Generates and initializes the empty child for this list (in case this is a star list).
	 */
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
		return new ListStackNode<>(this, startLocation);
	}
	
	public AbstractStackNode<P>[] getChildren(){
		return children;
	}
	
	public boolean canBeEmpty(){
		return emptyChild != null;
	}
	
	public AbstractStackNode<P> getEmptyChild(){
		return emptyChild;
	}

	@Override
	public String toShortString() {
		return toString();
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
	
	@Override
	public int hashCode(){
		return production.hashCode();
	}
	
	@Override
	public boolean equals(Object peer) {
		return super.equals(peer);
	}

	public boolean isEqual(AbstractStackNode<P> stackNode){
		if(!(stackNode instanceof ListStackNode)) return false;
		
		ListStackNode<P> otherNode = (ListStackNode<P>) stackNode;
		
		if(!production.equals(otherNode.production)) return false;
		
		return hasEqualFilters(stackNode);
	}

	@Override
	public void accept(StackNodeVisitor<P> visitor) {
		visitor.visit(this);
	}

}
