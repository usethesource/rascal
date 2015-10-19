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

import org.rascalmpl.value.INode;
import org.rascalmpl.value.IValue;

class NodeChildIterator implements Iterator<IValue> {
	private INode node;
	private int index;
	
	NodeChildIterator(INode node){
		this.node = node;
		index = 0;
	}

	public boolean hasNext() {
		return index < node.arity();
	}

	public IValue next() {
		return node.get(index++);
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in NodeChildGenerator");
	}
}
