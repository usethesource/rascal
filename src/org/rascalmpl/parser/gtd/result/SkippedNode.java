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
package org.rascalmpl.parser.gtd.result;

import org.rascalmpl.unicode.UnicodeConverter;

/**
 * Result tree node that represents a skipped portion of the input sentence.
 */
public class SkippedNode extends AbstractNode {
	public final static int ID = 9;
	
	private final Object production;
	private final int dot;
	private final int[] skippedChars;
	private final int offset;

	public SkippedNode(Object production, int dot, int[] skippedChars, int offset) {
		super();
		
		this.production = production;
		this.dot = dot;
		this.skippedChars = skippedChars;
		this.offset = offset;
	}
	
	public int getTypeIdentifier(){
		return ID;
	}

	public Object getProduction() {
		return production;
	}

	public int getDot() {
		return dot;
	}

	public int[] getSkippedChars(){
		return skippedChars;
	}
	
	/**
	 * Returns the offset in the input string this node starts at.
	 */
	public int getOffset(){
		return offset;
	}
	
	public boolean isEmpty() {
		return (skippedChars.length == 0);
	}
	
	public boolean isNonterminalSeparator() {
		return false;
	}

	public int getLength() {
		return skippedChars.length;
	}

	@Override
	public String toString() {
		return "SkippedNode[skippedChars=" + UnicodeConverter.unicodeArrayToString(skippedChars) + ",offset=" + offset + "]";
	}
}
