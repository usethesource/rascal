package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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

public class TopologicalVisitable<T extends IValue> implements IVisitable, IValue {

	protected RelationContext context;
	protected T value;
	protected final List<TopologicalVisitable<?>> children;

	public TopologicalVisitable(RelationContext root, T value, List<TopologicalVisitable<?>> children) {
		this.value = value;
		this.children = children;
		this.context = root;
	}
	
	public TopologicalVisitable(RelationContext context, T value) {
		this(context, value, computeChildren(context, value));
	}

	public int getChildrenNumber() {
		return children.size();
	}

	public IVisitable getChildAt(int i) throws IndexOutOfBoundsException {
		return children.get(i);
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer(getValue().toString());
		if (getChildrenNumber() != 0) {
			buffer.append("[");
			for (IVisitable v : children) {
				buffer.append(v.toString());
				buffer.append(",");
			}
			buffer.replace(buffer.length()-1, buffer.length(), "]");
		}
		return buffer.toString();
	}

	public void setChildAt(int i, IVisitable newChild)
	throws IndexOutOfBoundsException {
		if (i >= getChildrenNumber()) throw new IndexOutOfBoundsException();
		update(getChildAt(i).getValue(), newChild.getValue());
		children.set(i,TopologicalVisitableFactory.makeTopologicalVisitable(context,newChild));
	}

	public void update(IValue oldvalue, IValue newvalue) {
		if (oldvalue instanceof IVisitable || newvalue instanceof IVisitable) {
			throw new RuntimeException(oldvalue.getClass() + "->" + newvalue.getClass());
		}
		IRelation relation = context.getRelation();
		IRelationWriter writer = ValueFactory.getInstance().relationWriter(relation.getElementType());
		for (IValue v : relation) {
			if (v.getType().isTupleType()) {
				ITuple t = (ITuple) v;
				ITuple newt = t;
				if (t.get(0).isEqual(oldvalue)) newt = t.set(0, newvalue);
				if (t.get(1).isEqual(oldvalue)) newt = t.set(1, newvalue);
				writer.insert(newt);
			}
		}
		for (IVisitable child : children) {
			child.update(oldvalue, newvalue);
		}
		context.setRelation(writer.done());
	}
	
	protected static List<TopologicalVisitable<?>> computeChildren(RelationContext context, IValue v) {
		HashMap<IValue, LinkedList<IValue>> adjacencies = computeAdjacencies(context.getRelation());
		List<TopologicalVisitable<?>> successors = new ArrayList<TopologicalVisitable<?>>();
		if (adjacencies.get(v) != null) {
			for (IValue s: adjacencies.get(v)) {
				successors.add(TopologicalVisitableFactory.makeTopologicalVisitable(context, s));
			}
		}
		return successors;
	}

	protected static HashMap<IValue, LinkedList<IValue>> computeAdjacencies(IRelation relation) {
		HashMap<IValue, LinkedList<IValue>> adjacencies = new HashMap<IValue, LinkedList<IValue>> ();
		for(IValue v : relation){
			ITuple tup = (ITuple) v;
			IValue from = tup.get(0);
			IValue to = tup.get(1);
			LinkedList<IValue> children = adjacencies.get(from);
			if(children == null)
				children = new LinkedList<IValue>();
			children.add(to);
			adjacencies.put(from, children);
		}  
		return adjacencies;
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

	public void setChildren(List<IVisitable> newchildren)
	throws IndexOutOfBoundsException {
		int i = 0;
		for (IVisitable v : newchildren) {
			children.set(i, TopologicalVisitableFactory.makeTopologicalVisitable(context, v));
			i++;
		}
	}

	public RelationContext getContext() {
		return context;
	}

	public IVisitable setValue(IValue value) {
		update(getValue(),value);
		this.value = (T) value;
		return this;
	}

}
