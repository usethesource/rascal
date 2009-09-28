package org.meta_environment.rascal.interpreter.matching;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

class SubSetGenerator implements Iterator<ISet> {
	private final IValueFactory vf;
	private ISet remainingElements;
	private Iterator<IValue> elementGen;
	private SubSetGenerator subsetGen;
	private IValue currentElement;
	private boolean hasNext;

	SubSetGenerator(ISet elements, IValueFactory valueFactory){
		this.remainingElements = elements;
		elementGen = elements.iterator();
		this.vf = valueFactory;
		this.hasNext = true;
	}
	
	public boolean hasNext() {
		return hasNext;
	}

	public ISet next() {
		if(subsetGen == null || !subsetGen.hasNext()){
			if(elementGen.hasNext()){
				currentElement = elementGen.next();
				remainingElements = remainingElements.subtract(vf.set(currentElement));
				subsetGen = new SubSetGenerator(remainingElements, vf);
			} else {
				hasNext = false;
				return vf.set();
			}
		}
		return subsetGen.next().insert(currentElement);
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in SubSetGenerator");
	}
}
