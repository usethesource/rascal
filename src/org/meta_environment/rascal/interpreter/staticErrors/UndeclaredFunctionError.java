package org.meta_environment.rascal.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.meta_environment.rascal.ast.AbstractAST;

public class UndeclaredFunctionError extends StaticError {
	private static final long serialVersionUID = -3215674987633177L;
	
	public UndeclaredFunctionError(String name, AbstractAST node) {
		super("Undeclared function: " + name, node);
	}

	public UndeclaredFunctionError(String name, ISourceLocation location) {
		super("Undeclared function: " + name, location);
	}
}
