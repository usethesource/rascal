package org.rascalmpl.interpreter.cursors;

import java.util.Iterator;

import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.FactTypeUseException;

public class TupleCursor extends Cursor implements ITuple {

	public TupleCursor(IValue value) {
		super(value);
	}

	public TupleCursor(IValue value, Context ctx) {
		super(value, ctx);
	}

	private ITuple getTuple() {
		return (ITuple)getWrappedValue();
	}
	
	@Override
	public Iterator<IValue> iterator() {
		return new Iterator<IValue>() {
			int i = 0;
			Iterator<IValue> iter = getTuple().iterator();

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public IValue next() {
				return TupleCursor.this.get(i++);
			}

			@Override
			public void remove() {
			}
		};
	}

	@Override
	public IValue get(int i) throws IndexOutOfBoundsException {
		Context ctx = new TupleIndexContext(getCtx(), i, getTuple());
		return CursorFactory.makeCursor(getTuple().get(i), ctx);
	}

	@Override
	public IValue get(String label) throws FactTypeUseException {
		Context ctx = new TupleLabelContext(getCtx(), label, getTuple());
		return CursorFactory.makeCursor(getTuple().get(label), ctx);
	}

	@Override
	public ITuple set(int i, IValue arg) throws IndexOutOfBoundsException {
		return new TupleCursor(getTuple().set(i, arg), getCtx());
	}

	@Override
	public ITuple set(String label, IValue arg) throws FactTypeUseException {
		return new TupleCursor(getTuple().set(label, arg), getCtx());
	}

	@Override
	public int arity() {
		return getTuple().arity();
	}

	@Override
	public IValue select(int... fields) throws IndexOutOfBoundsException {
		Context ctx = new TupleSelectContext(getCtx(), fields, getTuple());
		return CursorFactory.makeCursor(getTuple().select(fields), ctx);
	}

	@Override
	public IValue selectByFieldNames(String... fields)
			throws FactTypeUseException {
		Context ctx = new TupleSelectByFieldNamesContext(getCtx(), fields, getTuple());
		return CursorFactory.makeCursor(getTuple().selectByFieldNames(fields), ctx);
	}

}
