package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.FunctionDeclaration;

public class TagMissingError extends TypeError {
	private static final long serialVersionUID = 8951799317606455150L;
    
	public TagMissingError(FunctionDeclaration func) {
		super("Java Function without body is missing a javaClass tag", func);
	}
}
