package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public interface ILocationCollector {
	
	void registerLocation(ISourceLocation src);
	
}
