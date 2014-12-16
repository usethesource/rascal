package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public class NullLocationReporter implements ILocationReporter {

	public void setLocation(ISourceLocation src) {
		// Nothing
	}

	@Override
	public void start() {
		// nothing
	}

	@Override
	public void stop() {
		// nothing
	}

	@Override
	public void report(PrintWriter out) {
		// nothing
	}
}
