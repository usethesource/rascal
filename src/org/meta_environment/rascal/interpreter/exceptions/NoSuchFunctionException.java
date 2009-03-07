package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class NoSuchFunctionException extends RascalException {
	private static final long serialVersionUID = -3215674987633177L;
	
	public NoSuchFunctionException(String message, AbstractAST node) {
		super("NoSuchFunctionException", message, node);
	}

}
