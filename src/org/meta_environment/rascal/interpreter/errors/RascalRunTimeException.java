package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

/*
 * This class is for representing runtime errors during the execution of
 * Rascal programs. Examples are subscript out of bounds and the like.
 */

public class RascalRunTimeException extends RascalException {
	
	private static final long serialVersionUID = 3715676299644311671L;
	
	public RascalRunTimeException(String message) {
		super(message);
	}
	
	public RascalRunTimeException(String message, AbstractAST ast) {
		super(message, ast);
	}
/*	
	public RascalRunTimeError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
	*/
}
