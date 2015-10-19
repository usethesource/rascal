package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.rascalmpl.value.ISourceLocation;

public class NullLocationCollector implements ILocationCollector {
	
	private static final NullLocationCollector instance = new NullLocationCollector();
	
	private NullLocationCollector() { }
	
	public static NullLocationCollector getInstance() {
		return instance;
	}

	public void registerLocation(ISourceLocation src) {
		//System.err.println("NullLocationCollector.registerLocation: " + src);
	}
}
