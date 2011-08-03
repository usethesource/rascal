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
package org.rascalmpl.parser.gtd.result;

public class CharNode extends AbstractNode{
	public final static int ID = 2;
	
	private final static CharNode[] charNodeConstants = new CharNode[128];
	
	private final char character;
	
	public CharNode(char character){
		super();
		
		this.character = character;
	}
	
	public int getTypeIdentifier(){
		return ID;
	}
	
	public int getCharValue(){
		return character;
	}
	
	public boolean isEmpty(){
		return false;
	}
	
	public boolean isSeparator(){
		return false;
	}
	
	// Cache the results for all 7-bit ascii characters.
	public static CharNode createCharNode(char character){
		if(character < charNodeConstants.length){
			CharNode charNode = charNodeConstants[character];
			if(charNode != null) return charNode;
			
			return (charNodeConstants[character] = new CharNode(character));
		}
		
		return new CharNode(character);
	}
}
