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
package org.rascalmpl.parser.gtd.result.action;

import org.eclipse.imp.pdb.facts.IConstructor;

/**
 * A dummy action executor.
 */
public class VoidActionExecutor implements IActionExecutor{
	
	public VoidActionExecutor(){
		super();
	}
	
	public Object createRootEnvironment(){
		return VoidEnvironment.ROOT_VOID_ENVIRONMENT; // Don't bother with environments.
	}
	
	public void completed(Object environment, boolean filtered){
		// Don't do anything.
	}
	
	public Object enteringProduction(IConstructor production, Object environment){
		return VoidEnvironment.ROOT_VOID_ENVIRONMENT; // Don't bother with environments.
	}
	
	public Object enteringListProduction(IConstructor production, Object environment){
		return VoidEnvironment.ROOT_VOID_ENVIRONMENT; // Don't bother with environments.
	}
	
	public Object enteringNode(IConstructor production, int index, Object environment){
		return VoidEnvironment.ROOT_VOID_ENVIRONMENT; // Don't bother with environments.
	}
	
	public Object enteringListNode(IConstructor production, int index, Object environment){
		return VoidEnvironment.ROOT_VOID_ENVIRONMENT; // Don't bother with environments.
	}
	
	public void exitedProduction(IConstructor production, boolean filtered, Object environment){
		// Don't do anything.
	}
	
	public void exitedListProduction(IConstructor production, boolean filtered, Object environment){
		// Don't do anything.
	}
	
	public IConstructor filterProduction(IConstructor tree, Object environment){
		return tree;
	}
	
	public IConstructor filterListProduction(IConstructor tree, Object environment){
		return tree;
	}
	
	public IConstructor filterAmbiguity(IConstructor ambCluster, Object environment){
		return ambCluster;
	}
	
	public IConstructor filterListAmbiguity(IConstructor ambCluster, Object environment){
		return ambCluster;
	}
	
	public IConstructor filterCycle(IConstructor cycle, Object environment){
		return cycle;
	}
	
	public IConstructor filterListCycle(IConstructor cycle, Object environment){
		return cycle;
	}
	
	public boolean isImpure(IConstructor rhs){
		return false;
	}
}
