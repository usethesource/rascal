package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.interpreter.strategy.IVisitable;
import org.meta_environment.rascal.interpreter.strategy.VisitableFactory;

public class TopologicalVisitable<T extends IValue> implements IVisitable, IValue {

	protected RelationContext context;
	protected final T value;
	protected final List<TopologicalVisitable<?>> children;

	public TopologicalVisitable(RelationContext root, T value, List<TopologicalVisitable<?>> children) {
		this.value = value;
		this.children = children;
		this.context = root;
	}

	public int arity() {
		return children.size();
	}

	public IVisitable getChildAt(int i) throws IndexOutOfBoundsException {
		return children.get(i);
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer(getValue().toString());
		if (arity() != 0) {
			buffer.append("[");
			for (IVisitable v : children) {
				buffer.append(v.toString());
				buffer.append(",");
			}
			buffer.replace(buffer.length()-1, buffer.length(), "]");
		}
		return buffer.toString();
	}

	public IVisitable setChildAt(int i, IVisitable newChild)
	throws IndexOutOfBoundsException {
		if (i >= arity()) throw new IndexOutOfBoundsException();
		update(getChildAt(i).getValue(), newChild.getValue());
		children.set(i,VisitableFactory.makeTopologicalVisitable(context,newChild));
		return this;
	}

	public void update(IValue oldvalue, IValue newvalue) {
		IRelation relation = context.getRelation();
		IRelationWriter writer = ValueFactory.getInstance().relationWriter(relation.getElementType());
		for (IValue v : relation) {
			if (v.getType().isTupleType()) {
				ITuple t = (ITuple) v;
				ITuple newt = t;
				if (t.get(0).equals(oldvalue)) newt = t.set(0, newvalue);
				if (t.get(1).equals(oldvalue)) newt = t.set(1, newvalue);
				writer.insert(newt);
			}
		}
		context.setRelation(writer.done());
	}

	public IValue getValue() {
		return value;
	}

	@SuppressWarnings("hiding")
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return value.accept(v);
	}

	public Type getType() {
		return value.getType();
	}

	public boolean isEqual(IValue other) {
		return value.equals(other);
	}

	public IVisitable setChildren(List<IVisitable> newchildren)
			throws IndexOutOfBoundsException {
		int i = 0;
		for (IVisitable v : newchildren) {
			children.set(i, VisitableFactory.makeTopologicalVisitable(context, v));
			i++;
		}
		return this;
	}
	
	

}
