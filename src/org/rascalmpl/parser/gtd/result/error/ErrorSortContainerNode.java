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

import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.result.CharNode;

/**
 * A error sort result node.
 * This node is equivalent to the regular sort result node, but (directly or
 * indirectly) contains incomplete parse results.
 */
public class ErrorSortContainerNode<P> extends AbstractContainerNode<P>{
	public final static int ID = 6;
	
	private CharNode[] unmatchedInput;
	
	public ErrorSortContainerNode(URI input, int offset, int endOffset, boolean isSeparator, boolean isLayout){
		super(input, offset, endOffset, false, isSeparator, isLayout);
		
		this.unmatchedInput = null;
	}
	
	public int getTypeIdentifier(){
		return ID;
	}
	
	/**
	 * Sets the unmatched input.
	 * The unmatched input represents the characters in the input string which
	 * were not successfully parsed.
	 */
	public void setUnmatchedInput(CharNode[] unmatchedInput){
		this.unmatchedInput = unmatchedInput;
	}
	
	/**
	 * Retrieves the chracters in the input string which were not successfully
	 * parsed. Only the top node in the parse tree will be in possession of
	 * this information. In all other cases 'null' will be returned.
	 */
	public CharNode[] getUnmatchedInput(){
		return unmatchedInput;
	}

	@Override
	public String toString() {
		return "ErrorSortContainerNode [unmatchedInput=" + Arrays.toString(unmatchedInput) + ", " + super.toString() + "]";
	}
}
