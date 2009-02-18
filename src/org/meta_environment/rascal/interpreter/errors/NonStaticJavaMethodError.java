package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.FunctionDeclaration;

public class NonStaticJavaMethodError extends TypeError {
	private static final long serialVersionUID = -5754136192225457390L;

	public NonStaticJavaMethodError(FunctionDeclaration node) {
		super("Java functions without a body must be implemented by a static Java method", node);
	}

}
