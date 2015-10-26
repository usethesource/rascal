package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.rascalmpl.value.ISourceLocation;

public interface ILocationCollector {
	
	void registerLocation(ISourceLocation src);
	
}
