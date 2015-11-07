package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import java.io.PrintWriter;

public interface IFrameReporter<T> {
	
	T getData();

	void report(T data, PrintWriter out);
}
