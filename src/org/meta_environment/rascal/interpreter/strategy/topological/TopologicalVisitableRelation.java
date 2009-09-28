package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class TopologicalVisitableRelation extends TopologicalVisitable<IRelation>
implements IRelation {
	
	public TopologicalVisitableRelation(RelationContext context, IRelation value,
			List<TopologicalVisitable<?>> children) {
		super(context, value, children);
	}
	
	public TopologicalVisitableRelation(RelationContext context, IRelation value) {
		super(context, value);
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return value.accept(v);
	}

	public ISet carrier() {
		return value.carrier();
	}

	public IRelation closure() throws FactTypeUseException {
		return value.closure();
	}

	public IRelation closureStar() throws FactTypeUseException {
		return value.closureStar();
	}

	public IRelation compose(IRelation rel) throws FactTypeUseException {
		return value.compose(rel);
	}

	public boolean contains(IValue element) {
		return value.contains(element);
	}

	public <SetOrRel extends ISet> SetOrRel delete(IValue elem) {
		return (SetOrRel) value.delete(elem);
	}

	public ISet domain() {
		return value.domain();
	}
	
	public Type getElementType() {
		return value.getElementType();
	}

	public Type getFieldTypes() {
		return value.getFieldTypes();
	}

	public Type getType() {
		return value.getType();
	}

	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		return (SetOrRel) value.insert(element);
	}

	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		return (SetOrRel) value.intersect(set);
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

	public ISet range() {
		return value.range();
	}

	public ISet select(int... fields) {
		return value.select(fields);
	}

	public ISet select(String... fields) throws FactTypeUseException {
		return value.select(fields);
	}

	public int size() {
		return value.size();
	}

	public <SetOrRel extends ISet> SetOrRel subtract(ISet set) {
		return (SetOrRel) value.subtract(set);
	}

	public String toString() {
		return value.toString();
	}

	public <SetOrRel extends ISet> SetOrRel union(ISet set) {
		return (SetOrRel) value.union(set);
	}
	

	public int arity() {
		return value.arity();
	}


}
