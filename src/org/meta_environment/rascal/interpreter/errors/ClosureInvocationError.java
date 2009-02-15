package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class ClosureInvocationError extends Error {
	 
	public ClosureInvocationError(String message) {
		super(null, message);
	}
	
	public ClosureInvocationError(String message, AbstractAST node) {
		super(message, null, node);
	}
	
	public ClosureInvocationError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
