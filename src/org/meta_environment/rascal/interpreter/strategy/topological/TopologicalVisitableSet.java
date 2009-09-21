package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class TopologicalVisitableSet extends TopologicalVisitable<ISet> implements
ISet {

	public TopologicalVisitableSet(RelationContext context, ISet value,
			List<TopologicalVisitable<?>> children) {
		super(context, value, children);
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return value.accept(v);
	}

	public boolean contains(IValue element) {
		return value.contains(element);
	}

	public <SetOrRel extends ISet> SetOrRel delete(IValue elem) {
		return value.delete(elem);
	}

	public boolean equals(Object other) {
		return value.equals(other);
	}

	public Type getElementType() {
		return value.getElementType();
	}

	public Type getType() {
		return value.getType();
	}

	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		return value.insert(element);
	}

	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		return value.intersect(set);
	}

	public boolean isEmpty() {
		return value.isEmpty();
	}

	public boolean isEqual(IValue other) {
		return value.isEqual(other);
	}

	public boolean isSubsetOf(ISet other) {
		return value.isSubsetOf(other);
	}

	public Iterator<IValue> iterator() {
		return value.iterator();
	}

	public IRelation product(ISet set) {
		return value.product(set);
	}

	public int size() {
		return value.size();
	}

	public <SetOrRel extends ISet> SetOrRel subtract(ISet set) {
		return value.subtract(set);
	}

	public String toString() {
		return value.toString();
	}

	public <SetOrRel extends ISet> SetOrRel union(ISet set) {
		return value.union(set);
	}

}
