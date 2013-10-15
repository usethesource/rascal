package org.eclipse.imp.pdb.facts.impl.primitive;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;

public abstract class OrgString implements IString, Iterable<Integer> {
	private final static Type STRING_TYPE = TypeFactory.getInstance().stringType();

	@Override
	public Type getType() {
		return STRING_TYPE;
	}

	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E {
		return v.visitString(this);
	}

	@Override
	public IString concat(IString other) {
		return new Concat(this, (OrgString)other);
	}

	@Override
	public IString substring(int start) {
		return substring(start, length());
	}
	
	@Override
	public boolean isAnnotatable() {
		return false;
	}

	@Override
	public IAnnotatable<? extends IValue> asAnnotatable() {
		throw new IllegalOperationException(
				"Cannot be viewed as annotatable.", getType());
	}

	
	public abstract void accept(IOrgStringVisitor visitor);

}