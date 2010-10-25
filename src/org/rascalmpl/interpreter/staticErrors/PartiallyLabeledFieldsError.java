package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class PartiallyLabeledFieldsError extends StaticError {
	private static final long serialVersionUID = 1156905929827375773L;

	public PartiallyLabeledFieldsError(AbstractAST ast) {
		super("either all fields must be labeled, or none.", ast);
	}

}
