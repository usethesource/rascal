package org.meta_environment.rascal.std;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.asserts.NotYetImplemented;

public class ParseTree {

	static public IValue parse(IConstructor start, ISourceLocation input) {
		// TODO implement parse function
		throw new NotYetImplemented("parse function");
	}

	static public IValue parse(ISourceLocation input) {
		throw new NotYetImplemented("parse function");
	}
}
