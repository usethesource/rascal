package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;

public class VisitableConstant implements Visitable {

	private IValue value;

	public VisitableConstant(IValue value) {
		this.value = value;
	}

	public int arity() {
		return 0;
	}

	public Visitable get(int i) throws IndexOutOfBoundsException {
		throw new IndexOutOfBoundsException();
	}

	public IValue getValue() {
		return value;
	}

	public Visitable set(int i, Visitable newChild)
	throws IndexOutOfBoundsException {
		throw new IndexOutOfBoundsException();
	}

}
