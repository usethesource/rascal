package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.interpreter.strategy.IVisitable;

public class TopologicalVisitable<T extends IValue> implements IVisitable, IValue {

	protected IRelation relation;
	protected final T value;
	protected final List<IVisitable> children;

	public TopologicalVisitable(IRelation root, T value, List<IVisitable> children) {
		this.value = value;
		this.children = children;
		this.relation = root;
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
		relation = relation.delete(ValueFactory.getInstance().tuple(getValue(),getChildAt(i).getValue()));
		children.set(i,newChild);
		relation = relation.insert(ValueFactory.getInstance().tuple(getValue(),getChildAt(i).getValue()));
		return this;
	}

	public IRelation getRelation() {
		return relation;
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

}
