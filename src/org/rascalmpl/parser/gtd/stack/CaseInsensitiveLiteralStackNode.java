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
import org.rascalmpl.parser.gtd.result.LiteralNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public final class CaseInsensitiveLiteralStackNode<P> extends AbstractMatchableStackNode<P>{
	private final Object production;
	
	private final int[][] ciLiteral;
	
	private final AbstractNode result;
	
	public CaseInsensitiveLiteralStackNode(int id, int dot, Object production, int[] ciLiteral){
		super(id, dot);
		
		this.production = production;
		
		this.ciLiteral = fill(ciLiteral);
		
		result = null;
	}
	
	public CaseInsensitiveLiteralStackNode(int id, int dot, P production, int[] ciLiteral, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);
		
		this.production = production;
		
		this.ciLiteral = fill(ciLiteral);
		
		result = null;
	}
	
	private CaseInsensitiveLiteralStackNode(CaseInsensitiveLiteralStackNode<P> original, int startLocation){
		super(original, startLocation);
		
		production = original.production;
		
		ciLiteral = original.ciLiteral;
		
		result = null;
	}
	
	private CaseInsensitiveLiteralStackNode(CaseInsensitiveLiteralStackNode<P> original, int startLocation, AbstractNode result){
		super(original, startLocation);
		
		this.production = original.production;
		
		this.ciLiteral = original.ciLiteral;
		
		this.result = result;
	}
	
	private static int[][] fill(int[] ciLiteral){
		int nrOfCharacters = ciLiteral.length;
		int[][] ciLiteralResult = new int[nrOfCharacters][];
		for(int i = nrOfCharacters - 1; i >= 0; --i){
			int character = ciLiteral[i];
			int type = Character.getType(character);
			if(type == Character.LOWERCASE_LETTER){
				ciLiteralResult[i] = new int[]{character, Character.toUpperCase(character)};
			}else if(type == Character.UPPERCASE_LETTER){
				ciLiteralResult[i] = new int[]{character, Character.toLowerCase(character)};
			}else{
				ciLiteralResult[i] = new int[]{character};
			}
		}
		return ciLiteralResult;
	}
	
	public boolean isEmptyLeafNode(){
		return false;
	}
	
	public AbstractNode match(int[] input, int location){
		int literalLength = ciLiteral.length;
		int[] resultLiteral = new int[literalLength];
		OUTER : for(int i = literalLength - 1; i >= 0; --i){
			int[] ciLiteralPart = ciLiteral[i];
			for(int j = ciLiteralPart.length - 1; j >= 0; --j){
				int character = ciLiteralPart[j];
				if(character == input[location + i]){
					resultLiteral[i] = character;
					continue OUTER;
				}
			}
			return null; // Did not match.
		}
		
		return new LiteralNode(production, resultLiteral);
	}
	
	public AbstractStackNode<P> getCleanCopy(int startLocation){
		return new CaseInsensitiveLiteralStackNode<P>(this, startLocation);
	}
	
	public AbstractStackNode<P> getCleanCopyWithResult(int startLocation, AbstractNode result){
		return new CaseInsensitiveLiteralStackNode<P>(this, startLocation, result);
	}
	
	public int getLength(){
		return ciLiteral.length;
	}
	
	public AbstractNode getResult(){
		return result;
	}

	public int[][] getLiteral() {
		return ciLiteral;
	}
	
	@Override
	public String toShortString() {
		int[] codePoints = new int[ciLiteral.length];
		for (int i=0; i<ciLiteral.length; i++) {
			codePoints[i] = ciLiteral[i][0];
		}
		return new String(codePoints, 0, codePoints.length);
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < ciLiteral.length; ++i){
			sb.append(ciLiteral[i][0]);
		}
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
	
	public int hashCode(){
		return production.hashCode();
	}
	
	public boolean isEqual(AbstractStackNode<P> stackNode){
		if(!(stackNode instanceof CaseInsensitiveLiteralStackNode)) return false;
		
		CaseInsensitiveLiteralStackNode<P> otherNode = (CaseInsensitiveLiteralStackNode<P>) stackNode;
		
		if(!production.equals(otherNode.production)) return false;
		
		return hasEqualFilters(stackNode);
	}
}
