package org.rascalmpl.parser;

import org.eclipse.imp.pdb.facts.IConstructor;

public class VoidActionExecutor implements IActionExecutor{
	
	public VoidActionExecutor(){
		super();
	}
	
	public IConstructor filterAppl(IConstructor tree){
		return tree;
	}
}
