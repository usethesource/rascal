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
import org.rascalmpl.parser.gtd.result.EpsilonNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public final class EpsilonStackNode<P> extends AbstractMatchableStackNode<P>{
	public final static EpsilonNode EPSILON_RESULT = new EpsilonNode();
	
	private final AbstractNode result;
	
	public EpsilonStackNode(int id, int dot){
		super(id, dot);
		
		result = null;
	}
	
	public EpsilonStackNode(int id, int dot, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);
		
		result = null;
	}
	
	private EpsilonStackNode(EpsilonStackNode<P> original, int startLocation){
		super(original, startLocation);
		
		result = null;
	}
	
	private EpsilonStackNode(EpsilonStackNode<P> original, int startLocation, AbstractNode result){
		super(original, startLocation);
		
		this.result = result;
	}
	
	public boolean isEmptyLeafNode(){
		return true;
	}
	
	public AbstractNode match(int[] input, int location){
		return EPSILON_RESULT;
	}
	
	public AbstractStackNode<P> getCleanCopy(int startLocation){
		return new EpsilonStackNode<>(this, startLocation);
	}
	
	public AbstractStackNode<P> getCleanCopyWithResult(int startLocation, AbstractNode result){
		return new EpsilonStackNode<>(this, startLocation, result);
	}
	
	public int getLength(){
		return 0;
	}
	
	public AbstractNode getResult(){
		return result;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
	
	@Override
	public int hashCode(){
		return 0;
	}
	
	@Override
	public boolean equals(Object peer) {
		return super.equals(peer);
	}

	public boolean isEqual(AbstractStackNode<P> stackNode){
		if(!(stackNode instanceof EpsilonStackNode)) return false;
		
		return hasEqualFilters(stackNode);
	}

	@Override
	public <R> R accept(StackNodeVisitor<P,R> visitor) {
		return visitor.visit(this);
	}

}
