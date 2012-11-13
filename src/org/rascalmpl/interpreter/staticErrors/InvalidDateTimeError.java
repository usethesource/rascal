package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class InvalidDateTimeError extends StaticError {

	private static final long serialVersionUID = -4152023566980099467L;

	public InvalidDateTimeError(String message, AbstractAST ast) {
		super("Unable to create datetime value, components out of range: " + message, ast);
	}

}
