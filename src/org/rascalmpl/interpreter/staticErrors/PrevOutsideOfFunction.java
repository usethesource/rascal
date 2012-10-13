package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class PrevOutsideOfFunction extends StaticError {
	
	private static final long serialVersionUID = -1115742973293437423L;

	public PrevOutsideOfFunction(AbstractAST ast) {
		super("Use of the 'prev' special variable is only allowed within the named extending functions.", ast);
	}

}
