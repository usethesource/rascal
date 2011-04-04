/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.stack;


import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.AtColumnNode;
import org.rascalmpl.parser.gtd.util.specific.PositionStore;

public class AtColumnStackNode extends AbstractStackNode implements IMatchableStackNode, ILocatableStackNode{
	private final AtColumnNode result;
	
	private final int column;
	
	private PositionStore positionStore;
	
	public AtColumnStackNode(int id, int dot, int column){
		super(id, dot);
		
		this.result = new AtColumnNode(column);
		this.column = column;
	}
	
	private AtColumnStackNode(AtColumnStackNode original){
		super(original);
		
		column = original.column;
		result = original.result;
	}
	
	public boolean isEmptyLeafNode(){
		return true;
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public void setPositionStore(PositionStore positionStore){
		this.positionStore = positionStore;
	}
	
	public boolean match(char[] input){
		return positionStore.isAtColumn(startLocation, column);
	}
	
	public boolean matchWithoutResult(char[] input, int location){
		return positionStore.isAtColumn(location, column);
	}
	
	public AbstractStackNode getCleanCopy(){
		return new AtColumnStackNode(this);
	}
	
	public int getLength(){
		return 0;
	}
	
	public AbstractStackNode[] getChildren(){
		throw new UnsupportedOperationException();
	}
	
	public boolean canBeEmpty(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode getEmptyChild(){
		throw new UnsupportedOperationException();
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
}
