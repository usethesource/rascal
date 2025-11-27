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
public class SequenceStackNode<P> extends AbstractExpandableStackNode<P>{
	private final P production;
	private final String name;
	
	private final AbstractStackNode<P>[] children;
	private final AbstractStackNode<P> emptyChild;
	
	public SequenceStackNode(int id, int dot, P production, AbstractStackNode<P>[] children){
		super(id, dot);
		
		this.production = production;
		this.name = String.valueOf(id);
		
		this.children = generateChildren(children);
		this.emptyChild = children.length == 0 ? generateEmptyChild() : null;
	}
	
	public SequenceStackNode(int id, int dot, P production, AbstractStackNode<P>[] children, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);
		
		this.production = production;
		this.name = String.valueOf(id);
		
		this.children = generateChildren(children);
		this.emptyChild = children.length == 0 ? generateEmptyChild() : null;
	}
	
	private SequenceStackNode(SequenceStackNode<P> original, int startLocation){
		super(original, startLocation);
		
		production = original.production;
		name = original.name;

		children = original.children;
		this.emptyChild = original.emptyChild;
	}
	
	/**
	 * Generates and initializes the alternatives for this sequence.
	 */
	@SuppressWarnings("unchecked")
	private AbstractStackNode<P>[] generateChildren(AbstractStackNode<P>[] children){
		if(children.length == 0){
			return new AbstractStackNode[]{};
		}

		AbstractStackNode<P>[] prod = (AbstractStackNode<P>[]) new AbstractStackNode[children.length];
		
		for(int i = children.length - 1; i >= 0; --i){
			AbstractStackNode<P> child = children[i].getCleanCopy(DEFAULT_START_LOCATION);
			child.setProduction(prod);
			prod[i] = child;
		}
		
		prod[prod.length - 1].setAlternativeProduction(production);
		
		return (AbstractStackNode<P>[]) new AbstractStackNode[]{prod[0]};
	}

	/**
	 * Generates and initializes the empty child for usage in empty sequences.
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
		return new SequenceStackNode<>(this, startLocation);
	}
	
	public AbstractStackNode<P>[] getChildren(){
		return children;
	}
	
	public boolean canBeEmpty(){
		return children.length == 0;
	}
	
	public AbstractStackNode<P> getEmptyChild(){
		if(children.length > 0) {
			throw new UnsupportedOperationException();
		}
		return emptyChild;
	}

	@Override
	public String toShortString() {
		return name;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("seq");
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
		if(!(stackNode instanceof SequenceStackNode)) return false;
		
		SequenceStackNode<P> otherNode = (SequenceStackNode<P>) stackNode;
		
		if(!production.equals(otherNode.production)) return false;
		
		return hasEqualFilters(stackNode);
	}

	@Override
	public <R> R accept(StackNodeVisitor<P,R> visitor) {
		return visitor.visit(this);
	}

}
