package org.meta_environment.rascal.interpreter.strategy;

import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class VisitableRelation implements IVisitable, IRelation {

	private IRelation relation;

	public VisitableRelation(IRelation relation) {
		this.relation = relation;
	}

	public int arity() {
		return relation.size();
	}

	public IVisitable getChildAt(int i) throws IndexOutOfBoundsException {
		int index = 0;
		for (IValue v : relation) {
			if (index == i) {
				return VisitableFactory.makeVisitable(v);
			}
			index++;
		}
		throw new IndexOutOfBoundsException();
	}

	public IValue getValue() {
		return relation;
	}

	public IVisitable setChildAt(int i, IVisitable newChild)
	throws IndexOutOfBoundsException {
		if (i >= arity()) {
			throw new IndexOutOfBoundsException();
		}
		int index = 0;
		IRelationWriter writer = ValueFactory.getInstance().relationWriter(relation.getFieldTypes());
		Iterator<IValue> elts = relation.iterator();
		while (elts.hasNext()) {
			IValue e = elts.next();
			if (index == i) {
				writer.insert(newChild.getValue());
			} else {
				writer.insert(e);
			}
			index++;
		}
		return new VisitableRelation(writer.done());
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return relation.accept(v);
	}

	public ISet carrier() {
		return relation.carrier();
	}

	public IRelation closure() throws FactTypeUseException {
		return relation.closure();
	}

	public IRelation closureStar() throws FactTypeUseException {
		return relation.closureStar();
	}

	public IRelation compose(IRelation rel) throws FactTypeUseException {
		return relation.compose(rel);
	}

	public boolean contains(IValue element) {
		return relation.contains(element);
	}

	public <SetOrRel extends ISet> SetOrRel delete(IValue elem) {
		return (SetOrRel) relation.delete(elem);
	}

	public ISet domain() {
		return relation.domain();
	}

	public boolean equals(Object other) {
		return relation.equals(other);
	}

	public Type getElementType() {
		return relation.getElementType();
	}

	public Type getFieldTypes() {
		return relation.getFieldTypes();
	}

	public Type getType() {
		return relation.getType();
	}

	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		return (SetOrRel) relation.insert(element);
	}

	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		return (SetOrRel) relation.intersect(set);
	}

	public boolean isEmpty() {
		return relation.isEmpty();
	}

	public boolean isEqual(IValue other) {
		return relation.isEqual(other);
	}

	public boolean isSubsetOf(ISet other) {
		return relation.isSubsetOf(other);
	}

	public Iterator<IValue> iterator() {
		return relation.iterator();
	}

	public IRelation product(ISet set) {
		return relation.product(set);
	}

	public ISet range() {
		return relation.range();
	}

	public ISet select(int... fields) {
		return relation.select(fields);
	}

	public ISet select(String... fields) throws FactTypeUseException {
		return relation.select(fields);
	}

	public int size() {
		return relation.size();
	}

	public <SetOrRel extends ISet> SetOrRel subtract(ISet set) {
		return (SetOrRel) relation.subtract(set);
	}

	public String toString() {
		return relation.toString();
	}

	public <SetOrRel extends ISet> SetOrRel union(ISet set) {
		return (SetOrRel) relation.union(set);
	}

	public IVisitable setChildren(List<IVisitable> newchildren)
	throws IndexOutOfBoundsException {
		relation = ValueFactory.getInstance().relation(relation.getFieldTypes());
		for (IVisitable iVisitable : newchildren) {
			relation = relation.insert(iVisitable.getValue());
		}
		return new VisitableRelation(relation);
	}

}
