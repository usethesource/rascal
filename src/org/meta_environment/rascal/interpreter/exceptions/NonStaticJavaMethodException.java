package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.FunctionDeclaration;

public class NonStaticJavaMethodException extends TypeErrorException {
	private static final long serialVersionUID = -5754136192225457390L;

	public NonStaticJavaMethodException(FunctionDeclaration node) {
		super("Java functions without a body must be implemented by a static Java method", node);
	}

}
