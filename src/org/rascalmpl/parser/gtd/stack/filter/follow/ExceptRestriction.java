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
package org.rascalmpl.parser.gtd.stack.filter.follow;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.util.ArrayList;

/**
 * A filter that make sure that at this position we do not have
 * this particular child.
 */
public class ExceptRestriction implements ICompletionFilter{
	private final Object child;
	
	public ExceptRestriction(Object child){
		super();
		this.child = child;
	}
	
	public boolean isFiltered(int[] input, int start, int end, AbstractNode result, PositionStore positionStore){
		if (result instanceof AbstractContainerNode) {
			return ((AbstractContainerNode) result).getFirstProduction().equals(child);
		}
		
		return false;
	}
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter) {
		if(!(otherCompletionFilter instanceof ExceptRestriction)) { 
			return false;
		}
		
		ExceptRestriction other = (ExceptRestriction) otherCompletionFilter;
		return child.equals(other.child); 
	}
}
