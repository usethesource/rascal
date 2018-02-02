/*******************************************************************************
 * Copyright (c) 2011-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.core.parser.gtd.result.out;

/**
 * This class can be used to keep track of post-parse filtering errors. These
 * kinds of errors can occur during parse tree conversion, due to semantic
 * actions.
 */
public class FilteringTracker{
	private int offset;
	private int endOffset;
	
	public FilteringTracker(){
		super();
	}
	
	/**
	 * Marks the last filtered location.
	 */
	public void setLastFiltered(int offset, int endOffset){
		this.offset = offset;
		this.endOffset = endOffset;
	}
	
	/**
	 * Returns the begin location in the input string of the last filtered
	 * location.
	 */
	public int getOffset(){
		return offset;
	}
	
	/**
	 * Returns the end location in the input string of the last filtered
	 * location.
	 */
	public int getEndOffset(){
		return endOffset;
	}
}
