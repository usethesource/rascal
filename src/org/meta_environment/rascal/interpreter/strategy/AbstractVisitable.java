package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;

public abstract class AbstractVisitable implements Visitable {
	
	private IValue value;
	private Visitable[] children;

	public AbstractVisitable(IValue value, Visitable[] children) {
		this.value = value;
		this.children = children;
	}

	public int arity() {
		return children.length;
	}

	public Visitable get(int i) throws IndexOutOfBoundsException {
		return children[i];
	}

	public IValue getValue() {
		return value;
	}

	public abstract Visitable set(int i, Visitable newChild)
			throws IndexOutOfBoundsException;

}
