package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

public interface ILocationReporter<T> {
	
	T getData();

	void printData(PrintWriter out);
}
