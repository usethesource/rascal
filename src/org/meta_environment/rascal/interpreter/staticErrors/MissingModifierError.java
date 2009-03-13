package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class MissingModifierError extends StaticError {
	private static final long serialVersionUID = 8951799317606455150L;
    
	public MissingModifierError(String name, AbstractAST onWhat) {
		super("Missing modifier: " + name, onWhat);
	}
}
