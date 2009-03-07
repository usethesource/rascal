package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class EmptySetException extends RascalSoftException {
	private static final long serialVersionUID = 5777799911110496681L;
	
	public EmptySetException(String message, AbstractAST node) {
		super("EmptySetException", message, node);
	}
}
