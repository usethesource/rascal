package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class NoSuchModuleException extends RascalException {
	private static final long serialVersionUID = -3215674111118811111L;
	
	public NoSuchModuleException(String message, AbstractAST node) {
		super("NoSuchModuleException", message, node);
	}

}
