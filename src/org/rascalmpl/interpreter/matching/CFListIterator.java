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
package org.rascalmpl.interpreter.matching;

import java.util.Iterator;

import org.rascalmpl.value.IList;
import org.rascalmpl.value.IValue;

class CFListIterator implements Iterator<IValue> {
	private IList list;
	private int index;
	private int delta;
	
	CFListIterator(IList l, int delta){
		this.list = l;
		this.index = 0;
		this.delta = delta;
	}

	public boolean hasNext() {
		return index < list.length();
	}

	public IValue next() {
		IValue v = list.get(index);
		//System.err.println("index = " + index + ": " + v);
		index += delta;
		return v;
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in CFListIterator");
	}
}
