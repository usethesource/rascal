package org.rascalmpl.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;

public class UnsupportedSubscriptError extends StaticError {
	private static final long serialVersionUID = -315365847166484727L;

	public UnsupportedSubscriptError(Type receiver, Type subscript, AbstractAST ast) {
		super("Unsupported subscript of type " + subscript + " on type " + receiver, ast);
	}

}
