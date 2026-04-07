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
package org.rascalmpl.parser.gtd.result;

/**
 * A character result node.
 */
public class CharNode extends AbstractNode{
	public final static int ID = 2;
	
	private final static CharNode[] charNodeConstants = new CharNode[128];
	
	private final int character;
	
	public CharNode(int character){
		super();
		
		this.character = character;
	}
	
	public int getTypeIdentifier(){
		return ID;
	}
	
	/**
	 * Retrieve the character in this result.
	 */
	public int getCharacter(){
		return character;
	}
	
	/**
	 * Characters have a length.
	 */
	public boolean isEmpty(){
		return false;
	}
	
	/**
	 * Characters aren't non-terminals.
	 */
	public boolean isNonterminalSeparator(){
		return false;
	}

	
	@Override
	public String toString() {
		return "CharNode[c=" + character + "]";
	}

	/**
	 * Create a new character node.
	 * If it's a 7 bit ascii character cache it as well, so we can reuse it.
	 */
	public static CharNode createCharNode(int character){
		if(character < charNodeConstants.length){
			CharNode charNode = charNodeConstants[character];
			if(charNode != null) return charNode;
			
			return (charNodeConstants[character] = new CharNode(character));
		}
		
		return new CharNode(character);
	}
}
