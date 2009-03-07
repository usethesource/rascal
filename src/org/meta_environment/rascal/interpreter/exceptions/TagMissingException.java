package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.FunctionDeclaration;

public class TagMissingException extends TypeErrorException {
	private static final long serialVersionUID = 8951799317606455150L;
    
	public TagMissingException(FunctionDeclaration func) {
		super("Java Function without body is missing a javaClass tag", func);
	}
}
