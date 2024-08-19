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

import io.usethesource.vallang.IConstructor;

public final class SkippingStackNode<P> extends AbstractMatchableStackNode<P>{
	private final SkippedNode result;
	
	public static SkippedNode createResultUntilCharClass(int[] until, int[] input, int startLocation, IConstructor production, int dot) {
		for (int to = startLocation ; to < input.length; ++to) {
			for (int i = 0; i < until.length; ++i) {
				if (input[to] == until[i]) {
					int length = to - startLocation;
					return new SkippedNode(production, dot, createSkippedToken(input, startLocation, length), startLocation);
				}
			}
		}

		return new SkippedNode(production, dot, new int[0], startLocation);
	}

	public static SkippedNode createResultUntilEndOfInput(int[] input, int startLocation, IConstructor production, int dot) {
		int length = input.length - startLocation;
		return new SkippedNode(production, dot, createSkippedToken(input, startLocation, length), startLocation);
	}

	public static SkippedNode createResultUntilToken(String token, int[] input, int startLocation, IConstructor production, int dot) {
		int length = token.length();
		for (int start=startLocation; start+length < input.length; start++) {
			boolean match = true;
			for (int j=0; j<length && match; j++) {
				if (token.codePointAt(j) != input[start+j]) {
					match = false;
				}

				if (match) {
					return createResultUntilChar(input, startLocation, start+length-startLocation, production, dot);
				}
			}
		}

		return null;
	}

	public static SkippedNode createResultUntilChar(int[] input, int startLocation, int endLocation, IConstructor production, int dot) {
		return new SkippedNode(production, dot, createSkippedToken(input, startLocation, endLocation - startLocation), startLocation);
	}

	private static int[] createSkippedToken(int[] input, int startLocation, int length) {
		int[] token = new int[length];
		System.arraycopy(input, startLocation, token, 0, length);
		return token;
	}

	public SkippingStackNode(int id, P parentProduction, SkippedNode result) {
		super(id, 0);
		
		this.result = result;
		setAlternativeProduction(parentProduction);
	}

	public SkippingStackNode(int id, P parentProduction, SkippedNode result, int startLocation) {
		super(id, 0, startLocation);
		
		this.result = result;
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
