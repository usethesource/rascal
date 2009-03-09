package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class IllegalSourceLocationException extends TypeErrorException {
	private static final long serialVersionUID = -6450963485067644130L;

	public IllegalSourceLocationException(AbstractAST node) {
		super("Illegal source location", node);
	}
}
