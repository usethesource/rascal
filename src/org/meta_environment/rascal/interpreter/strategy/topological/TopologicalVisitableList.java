package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class TopologicalVisitableList extends TopologicalVisitable<IList> implements
IList {

	public TopologicalVisitableList(RelationContext context, IList value,
			List<TopologicalVisitable<?>> children) {
		super(context, value, children);
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return value.accept(v);
	}

	public IList append(IValue e) {
		return value.append(e);
	}

	public IList concat(IList o) {
		return value.concat(o);
	}

	public boolean contains(IValue e) {
		return value.contains(e);
	}

	public IList delete(int i) {
		return value.delete(i);
	}

	public IList delete(IValue e) {
		return value.delete(e);
	}

	public boolean equals(Object other) {
		return value.equals(other);
	}

	public IValue get(int i) throws IndexOutOfBoundsException {
		return value.get(i);
	}

	public Type getElementType() {
		return value.getElementType();
	}

	public Type getType() {
		return value.getType();
	}

	public IList insert(IValue e) {
		return value.insert(e);
	}

	public boolean isEmpty() {
		return value.isEmpty();
	}

	public boolean isEqual(IValue other) {
		return value.isEqual(other);
	}

	public Iterator<IValue> iterator() {
		return value.iterator();
	}

	public int length() {
		return value.length();
	}

	public IList put(int i, IValue e) throws FactTypeUseException,
	IndexOutOfBoundsException {
		return value.put(i, e);
	}

	public IList reverse() {
		return value.reverse();
	}

	public IList sublist(int offset, int length) {
		return value.sublist(offset, length);
	}

	public String toString() {
		return value.toString();
	}

}
