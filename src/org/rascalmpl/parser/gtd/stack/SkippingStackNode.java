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
import org.rascalmpl.parser.gtd.result.SkippedNode;

public final class SkippingStackNode<P> extends AbstractMatchableStackNode<P>{
	private final SkippedNode result;
	
	public SkippingStackNode(int id, int[] until, int[] input, int startLocation, P parentProduction){
		super(id, 0);
		
		this.result = buildResult(input, until, startLocation);
		setAlternativeProduction(parentProduction);
	}
	
	private SkippingStackNode(SkippingStackNode<P> original, int startLocation){
		super(original, startLocation);
		
		this.result = original.result;
	}
	
	private SkippingStackNode(SkippingStackNode<P> original, SkippedNode result, int startLocation){
		super(original, startLocation);
		
		this.result = result;
	}
	
	private static SkippedNode buildResult(int[] input, int[] until, int startLocation){
		for (int to = startLocation ; to < input.length; ++to) {
			for (int i = 0; i < until.length; ++i) {
				if (input[to] == until[i]) {
					int length = to - startLocation;
					int[] chars = new int[length];
					System.arraycopy(input, startLocation, chars, 0, length);
					
					return new SkippedNode(chars, startLocation);
				}
			}
		}
		
		return new SkippedNode(new int[0], startLocation);
	}
	
	public boolean isEmptyLeafNode(){
		return result.isEmpty();
	}
	
	public AbstractNode match(int[] input, int location){
		return result;
	}

	public AbstractStackNode<P> getCleanCopy(int startLocation){
		return new SkippingStackNode<P>(this, startLocation);
	}
	
	public AbstractStackNode<P> getCleanCopyWithResult(int startLocation, AbstractNode result){
		return new SkippingStackNode<P>(this, (SkippedNode) result, startLocation);
	}
	
	public int getLength(){
		return result.getLength();
	}
	
	public AbstractNode getResult(){
		return result;
	}
	
	/*Original: public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}*/
	
	@Override
	public String toString() {
		return "SkippingStackNode[result=" + result + "," + super.toString() + "]";
	}

	@Override
	public int hashCode(){
		return getParentProduction().hashCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object rhs) {
		if (rhs instanceof AbstractStackNode) {
			return isEqual((AbstractStackNode<P>)rhs);
		}

		return false;
	}

	public boolean isEqual(AbstractStackNode<P> stackNode){
		if ( !(stackNode instanceof SkippingStackNode)) {
		    return false;
		}
		
		SkippingStackNode<P> otherNode = (SkippingStackNode<P>) stackNode;
		
		return otherNode.id == id;
	}
}
