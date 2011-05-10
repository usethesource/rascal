/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;

class SubSetGenerator implements Iterator<ISet> {
	private final IEvaluatorContext ctx;
	private ISet remainingElements;
	private Iterator<IValue> elementGen;
	private SubSetGenerator subsetGen;
	private IValue currentElement;
	private boolean hasNext;

	SubSetGenerator(ISet elements, IEvaluatorContext ctx){
		this.remainingElements = elements;
		elementGen = elements.iterator();
		this.ctx = ctx;
		this.hasNext = true;
	}
	
	public boolean hasNext() {
		return hasNext;
	}

	public ISet next() {
		if(subsetGen == null || !subsetGen.hasNext()){
			if(elementGen.hasNext()){
				currentElement = elementGen.next();
				remainingElements = remainingElements.subtract(ctx.getValueFactory().set(currentElement));
				subsetGen = new SubSetGenerator(remainingElements, ctx);
			} else {
				hasNext = false;
				return ctx.getValueFactory().set();
			}
		}
		return subsetGen.next().insert(currentElement);
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in SubSetGenerator");
	}
}
