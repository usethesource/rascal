package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class NoSuchModuleError extends Error {
	 
	public NoSuchModuleError(String message) {
		super(null, message);
	}
	
	public NoSuchModuleError(String message, AbstractAST node) {
		super(message, null, node);
	}
	
	public NoSuchModuleError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
