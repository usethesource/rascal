package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;

public class VisitableList implements Visitable {

	private IList list;

	public VisitableList(IList list) {
		this.list = list;
	}

	public int arity() {
		return list.length();
	}

	public Visitable get(int i) throws IndexOutOfBoundsException {
		return VisitableFactory.make(list.get(i));
	}

	public Visitable set(int i, Visitable newChild)
	throws IndexOutOfBoundsException {
		// we need to reconstruct the list
		if (i>0) {
			IList prefix = list.sublist(0, i-1);
			IList suffix = list.sublist(i+1, arity());
			IList newlist = prefix.append(newChild.getValue()).concat(suffix);
			return VisitableFactory.make(newlist);
		} else {
			IList suffix = list.sublist(i+1, arity());
			return VisitableFactory.make(suffix.insert(newChild.getValue()));
		}
	}

	public IValue getValue() {
		return list;
	}

}
