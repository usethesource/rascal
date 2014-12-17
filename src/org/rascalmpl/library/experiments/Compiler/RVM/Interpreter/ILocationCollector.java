package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;

public interface ILocationCollector {
	
	void registerLocation(ISourceLocation src);
	
	IValue get();

	void print(PrintWriter out);
	
}
