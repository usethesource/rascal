package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class SyntaxErrorException extends RascalException {
	private static final long serialVersionUID = 333331541118811177L;
	 
	//TODO: remove
	public SyntaxErrorException(String message) {
		super("SyntaxErrorException", message);
	}
	
	public SyntaxErrorException(String message, AbstractAST node) {
		super("SyntaxErrorException", message, node);
	}
}
