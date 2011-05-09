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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;
import org.rascalmpl.parser.gtd.util.specific.PositionStore;

public class AlternativeStackNode extends AbstractStackNode implements IExpandableStackNode{
	private final IConstructor production;
	private final String name;
	
	private final AbstractStackNode[] children;
	
	public AlternativeStackNode(int id, int dot, IConstructor production, AbstractStackNode[] alternatives){
		super(id, dot);
		
		this.production = production;
		this.name = String.valueOf(id);
		
		this.children = generateAlternatives(alternatives);
	}
	
	public AlternativeStackNode(int id, int dot, IConstructor production, IMatchableStackNode[] followRestrictions, AbstractStackNode[] alternatives){
		super(id, dot, followRestrictions);
		
		this.production = production;
		this.name = String.valueOf(id);
		
		this.children = generateAlternatives(alternatives);
	}
	
	public AlternativeStackNode(int id, int dot, IConstructor production, AbstractStackNode[] alternatives, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);
		
		this.production = production;
		this.name = String.valueOf(id);
		
		this.children = generateAlternatives(alternatives);
	}
	
	private AlternativeStackNode(AlternativeStackNode original){
		super(original);
		
		production = original.production;
		name = original.name;

		children = original.children;
	}
	
	private AbstractStackNode[] generateAlternatives(AbstractStackNode[] alternatives){
		AbstractStackNode[] children = new AbstractStackNode[alternatives.length];
		
		for(int i = alternatives.length - 1; i >= 0; --i){
			AbstractStackNode child = alternatives[i].getCleanCopy();

			AbstractStackNode[] prod = new AbstractStackNode[]{child};
			child.setProduction(prod);
			child.markAsEndNode();
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
	
	public void setPositionStore(PositionStore positionStore){
		throw new UnsupportedOperationException();
	}
	
	public boolean match(char[] input){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode getCleanCopy(){
		return new AlternativeStackNode(this);
	}
	
	public int getLength(){
		throw new UnsupportedOperationException();
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
	
	public AbstractNode getResult(){
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
}
