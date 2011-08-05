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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.LiteralNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public final class CaseInsensitiveLiteralStackNode extends AbstractStackNode implements IMatchableStackNode{
	private final IConstructor production;
	
	private final char[][] ciLiteral;
	
	private final AbstractNode result;
	
	public CaseInsensitiveLiteralStackNode(int id, int dot, IConstructor production, char[] ciLiteral){
		super(id, dot);
		
		this.production = production;
		
		this.ciLiteral = fill(ciLiteral);
		
		result = null;
	}
	
	public CaseInsensitiveLiteralStackNode(int id, int dot, IConstructor production, char[] ciLiteral, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);
		
		this.production = production;
		
		this.ciLiteral = fill(ciLiteral);
		
		result = null;
	}
	
	private CaseInsensitiveLiteralStackNode(CaseInsensitiveLiteralStackNode original, int startLocation){
		super(original, startLocation);
		
		production = original.production;
		
		ciLiteral = original.ciLiteral;
		
		result = null;
	}
	
	private CaseInsensitiveLiteralStackNode(CaseInsensitiveLiteralStackNode original, int startLocation, AbstractNode result){
		super(original, startLocation);
		
		this.production = original.production;
		
		this.ciLiteral = original.ciLiteral;
		
		this.result = result;
	}
	
	private char[][] fill(char[] ciLiteral){
		int nrOfCharacters = ciLiteral.length;
		char[][] ciLiteralResult = new char[nrOfCharacters][];
		for(int i = nrOfCharacters - 1; i >= 0; --i){
			char character = ciLiteral[i];
			int type = Character.getType(character);
			if(type == Character.LOWERCASE_LETTER){
				ciLiteralResult[i] = new char[]{character, Character.toUpperCase(character)};
			}else if(type == Character.UPPERCASE_LETTER){
				ciLiteralResult[i] = new char[]{character, Character.toLowerCase(character)};
			}else{
				ciLiteralResult[i] = new char[]{character};
			}
		}
		return ciLiteralResult;
	}
	
	public boolean isEmptyLeafNode(){
		return false;
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractNode match(char[] input, int location){
		int literalLength = ciLiteral.length;
		char[] resultLiteral = new char[literalLength];
		OUTER : for(int i = literalLength - 1; i >= 0; --i){
			char[] ciLiteralPart = ciLiteral[i];
			for(int j = ciLiteralPart.length - 1; j >= 0; --j){
				char character = ciLiteralPart[j];
				if(character == input[location + i]){
					resultLiteral[i] = character;
					continue OUTER;
				}
			}
			return null; // Did not match.
		}
		
		return new LiteralNode(production, resultLiteral);
	}
	
	public boolean isClean(){
		return true;
	}
	
	public AbstractStackNode getCleanCopy(int startLocation){
		return new CaseInsensitiveLiteralStackNode(this, startLocation);
	}
	
	public AbstractStackNode getCleanCopyWithResult(int startLocation, AbstractNode result){
		return new CaseInsensitiveLiteralStackNode(this, startLocation, result);
	}
	
	public int getLength(){
		return ciLiteral.length;
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
	
	public boolean isEqual(AbstractStackNode stackNode){
		if(!(stackNode instanceof CaseInsensitiveLiteralStackNode)) return false;
		
		CaseInsensitiveLiteralStackNode otherNode = (CaseInsensitiveLiteralStackNode) stackNode;
		
		if(!production.isEqual(otherNode.production)) return false;
		
		return hasEqualFilters(stackNode);
	}
}
