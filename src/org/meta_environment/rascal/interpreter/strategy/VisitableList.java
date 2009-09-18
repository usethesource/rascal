package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;

public class VisitableList implements Visitable {
	private final IList list;

	public VisitableList(IList list) {
		super();
		
		this.list = list;
	}

	public int arity() {
		return list.length();
	}

	public Visitable getChildAt(int i) throws IndexOutOfBoundsException {
		return VisitableFactory.make(list.get(i));
	}

	public Visitable setChildAt(int i, Visitable newChild) throws IndexOutOfBoundsException {
		return new VisitableList(list.put(i, newChild.getValue()));
	}

	public IValue getValue() {
		return list;
	}
}
