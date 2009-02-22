package org.meta_environment.rascal.interpreter.LazySet;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class LazyIntersect extends LazySet {
	private Integer size = null;
	
	LazyIntersect(ISet V, ISet other){
		super(V);
		typecheckSet(other);
	}

	@Override
	public boolean contains(IValue element) {
		return base.contains(element) && partner.contains(element);
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel delete(IValue element) {
		if(base.contains(element) && partner.contains(element))
			return (SetOrRel) new LazyDelete(this, element);
		return (SetOrRel) this;
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		if(base.contains(element) && partner.contains(element))
			return (SetOrRel) this;
		return (SetOrRel) new LazyInsert(this, element);
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		return (SetOrRel) new LazyIntersect(this, set);
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean isSubSet(ISet other) {
		for(IValue v : this){
			if(!other.contains(v))
				return false;
		}
		return true;
	}

	@Override
	public int size() {
		if(size == null){
			size = 0;
			for(IValue v : base){
				if(partner.contains(v))
					size++;
			}
		}
		return size;
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
		return new LazyIntersectIterator(this);
	}

}

class LazyIntersectIterator implements Iterator<IValue> {
	private final Iterator<IValue> iter1;
	private Iterator<IValue> iter2;
	private LazyIntersect Inter;
	private int seen;
	private int size;


	LazyIntersectIterator(LazyIntersect I) {
		Inter = I;
		iter1 = I.base.iterator();
		iter2 = I.partner.iterator();
		seen = 0;
		size = I.size();
	}

	@Override
	public boolean hasNext() {
		return seen < size;
	}

	@Override
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

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove in LazyIntersectIterator");

	}

}
