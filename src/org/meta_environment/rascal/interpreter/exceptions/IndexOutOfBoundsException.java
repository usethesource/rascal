package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class IndexOutOfBoundsException extends RascalSoftException {
	private static final long serialVersionUID = -8740824674121144282L;
	
	public IndexOutOfBoundsException(String message, AbstractAST node) {
		super("IndexOutOfBoundsException", message, node);
	}

}
