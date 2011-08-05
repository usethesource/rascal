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
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public final class CharStackNode extends AbstractStackNode implements IMatchableStackNode{
	private final char[][] ranges;
	
	private final AbstractNode result;
	
	public CharStackNode(int id, int dot, char[][] ranges){
		super(id, dot);

		this.ranges = ranges;
		
		result = null;
	}
	
	public CharStackNode(int id, int dot, char[][] ranges, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);

		this.ranges = ranges;
		
		result = null;
	}
	
	private CharStackNode(CharStackNode original, int startLocation){
		super(original, startLocation);
		
		ranges = original.ranges;
		
		result = null;
	}
	
	private CharStackNode(CharStackNode original, int startLocation, AbstractNode result){
		super(original, startLocation);
		
		this.ranges = original.ranges;
		
		this.result = result;
	}
	
	public boolean isEmptyLeafNode(){
		return false;
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractNode match(char[] input, int location){
		char next = input[location];
		for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			if(next >= range[0] && next <= range[1]){
				return CharNode.createCharNode(next);
			}
		}
		
		return null;
	}
	
	public AbstractStackNode getCleanCopy(int startLocation){
		return new CharStackNode(this, startLocation);
	}
	
	public AbstractStackNode getCleanCopyWithResult(int startLocation, AbstractNode result){
		return new CharStackNode(this, startLocation, result);
	}
	
	public int getLength(){
		return 1;
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
		
		sb.append('[');
		char[] range = ranges[0];
		sb.append(range[0]);
		sb.append('-');
		sb.append(range[1]);
		for(int i = ranges.length - 2; i >= 0; --i){
			sb.append(',');
			range = ranges[i];
			sb.append(range[0]);
			sb.append('-');
			sb.append(range[1]);
		}
		sb.append(']');
		
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
	
	public int hashCode(){
		int hash = 0;
		
		for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			hash = hash << 3 + hash >> 5;
			hash ^= range[0] +  (range[1] << 2);
		}
		
		return hash;
	}
	
	public boolean isEqual(AbstractStackNode stackNode){
		if(!(stackNode instanceof CharStackNode)) return false;
		
		CharStackNode otherNode = (CharStackNode) stackNode;
		
		char[][] otherRanges = otherNode.ranges;
		if(ranges.length != otherRanges.length) return false;
		
		OUTER: for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			for(int j = otherRanges.length - 1; j >= 0; --j){
				char[] otherRange = otherRanges[j];
				if(range[0] == otherRange[0] && range[1] == otherRange[1]) continue OUTER;
			}
			return false; // Could not find a certain range.
		}
		// Found all ranges.
		
		return hasEqualFilters(stackNode);
	}
}
