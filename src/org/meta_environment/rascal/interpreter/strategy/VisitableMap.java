package org.meta_environment.rascal.interpreter.strategy;

import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;

public class VisitableMap implements Visitable {

	private IMap map;

	public VisitableMap(IMap map) {
		this.map = map;
	}

	public int arity() {
		return map.size();
	}

	public Visitable get(int i) throws IndexOutOfBoundsException {
		int index = 0;
		Iterator<Entry<IValue, IValue>> entries = map.entryIterator();
		while(entries.hasNext()) {
			Entry<IValue, IValue> e = entries.next();
			if (index==i) {
				ITuple tuple = ValueFactory.getInstance().tuple(e.getKey(),e.getValue());
				return new VisitableTuple(tuple);
			}
			index ++;
		}
		throw new IndexOutOfBoundsException();
	}

	public IValue getValue() {
		return map;
	}

	public Visitable set(int i, Visitable newChild)
	throws IndexOutOfBoundsException {
		if (i>=arity()) {
			throw new IndexOutOfBoundsException();
		}
		int index = 0;
		IMap newmap = ValueFactory.getInstance().map(map.getKeyType(), map.getValueType());
		Iterator<Entry<IValue, IValue>> entries = map.entryIterator();
		while(entries.hasNext()) {
			Entry<IValue, IValue> e = entries.next();
			IValue key = e.getKey();
			IValue value = e.getValue();
			if (index==i) {
				// safe cast
				// because strategies are type preserving
				ITuple newtuple = (ITuple) (newChild.getValue());
				map.put(newtuple.get(0), newtuple.get(1));
			} else {
				map.put(key, value);
			}
			index ++;
		}
		return new VisitableMap(newmap);
	}



}
