package org.meta_environment.rascal.interpreter.LazySet;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class LazySubtract extends LazySet {
	private Integer size;

	LazySubtract(ISet V, ISet other){
		super(V);
		typecheckSet(other);
	}
	@Override
	public boolean contains(IValue element) {
		return base.contains(element) && !partner.contains(element);
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel delete(IValue element) {
		if(partner.contains(element))
			return (SetOrRel) this;
		return (SetOrRel) new LazyDelete(this, element);
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		if(partner.contains(element) || !base.contains(element))
			return (SetOrRel) this;
		return (SetOrRel) new LazyDelete(this, element);
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
			if(!partner.contains(v) && !other.contains(v))
				return false;
		}
		return true;
	}

	@Override
	public int size() {
		if(size == 0){
			size = 0;
			for(IValue v : base){
				if(!partner.contains(v))
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
		return (SetOrRel) new LazyUnion(this,set);
	}

	@Override
	public Iterator<IValue> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}


class LazySubtractIterator implements Iterator<IValue> {
	private final Iterator<IValue> iter;
	private LazySubtract Sub;
	private int seen;
	private int size;


	LazySubtractIterator(LazySubtract S) {
		Sub = S;
		iter = S.base.iterator();
		seen = 0;
		size = S.size();
	}

	@Override
	public boolean hasNext() {
		return seen < size;
	}

	@Override
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

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove in LazyIntersectIterator");

	}

}

