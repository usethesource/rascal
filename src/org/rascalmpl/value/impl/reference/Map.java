/*******************************************************************************
 * Copyright (c) 2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *
 * Based on code by:
 *
 *   * Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation
 *******************************************************************************/
package org.rascalmpl.value.impl.reference;

import java.util.Iterator;
import java.util.Map.Entry;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.impl.AbstractMap;
import org.rascalmpl.value.impl.func.MapFunctions;
import org.rascalmpl.value.type.Type;

/*package*/ class Map extends AbstractMap {

	final Type type;
	final java.util.Map<IValue, IValue> content;

	/*package*/ Map(Type candidateMapType, java.util.Map<IValue, IValue> content) {
		super();
		this.type = inferMapType(candidateMapType, content);
		this.content = content;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	protected IValueFactory getValueFactory() {
		return ValueFactory.getInstance();
	}

	@Override
	public int size() {
		return content.size();
	}

	@Override
	public boolean isEmpty() {
		return content.isEmpty();
	}

	@Override
	public IValue get(IValue key) {
		return MapFunctions.get(getValueFactory(), this, key);
	}

	@Override
	public Iterator<IValue> iterator() {
		return content.keySet().iterator();
	}

	@Override
	public Iterator<IValue> valueIterator() {
		return content.values().iterator();
	}

	@Override
	public Iterator<Entry<IValue, IValue>> entryIterator() {
		return content.entrySet().iterator();
	}

	@Override
	public boolean equals(Object other) {
		return MapFunctions.equals(getValueFactory(), this, other);
	}

	@Override
	public boolean isEqual(IValue other) {
		return MapFunctions.isEqual(getValueFactory(), this, other);
	}

	@Override
	public int hashCode() {
		return MapFunctions.hashCode(getValueFactory(), this);
	}
	
}
