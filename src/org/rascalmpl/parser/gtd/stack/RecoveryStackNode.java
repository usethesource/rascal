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
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.result.RecoveryNode;

public final class RecoveryStackNode extends AbstractMatchableStackNode{
	private final int[] until;
	private final Object production;
	private RecoveryNode result;
	
	public RecoveryStackNode(int id, Object production, int[] until){
		super(id, 0);
		this.until = until;
		this.production = production;
	}
	
	private RecoveryStackNode(RecoveryStackNode original, int startLocation){
		super(original, startLocation);
		this.until = original.until;
		this.production = original.production;
	}
	
	public boolean isEmptyLeafNode(){
		return false;
	}
	
	public AbstractNode match(int[] input, int location) {
		int from = location;
		int to = location;
		
		for ( ; to < input.length; to++) {
			for (int i = 0; i < until.length; i++) {
				if (input[location] == until[i]) {
					result = buildResult(input, from, to);
					return result;
				}
			}
		}
		
		return null; // no lookahead character found to skip to, match failes
	}
	
	private RecoveryNode buildResult(int[] input, int from, int to) {
		CharNode[] chars = new CharNode[to - from + 1];
		for (int i = from; i <= to; i++) {
			chars[i] = new CharNode(input[i]);
		}
		
		return new RecoveryNode(chars, production, from);
	}

	public AbstractStackNode getCleanCopy(int startLocation){
		return new RecoveryStackNode(this, startLocation);
	}
	
	public AbstractStackNode getCleanCopyWithResult(int startLocation, AbstractNode result){
		return new RecoveryStackNode(this, startLocation);
	}
	
	public int getLength(){
		return result.getLength();
	}
	
	public AbstractNode getResult(){
		return result;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
	
	public int hashCode(){
		return production.hashCode();
	}
	
	public boolean isEqual(AbstractStackNode stackNode){
		if(!(stackNode instanceof RecoveryStackNode)) return false;
		
		RecoveryStackNode otherNode = (RecoveryStackNode) stackNode;
		
		if(!production.equals(otherNode.production)) return false;
		
		return true;
	}
}
