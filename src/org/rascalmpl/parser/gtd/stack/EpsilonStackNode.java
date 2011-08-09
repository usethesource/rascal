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
package org.rascalmpl.parser.gtd.stack;

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.EpsilonNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public final class EpsilonStackNode extends AbstractMatchableStackNode{
	private final static EpsilonNode result = new EpsilonNode();
	
	public EpsilonStackNode(int id, int dot){
		super(id, dot);
	}
	
	public EpsilonStackNode(int id, int dot, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);
	}
	
	private EpsilonStackNode(EpsilonStackNode original, int startLocation){
		super(original, startLocation);
	}
	
	public boolean isEmptyLeafNode(){
		return true;
	}
	
	public AbstractNode match(char[] input, int location){
		return result;
	}
	
	public AbstractStackNode getCleanCopy(int startLocation){
		return new EpsilonStackNode(this, startLocation);
	}
	
	public AbstractStackNode getCleanCopyWithResult(int startLocation, AbstractNode result){
		return new EpsilonStackNode(this, startLocation);
	}
	
	public int getLength(){
		return 0;
	}
	
	public AbstractNode getResult(){
		return result;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
	
	public int hashCode(){
		return 0;
	}
	
	public boolean isEqual(AbstractStackNode stackNode){
		if(!(stackNode instanceof EpsilonStackNode)) return false;
		
		return hasEqualFilters(stackNode);
	}
}
