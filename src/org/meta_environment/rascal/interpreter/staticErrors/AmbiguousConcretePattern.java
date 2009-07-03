package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class AmbiguousConcretePattern extends StaticError {
	private static final long serialVersionUID = -1319922379120654138L;

	public AmbiguousConcretePattern(AbstractAST ast) {
		super("Concrete pattern is ambiguous, please rephrase (add more typed variables for example).", ast);
	}

}
