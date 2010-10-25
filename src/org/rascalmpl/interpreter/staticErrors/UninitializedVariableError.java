package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class UninitializedVariableError extends StaticError {
	private static final long serialVersionUID = -7290225483329876543L;
    
    public UninitializedVariableError(String name, AbstractAST ast) {
		super("Uninitialized variable: " + name, ast);
	}
}
