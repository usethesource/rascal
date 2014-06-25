package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;

public abstract class Cursor implements ICursor, IValue {
	
	private IValue value;
	private Context ctx;

	public Cursor(IValue value) {
		this(value, new TopContext());
	}
	
	public Cursor(IValue value, Context ctx) {
		this.value = value;
		this.ctx = ctx;
	}
	
	@Override
	public IValue getWrappedValue() {
		return value;
	}
	
	@Override
	public Context getCtx() {
		return ctx;
	}

	@Override
	public Type getType() {
		return value.getType();
	}


	@Override
	public boolean isEqual(IValue other) {
		return value.isEqual(other);
	}

	@Override
	public boolean isAnnotatable() {
		return value.isAnnotatable();
	}

	@Override
	public IAnnotatable<? extends IValue> asAnnotatable() {
		return value.asAnnotatable();
	}
	
	@Override
	public IValue up() {
		return ctx.up(value);
	}
	
	@Override
	public IValue root() {
		IValue current = this;
		while (current instanceof ICursor) {
			current = ((ICursor)current).up();
		}
		return current;
	}
	
	@Override
	public String toString() {
		return "%" + value.toString();
	}
	
	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return getWrappedValue().accept(v);
	}

}
