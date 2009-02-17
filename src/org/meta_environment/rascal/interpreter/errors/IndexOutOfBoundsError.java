package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class IndexOutOfBoundsError extends Error {
	private static final long serialVersionUID = -8740824674121144282L;
	
	public IndexOutOfBoundsError(String message, AbstractAST node) {
		super("IndexOutOfBoundsError", message, node);
	}
	
	public IndexOutOfBoundsError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
