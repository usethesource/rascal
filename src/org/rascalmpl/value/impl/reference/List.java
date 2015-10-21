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
 *******************************************************************************/
package org.rascalmpl.value.impl.reference;

import java.util.Iterator;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.impl.AbstractList;
import org.rascalmpl.value.impl.func.ListFunctions;
import org.rascalmpl.value.type.Type;

/*package*/ class List extends AbstractList {

	private final Type type;
	private final java.util.List<IValue> content;

	/*package*/ List(Type elementType, java.util.List<IValue> content) {
		super();
		this.type = inferListOrRelType(elementType, content);
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
	public Iterator<IValue> iterator() {
		return content.iterator();
	}

	@Override
	public int length() {
		return content.size();
	}

	@Override
	public IValue get(int i) {
		return content.get(i);
	}

	@Override
	public boolean isEmpty() {
		return content.isEmpty();
	}

	@Override
	public int hashCode() {
		return content.hashCode();
	}

	@Override
	public boolean equals(Object that) {
		return ListFunctions.equals(getValueFactory(), this, that);
	}

	@Override
	public boolean isEqual(IValue that) {
		return ListFunctions.isEqual(getValueFactory(), this, that);
	}

}
