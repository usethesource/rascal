package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.FunctionDeclaration;

public class NonAbstractJavaFunctionError extends StaticError {
	private static final long serialVersionUID = -5754136192225457390L;

	public NonAbstractJavaFunctionError(FunctionDeclaration node) {
		super("Java function has a body", node);
	}
}
