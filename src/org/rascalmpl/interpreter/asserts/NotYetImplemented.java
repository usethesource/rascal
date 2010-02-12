package org.rascalmpl.interpreter.asserts;

import org.rascalmpl.ast.AbstractAST;


public final class NotYetImplemented extends AssertionError {
	private static final long serialVersionUID = -8740312542969306482L;

	public NotYetImplemented(String message) {
		super("Not yet implemented " + message);
	}
	
	public NotYetImplemented(AbstractAST ast) {
		super(ast.toString());
	}
}
