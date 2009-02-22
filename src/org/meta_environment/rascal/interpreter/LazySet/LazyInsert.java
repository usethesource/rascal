package org.meta_environment.rascal.interpreter.LazySet;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public class LazyInsert extends LazySet implements ISet {
	final IValue inserted;
	private final boolean baseContainsInserted;
	private final int sizeBase;
	
	public LazyInsert(ISet V, IValue Ins){
		super(V);
		typecheckElement(Ins);
		inserted = Ins;
		sizeBase = base.size();
		baseContainsInserted = base.contains(inserted);
	}

	@Override
	public boolean contains(IValue element) {
		return element.equals(inserted) || base.contains(element);
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel delete(IValue elem) {
		return (SetOrRel) (inserted.equals(elem) ? base : new LazyDelete(this, elem));
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel insert(IValue element) {
		if(base.contains(element) || element.equals(inserted))
			return (SetOrRel) this;
		return (SetOrRel) new LazyInsert(this, element);
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel intersect(ISet set) {
		if(baseContainsInserted && set.contains(inserted))
			return (SetOrRel) new LazyIntersect(base, set);
		return (SetOrRel) new LazyIntersect(this, set);
	}


	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isSubSet(ISet other) {
		return other.contains(inserted) && base.isSubSet(other);
	}

	@Override
	public int size() {
		return base.size() + (baseContainsInserted ? 0 : 1);
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel subtract(ISet set) {
		if(set.contains(inserted))
			return (SetOrRel) new LazySubtract(base, set);
		return (SetOrRel) new LazySubtract(this,set);
	}

	@Override
	public <SetOrRel extends ISet> SetOrRel union(ISet set) {
		if(set.contains(inserted))
			return (SetOrRel) new LazyUnion(base, set);
		return (SetOrRel) new LazyUnion(this,set);
	}

	@Override
	public Iterator<IValue> iterator() {
		return new LazyInsertIterator(this);
	}

}

class LazyInsertIterator implements Iterator<IValue> {
	private final Iterator<IValue> iter;
	private LazyInsert Ins;
	private int seen;
	private int size;

	LazyInsertIterator(LazyInsert I) {
		Ins = I;
		iter = I.base.iterator();
		seen = 0;
		size = I.size();
	}

	@Override
	public boolean hasNext() {
		return seen < size;
	}

	@Override
	public IValue next() {
		if(seen == 0){
			seen++;
			return Ins.inserted;
		}
		IValue v = iter.next();
		seen++;
		return v;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
