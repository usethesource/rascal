package org.meta_environment.rascal.interpreter.matching;

import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;

class MapKeyValueIterator implements Iterator<IValue> {
	private Iterator<Entry<IValue,IValue>> iter;
	private Entry <IValue,IValue> prevEntry;
	
	MapKeyValueIterator(IMap map){
		iter = map.entryIterator();
		prevEntry = null;
	}

	public boolean hasNext() {
		return prevEntry != null || iter.hasNext();
	}

	public IValue next() {
		if(prevEntry == null){
			prevEntry = iter.next();
			return prevEntry.getKey();
		}
		IValue val = prevEntry.getValue();
		prevEntry = null;
		return val;
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in MapKeyValueIterator");
	}
}