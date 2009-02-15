package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class NoSuchFileError extends Error {
	
	private static final long serialVersionUID = -7406567415987633177L;
	 
	public NoSuchFileError(String message) {
		super(null, message);
	}
	
	public NoSuchFileError(String message, AbstractAST node) {
		super(message, null, node);
	}
	
	public NoSuchFileError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
