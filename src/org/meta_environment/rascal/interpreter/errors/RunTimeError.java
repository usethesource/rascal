package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

/*
 * This class is for representing runtime errors during the execution of
 * Rascal programs. Examples are subscript out of bounds and the like.
 */

public class RunTimeError extends Error {
	
	private static final long serialVersionUID = 3715676299644311671L;
	
	public RunTimeError(String message) {
		super(null, message);
	}
	
	public RunTimeError(String message, AbstractAST ast) {
		super(message, null, ast);
	}
	
	public RunTimeError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}

}
