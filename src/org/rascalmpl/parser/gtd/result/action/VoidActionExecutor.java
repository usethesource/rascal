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
	private final static VoidEnvironment ROOT_VOID_ENVIRONMENT = new VoidEnvironment();
	
	public VoidActionExecutor(){
		super();
	}
	
	public IEnvironment createEnvironment(IEnvironment parent){
		return ROOT_VOID_ENVIRONMENT; // Don't bother with environments.
	}
	
	public IConstructor filterProduction(IConstructor tree){
		return tree;
	}
	
	public IConstructor filterAmbiguity(IConstructor ambCluster){
		return ambCluster;
	}
	
	public IConstructor filterCycle(IConstructor cycle){
		return cycle;
	}
	
	public void enteredProduction(IConstructor production){
		// Don't do anything.
	}
	
	public void exitedProduction(IConstructor production, boolean filtered){
		// Don't do anything.
	}
}
