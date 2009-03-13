package org.meta_environment.rascal.interpreter.staticErrors;

import org.meta_environment.rascal.ast.AbstractAST;

public class RedeclaredFunctionError extends StaticError {
	private static final long serialVersionUID = 8306385560142947662L;

	public RedeclaredFunctionError(String header, String header2,
			AbstractAST ast) {
		super("Redeclared function " + header + " overlaps with " + header2, ast);
	}

}
