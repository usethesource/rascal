package org.meta_environment.rascal.interpreter.LazySet;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;

public class LazyInsert extends LazySet implements ISet {
	private final IValue inserted;
	private final boolean baseContainsInserted;
	private final int sizeBase;
	
	public LazyInsert(ISet V, IValue Ins){
		super(V);
		typecheckElement(Ins);
		inserted = Ins;
		sizeBase = base.size();
		baseContainsInserted = base.contains(inserted);
	}

	public boolean contains(IValue element) {
		return element.equals(inserted) || base.contains(element);
	}

	public <SetOrRel extends ISet> SetOrRel delete(IValue elem) {
		return (SetOrRel) (inserted.equals(elem) ? base : new LazyDelete(this, elem));
	}

	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		if(base.contains(element) || element.equals(inserted))
			return (SetOrRel) this;
		return (SetOrRel) new LazyInsert(this, element);
	}

	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		if(baseContainsInserted && set.contains(inserted))
			return (SetOrRel) new LazyIntersect(base, set);
		return (SetOrRel) new LazyIntersect(this, set);
	}


	public boolean isEmpty() {
		return false;
	}

	public boolean isSubsetOf(ISet other) {
		return other.contains(inserted) && base.isSubsetOf(other);
	}

	public int size() {
		return base.size() + (baseContainsInserted ? 0 : 1);
	}

	public <SetOrRel extends ISet> SetOrRel subtract(ISet set) {
		if(set.contains(inserted))
			return (SetOrRel) new LazySubtract(base, set);
		return (SetOrRel) new LazySubtract(this,set);
	}

	public <SetOrRel extends ISet> SetOrRel union(ISet set) {
		if(set.contains(inserted))
			return (SetOrRel) new LazyUnion(base, set);
		return (SetOrRel) new LazyUnion(this,set);
	}

	public Iterator<IValue> iterator() {
		return new LazyInsertIterator(this);
	}
	
	private static class LazyInsertIterator implements Iterator<IValue> {
		private final Iterator<IValue> iter;
		private final LazyInsert Ins;
		private int seen;
		private int size;

		public LazyInsertIterator(LazyInsert I) {
			Ins = I;
			iter = I.base.iterator();
			seen = 0;
			size = I.size();
		}

		public boolean hasNext() {
			return seen < size;
		}

		public IValue next() {
			if(seen == 0){
				seen++;
				return Ins.inserted;
			}
			IValue v = iter.next();
			seen++;
			return v;
		}

		public void remove() {
			throw new UnsupportedOperationException("remove in LazyInsertIterator");
		}
	}
}
