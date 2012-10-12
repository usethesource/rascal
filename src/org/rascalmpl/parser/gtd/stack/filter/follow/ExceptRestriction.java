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
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.util.ArrayList;

/**
 * A filter that makes sure that at this position we do not have this
 * particular child.
 */
@SuppressWarnings("unchecked")
public class ExceptRestriction<P> implements ICompletionFilter{
	private final P production;
	
	public ExceptRestriction(P production){
		super();
		
		this.production = production;
	}
	
	public boolean isFiltered(int[] input, int start, int end, AbstractNode result, PositionStore positionStore){
		if (result instanceof AbstractContainerNode) {
			AbstractContainerNode<P> node = (AbstractContainerNode<P>) result;
			P production = node.getFirstProduction();
			if (this.production.equals(production)) {
				return true;
			}
			
			ArrayList<P> others = node.getAdditionalProductions();
			if (others != null) {
				for (int i = 0; i < others.size(); i++) {
					if (others.get(i).equals(this.production)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean isEqual(ICompletionFilter otherCompletionFilter) {
		if(!(otherCompletionFilter instanceof ExceptRestriction)) { 
			return false;
		}
		
		ExceptRestriction<P> other = (ExceptRestriction<P>) otherCompletionFilter;
		return production.equals(other.production); 
	}
}
