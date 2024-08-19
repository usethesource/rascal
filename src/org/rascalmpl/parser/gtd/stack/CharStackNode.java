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
package org.rascalmpl.parser.gtd.stack;

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;
import org.rascalmpl.unicode.UnicodeConverter;

public final class CharStackNode<P> extends AbstractMatchableStackNode<P>{
	private final int[][] ranges;
	
	private final AbstractNode result;
	
	public CharStackNode(int id, int dot, int[][] ranges){
		super(id, dot);

		this.ranges = ranges;
		
		result = null;
	}
	
	public CharStackNode(int id, int dot, int[][] ranges, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);

		this.ranges = ranges;
		
		result = null;
	}
	
	private CharStackNode(CharStackNode<P> original, int startLocation){
		super(original, startLocation);
		
		ranges = original.ranges;
		
		result = null;
	}
	
	private CharStackNode(CharStackNode<P> original, int startLocation, AbstractNode result){
		super(original, startLocation);
		
		this.ranges = original.ranges;
		
		this.result = result;
	}
	
	public boolean isEmptyLeafNode(){
		return false;
	}
	
	public AbstractNode match(int[] input, int location){
		int next = input[location];
		
		for(int i = ranges.length - 1; i >= 0; --i){
			int[] range = ranges[i];
			if(next >= range[0] && next <= range[1]){
				return CharNode.createCharNode(next);
			}
		}
		
		return null;
	}
	
	public AbstractStackNode<P> getCleanCopy(int startLocation){
		return new CharStackNode<P>(this, startLocation);
	}
	
	public AbstractStackNode<P> getCleanCopyWithResult(int startLocation, AbstractNode result){
		return new CharStackNode<P>(this, startLocation, result);
	}
	
	public int getLength(){
		return 1;
	}
	
	public AbstractNode getResult(){
		return result;
	}
	
	@Override
	public String toShortString() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<ranges.length; i++) {
			if (i > 0) {
				sb.append(',');
			}

			int[] range = ranges[i];
			sb.append(codePointToString(range[0]));
			if (range[0] != range[1]) {
				sb.append('-');
				sb.append(codePointToString(range[1]));
			}
		}

		return sb.toString();
	}

	private String codePointToString(int codePoint) {
		if (Character.isLetterOrDigit(codePoint)) {
			return new String(Character.toChars(codePoint));
		}

		return String.valueOf(codePoint);
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("CharStackNode[class=");
		int[] range = ranges[0];
		sb.append(range[0]);
		sb.append('-');
		sb.append(range[1]);
		for(int i = 1; i<ranges.length; i++){
			sb.append(',');
			range = ranges[i];
			sb.append(range[0]);
			sb.append('-');
			sb.append(range[1]);
		}
		sb.append(",");
		sb.append(super.toString());
		sb.append(']');
		
		return sb.toString();
	}
	
	public int hashCode(){
		int hash = 0;
		
		for(int i = ranges.length - 1; i >= 0; --i){
			int[] range = ranges[i];
			hash = hash << 3 + hash >> 5;
			hash ^= range[0] +  (range[1] << 2);
		}
		
		return hash;
	}
	
	public boolean isEqual(AbstractStackNode<P> stackNode){
		if(!(stackNode instanceof CharStackNode)) return false;
		
		CharStackNode<P> otherNode = (CharStackNode<P>) stackNode;
		
		int[][] otherRanges = otherNode.ranges;
		if(ranges.length != otherRanges.length) return false;
		
		OUTER: for(int i = ranges.length - 1; i >= 0; --i){
			int[] range = ranges[i];
			for(int j = otherRanges.length - 1; j >= 0; --j){
				int[] otherRange = otherRanges[j];
				if(range[0] == otherRange[0] && range[1] == otherRange[1]) continue OUTER;
			}
			return false; // Could not find a certain range.
		}
		// Found all ranges.
		
		return hasEqualFilters(stackNode);
	}
}
