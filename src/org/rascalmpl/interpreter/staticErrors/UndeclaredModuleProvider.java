package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class UndeclaredModuleProvider extends StaticError {
	private static final long serialVersionUID = 7649069585736694101L;

	public UndeclaredModuleProvider(String scheme, AbstractAST node) {
		super("There was no module provider registered for scheme:" + scheme, node);
	}
}
