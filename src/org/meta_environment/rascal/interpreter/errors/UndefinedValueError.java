package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class UndefinedValueError extends Error {
	private static final long serialVersionUID = -7290225483329876543L;
    
    public UndefinedValueError(String message, AbstractAST ast) {
		super("UndefinedValueError", message, ast);
	}
}
