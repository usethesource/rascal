package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class SubscriptException extends RascalSoftException {
	private static final long serialVersionUID = 77321541118811177L;
	
	public SubscriptException(String message, AbstractAST node) {
		super("SubscriptException", message, node);
	}
}
