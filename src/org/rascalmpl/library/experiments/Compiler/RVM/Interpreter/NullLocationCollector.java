package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public class NullLocationCollector implements ILocationCollector {

	public void registerLocation(ISourceLocation src) {
		//System.err.println("NullLocationCollector.registerLocation: " + src);
	}
}
