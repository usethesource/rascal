package org.meta_environment.rascal.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;

public class UnexpectedTypeError extends StaticError {
	private static final long serialVersionUID = -9009407553448884728L;
	
	public UnexpectedTypeError(Type expected, Type got, AbstractAST ast) {
		super("Expected " + expected + ", but got " + got, ast);
	}
}
