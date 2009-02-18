package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

// TODO: something is wrong here, Error is a user code exception, while
// TypeError is thrown by the implementation of Rascal...
public class TypeError extends Error {
	private static final long serialVersionUID = 33333767154564288L;
   
	//TODO: remove, only one reference remaining in Result.
	public TypeError(String message) {
		super(null, message);
	}

	public TypeError(String message, AbstractAST node) {
		super("TypeError", message, node);
	}
}
