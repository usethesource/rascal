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

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.LiteralNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public final class LiteralStackNode<P> extends AbstractMatchableStackNode<P>{
	private final int[] literal;
	private final P production;

	private final LiteralNode result;

	public LiteralStackNode(int id, int dot, P production, int[] literal){
		super(id, dot);

		this.literal = literal;
		this.production = production;

		result = new LiteralNode(production, literal);
	}

	public LiteralStackNode(int id, int dot, P production, int[] literal, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);

		this.literal = literal;
		this.production = production;

		result = new LiteralNode(production, literal);
	}

	public int[] getLiteral() {
		return literal;
	}

	private LiteralStackNode(LiteralStackNode<P> original, int startLocation){
		super(original, startLocation);

		literal = original.literal;
		production = original.production;

		result = original.result;
	}

	public boolean isEmptyLeafNode(){
		return false;
	}

	public AbstractNode match(int[] input, int location){
		for(int i = literal.length - 1; i >= 0; --i){
			if(literal[i] != input[location + i]) return null; // Did not match.
		}

		return result;
	}

	public AbstractStackNode<P> getCleanCopy(int startLocation){
		return new LiteralStackNode<P>(this, startLocation);
	}

	public AbstractStackNode<P> getCleanCopyWithResult(int startLocation, AbstractNode result){
		return new LiteralStackNode<P>(this, startLocation);
	}

	public int getLength(){
		return literal.length;
	}

	public AbstractNode getResult(){
		return result;
	}

	@Override
	public String toShortString() {
		return "'" + new String(literal, 0, literal.length) + "'";
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("lit['");
		sb.append(new String(literal, 0, literal.length));
		sb.append("',");
		sb.append(super.toString());

		sb.append(']');

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
		if(!(stackNode instanceof LiteralStackNode)) return false;

		LiteralStackNode<P> otherNode = (LiteralStackNode<P>) stackNode;

		if(!production.equals(otherNode.production)) return false;

		return hasEqualFilters(stackNode);
	}

	@Override
	public <R> R accept(StackNodeVisitor<P,R> visitor) {
		return visitor.visit(this);
	}

}
