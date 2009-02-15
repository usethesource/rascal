package org.meta_environment.rascal.interpreter;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.exceptions.ImplementationError;

/*
 * SingleIValueIterator turns a single IValue into an Iterator that
 * can be used for implementing generators.
 */

public class SingleIValueIterator implements Iterator<IValue> {	
	private IValue value;
	private boolean firstCall;

	SingleIValueIterator(IValue value){
		this.value = value;
		this.firstCall = true;
	}

	public boolean hasNext() {
		
		return firstCall;
	}

	public IValue next() {
		if(!firstCall){
			throw new ImplementationError("next called more than once");
		}
		firstCall = false;
		return value;
	}

	public void remove() {
		throw new UnsupportedOperationException("remove for SingleIValueIterator");
	}	
}