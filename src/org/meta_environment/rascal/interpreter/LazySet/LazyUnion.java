package org.meta_environment.rascal.interpreter.LazySet;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class LazyUnion extends LazySet {
	private int size;

	public LazyUnion(ISet S, ISet other){
		super(S);
		typecheckSet(other);
	}
	
	@Override
	public boolean contains(IValue element) {
		return base.contains(element) || partner.contains(element);
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel delete(IValue element) {
		if(contains(element))
			return (SetOrRel) new LazyDelete(this, element);
		return (SetOrRel) this;
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		if(contains(element))
			return (SetOrRel) this;
		return (SetOrRel) new LazyInsert(this, element);
		
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		return (SetOrRel) new LazyIntersect(this, set);
	}

	@Override
	public boolean isEmpty() {
		return base.isEmpty() && partner.isEmpty();
	}

	@Override
	public boolean isSubSet(ISet other) {
		return base.isSubSet(other) && partner.isSubSet(other);
	}
	
	@Override
	public int size(){
		int s = size;
		if(s == 0){
			for(IValue v : base){
				if(!partner.contains(v))
					s++;
			}
			for(IValue v : partner){
				if(!base.contains(v))
					s++;
			}
			size = s;
		}
		
		return s;
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel subtract(ISet set) {
		return (SetOrRel) new LazySubtract(this, set);
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel union(ISet set) {
		return (SetOrRel) new LazyUnion(this, set);
	}

	@Override
	public Iterator<IValue> iterator() {
		return new LazyUnionIterator(this);
	}

	@Override
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static class LazyUnionIterator implements Iterator<IValue> {
		private final Iterator<IValue> iter1;
		private final Iterator<IValue> iter2;
		private final LazyUnion Union;
		private int seen;
		private int size;

		public LazyUnionIterator(LazyUnion U) {
			Union = U;
			iter1 = U.base.iterator();
			iter2 = U.partner.iterator();
			seen = 0;
			size = U.size();
		}

		@Override
		public boolean hasNext() {
			return seen < size;
		}

		@Override
		public IValue next() {
			if(iter1.hasNext()){
				IValue v = iter1.next();
				seen++;
				return v;
			}
			while(iter2.hasNext()){
				IValue v = iter2.next();
				if(!Union.base.contains(v)){
					seen++;
					return v;
				}
			}
			throw new ImplementationError("LazyIntersectIterator");
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove in LazyUnionIterator");
		}
	}
}
