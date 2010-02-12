package org.rascalmpl.interpreter.matching;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;

class TupleElementIterator implements Iterator<IValue> {
	private ITuple tuple;
	private int index;
	
	TupleElementIterator(ITuple tuple){
		this.tuple = tuple;
		index = 0;
	}

	public boolean hasNext() {
		return index < tuple.arity();
	}

	public IValue next() {
		return tuple.get(index++);
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in TupleElementGenerator");
	}
}