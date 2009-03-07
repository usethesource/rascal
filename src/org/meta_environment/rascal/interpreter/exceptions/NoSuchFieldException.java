package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class NoSuchFieldException extends RascalException {
	private static final long serialVersionUID = -7406655567412555533L;
	
	public NoSuchFieldException(String message, AbstractAST node) {
		super("NoSuchFieldException", message, node);
	}
}
