package org.rascalmpl.interpreter.matching;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;

class SingleElementIterator implements Iterator<ISet> {
	private final IEvaluatorContext ctx;
	private final Iterator<IValue> elementIter;
	
	SingleElementIterator(ISet elements, IEvaluatorContext ctx){
		this.elementIter = elements.iterator();
		this.ctx = ctx;
	}

	public boolean hasNext() {
		return elementIter.hasNext();
	}

	public ISet next() {
		return ctx.getValueFactory().set(elementIter.next());
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in SingleElementGenerator");
	}
}