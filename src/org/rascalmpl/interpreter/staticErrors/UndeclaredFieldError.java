package org.rascalmpl.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;

public class UndeclaredFieldError extends StaticError {
	private static final long serialVersionUID = -7406655567412555533L;
	
	public UndeclaredFieldError(String name, Type forType, AbstractAST node) {
		super("Undeclared field: " + name + " for " + forType, node);
	}
}
