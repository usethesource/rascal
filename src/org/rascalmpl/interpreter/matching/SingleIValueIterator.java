/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.Iterator;

import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.value.IValue;


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
