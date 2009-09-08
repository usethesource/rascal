package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;

public class VisitableNode implements Visitable {

	private INode node;

	public VisitableNode(INode node) {
		this.node = node;
	}

	public int arity() {
		return node.arity();
	}

	public Visitable get(int i) throws IndexOutOfBoundsException {
		return VisitableFactory.make(node.get(i));
	}

	public Visitable set(int i, Visitable newChild)
			throws IndexOutOfBoundsException {
		return VisitableFactory.make(node.set(i, newChild.getValue()));
	}

	public IValue getValue() {
		return node;
	}

}
