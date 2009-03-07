package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class EmptyMapException extends RascalSoftException {
	private static final long serialVersionUID = 555779952495556681L;
	
	public EmptyMapException(String message, AbstractAST node) {
		super("EmptyMapException", message, node);
	}
}
