package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class IllegalQualifiedDeclaration extends StaticError {
	private static final long serialVersionUID = -2107466760568730273L;

	public IllegalQualifiedDeclaration(AbstractAST ast) {
		super("Declaration of qualified names is not allowed", ast);
	}

}
