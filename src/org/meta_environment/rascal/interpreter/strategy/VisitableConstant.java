package org.meta_environment.rascal.interpreter.strategy;

import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class VisitableConstant implements IVisitable, IValue {

	private IValue value;

	public VisitableConstant(IValue value) {
		this.value = value;
	}

	public int getChildrenNumber() {
		return 0;
	}

	public IVisitable getChildAt(int i) throws IndexOutOfBoundsException {
		throw new IndexOutOfBoundsException();
	}

	public IValue getValue() {
		return value;
	}

	public IVisitable setChildAt(int i, IVisitable newChild)
	throws IndexOutOfBoundsException {
		throw new IndexOutOfBoundsException();
	}

	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return value.accept(v);
	}

	public boolean equals(Object other) {
		return value.equals(other);
	}

	public Type getType() {
		return value.getType();
	}

	public boolean isEqual(IValue other) {
		return value.isEqual(other);
	}

	public String toString() {
		return value.toString();
	}

	public IVisitable setChildren(List<IVisitable> newchildren) {
		if (newchildren.size() != 0) throw new IndexOutOfBoundsException();
		return this;
	}

	public void update(IValue oldvalue, IValue newvalue) {
		// TODO Auto-generated method stub
		
	}

}
