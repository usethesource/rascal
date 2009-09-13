package org.meta_environment.rascal.interpreter.matching;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.ValueFactoryFactory;

class SingleElementIterator implements Iterator<ISet> {
	private Iterator<IValue> elementIter;
	
	SingleElementIterator(ISet elements){
		this.elementIter = elements.iterator();
	}

	public boolean hasNext() {
		return elementIter.hasNext();
	}

	public ISet next() {
		return ValueFactoryFactory.getValueFactory().set(elementIter.next());
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in SingleElementGenerator");
	}
}