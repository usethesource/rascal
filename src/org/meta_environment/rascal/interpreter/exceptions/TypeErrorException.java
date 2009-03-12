package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

// TODO: something is wrong here, Error is a user code exception, while
// TypeError is thrown by the implementation of Rascal...
public class TypeErrorException extends RascalException {
	private static final long serialVersionUID = 33333767154564288L;
   
	//TODO: remove, only one reference remaining in Result.
	public TypeErrorException(String message) {
		super("TypeErrorException", message);
	}

	public TypeErrorException(String message, AbstractAST node) {
		super("TypeErrorException", message, node);
	}
}
