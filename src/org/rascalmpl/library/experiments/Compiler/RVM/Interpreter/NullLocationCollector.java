package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.values.ValueFactoryFactory;

public class NullLocationCollector implements ILocationCollector {

	public void registerLocation(ISourceLocation src) {
		// Nothing
	}

	@Override
	public IValue get() {
		return ValueFactoryFactory.getValueFactory().set();
	}

	@Override
	public void print(PrintWriter out) {
		// Nothing
	}
}
