package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class JavaMethodLinkError extends StaticError {
	private static final long serialVersionUID = 3867556518416718308L;

	public JavaMethodLinkError(String name, String cause, AbstractAST ast) {
		super("Can not link method " + name + " because: " + cause, ast);
	}
}
