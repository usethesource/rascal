package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

/*
 * This class is for representing runtime errors during the execution of
 * Rascal programs. Examples are subscript out of bounds and the like.
 */

public class RunTimeException extends RascalException {
	private static final long serialVersionUID = 3715676299644311671L;
	
	public RunTimeException(String message, AbstractAST ast) {
		super("RunTimeException", message, ast);
	}
	
	public RunTimeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public boolean hasCause() {
		return getCause() != null;
	}

}
