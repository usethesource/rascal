package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public interface ILocationReporter {
	
	void setLocation(ISourceLocation src);
	
	void start();
	
	void stop();
	
	void report(PrintWriter out);
	
}
