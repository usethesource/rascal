package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class UndefinedValueException extends RascalSoftException {
	private static final long serialVersionUID = -7290225483329876543L;
    
    public UndefinedValueException(String message, AbstractAST ast) {
		super("UndefinedValueException", message, ast);
	}
}
