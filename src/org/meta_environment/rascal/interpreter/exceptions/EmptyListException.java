package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class EmptyListException extends RascalSoftException {
	private static final long serialVersionUID = 966334948133121000L;
	
	public EmptyListException(String message, AbstractAST node) {
		super("EmptyListException", message, node);
	}

}
