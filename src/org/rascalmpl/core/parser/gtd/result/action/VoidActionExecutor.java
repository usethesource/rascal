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
package org.rascalmpl.core.parser.gtd.result.action;


/**
 * A dummy action executor.
 */
public class VoidActionExecutor<T> implements IActionExecutor<T>{
	
	public VoidActionExecutor(){
		super();
	}
	
	public Object createRootEnvironment(){
		return VoidEnvironment.ROOT_VOID_ENVIRONMENT; // Don't bother with environments.
	}
	
	public void completed(Object environment, boolean filtered){
		// Don't do anything.
	}
	
	public Object enteringProduction(Object production, Object environment){
		return VoidEnvironment.ROOT_VOID_ENVIRONMENT; // Don't bother with environments.
	}
	
	public Object enteringListProduction(Object production, Object environment){
		return VoidEnvironment.ROOT_VOID_ENVIRONMENT; // Don't bother with environments.
	}
	
	public Object enteringNode(Object production, int index, Object environment){
		return VoidEnvironment.ROOT_VOID_ENVIRONMENT; // Don't bother with environments.
	}
	
	public Object enteringListNode(Object production, int index, Object environment){
		return VoidEnvironment.ROOT_VOID_ENVIRONMENT; // Don't bother with environments.
	}
	
	public void exitedProduction(Object production, boolean filtered, Object environment){
		// Don't do anything.
	}
	
	public void exitedListProduction(Object production, boolean filtered, Object environment){
		// Don't do anything.
	}
	
	public T filterProduction(T tree, Object environment){
		return tree;
	}
	
	public T filterListProduction(T tree, Object environment){
		return tree;
	}
	
	public T filterAmbiguity(T ambCluster, Object environment){
		return ambCluster;
	}
	
	public T filterListAmbiguity(T ambCluster, Object environment){
		return ambCluster;
	}
	
	public T filterCycle(T cycle, Object environment){
		return cycle;
	}
	
	public T filterListCycle(T cycle, Object environment){
		return cycle;
	}
	
	public boolean isImpure(Object rhs){
		return false;
	}
}
