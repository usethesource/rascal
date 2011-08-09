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

public final class SeparatedListStackNode extends AbstractExpandableStackNode{
	private final Object production;
	private final String name;

	private final AbstractStackNode[] children;
	private final AbstractStackNode emptyChild;
	
	public SeparatedListStackNode(int id, int dot, Object production, AbstractStackNode child, AbstractStackNode[] separators, boolean isPlusList){
		super(id, dot);
		
		this.production = production;
		this.name = String.valueOf(id); // Add the id to make it unique.
		
		this.children = generateChildren(child, separators);
		this.emptyChild = isPlusList ? null : generateEmptyChild();
	}
	

	public SeparatedListStackNode(int id, int dot, Object production, AbstractStackNode child, AbstractStackNode[] separators, boolean isPlusList, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);
		
		this.production = production;
		this.name = String.valueOf(id); // Add the id to make it unique.
		
		this.children = generateChildren(child, separators);
		this.emptyChild = isPlusList ? null : generateEmptyChild();
	}
	
	private SeparatedListStackNode(SeparatedListStackNode original, int startLocation){
		super(original, startLocation);
		
		production = original.production;
		name = original.name;
		
		children = original.children;
		emptyChild = original.emptyChild;
	}
	
	/**
	 * Generates and initializes the alternative for this separated list.
	 */
	private AbstractStackNode[] generateChildren(AbstractStackNode child, AbstractStackNode[] separators){
		AbstractStackNode listNode = child.getCleanCopy(DEFAULT_START_LOCATION);
		listNode.setParentProduction(production);
		
		int numberOfSeparators = separators.length;
		AbstractStackNode[] prod = new AbstractStackNode[numberOfSeparators + 2];
		
		listNode.setProduction(prod);
		prod[0] = listNode; // Start
		for(int i = numberOfSeparators - 1; i >= 0; --i){
			AbstractStackNode separator = separators[i];
			separator.setProduction(prod);
			separator.markAsSeparator();
			prod[i + 1] = separator;
		}
		prod[numberOfSeparators + 1] = listNode; // End
		
		return new AbstractStackNode[]{listNode};
	}
	
	/**
	 * Generates and initializes the empty child for this separated list (if it's a star list).
	 */
	private AbstractStackNode generateEmptyChild(){
		AbstractStackNode empty = EMPTY.getCleanCopy(DEFAULT_START_LOCATION);
		empty.setParentProduction(production);
		return empty;
	}
	
	public String getName(){
		return name;
	}
	
	public AbstractStackNode getCleanCopy(int startLocation){
		return new SeparatedListStackNode(this, startLocation);
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
		if(!(stackNode instanceof SeparatedListStackNode)) return false;
		
		SeparatedListStackNode otherNode = (SeparatedListStackNode) stackNode;
		
		if(!production.equals(otherNode.production)) return false;
		
		return hasEqualFilters(stackNode);
	}
}
