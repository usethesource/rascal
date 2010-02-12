package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class JavaCompilationError extends StaticError {
	private static final long serialVersionUID = 3200356264732532487L;

	public JavaCompilationError(String message, AbstractAST ast) {
		super("Java compilation failed due to " + message, ast);
	}

}
