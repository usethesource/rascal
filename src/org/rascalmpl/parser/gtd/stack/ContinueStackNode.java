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

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public class ContinueStackNode extends AbstractStackNode{
	private final String name;
	private final Object parent;

	public ContinueStackNode(int id, Object parent, AbstractStackNode robustNode){
		super(id, robustNode, robustNode.startLocation);
		this.prefixesMap = robustNode.prefixesMap;
		this.alternateProductions = robustNode.alternateProductions;
		// TODO: could modify production here to include recovery literal
		this.production = robustNode.production;
		this.parent = parent;
		this.name = robustNode.getName();
		this.edgesMap = robustNode.edgesMap;
	}
	
	@Override
	public Object getParentProduction() {
		return parent;
	}
	
	@Override
	public boolean isEmptyLeafNode(){
		return false;
	}
	
	@Override
	public boolean isEndNode() {
		return true;
	}
	
	@Override
	public ICompletionFilter[] getCompletionFilters() {
		return new ICompletionFilter[] {};
	};
	
	@Override
	public IEnterFilter[] getEnterFilters() {
		return new IEnterFilter[] {};
	};

	@Override
	public String getName(){
		return "***robust:" + name + "***";
	}
	
	public AbstractNode match(int[] input, int location){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode getCleanCopy(int startLocation){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode getCleanCopyWithResult(int startLocation, AbstractNode result){
		throw new UnsupportedOperationException();
	}
	
	public int getLength(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode[] getChildren(){
		throw new UnsupportedOperationException();
	}
	
	public boolean canBeEmpty(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode getEmptyChild(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractNode getResult(){
		throw new UnsupportedOperationException();
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		sb.append(':');
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
	
	public int hashCode(){
		return getName().hashCode();
	}
	
	public boolean isEqual(AbstractStackNode stackNode){
		if(!(stackNode instanceof ContinueStackNode)) return false;
		
		ContinueStackNode otherNode = (ContinueStackNode) stackNode;

		return otherNode.name.equals(name) && otherNode.startLocation == startLocation;
	}
}
