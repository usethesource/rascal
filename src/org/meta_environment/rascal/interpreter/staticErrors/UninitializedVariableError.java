package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class UninitializedVariableError extends StaticError {
	private static final long serialVersionUID = -7290225483329876543L;
    
    public UninitializedVariableError(String name, AbstractAST ast) {
		super("Uninitialized variable: " + name, ast);
	}
}
