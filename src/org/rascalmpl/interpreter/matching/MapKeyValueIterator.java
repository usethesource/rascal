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
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;

public class MapKeyValueIterator implements Iterator<IValue> {
	private Iterator<Entry<IValue,IValue>> iter;
	private Entry <IValue,IValue> prevEntry;
	
	public MapKeyValueIterator(IMap map){
		iter = map.entryIterator();
		prevEntry = null;
	}

	public boolean hasNext() {
		return prevEntry != null || iter.hasNext();
	}

	public IValue next() {
		if(prevEntry == null){
			prevEntry = iter.next();
			return prevEntry.getKey();
		}
		IValue val = prevEntry.getValue();
		prevEntry = null;
		return val;
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in MapKeyValueIterator");
	}
}
