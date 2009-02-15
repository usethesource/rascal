package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class AssignmentError extends Error {
	private static final long serialVersionUID = 5709066456663349481L;
	 
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
