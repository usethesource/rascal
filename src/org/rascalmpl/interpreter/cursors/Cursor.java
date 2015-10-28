package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.value.IAnnotatable;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.visitors.IValueVisitor;

public abstract class Cursor implements ICursor {
	
	/* TODO
	 * 
	 * Currently, many cursors return new cursors as if the operation on the value was actually
	 * an update of the cursor. This is probably wrong: it doesn't work that way for atoms either.
	 * Operations should just return the new value, - only navigation leads to new cursors. If
	 * you want update, you should do update(...) in Rascal.
	 */
	
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
	public boolean mayHaveKeywordParameters() {
		return value.mayHaveKeywordParameters();
	}

	@Override
	public IAnnotatable<? extends IValue> asAnnotatable() {
		return value.asAnnotatable();
	}
	
	@Override
	public IWithKeywordParameters<? extends IValue> asWithKeywordParameters() {
		return value.asWithKeywordParameters();
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
		return CursorFactory.makeCursor(current, new TopContext());
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return getWrappedValue().accept(v);
	}

}
