package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;

public class VisitableTuple implements Visitable {

	private ITuple tuple;

	public VisitableTuple(ITuple tuple) {
		this.tuple = tuple;
	}

	public int arity() {
		return tuple.arity();
	}

	public Visitable getChildAt(int i) throws IndexOutOfBoundsException {
		return VisitableFactory.make(tuple.get(i));
	}

	public Visitable setChildAt(int i, Visitable newChild)
			throws IndexOutOfBoundsException {
		return new VisitableTuple(tuple.set(i, newChild.getValue()));
	}

	public IValue getValue() {
		return tuple;
	}

}
