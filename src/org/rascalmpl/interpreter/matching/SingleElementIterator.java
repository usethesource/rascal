/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.Iterator;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IValue;

class SingleElementIterator implements Iterator<IValue> {
	private final Iterator<IValue> elementIter;
	private boolean debug = false;
	
	SingleElementIterator(ISet elements, IEvaluatorContext ctx){
		this.elementIter = elements.iterator();
		if(debug) System.err.println("SingleElementIterator: " + elements);
	}

	public boolean hasNext() {
		return elementIter.hasNext();
	}

	public IValue next() {
		//return ctx.getValueFactory().set(elementIter.next());
		return elementIter.next();
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in SingleElementIterator");
	}
}
