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
 * A epsilon result node.
 */
public class EpsilonNode extends AbstractNode{
	public final static int ID = 1;
	
	public EpsilonNode(){
		super();
	}
	
	public int getTypeIdentifier(){
		return ID;
	}
	
	/**
	 * Epsilons are empty.
	 */
	public boolean isEmpty(){
		return true;
	}
	
	/**
	 * Epsilons aren't non-terminals.
	 */
	public boolean isNonterminalSeparator(){
		return false;
	}
	
	public boolean isEpsilon(){
		return true;
	}

	@Override
	public String toString() {
		return "EpsilonNode[]";
	}

}
