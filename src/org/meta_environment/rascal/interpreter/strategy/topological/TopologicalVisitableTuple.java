package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.interpreter.strategy.IVisitable;

public class TopologicalVisitableTuple extends TopologicalVisitable<ITuple>
implements ITuple {

	public TopologicalVisitableTuple(IRelation root, ITuple value,
			List<IVisitable> children) {
		super(root, value, children);
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return value.accept(v);
	}
	
	public boolean equals(Object o) {
		return value.equals(o);
	}

	public IValue get(int i) throws IndexOutOfBoundsException {
		return value.get(i);
	}

	public IValue get(String label) throws FactTypeUseException {
		return value.get(label);
	}

	public Type getType() {
		return value.getType();
	}

	public boolean isEqual(IValue other) {
		return value.isEqual(other);
	}

	public Iterator<IValue> iterator() {
		return value.iterator();
	}

	public IValue select(int... fields) throws IndexOutOfBoundsException {
		return value.select(fields);
	}

	public IValue select(String... fields) throws FactTypeUseException {
		return value.select(fields);
	}

	public ITuple set(int i, IValue arg) throws IndexOutOfBoundsException {
		return value.set(i, arg);
	}

	public ITuple set(String label, IValue arg) throws FactTypeUseException {
		return value.set(label, arg);
	}

	public String toString() {
		return value.toString();
	}


}
