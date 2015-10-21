/*******************************************************************************
 * Copyright (c) 2007-2013 IBM Corporation & CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.value.impl.reference;

import java.util.Iterator;

import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.impl.AbstractValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.visitors.IValueVisitor;

class Tuple extends AbstractValue implements ITuple {

	protected final Type fType;
	protected final IValue[] fElements;

	/*package*/ Tuple(IValue... elements) {
		super();
		this.fType = TypeFactory.getInstance().tupleType(elements);
		this.fElements = elements;
	}

	private Tuple(Tuple other, int i, IValue elem) {
		this.fType = other.getType();
		fElements = other.fElements.clone();
		fElements[i] = elem;
	}

	@Override
	public Type getType() {
		return fType;
	}

	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return v.visitTuple(this);
	}

	@Override
	public boolean isEqual(IValue other) {
		return equals(other);
	}

	/*package*/ Tuple(Type tupleType, IValue[] elems) {
		super();
		this.fType = tupleType;
		this.fElements = elems;
	}

	@Override
	public Iterator<IValue> iterator() {
		return new Iterator<IValue>() {
			private int count = 0;

			@Override
			public boolean hasNext() {
				return count < arity();
			}

			@Override
			public IValue next() {
				return get(count++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Can not remove elements from a tuple");
			}
		};
	}

	@Override
	public int hashCode() {
		int hash = 0;

		for (int i = 0; i < fElements.length; i++) {
			hash = (hash << 1) ^ (hash >> 1) ^ fElements[i].hashCode();
		}
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o == null) {
			return false;
		} else if (getClass() == o.getClass()) {
			Tuple peer = (Tuple) o;

			if (!fType.comparable(peer.fType)) {
				return false;
			}

			int arity = arity();
			if (arity != peer.arity()) {
				return false;
			}
			for (int i = 0; i < arity; i++) {
				if (!get(i).equals(peer.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public IValue get(int i) throws IndexOutOfBoundsException {
		try {
			return fElements[i];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException("Tuple index " + i + " is larger than tuple width " + arity());
		}
	}

	@Override
	public IValue get(String label) throws FactTypeUseException {
		return fElements[fType.getFieldIndex(label)];
	}

	@Override
	public ITuple set(int i, IValue arg) throws IndexOutOfBoundsException {
		return new Tuple(this, i, arg);
	}

	@Override
	public ITuple set(String label, IValue arg) throws FactTypeUseException {
		int i = fType.getFieldIndex(label);
		return new Tuple(this, i, arg);
	}

	@Override
	public int arity() {
		return fElements.length;
	}

	@Override
	public IValue select(int... fields) throws IndexOutOfBoundsException {
		Type type = fType.select(fields);

		if (type.isFixedWidth()) {
			return doSelect(type, fields);
		}

		return get(fields[0]);
	}

	@Override
	public IValue selectByFieldNames(String... fields) throws FactTypeUseException {
		Type type = fType.select(fields);

		if (type.isFixedWidth()) {
			int[] indexes = new int[fields.length];
			int i = 0;
			for (String name : fields) {
				indexes[i] = type.getFieldIndex(name);
			}

			return doSelect(type, indexes);
		}

		return get(fields[0]);
	}

	private IValue doSelect(Type type, int... fields) throws IndexOutOfBoundsException {
		if (fields.length == 1)
			return get(fields[0]);
		IValue[] elems = new IValue[fields.length];
		Type[] elemTypes = new Type[fields.length];
		for (int i = 0; i < fields.length; i++) {
			elems[i] = get(fields[i]);
			elemTypes[i] = elems[i].getType();
		}
		return new Tuple(TypeFactory.getInstance().tupleType(elemTypes), elems);
	}
}
