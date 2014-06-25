package org.rascalmpl.cursors;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetRelation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;

public class SetCursor extends Cursor implements ISet {

	public SetCursor(ISet value) {
		super(value);
	}

	
	public SetCursor(ISet value, Context ctx) {
		super(value, ctx);
	}
	
	private ISet getSet() {
		return (ISet)getWrappedValue();
	}

	@Override
	public ISet union(ISet collection) {
		return new SetCursor(getSet().union(collection), getCtx());
	}

	@Override
	public ISet intersect(ISet collection) {
		return new SetCursor(getSet().intersect(collection), getCtx());
	}

	@Override
	public ISet subtract(ISet collection) {
		return new SetCursor(getSet().subtract(collection), getCtx());
	}

	@Override
	public Iterator<IValue> iterator() {
		return new Iterator<IValue>() {
			Iterator<IValue> iter = getSet().iterator();

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public IValue next() {
				IValue elt = iter.next();
				Context ctx = new SetContext(getCtx(), getSet().delete(elt));
				return TypeToCursor.makeCursor(elt, ctx);
			}

			@Override
			public void remove() {
			}
		};
	}

	@Override
	public Type getElementType() {
		return getSet().getElementType();
	}

	@Override
	public boolean isEmpty() {
		return getSet().isEmpty();
	}

	@Override
	public int size() {
		return getSet().size();
	}

	@Override
	public boolean contains(IValue element) {
		return getSet().contains(element);
	}

	@Override
	public ISet insert(IValue element) {
		return new SetCursor(getSet().insert(element), getCtx());
	}

	@Override
	public ISet delete(IValue elem) {
		return new SetCursor(getSet().delete(elem), getCtx());
	}

	@Override
	public ISet product(ISet set) {
		return new SetCursor(getSet().product(set), getCtx());
	}

	@Override
	public boolean isSubsetOf(ISet other) {
		return getSet().isSubsetOf(other);
	}

	@Override
	public boolean isRelation() {
		return getSet().isRelation();
	}

	@Override
	public ISetRelation<ISet> asRelation() {
		return getSet().asRelation();
	}

}
