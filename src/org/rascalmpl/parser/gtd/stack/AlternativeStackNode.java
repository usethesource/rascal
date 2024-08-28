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
public class AlternativeStackNode<P> extends AbstractExpandableStackNode<P>{
	private final P production;
	private final String name;

	private final AbstractStackNode<P>[] children;

	public AlternativeStackNode(int id, int dot, P production, AbstractStackNode<P>[] alternatives){
		super(id, dot);

		this.production = production;
		this.name = String.valueOf(id);

		this.children = generateAlternatives(alternatives);
	}

	public AlternativeStackNode(int id, int dot, P production, AbstractStackNode<P>[] alternatives, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);

		this.production = production;
		this.name = String.valueOf(id);

		this.children = generateAlternatives(alternatives);
	}

	private AlternativeStackNode(AlternativeStackNode<P> original, int startLocation){
		super(original, startLocation);

		production = original.production;
		name = original.name;

		children = original.children;
	}

	/**
	 * Generates and initializes the alternatives for this alternative.
	 */
	@SuppressWarnings("unchecked")
	private AbstractStackNode<P>[] generateAlternatives(AbstractStackNode<P>[] alternatives){
		AbstractStackNode<P>[] children = (AbstractStackNode<P>[]) new AbstractStackNode[alternatives.length];

		for(int i = alternatives.length - 1; i >= 0; --i){
			AbstractStackNode<P> child = alternatives[i].getCleanCopy(DEFAULT_START_LOCATION);

			AbstractStackNode<P>[] prod = (AbstractStackNode<P>[]) new AbstractStackNode[]{child};
			child.setProduction(prod);
			child.setAlternativeProduction(production);

			children[i] = child;
		}

		return children;
	}

	public String getName(){
		return name;
	}

	public AbstractStackNode<P> getCleanCopy(int startLocation){
		return new AlternativeStackNode<P>(this, startLocation);
	}

	public AbstractStackNode<P>[] getChildren(){
		return children;
	}

	public boolean canBeEmpty(){
		return false;
	}

	public AbstractStackNode<P> getEmptyChild(){
		throw new UnsupportedOperationException();
	}

	@Override
	public String toShortString() {
		return toString();
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("alt");
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
		if(!(stackNode instanceof AlternativeStackNode)) return false;

		AlternativeStackNode<P> otherNode = (AlternativeStackNode<P>) stackNode;

		if(!production.equals(otherNode.production)) return false;

		return hasEqualFilters(stackNode);
	}

	@Override
	public <R> R accept(StackNodeVisitor<P, R> visitor) {
		return visitor.visit(this);
	}

}
