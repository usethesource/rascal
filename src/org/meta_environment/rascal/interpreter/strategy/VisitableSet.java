package org.meta_environment.rascal.interpreter.strategy;

import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class VisitableSet implements IVisitable, ISet {

	private ISet set;

	public VisitableSet(ISet set) {
		this.set = set;
	}

	public int getChildrenNumber() {
		return set.size();
	}

	public IVisitable getChildAt(int i) throws IndexOutOfBoundsException {
		int index = 0;
		for (IValue v : set) {
			if (index == i) {
				return VisitableFactory.makeVisitable(v);
			}
			index++;
		}
		throw new IndexOutOfBoundsException();
	}

	public IValue getValue() {
		return set;
	}

	public void setChildAt(int i, IVisitable newChild)
	throws IndexOutOfBoundsException {
		if (i >= getChildrenNumber()) {
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
		set = writer.done();
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return set.accept(v);
	}

	public boolean contains(IValue element) {
		return set.contains(element);
	}

	public <SetOrRel extends ISet> SetOrRel delete(IValue elem) {
		return (SetOrRel) set.delete(elem);
	}

	public boolean equals(Object other) {
		return set.equals(other);
	}

	public Type getElementType() {
		return set.getElementType();
	}

	public Type getType() {
		return set.getType();
	}

	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		return (SetOrRel) set.insert(element);
	}

	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		return (SetOrRel) set.intersect(set);
	}

	public boolean isEmpty() {
		return set.isEmpty();
	}

	public boolean isEqual(IValue other) {
		return set.isEqual(other);
	}

	public boolean isSubsetOf(ISet other) {
		return set.isSubsetOf(other);
	}

	public Iterator<IValue> iterator() {
		return set.iterator();
	}

	public IRelation product(ISet set) {
		return set.product(set);
	}

	public int size() {
		return set.size();
	}

	public <SetOrRel extends ISet> SetOrRel subtract(ISet set) {
		return (SetOrRel) set.subtract(set);
	}

	public String toString() {
		return set.toString();
	}

	public <SetOrRel extends ISet> SetOrRel union(ISet set) {
		return (SetOrRel) set.union(set);
	}

	public void setChildren(List<IVisitable> newchildren)
	throws IndexOutOfBoundsException {
		ISetWriter writer = ValueFactory.getInstance().setWriter(set.getElementType());
		for (int j = 0; j < set.size(); j++) {
			writer.insert(newchildren.get(j).getValue());
		}
		set = writer.done();
	}

	public void update(IValue oldvalue, IValue newvalue) {}

	public IVisitable setValue(IValue value) {
		this.set = (ISet) value;
		return this;
	}

}
