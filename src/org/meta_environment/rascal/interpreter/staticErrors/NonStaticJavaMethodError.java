package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.FunctionDeclaration;

public class NonStaticJavaMethodError extends StaticError {
	private static final long serialVersionUID = -5754136192225457390L;

	public NonStaticJavaMethodError(FunctionDeclaration node) {
		super("Non-static Java Method", node);
	}
}
