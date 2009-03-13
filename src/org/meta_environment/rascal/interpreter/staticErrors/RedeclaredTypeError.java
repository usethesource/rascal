package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class RedeclaredTypeError extends StaticError {
	private static final long serialVersionUID = 4898431127235860576L;

	public RedeclaredTypeError(String name, AbstractAST decl) {
		super("Illegal re-declaration of type: " + name, decl);
	}

}
