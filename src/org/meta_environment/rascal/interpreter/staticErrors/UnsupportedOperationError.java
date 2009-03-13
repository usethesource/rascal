package org.meta_environment.rascal.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;

public class UnsupportedOperationError extends StaticError {
	private static final long serialVersionUID = 4097641812252466488L;
	
	public UnsupportedOperationError(String operation, Type on, AbstractAST ast) {
		super(operation + " not supported on " + on, ast);
	}
	
	public UnsupportedOperationError(String operation, Type on1, Type on2, AbstractAST ast) {
		super(operation + " not supported on " + on1 + " and " + on2, ast);
	}
}
