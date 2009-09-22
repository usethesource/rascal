package org.meta_environment.rascal.interpreter.strategy;

import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class VisitableTuple implements IVisitable, ITuple {

	private ITuple tuple;

	public VisitableTuple(ITuple tuple) {
		this.tuple = tuple;
	}

	public int getChildrenNumber() {
		return tuple.arity();
	}

	public IVisitable getChildAt(int i) throws IndexOutOfBoundsException {
		return VisitableFactory.makeVisitable(tuple.get(i));
	}

	public void setChildAt(int i, IVisitable newChild)
			throws IndexOutOfBoundsException {
		tuple = tuple.set(i, newChild.getValue());
	}

	public IValue getValue() {
		return tuple;
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return tuple.accept(v);
	}

	public boolean equals(Object o) {
		return tuple.equals(o);
	}

	public IValue get(int i) throws IndexOutOfBoundsException {
		return tuple.get(i);
	}

	public IValue get(String label) throws FactTypeUseException {
		return tuple.get(label);
	}

	public Type getType() {
		return tuple.getType();
	}

	public boolean isEqual(IValue other) {
		return tuple.isEqual(other);
	}

	public Iterator<IValue> iterator() {
		return tuple.iterator();
	}

	public IValue select(int... fields) throws IndexOutOfBoundsException {
		return tuple.select(fields);
	}

	public IValue select(String... fields) throws FactTypeUseException {
		return tuple.select(fields);
	}

	public ITuple set(int i, IValue arg) throws IndexOutOfBoundsException {
		return tuple.set(i, arg);
	}

	public ITuple set(String label, IValue arg) throws FactTypeUseException {
		return tuple.set(label, arg);
	}

	public String toString() {
		return tuple.toString();
	}

	public void setChildren(List<IVisitable> newchildren)
			throws IndexOutOfBoundsException {
		for (int j = 0; j < tuple.arity(); j++) {
			tuple = tuple.set(j,newchildren.get(j));
		}
	}

	public void update(IValue oldvalue, IValue newvalue) {}
	

	public int arity() {
		return tuple.arity();
	}


}
