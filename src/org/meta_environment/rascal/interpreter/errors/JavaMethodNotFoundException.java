package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.FunctionDeclaration;

public class JavaMethodNotFoundException extends TypeError {
	private static final long serialVersionUID = -3645474482816345282L;

	public JavaMethodNotFoundException(FunctionDeclaration func) {
		super("Unable to find static method for Java function without body", func);
	}
}
