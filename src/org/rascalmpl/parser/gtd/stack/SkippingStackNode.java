/*******************************************************************************
 * Copyright (c) 2009-2024 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Pieter Olivier - Pieter.Olivier@swat.engineering
*******************************************************************************/
package org.rascalmpl.parser.gtd.stack;

import java.net.URI;

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.SkippedNode;

public final class SkippingStackNode<P> extends AbstractMatchableStackNode<P>{
	private final SkippedNode result;
	
	public static SkippedNode createResultUntilCharClass(URI uri, int[] until, int[] input, int startLocation) {
		for (int to = startLocation ; to < input.length; ++to) {
			for (int i = 0; i < until.length; ++i) {
				if (input[to] == until[i]) {
					int length = to - startLocation;
					return new SkippedNode(uri, createSkippedToken(input, startLocation, length), startLocation);
				}
			}
		}

		return new SkippedNode(uri, new int[0], startLocation);
	}

	public static SkippedNode createResultUntilEndOfInput(URI uri, int[] input, int startLocation) {
		int length = input.length - startLocation;
		return new SkippedNode(uri, createSkippedToken(input, startLocation, length), startLocation);
	}

	public static SkippedNode createResultUntilToken(URI uri, String token, int[] input, int startLocation) {
		int length = token.length();
		for (int start=startLocation; start+length < input.length; start++) {
			boolean match = true;
			for (int j=0; j<length && match; j++) {
				if (token.codePointAt(j) != input[start+j]) {
					match = false;
				}

				if (match) {
					return createResultUntilChar(uri, input, startLocation, start+length-startLocation);
				}
			}
		}

		return null;
	}

	public static SkippedNode createResultUntilChar(URI uri, int[] input, int startLocation, int endLocation) {
		return new SkippedNode(uri, createSkippedToken(input, startLocation, endLocation - startLocation), startLocation);
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
		return new SkippingStackNode<>(this, startLocation);
	}
	
	public AbstractStackNode<P> getCleanCopyWithResult(int startLocation, AbstractNode result){
		return new SkippingStackNode<>(this, (SkippedNode) result, startLocation);
	}
	
	public int getLength(){
		return result.getLength();
	}
	
	public AbstractNode getResult(){
		return result;
	}
	
	@Override
	public String toShortString() {
		return "skip(" + result.toString() + ")";
	}

	@Override
	public String toString() {
		return "SkippingStackNode[result=" + result + "," + super.toString() + "]";
	}

	@Override
	public int hashCode(){
		return getParentProduction().hashCode();
	}

	@Override
	public boolean equals(Object rhs) {
		return super.equals(rhs);
	}

	public boolean isEqual(AbstractStackNode<P> stackNode){
		if ( !(stackNode instanceof SkippingStackNode)) {
		    return false;
		}
		
		SkippingStackNode<P> otherNode = (SkippingStackNode<P>) stackNode;
		
		return otherNode.id == id;
	}

	@Override
	public void accept(StackNodeVisitor<P> visitor) {
		visitor.visit(this);
	}
}
