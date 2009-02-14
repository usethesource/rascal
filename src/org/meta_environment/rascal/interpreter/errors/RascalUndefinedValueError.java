package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class RascalUndefinedValueError extends RascalError {
	private static final long serialVersionUID = -7290225483329876543L;
	
    public RascalUndefinedValueError(String message) {
    	super(message);
    };
    
    public RascalUndefinedValueError(String message, AbstractAST ast) {
		super(message, ast);
	}
    
    public RascalUndefinedValueError(String message, Throwable cause) {
		super(message, cause);
	}
}
