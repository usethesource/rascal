package org.rascalmpl.parser.gtd.result.action;

import org.eclipse.imp.pdb.facts.IConstructor;

public class VoidActionExecutor implements IActionExecutor{
	
	public VoidActionExecutor(){
		super();
	}
	
	public IConstructor filterAmbiguity(IConstructor ambCluster){
		return ambCluster;
	}
	
	public IConstructor filterProduction(IConstructor tree){
		return tree;
	}
}
