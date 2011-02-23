package org.rascalmpl.parser.gtd.result.action;

import org.eclipse.imp.pdb.facts.IConstructor;

public interface IActionExecutor{
	IConstructor filterProduction(IConstructor tree);
	
	IConstructor filterAmbiguity(IConstructor ambCluster);
}
