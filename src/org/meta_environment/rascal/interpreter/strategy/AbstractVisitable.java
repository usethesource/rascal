package org.meta_environment.rascal.interpreter.strategy;

import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;

public abstract class AbstractVisitable extends VisitableConstructor {

	protected List<Visitable> children;

	public AbstractVisitable(IConstructor Node, List<Visitable> children) {
		super(Node);
		this.children = children;
	}

	public int arity() {
		return children.size();
	}

	public Visitable getChildAt(int i) throws IndexOutOfBoundsException {
		return children.get(i);
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

	public abstract Visitable setChildAt(int i, Visitable newChild)
	throws IndexOutOfBoundsException;

}
