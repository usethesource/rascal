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
import org.rascalmpl.parser.gtd.util.specific.PositionStore;
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
		this.name = SymbolAdapter.toString(ProductionAdapter.getRhs(production))+id; // Add the id to make it unique.
		
		this.children = generateChildren(optional);
		this.emptyChild = generateEmptyChild();
	}
	
	public OptionalStackNode(int id, int dot, IConstructor production, IMatchableStackNode[] followRestrictions, AbstractStackNode optional){
		super(id, dot, followRestrictions);
		
		this.production = production;
		this.name = SymbolAdapter.toString(ProductionAdapter.getRhs(production))+id; // Add the id to make it unique.
		
		this.children = generateChildren(optional);
		this.emptyChild = generateEmptyChild();
	}
	
	private OptionalStackNode(OptionalStackNode original){
		super(original);
		
		production = original.production;
		name = original.name;
		
		children = original.children;
		emptyChild = original.emptyChild;
	}
	
	private AbstractStackNode[] generateChildren(AbstractStackNode optional){
		AbstractStackNode child = optional.getCleanCopy();
		child.markAsEndNode();
		child.setParentProduction(production);
		return new AbstractStackNode[]{child};
	}
	
	private AbstractStackNode generateEmptyChild(){
		AbstractStackNode empty = EMPTY.getCleanCopy();
		empty.markAsEndNode();
		empty.setParentProduction(production);
		return empty;
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
		return new OptionalStackNode(this);
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
}
