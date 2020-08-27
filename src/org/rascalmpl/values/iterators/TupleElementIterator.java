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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.values.iterators;

import java.util.Iterator;

import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;

public class TupleElementIterator implements Iterator<IValue> {
	private ITuple tuple;
	private int index;
	
	public TupleElementIterator(ITuple tuple){
		this.tuple = tuple;
		index = 0;
	}

	public boolean hasNext() {
		return index < tuple.arity();
	}

	public IValue next() {
		return tuple.get(index++);
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in TupleElementGenerator");
	}
}
