package org.meta_environment.rascal.interpreter.LazySet;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;

class LazyDelete extends LazySet {
	private final IValue deleted;
	private final boolean baseContainsDeleted;
	private final int baseSize;

	LazyDelete(ISet V, IValue D) {
		super(V);
		deleted = D;
		baseContainsDeleted = base.contains(deleted);
		baseSize = base.size();
	}

	@Override
	public boolean isEmpty() {
		return baseSize == 0 || (baseSize == 1 && baseContainsDeleted);
	}

	@Override
	public int size() {
		return baseSize;
	}

	@Override
	public boolean contains(IValue element) {
		return element.equals(deleted) ? false : base.contains(element);
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		return (SetOrRel) (element.equals(deleted) ? base : new LazyInsert(
				this, element));
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		if (set.contains(deleted) && baseContainsDeleted) {
			return (SetOrRel) new LazyIntersect(this, set);
		} else {
			return (SetOrRel) new LazyIntersect(base, set);
		}
	}

	@Override
	public boolean isSubSet(ISet other) {
		for (IValue v : base) {
			if (!v.equals(deleted) && !other.contains(v)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel subtract(ISet other) {
		if (other.contains(deleted)) {
			return (SetOrRel) new LazySubtract(base, other);
		} else {
			return (SetOrRel) new LazySubtract(this, other);
		}
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel union(ISet set) {
		if (set.contains(deleted)) {
			return (SetOrRel) new LazyIntersect(base, set);
		} else {
			return (SetOrRel) new LazyIntersect(this, set);
		}
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel delete(IValue elem) {
		if (base.contains(elem)){
			return (SetOrRel) new LazyDelete(this, elem);
		}else{
			return (SetOrRel) this;
		}
	}

	@Override
	public Iterator<IValue> iterator() {
		return new LazyDeleteIterator(this);
	}
	
	private static class LazyDeleteIterator implements Iterator<IValue> {
		private final Iterator<IValue> iter;
		private final LazyDelete Del;
		private int seen;
		private int size;

		public LazyDeleteIterator(LazyDelete D) {
			Del = D;
			iter = D.base.iterator();
			seen = 0;
			size = D.size();
		}

		@Override
		public boolean hasNext() {
			return seen < size;
		}

		@Override
		public IValue next() {
			IValue v = iter.next();
			if (v.equals(Del.deleted)) {
				v = iter.next();
			}
			seen++;
			return v;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove in LazyDeleteIterator");
		}
	}
}
