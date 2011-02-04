package org.rascalmpl.parser.gtd.result.action;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISet;

public class VoidActionExecutor implements IActionExecutor{
	
	public VoidActionExecutor(){
		super();
	}
	
	public ISet filterAmbiguity(ISet ambCluster){
		return ambCluster;
	}
	
	public IConstructor filterProduction(IConstructor tree){
		return tree;
	}
}
