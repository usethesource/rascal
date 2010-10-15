package org.rascalmpl.ast;

import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.interpreter.asserts.ImplementationError;

public class JavaFunctionBody extends FunctionBody {
	private final String string;

	protected JavaFunctionBody(INode node, String string) {
		this.node = node;
		this.string = string;
	}

	@Override
	public <T> T accept(IASTVisitor<T> v) {
		throw new ImplementationError("Can not visit JavaFunctionBody");
	}

	public String getString() {
		return string;
	}
}