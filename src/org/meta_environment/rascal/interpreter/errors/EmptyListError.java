package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class EmptyListError extends Error {
	private static final long serialVersionUID = 966334948133121000L;
	 
	public EmptyListError(String message) {
		super(null, message);
	}
	
	public EmptyListError(String message, AbstractAST node) {
		super(message, null, node);
	}
	
	public EmptyListError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
