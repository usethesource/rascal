package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class MissingTagError extends StaticError {
	private static final long serialVersionUID = 8951799317606455150L;
    
	public MissingTagError(String name, AbstractAST onWhat) {
		super("Missing tag: " + name, onWhat);
	}
}
