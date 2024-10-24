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
package org.rascalmpl.parser.gtd.result.error;

import java.net.URI;
import java.util.Arrays;

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;

/**
 * A expected result node.
 * Expected nodes represent possible futures in error parse trees.
 */
public class ExpectedNode<N> extends AbstractNode{
	public final static int ID = 8;
	
	private final CharNode[] mismatchedChildren;
	private final N symbol;
	
	private final URI input;
	private final int offset;
	private final int endOffset;
	
	private final boolean isSeparator;
	private final boolean isLayout;
	
	public ExpectedNode(CharNode[] mismatchedChildren, N symbol, URI input, int offset, int endOffset, boolean isSeparator, boolean isLayout){
		super();
		
		this.mismatchedChildren = mismatchedChildren;
		this.symbol = symbol;
		
		this.input = input;
		this.offset = offset;
		this.endOffset = endOffset;
		
		this.isSeparator = isSeparator;
		this.isLayout = isLayout;
	}
	
	public int getTypeIdentifier(){
		return ID;
	}
	
	/**
	 * Returns the mismatched children associated with this node.
	 * The will only be present in case this node represents a leaf node (like a
	 * literal or a character).
	 */
	public CharNode[] getMismatchedChildren(){
		return mismatchedChildren;
	}
	
	/**
	 * Returns the symbol associated with this possible future.
	 */
	public N getSymbol(){
		return symbol;
	}
	
	/**
	 * Returns the input location; may be null.
	 */
	public URI getInput(){
		return input;
	}
	
	/**
	 * Returns the offset in the input string this node starts at.
	 */
	public int getOffset(){
		return offset;
	}
	
	/**
	 * Returns the offset in the input string at which this node would have ended.
	 */
	public int getEndOffset(){
		return endOffset;
	}
	
	/**
	 * Returns whether or not this expected node represents layout.
	 */
	public boolean isLayout(){
		return isLayout;
	}
	
	/**
	 * Expected nodes don't have content.
	 */
	public boolean isEmpty(){
		return false;
	}
	
	/**
	 * Returns whether or not this expected node represents a separator.
	 */
	public boolean isNonterminalSeparator(){
		return isSeparator;
	}

	@Override
	public String toString() {
		return "ExpectedNode[mismatchedChildren=" + Arrays.toString(mismatchedChildren) + ", symbol=" + symbol
			+ ", input=" + input + ", offset=" + offset + ", endOffset=" + endOffset + ", isSeparator=" + isSeparator
			+ ", isLayout=" + isLayout + "]";
	}
}
