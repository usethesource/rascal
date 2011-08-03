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
package org.rascalmpl.parser.gtd.result.error;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.result.AbstractNode;

public class ExpectedNode extends AbstractNode{
	public final static int ID = 8;
	
	private final AbstractNode[] mismatchedChildren;
	private final IConstructor symbol;
	
	private final URI input;
	private final int offset;
	private final int endOffset;
	
	private final boolean isSeparator;
	private final boolean isLayout;
	
	public ExpectedNode(AbstractNode[] mismatchedChildren, IConstructor symbol, URI input, int offset, int endOffset, boolean isSeparator, boolean isLayout){
		super();
		
		this.mismatchedChildren = mismatchedChildren;
		this.symbol = symbol;
		
		this.input = input;
		this.offset = offset;
		this.endOffset = endOffset;
		
		this.isSeparator = isSeparator;
		this.isLayout = isLayout;
	}
	
	public int getTypeIdentifier(){
		return ID;
	}
	
	public AbstractNode[] getMismatchedChildren(){
		return mismatchedChildren;
	}
	
	public IConstructor getSymbol(){
		return symbol;
	}
	
	public URI getInput(){
		return input;
	}
	
	public int getOffset(){
		return offset;
	}
	
	public int getEndOffset(){
		return endOffset;
	}
	
	public boolean isLayout(){
		return isLayout;
	}
	
	public boolean isEmpty(){
		return false;
	}
	
	public boolean isNonterminalSeparator(){
		return isSeparator;
	}
}
