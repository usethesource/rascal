package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class ModuleNameMismatchError extends StaticError {
	private static final long serialVersionUID = 6984933453355489423L;

	public ModuleNameMismatchError(String name, String file, AbstractAST ast) {
		super("Module name " + name + " does not match " + file, ast);
	}

}
