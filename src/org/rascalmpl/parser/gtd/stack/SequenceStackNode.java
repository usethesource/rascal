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

public class SequenceStackNode extends AbstractStackNode implements IExpandableStackNode{
	private final Object production;
	private final String name;
	
	private final AbstractStackNode[] children;
	
	public SequenceStackNode(int id, int dot, Object production, AbstractStackNode[] children){
		super(id, dot);
		
		this.production = production;
		this.name = String.valueOf(id);
		
		this.children = generateChildren(children);
	}
	
	public SequenceStackNode(int id, int dot, Object production, AbstractStackNode[] children, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);
		
		this.production = production;
		this.name = String.valueOf(id);
		
		this.children = generateChildren(children);
	}
	
	private SequenceStackNode(SequenceStackNode original, int startLocation){
		super(original, startLocation);
		
		production = original.production;
		name = original.name;

		children = original.children;
	}
	
	/**
	 * Generates and initializes the alternatives for this sequence.
	 */
	private AbstractStackNode[] generateChildren(AbstractStackNode[] children){
		AbstractStackNode[] prod = new AbstractStackNode[children.length];
		
		for(int i = children.length - 1; i >= 0; --i){
			AbstractStackNode child = children[i].getCleanCopy(DEFAULT_START_LOCATION);
			child.setProduction(prod);
			prod[i] = child;
		}
		
		prod[prod.length - 1].setParentProduction(production);
		
		return new AbstractStackNode[]{prod[0]};
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
		return new SequenceStackNode(this, startLocation);
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
		sb.append("seq");
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
		if(!(stackNode instanceof SequenceStackNode)) return false;
		
		SequenceStackNode otherNode = (SequenceStackNode) stackNode;
		
		if(!production.equals(otherNode.production)) return false;
		
		return hasEqualFilters(stackNode);
	}
}
