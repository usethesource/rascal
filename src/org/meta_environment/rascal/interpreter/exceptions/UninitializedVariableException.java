package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class UninitializedVariableException extends RascalSoftException {
	
	private static final long serialVersionUID = -44225561154564288L;
	
	public UninitializedVariableException(String message, AbstractAST node) {
		super("UninitializedVariableException", message, node);
	}
}
