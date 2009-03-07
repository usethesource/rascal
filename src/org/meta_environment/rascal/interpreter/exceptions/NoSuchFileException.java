package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class NoSuchFileException extends RascalSoftException {
	private static final long serialVersionUID = -7406567415987633177L;
	
	//TODO: remove
	public NoSuchFileException(String message) {
		super(null, message);
	}
	
	public NoSuchFileException(String message, AbstractAST node) {
		super("NoSuchFileException", message, node);
	}
}
