/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.result;


/**
 * Result tree node that represents a skipped portion of the input sentence.
 */
public class RecoveryNode extends AbstractNode {
	public final static int ID = 9;
	
	private final CharNode[] skippedChars;
	private final Object production;
	
	private final int offset;
	
	public RecoveryNode(CharNode[] skippedChars, Object production, int offset){
		super();
		
		this.production = production;
		this.skippedChars = skippedChars;
		
		this.offset = offset;
	}
	
	public int getTypeIdentifier(){
		return ID;
	}
	
	public CharNode[] getSkippedChars(){
		return skippedChars;
	}
	
	public Object getProduction(){
		return production;
	}
	
	/**
	 * Returns the offset in the input string this node starts at.
	 */
	public int getOffset(){
		return offset;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isNonterminalSeparator() {
		return false;
	}

	public int getLength() {
		return skippedChars.length;
	}
}
