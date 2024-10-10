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

public class MultiCharacterStackNode<P> extends AbstractMatchableStackNode<P>{
	private final P production;

	private final int[][] characters;

	private final AbstractNode result;

	public MultiCharacterStackNode(int id, int dot, P production, int[][] characters){
		super(id, dot);

		this.production = production;

		this.characters = characters;

		result = null;
	}

	public MultiCharacterStackNode(int id, int dot, P production, int[][] characters, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);

		this.production = production;

		this.characters = characters;

		result = null;
	}

	private MultiCharacterStackNode(MultiCharacterStackNode<P> original, int startLocation){
		super(original, startLocation);

		production = original.production;

		characters = original.characters;

		result = null;
	}

	private MultiCharacterStackNode(MultiCharacterStackNode<P> original, int startLocation, AbstractNode result){
		super(original, startLocation);

		this.production = original.production;

		this.characters = original.characters;

		this.result = result;
	}

	public boolean isEmptyLeafNode(){
		return false;
	}

	public AbstractNode match(int[] input, int location){
		int nrOfCharacters = characters.length;
		int[] resultArray = new int[nrOfCharacters];

		OUTER : for(int i = nrOfCharacters - 1; i >= 0; --i){
			int next = input[location + i];

			int[] alternatives = characters[i];
			for(int j = alternatives.length - 1; j >= 0; --j){
				int alternative = alternatives[j];
				if(next == alternative){
					resultArray[i] = alternative;
					continue OUTER;
				}
			}
			return null;
		}

		return new LiteralNode(production, resultArray);
	}

	public AbstractStackNode<P> getCleanCopy(int startLocation){
		return new MultiCharacterStackNode<P>(this, startLocation);
	}

	public AbstractStackNode<P> getCleanCopyWithResult(int startLocation, AbstractNode result){
		return new MultiCharacterStackNode<P>(this, startLocation, result);
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
		sb.append('[');
		for(int i = characters.length - 1; i >= 0; --i){
			int[] range = characters[i];
			sb.append(range[0]);
			sb.append('-');
			sb.append(range[1]);
		}
		sb.append(']');
		return toString();
	}

	@Override
	public String toString(){
		return toShortString();
	}

	@Override
	public int hashCode(){
		int hash = 0;

		for(int i = characters.length - 1; i >= 0; --i){
			int[] chars = characters[i];
			for(int j = chars.length - 1; j >= 0; --j){
				hash = hash << 3 + hash >> 5;
				hash ^= chars[0] +  (chars[1] << 2);
			}
		}

		return hash;
	}

	@Override
	public boolean equals(Object peer) {
		return super.equals(peer);
	}

	public boolean isEqual(AbstractStackNode<P> stackNode){
		if(!(stackNode instanceof MultiCharacterStackNode)) return false;

		MultiCharacterStackNode<P> otherNode = (MultiCharacterStackNode<P>) stackNode;

		int[][] otherCharacters = otherNode.characters;
		if(characters.length != otherCharacters.length) return false;

		for(int i = characters.length - 1; i >= 0; --i){
			int[] chars = characters[i];
			int[] otherChars = otherCharacters[i];
			if(chars.length != otherChars.length) return false;

			POS: for(int j = chars.length - 1; j <= 0; --j){
				int c = chars[j];
				for(int k = otherChars.length - 1; k <= 0; --k){
					if(c == otherChars[k]) continue POS;
				}
				return false;
			}
		}
		// Found all characters.

		return hasEqualFilters(stackNode);
	}

	@Override
	public <R> R accept(StackNodeVisitor<P,R> visitor) {
		return visitor.visit(this);
	}

}
