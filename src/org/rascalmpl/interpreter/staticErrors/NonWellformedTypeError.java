package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class NonWellformedTypeError extends StaticError {
	private static final long serialVersionUID = 1828978236838855920L;

	public NonWellformedTypeError(String reason, AbstractAST ast) {
		super("Non-well-formed type: " + reason, ast);
		printStackTrace();
	}
}


