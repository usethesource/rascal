package org.meta_environment.rascal.interpreter.strategy;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;

public class VisitableSet implements Visitable {

	private ISet set;

	public VisitableSet(ISet set) {
		this.set = set;
	}

	public int arity() {
		return set.size();
	}

	public Visitable getChildAt(int i) throws IndexOutOfBoundsException {
		int index = 0;
		for (IValue v : set) {
			if (index == i) {
				return VisitableFactory.make(v);
			}
			index++;
		}
		throw new IndexOutOfBoundsException();
	}

	public IValue getValue() {
		return set;
	}

	public Visitable setChildAt(int i, Visitable newChild)
	throws IndexOutOfBoundsException {
		if (i >= arity()) {
			throw new IndexOutOfBoundsException();
		}
		int index = 0;
		ISetWriter writer = ValueFactory.getInstance().setWriter(set.getElementType());
		Iterator<IValue> elts = set.iterator();
		while (elts.hasNext()) {
			IValue e = elts.next();
			if (index == i) {
				writer.insert(newChild.getValue());
			} else {
				writer.insert(e);
			}
			index++;
		}
		return new VisitableSet(writer.done());
	}

}
