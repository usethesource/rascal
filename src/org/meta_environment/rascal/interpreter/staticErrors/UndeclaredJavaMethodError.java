package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.FunctionDeclaration;

public class UndeclaredJavaMethodError extends StaticError {
	private static final long serialVersionUID = -3645474482816345282L;

	public UndeclaredJavaMethodError(String name, FunctionDeclaration func) {
		super("No such Java method: " + name, func);
	}
}
