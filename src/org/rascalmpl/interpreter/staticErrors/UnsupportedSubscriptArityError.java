package org.rascalmpl.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;

public class UnsupportedSubscriptArityError extends StaticError {

	private static final long serialVersionUID = 7084265104938510456L;

	public UnsupportedSubscriptArityError(Type receiver, int arity, AbstractAST ast) {
		super("Unsupported subscript arity of " + arity + " on type " + receiver, ast);
	}

}
