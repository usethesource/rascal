package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;
import org.meta_environment.rascal.interpreter.exceptions.RascalBug;

public class JavaFunctionBody extends FunctionBody {
	private final String string;

	public JavaFunctionBody(INode node, String string) {
		this.node = node;
		this.string = string;
	}

	@Override
	public <T> T accept(IASTVisitor<T> v) {
		throw new RascalBug("Can not visit JavaFunctionBody");
	}

	public String getString() {
		return string;
	}
}