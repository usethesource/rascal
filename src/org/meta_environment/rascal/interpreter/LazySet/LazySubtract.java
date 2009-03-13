package org.meta_environment.rascal.interpreter.LazySet;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;


public class LazySubtract extends LazySet {
	private int size = 0;

	LazySubtract(ISet V, ISet other){
		super(V);
		typecheckSet(other);
	}
	public boolean contains(IValue element) {
		return base.contains(element) && !partner.contains(element);
	}

	public <SetOrRel extends ISet> SetOrRel delete(IValue element) {
		if(partner.contains(element))
			return (SetOrRel) this;
		return (SetOrRel) new LazyDelete(this, element);
	}

	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		if(partner.contains(element) || !base.contains(element))
			return (SetOrRel) this;
		return (SetOrRel) new LazyDelete(this, element);
	}

	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		return (SetOrRel) new LazyIntersect(this, set);
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean isSubsetOf(ISet other) {
		for(IValue v : this){
			if(!partner.contains(v) && !other.contains(v))
				return false;
		}
		return true;
	}

	public int size() {
		int s = size;
		if(s == 0){
			for(IValue v : base){
				if(!partner.contains(v))
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
		return (SetOrRel) new LazyUnion(this,set);
	}

	public Iterator<IValue> iterator() {
		return new LazySubtractIterator(this);
	}
	
	private static class LazySubtractIterator implements Iterator<IValue> {
		private final Iterator<IValue> iter;
		private final LazySubtract Sub;
		private int seen;
		private int size;
		
		public LazySubtractIterator(LazySubtract S) {
			Sub = S;
			iter = S.base.iterator();
			seen = 0;
			size = S.size();
		}

		public boolean hasNext() {
			return seen < size;
		}

		public IValue next() {
			while(iter.hasNext()){
				IValue v = iter.next();
				if(!Sub.partner.contains(v)){
					seen++;
					return v;
				}
			}
			throw new ImplementationError("LazyIntersectIterator");
		}

		public void remove() {
			throw new UnsupportedOperationException("remove in LazySubstractIterator");
		}
	}
}
