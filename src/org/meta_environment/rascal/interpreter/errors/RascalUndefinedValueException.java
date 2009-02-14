package org.meta_environment.rascal.interpreter.errors;

import org.meta_environment.rascal.ast.AbstractAST;

public class RascalUndefinedValueException extends RascalException {
	private static final long serialVersionUID = -7290225483329876543L;
	
    public RascalUndefinedValueException(String message) {
    	super(message);
    };
    
    public RascalUndefinedValueException(String message, AbstractAST ast) {
		super(message, ast);
	}
    /*
    public RascalUndefinedValueError(String message, Throwable cause) {
		super(message, cause);
	}
	*/
}
