/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.result;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.ArrayList;

public abstract class AbstractContainerNode extends AbstractNode{
	private final static TypeFactory TF = TypeFactory.getInstance();
	private final static TypeStore typeStore = new TypeStore();
	protected final static Type CACHED_RESULT_TYPE = TF.abstractDataType(typeStore, "cached");
	protected final static Type FILTERED_RESULT_TYPE = TF.constructor(typeStore, CACHED_RESULT_TYPE, "filtered", TF.valueType());
	protected final static IConstructor FILTERED_RESULT = VF.constructor(FILTERED_RESULT_TYPE, VF.node("EMPTY"));
	protected final static IList EMPTY_LIST = VF.list();
	
	protected final URI input;
	protected final int offset;
	protected final int endOffset;
	
	protected boolean rejected;
	
	protected final boolean isNullable;
	protected final boolean isSeparator;
	protected final boolean isLayout;

	protected Link firstAlternative;
	protected IConstructor firstProduction;
	protected ArrayList<Link> alternatives;
	protected ArrayList<IConstructor> productions;
	
	public AbstractContainerNode(URI input, int offset, int endOffset, boolean isNullable, boolean isSeparator, boolean isLayout){
		super();
		
		this.input = input;
		this.offset = offset;
		this.endOffset = endOffset;
		
		this.isNullable = isNullable;
		this.isSeparator = isSeparator;
		this.isLayout = isLayout;
	}
	
	public void addAlternative(IConstructor production, Link children){
		if(firstAlternative == null){
			firstAlternative = children;
			firstProduction = production;
		}else{
			if(alternatives == null){
				alternatives = new ArrayList<Link>(1);
				productions = new ArrayList<IConstructor>(1);
			}
			alternatives.add(children);
			productions.add(production);
		}
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	public boolean isEmpty(){
		return isNullable;
	}
	
	public boolean isSeparator(){
		return isSeparator;
	}
	
	public void setRejected(){
		rejected = true;
		
		// Clean up.
		firstAlternative = null;
		alternatives = null;
		productions = null;
	}
	
	public boolean isRejected(){
		return rejected;
	}
}
