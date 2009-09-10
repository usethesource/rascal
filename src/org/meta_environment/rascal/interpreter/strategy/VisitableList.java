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
		if (i<0 || i>=arity()) throw new IndexOutOfBoundsException();
		if (i==0) {
			IList suffix = list.sublist(1, arity()-1);
			return new VisitableList(suffix.insert(newChild.getValue()));
		} else {
			IList newlist = list.sublist(0, i);
			if (i<arity()-1) {
				IList suffix = list.sublist(i+1, arity()-i-1);
				newlist = newlist.append(newChild.getValue()).concat(suffix);
			} else {
				newlist = newlist.append(newChild.getValue());
			}
			return new VisitableList(newlist);
		} 
	}

	public IValue getValue() {
		return list;
	}

}
