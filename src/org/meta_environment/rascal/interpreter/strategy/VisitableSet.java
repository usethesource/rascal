package org.meta_environment.rascal.interpreter.strategy;

import java.util.Iterator;

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

	public int arity() {
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

	public IVisitable setChildAt(int i, IVisitable newChild)
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

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return set.accept(v);
	}

	public boolean contains(IValue element) {
		return set.contains(element);
	}

	public <SetOrRel extends ISet> SetOrRel delete(IValue elem) {
		return set.delete(elem);
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
		return set.insert(element);
	}

	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		return set.intersect(set);
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
		return set.subtract(set);
	}

	public String toString() {
		return set.toString();
	}

	public <SetOrRel extends ISet> SetOrRel union(ISet set) {
		return set.union(set);
	}

}
