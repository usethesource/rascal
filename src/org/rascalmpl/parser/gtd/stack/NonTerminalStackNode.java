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
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public final class NonTerminalStackNode<P> extends AbstractStackNode<P>{
	private final String expectIdentifier;

	public NonTerminalStackNode(int id, int dot, String expectIdentifier){
		super(id, dot);

		this.expectIdentifier = expectIdentifier;
	}

	public NonTerminalStackNode(int id, int dot, String expectIdentifier, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);

		this.expectIdentifier = expectIdentifier;
	}

	private NonTerminalStackNode(NonTerminalStackNode<P> original, int startLocation){
		super(original, startLocation);

		expectIdentifier = original.expectIdentifier;
	}

	public boolean isEmptyLeafNode(){
		return false;
	}

	public String getName(){
		return expectIdentifier;
	}

	public AbstractNode match(int[] input, int location){
		throw new UnsupportedOperationException();
	}

	public AbstractStackNode<P> getCleanCopy(int startLocation){
		return new NonTerminalStackNode<P>(this, startLocation);
	}

	public AbstractStackNode<P> getCleanCopyWithResult(int startLocation, AbstractNode result){
		throw new UnsupportedOperationException();
	}

	public int getLength(){
		throw new UnsupportedOperationException();
	}

	public AbstractStackNode<P>[] getChildren(){
		throw new UnsupportedOperationException();
	}

	public boolean canBeEmpty(){
		throw new UnsupportedOperationException();
	}

	public AbstractStackNode<P> getEmptyChild(){
		throw new UnsupportedOperationException();
	}

	public AbstractNode getResult(){
		throw new UnsupportedOperationException();
	}

	public String toShortString() {
		return expectIdentifier;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder("NonTerminal[");
		sb.append(expectIdentifier);
		sb.append(",");
		sb.append(super.toString());
		sb.append("]");

		return sb.toString();
	}

	public int hashCode(){
		return expectIdentifier.hashCode();
	}

	public boolean isEqual(AbstractStackNode<P> stackNode){
		if(!(stackNode instanceof NonTerminalStackNode)) return false;

		NonTerminalStackNode<P> otherNode = (NonTerminalStackNode<P>) stackNode;

		if(!expectIdentifier.equals(otherNode.expectIdentifier)) return false;

		return hasEqualFilters(stackNode);
	}

	void accept(StackNodeVisitor<P> visitor) {
		visitor.visit(this);
	}

}
