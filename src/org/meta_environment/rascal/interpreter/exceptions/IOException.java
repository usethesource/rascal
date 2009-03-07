package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class IOException extends RascalSoftException {
	private static final long serialVersionUID = 674066674121144282L;
	
	public IOException(String message, AbstractAST node) {
		super("IOException", message, node);
	}

}
