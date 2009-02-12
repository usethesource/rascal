package org.meta_environment.rascal.interpreter.exceptions;

import org.meta_environment.rascal.ast.AbstractAST;

public class RascalUndefinedValue extends RascalTypeError {
	private static final long serialVersionUID = -7290225483329876543L;
	
    public RascalUndefinedValue(String message) {
    	super(message);
    };
    
    public RascalUndefinedValue(String message, AbstractAST ast) {
    	super(message, ast);
	}
}
