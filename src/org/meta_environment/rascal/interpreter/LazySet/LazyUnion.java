package org.meta_environment.rascal.interpreter.LazySet;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;


public class LazyUnion extends LazySet {
	private int size;

	public LazyUnion(ISet S, ISet other){
		super(S);
		typecheckSet(other);
	}
	
	public boolean contains(IValue element) {
		return base.contains(element) || partner.contains(element);
	}

	public <SetOrRel extends ISet> SetOrRel delete(IValue element) {
		if(contains(element))
			return (SetOrRel) new LazyDelete(this, element);
		return (SetOrRel) this;
	}

	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		if(contains(element))
			return (SetOrRel) this;
		return (SetOrRel) new LazyInsert(this, element);
		
	}

	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		return (SetOrRel) new LazyIntersect(this, set);
	}

	public boolean isEmpty() {
		return base.isEmpty() && partner.isEmpty();
	}

	public boolean isSubsetOf(ISet other) {
		return base.isSubsetOf(other) && partner.isSubsetOf(other);
	}
	
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

	public <SetOrRel extends ISet> SetOrRel subtract(ISet set) {
		return (SetOrRel) new LazySubtract(this, set);
	}

	public <SetOrRel extends ISet> SetOrRel union(ISet set) {
		return (SetOrRel) new LazyUnion(this, set);
	}

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

		public boolean hasNext() {
			return seen < size;
		}

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

		public void remove() {
			throw new UnsupportedOperationException("remove in LazyUnionIterator");
		}
	}
}
