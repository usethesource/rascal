package org.meta_environment.rascal.interpreter.matching;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

class SingleElementIterator implements Iterator<ISet> {
	private final IValueFactory vf;
	private final Iterator<IValue> elementIter;
	
	SingleElementIterator(ISet elements, IValueFactory valueFactory){
		this.elementIter = elements.iterator();
		this.vf = valueFactory;
	}

	public boolean hasNext() {
		return elementIter.hasNext();
	}

	public ISet next() {
		return vf.set(elementIter.next());
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in SingleElementGenerator");
	}
}