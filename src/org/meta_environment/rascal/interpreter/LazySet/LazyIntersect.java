package org.meta_environment.rascal.interpreter.LazySet;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class LazyIntersect extends LazySet {
	private int size = 0;
	
	LazyIntersect(ISet V, ISet other){
		super(V);
		typecheckSet(other);
	}

	public boolean contains(IValue element) {
		return base.contains(element) && partner.contains(element);
	}

	public <SetOrRel extends ISet> SetOrRel delete(IValue element) {
		if(base.contains(element) && partner.contains(element))
			return (SetOrRel) new LazyDelete(this, element);
		return (SetOrRel) this;
	}

	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		if(base.contains(element) && partner.contains(element))
			return (SetOrRel) this;
		return (SetOrRel) new LazyInsert(this, element);
	}

	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		return (SetOrRel) new LazyIntersect(this, set);
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean isSubSet(ISet other) {
		for(IValue v : this){
			if(!other.contains(v))
				return false;
		}
		return true;
	}

	public int size() {
		int s = size;
		if(s == 0){
			for(IValue v : base){
				if(partner.contains(v))
					s++;
			}
			size = s;
		}
		
		return s;
	}

	public <SetOrRel extends ISet> SetOrRel subtract(ISet set) {
		return (SetOrRel) new LazySubtract(this, set);
	}

	public <SetOrRel extends ISet> SetOrRel union(ISet set) {
		return (SetOrRel) new LazyUnion(this, set);
	}

	public Iterator<IValue> iterator() {
		return new LazyIntersectIterator(this);
	}
	
	private static class LazyIntersectIterator implements Iterator<IValue> {
		private final Iterator<IValue> iter1;
		private final Iterator<IValue> iter2;
		private final LazyIntersect Inter;
		private int seen;
		private int size;
		
		public LazyIntersectIterator(LazyIntersect I) {
			Inter = I;
			iter1 = I.base.iterator();
			iter2 = I.partner.iterator();
			seen = 0;
			size = I.size();
		}

		public boolean hasNext() {
			return seen < size;
		}

		public IValue next() {
			while(iter1.hasNext()){
				IValue v = iter1.next();
				if(Inter.partner.contains(v)){
					seen++;
					return v;
				}
			}
			while(iter2.hasNext()){
				IValue v = iter2.next();
				if(Inter.base.contains(v)){
					seen++;
					return v;
				}
			}
			throw new ImplementationError("LazyIntersectIterator");
		}

		public void remove() {
			throw new UnsupportedOperationException("remove in LazyIntersectIterator");
		}
	}
}
