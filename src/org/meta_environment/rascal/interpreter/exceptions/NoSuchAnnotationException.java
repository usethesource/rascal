package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class NoSuchAnnotationException extends RascalSoftException {
	private static final long serialVersionUID = -7406667412199993333L;
	
	public NoSuchAnnotationException(String message, AbstractAST node) {
		super("NoSuchAnnotationException", message, node);
	}
	
}
