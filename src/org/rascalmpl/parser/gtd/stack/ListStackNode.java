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

import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public final class ListStackNode extends AbstractExpandableStackNode{
	private final Object production;
	private final String name;

	private final AbstractStackNode[] children;
	private final AbstractStackNode emptyChild;
	
	public ListStackNode(int id, int dot, Object production, AbstractStackNode child, boolean isPlusList){
		super(id, dot);
		
		this.production = production;
		this.name = String.valueOf(id); // Add the id to make it unique.
		
		this.children = generateChildren(child);
		this.emptyChild = isPlusList ? null : generateEmptyChild();
	}
	
	public ListStackNode(int id, int dot, Object production, AbstractStackNode child, boolean isPlusList, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);
		
		this.production = production;
		this.name = String.valueOf(id); // Add the id to make it unique.
		
		this.children = generateChildren(child);
		this.emptyChild = isPlusList ? null : generateEmptyChild();
	}
	
	private ListStackNode(ListStackNode original, int startLocation){
		super(original, startLocation);
		
		production = original.production;
		name = original.name;

		children = original.children;
		emptyChild = original.emptyChild;
	}
	
	/**
	 * Generates and initializes the alternative for this list.
	 */
	private AbstractStackNode[] generateChildren(AbstractStackNode child){
		AbstractStackNode listNode = child.getCleanCopy(DEFAULT_START_LOCATION);
		listNode.setAlternativeProduction(production);
		listNode.setProduction(new AbstractStackNode[]{listNode, listNode});
		return new AbstractStackNode[]{listNode};
	}
	
	/**
	 * Generates and initializes the empty child for this list (in case this is a star list).
	 */
	private AbstractStackNode generateEmptyChild(){
		AbstractStackNode empty = EMPTY.getCleanCopy(DEFAULT_START_LOCATION);
		empty.setAlternativeProduction(production);
		return empty;
	}
	
	public String getName(){
		return name;
	}
	
	public AbstractStackNode getCleanCopy(int startLocation){
		return new ListStackNode(this, startLocation);
	}
	
	public AbstractStackNode[] getChildren(){
		return children;
	}
	
	public boolean canBeEmpty(){
		return emptyChild != null;
	}
	
	public AbstractStackNode getEmptyChild(){
		return emptyChild;
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
		return production.hashCode();
	}
	
	public boolean isEqual(AbstractStackNode stackNode){
		if(!(stackNode instanceof ListStackNode)) return false;
		
		ListStackNode otherNode = (ListStackNode) stackNode;
		
		if(!production.equals(otherNode.production)) return false;
		
		return hasEqualFilters(stackNode);
	}
}
