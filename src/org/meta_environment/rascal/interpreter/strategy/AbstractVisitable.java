package org.meta_environment.rascal.interpreter.strategy;

import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;

public abstract class AbstractVisitable implements Visitable {

	private IValue value;
	protected List<Visitable> children;

	public AbstractVisitable(IValue value, List<Visitable> children) {
		this.value = value;
		this.children = children;
	}

	public int arity() {
		return children.size();
	}

	public Visitable get(int i) throws IndexOutOfBoundsException {
		return children.get(i);
	}

	public IValue getValue() {
		return value;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer(getValue().toString());
		if (arity() != 0) {
			buffer.append("[");
			for (Visitable v : children) {
				buffer.append(v.toString());
				buffer.append(",");
			}
			buffer.replace(buffer.length()-1, buffer.length(), "]");
		}
		return buffer.toString();
	}

	public abstract Visitable set(int i, Visitable newChild)
	throws IndexOutOfBoundsException;

}
