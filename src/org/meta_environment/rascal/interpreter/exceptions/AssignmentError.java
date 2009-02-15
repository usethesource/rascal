package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class AssignmentError extends RascalException {
	 
	public AssignmentError(String message) {
		super(null, message);
	}
	
	public AssignmentError(String message, AbstractAST node) {
		super(message, null, node);
	}
	
	public AssignmentError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public boolean hasCause() {
		return getCause() != null;
	}
}
