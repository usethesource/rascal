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

public class AlternativeStackNode extends AbstractExpandableStackNode{
	private final Object production;
	private final String name;
	
	private final AbstractStackNode[] children;
	
	public AlternativeStackNode(int id, int dot, Object production, AbstractStackNode[] alternatives){
		super(id, dot);
		
		this.production = production;
		this.name = String.valueOf(id);
		
		this.children = generateAlternatives(alternatives);
	}
	
	public AlternativeStackNode(int id, int dot, Object production, AbstractStackNode[] alternatives, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);
		
		this.production = production;
		this.name = String.valueOf(id);
		
		this.children = generateAlternatives(alternatives);
	}
	
	private AlternativeStackNode(AlternativeStackNode original, int startLocation){
		super(original, startLocation);
		
		production = original.production;
		name = original.name;

		children = original.children;
	}
	
	/**
	 * Generates and initializes the alternatives for this alternative.
	 */
	private AbstractStackNode[] generateAlternatives(AbstractStackNode[] alternatives){
		AbstractStackNode[] children = new AbstractStackNode[alternatives.length];
		
		for(int i = alternatives.length - 1; i >= 0; --i){
			AbstractStackNode child = alternatives[i].getCleanCopy(DEFAULT_START_LOCATION);

			AbstractStackNode[] prod = new AbstractStackNode[]{child};
			child.setProduction(prod);
			child.setParentProduction(production);
			
			children[i] = child;
		}
		
		return children;
	}
	
	public boolean isEmptyLeafNode(){
		return false;
	}
	
	public String getName(){
		return name;
	}
	
	public AbstractStackNode getCleanCopy(int startLocation){
		return new AlternativeStackNode(this, startLocation);
	}
	
	public AbstractStackNode[] getChildren(){
		return children;
	}
	
	public boolean canBeEmpty(){
		return false;
	}
	
	public AbstractStackNode getEmptyChild(){
		throw new UnsupportedOperationException();
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("alt");
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
		if(!(stackNode instanceof AlternativeStackNode)) return false;
		
		AlternativeStackNode otherNode = (AlternativeStackNode) stackNode;

		if(!production.equals(otherNode.production)) return false;
		
		return hasEqualFilters(stackNode);
	}
}
