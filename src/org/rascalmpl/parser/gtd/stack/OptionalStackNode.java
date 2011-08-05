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
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;

public final class OptionalStackNode extends AbstractStackNode implements IExpandableStackNode{
	private final IConstructor production;
	private final String name;
	
	private final AbstractStackNode[] children;
	private final AbstractStackNode emptyChild;
	
	public OptionalStackNode(int id, int dot, IConstructor production, AbstractStackNode optional){
		super(id, dot);
		
		this.production = production;
		this.name = SymbolAdapter.toString(ProductionAdapter.getType(production))+id; // Add the id to make it unique.
		
		this.children = generateChildren(optional);
		this.emptyChild = generateEmptyChild();
	}
	

	public OptionalStackNode(int id, int dot, IConstructor production, AbstractStackNode optional, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);
		
		this.production = production;
		this.name = SymbolAdapter.toString(ProductionAdapter.getType(production))+id; // Add the id to make it unique.
		
		this.children = generateChildren(optional);
		this.emptyChild = generateEmptyChild();
	}
	
	private OptionalStackNode(OptionalStackNode original, int startLocation){
		super(original, startLocation);
		
		production = original.production;
		name = original.name;
		
		children = original.children;
		emptyChild = original.emptyChild;
	}
	
	/**
	 * Generates and initializes the alternative for this optional.
	 */
	private AbstractStackNode[] generateChildren(AbstractStackNode optional){
		AbstractStackNode child = optional.getCleanCopy(DEFAULT_START_LOCATION);
		child.setParentProduction(production);
		return new AbstractStackNode[]{child};
	}
	
	/**
	 * Generates and initializes the empty child for this optional.
	 */
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
		return new OptionalStackNode(this, startLocation);
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
		return production.hashCode();
	}
	
	public boolean isEqual(AbstractStackNode stackNode){
		if(!(stackNode instanceof OptionalStackNode)) return false;
		
		OptionalStackNode otherNode = (OptionalStackNode) stackNode;
		
		if(!production.isEqual(otherNode.production)) return false;
		
		return hasEqualFilters(stackNode);
	}
}
