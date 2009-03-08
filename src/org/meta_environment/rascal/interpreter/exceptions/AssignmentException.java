package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class AssignmentException extends RascalException {
	private static final long serialVersionUID = 5709066456663349481L;
	
	public AssignmentException(String message, AbstractAST node) {
		super("AssignmentException", message, node);
	}
}
