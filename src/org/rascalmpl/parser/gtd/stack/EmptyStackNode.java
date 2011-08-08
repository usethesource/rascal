/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

/**
 * Empty is different from epsilon; it is the () symbol which is one of the 'regular' expressions.
 * it produces a tree!
 */
public final class EmptyStackNode extends AbstractStackNode implements IExpandableStackNode{
	private final Object production;
	private final String name;

	private final AbstractStackNode emptyChild;
	private static final AbstractStackNode[] children = new AbstractStackNode[0];
	
	public EmptyStackNode(int id, int dot, Object production){
		super(id, dot);
		
		this.production = production;
		this.name = "empty"+id; // Add the id to make it unique.
		
		this.emptyChild = generateEmptyChild();
	}
	
	public EmptyStackNode(int id, int dot, Object production, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters) {
		super(id, dot, enterFilters, completionFilters);
		
		this.production = production;
		this.name = "empty"+id;
		this.emptyChild = generateEmptyChild(); 
	}
	
	private EmptyStackNode(EmptyStackNode original, int startLocation){
		super(original, startLocation);
		
		production = original.production;
		name = original.name;

		emptyChild = original.emptyChild;
	}
	
	private AbstractStackNode generateEmptyChild(){
		AbstractStackNode empty = EMPTY.getCleanCopy(DEFAULT_START_LOCATION);
		empty.setParentProduction(production);
		return empty;
	}
	
	public boolean isEmptyLeafNode(){
		return false;
	}
	
	public String getName(){
		return name;
	}
	
	public AbstractNode match(char[] input, int location){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode getCleanCopy(int startLocation){
		return new EmptyStackNode(this, startLocation);
	}
	
	public AbstractStackNode getCleanCopyWithResult(int startLocation, AbstractNode result){
		throw new UnsupportedOperationException();
	}
	
	public int getLength(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode[] getChildren(){
		return children;
	}
	
	public boolean canBeEmpty(){
		return true;
	}
	
	public AbstractStackNode getEmptyChild(){
		return emptyChild;
	}
	
	public AbstractNode getResult(){
		throw new UnsupportedOperationException();
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
	
	public boolean isEqual(AbstractStackNode stackNode){
		if(!(stackNode instanceof EmptyStackNode)) return false;
		
		return hasEqualFilters(stackNode);
	}
}
