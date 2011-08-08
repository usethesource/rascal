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

/**
 * A literal result node.
 */
public class LiteralNode extends AbstractNode{
	public final static int ID = 3;
	
	private final Object production;
	private final char[] content;
	
	public LiteralNode(Object production, char[] content){
		super();
		
		this.production = production;
		this.content = content;
	}
	
	public int getTypeIdentifier(){
		return ID;
	}
	
	/**
	 * Returns the production associated with this literal.
	 */
	public Object getProduction(){
		return production;
	}
	
	/**
	 * Returns the substring this literal matched.
	 */
	public char[] getContent(){
		return content;
	}
	
	/**
	 * Literals aren't allowed to be zero length.
	 */
	public boolean isEmpty(){
		return false;
	}
	
	/**
	 * Literals aren't non-terminals.
	 */
	public boolean isNonterminalSeparator(){
		return false;
	}
}
