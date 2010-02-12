package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class RedeclaredTypeError extends StaticError {
	private static final long serialVersionUID = 4898431127235860576L;

	public RedeclaredTypeError(String name, AbstractAST decl) {
		super("Illegal re-declaration of type: " + name, decl);
	}

}
