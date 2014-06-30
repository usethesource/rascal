package org.rascalmpl.interpreter.cursors;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListRelation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;

public class ListCursor extends Cursor implements IList {

	public ListCursor(IList list) {
		super(list);
	}

	public ListCursor(IList list, Context ctx) {
		super(list, ctx);
	}
	
	IList getList() {
		return (IList)getWrappedValue();
	}

	@Override
	public Iterator<IValue> iterator() {
		return new Iterator<IValue>() {
			private int i = 0;
			private Iterator<IValue> iter = getList().iterator();
			
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public IValue next() {
				iter.next();
				return ListCursor.this.get(i++);
			}

			@Override
			public void remove() {
				
			}
			
		};
	}


	@Override
	public Type getElementType() {
		return getList().getElementType();
	}

	@Override
	public int length() {
		return getList().length();
	}

	@Override
	public IList reverse() {
		return new ListCursor(getList().reverse(), getCtx());
	}

	@Override
	public IList append(IValue e) {
		return new ListCursor(getList().append(e), getCtx());
	}

	@Override
	public IList insert(IValue e) {
		return new ListCursor(getList().insert(e), getCtx());
	}

	@Override
	public IList concat(IList o) {
		return new ListCursor(getList().concat(o), getCtx());
	}

	@Override
	public IList put(int i, IValue e) throws FactTypeUseException,
			IndexOutOfBoundsException {
		return new ListCursor(getList().put(i, e), getCtx());
	}

	@Override
	public IList replace(int first, int second, int end, IList repl)
			throws FactTypeUseException, IndexOutOfBoundsException {
		return new ListCursor(getList().replace(first, second, end, repl), getCtx());
	}

	@Override
	public IValue get(int i) throws IndexOutOfBoundsException {
		ListContext ctx = new ListContext(getCtx(), i, getList());
		return CursorFactory.makeCursor(getList().get(i), ctx);
	}

	@Override
	public IList sublist(int offset, int length) {
		Context ctx = new SubListContext(getCtx(), offset, length, getList());
		return new ListCursor(getList().sublist(offset, length), ctx);
	}

	@Override
	public boolean isEmpty() {
		return getList().isEmpty();
	}

	@Override
	public boolean contains(IValue e) {
		return getList().contains(e);
	}

	@Override
	public IList delete(IValue e) {
		return new ListCursor(getList().delete(e), getCtx());
	}

	@Override
	public IList delete(int i) {
		return new ListCursor(getList().delete(i), getCtx());
	}

	@Override
	public IList product(IList l) {
		return new ListCursor(getList().product(l), getCtx());
	}

	@Override
	public IList intersect(IList l) {
		return new ListCursor(getList().intersect(l), getCtx());
	}

	@Override
	public IList subtract(IList l) {
		return new ListCursor(getList().subtract(l), getCtx());
	}

	@Override
	public boolean isSubListOf(IList l) {
		return getList().isSubListOf(l);
	}

	@Override
	public boolean isRelation() {
		return getList().isRelation();
	}

	@Override
	public IListRelation<IList> asRelation() {
		// wrap in cursor?
		return getList().asRelation();
	}

}
