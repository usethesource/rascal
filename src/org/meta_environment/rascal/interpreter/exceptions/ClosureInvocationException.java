package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class ClosureInvocationException extends RascalException {
	private static final long serialVersionUID = 570911223306604481L;
	 
	
	public ClosureInvocationException(String message, AbstractAST node) {
		super("ClosureInvocationException", message, node);
	}

}
