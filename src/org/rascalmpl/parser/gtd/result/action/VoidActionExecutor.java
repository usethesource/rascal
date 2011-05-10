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

public class VoidActionExecutor implements IActionExecutor{
	
	public VoidActionExecutor(){
		super();
	}
	
	public IEnvironment createRootEnvironment(){
		return VoidEnvironment.ROOT_VOID_ENVIRONMENT; // Don't bother with environments.
	}
	
	public IEnvironment enteredProduction(IConstructor production, IEnvironment environment){
		return VoidEnvironment.ROOT_VOID_ENVIRONMENT; // Don't bother with environments.
	}
	
	public IEnvironment split(IConstructor production, int index, IEnvironment environment){
		return VoidEnvironment.ROOT_VOID_ENVIRONMENT; // Don't bother with environments.
	}
	
	public void exitedProduction(IConstructor production, boolean filtered, IEnvironment environment){
		// Don't do anything.
	}
	
	public IConstructor filterProduction(IConstructor tree, IEnvironment environment){
		return tree;
	}
	
	public IConstructor filterAmbiguity(IConstructor ambCluster, IEnvironment environment){
		return ambCluster;
	}
	
	public IConstructor filterCycle(IConstructor cycle, IEnvironment environment){
		return cycle;
	}
}
