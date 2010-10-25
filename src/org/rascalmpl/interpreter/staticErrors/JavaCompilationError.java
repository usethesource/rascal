package org.rascalmpl.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.ast.AbstractAST;

public class JavaCompilationError extends StaticError {
	private static final long serialVersionUID = 3200356264732532487L;

	public JavaCompilationError(String message, AbstractAST ast) {
		super("Java compilation failed due to " + message, ast);
	}
	
	public JavaCompilationError(String message, ISourceLocation loc) {
		super("Java compilation failed due to " + message, loc);
	}

}
