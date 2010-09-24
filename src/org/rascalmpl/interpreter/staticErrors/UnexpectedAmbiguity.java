package org.rascalmpl.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.AbstractAST;

public class UnexpectedAmbiguity extends StaticError {
	private static final long serialVersionUID = -1319922379120654138L;
	private final INode tree;

	public UnexpectedAmbiguity(AbstractAST ast) {
		super("Concrete pattern is ambiguous, please rephrase (add more typed variables for example).", ast);
		this.tree = ast.getTree();
	}
	
	public INode getTree() {
		return tree;
	}

}
