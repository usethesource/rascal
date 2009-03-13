package org.meta_environment.rascal.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;

public class ArityError extends StaticError {
	private static final long serialVersionUID = -8995239033315812561L;
	
	public ArityError(Type off, int arity, AbstractAST ast) {
		super("Arity " + off + " unequal to " + arity, ast);
	}
	
	public ArityError(int expected, int got, AbstractAST ast) {
		super("Expected arity : " + expected + ", unequal to " + got, ast);
	}
}
