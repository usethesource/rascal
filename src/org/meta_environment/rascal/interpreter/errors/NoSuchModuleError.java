package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class NoSuchModuleError extends Error {
	private static final long serialVersionUID = -3215674111118811111L;
	
	public NoSuchModuleError(String message, AbstractAST node) {
		super("NoSuchModuleError", message, node);
	}

}
