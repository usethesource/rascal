/*******************************************************************************
 * Copyright (c) 2013-2014 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.value.impl.persistent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;

import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.UnexpectedElementTypeException;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.util.AbstractTypeBag;
import org.rascalmpl.value.util.EqualityUtils;

import io.usethesource.capsule.DefaultTrieMap;
import io.usethesource.capsule.TransientMap;

final class MapWriter implements IMapWriter {

	@SuppressWarnings("unchecked")
	private static final Comparator<Object> equivalenceComparator = EqualityUtils
					.getEquivalenceComparator();

	protected AbstractTypeBag keyTypeBag;
	protected AbstractTypeBag valTypeBag;
	protected final TransientMap<IValue, IValue> mapContent;

	protected final boolean checkUpperBound;
	protected final Type upperBoundKeyType;
	protected final Type upperBoundValType;
	protected IMap constructedMap;

	MapWriter() {
		super();

		this.checkUpperBound = false;
		this.upperBoundKeyType = null;
		this.upperBoundValType = null;

		keyTypeBag = AbstractTypeBag.of();
		valTypeBag = AbstractTypeBag.of();
		mapContent = DefaultTrieMap.transientOf();
		constructedMap = null;
	}

	MapWriter(Type prototypeType) {
		super();

		this.checkUpperBound = false;
		this.upperBoundKeyType = prototypeType.getKeyType();
		this.upperBoundValType = prototypeType.getValueType();

		if (prototypeType.hasFieldNames()) {
			keyTypeBag = AbstractTypeBag.of(prototypeType.getKeyLabel());
			valTypeBag = AbstractTypeBag.of(prototypeType.getValueLabel());
		} else {
			keyTypeBag = AbstractTypeBag.of();
			valTypeBag = AbstractTypeBag.of();
		}

		mapContent = DefaultTrieMap.transientOf();
		constructedMap = null;
	}

	@Override
	public void put(IValue key, IValue value) {
		checkMutation();

		final Type keyType = key.getType();
		final Type valType = value.getType();

		if (checkUpperBound) {
			if (!keyType.isSubtypeOf(upperBoundKeyType)) {
				throw new UnexpectedElementTypeException(upperBoundKeyType, keyType);
			}

			if (!valType.isSubtypeOf(upperBoundValType)) {
				throw new UnexpectedElementTypeException(upperBoundValType, valType);
			}
		}

		final IValue replaced = mapContent.__putEquivalent(key, value, equivalenceComparator);

		keyTypeBag = keyTypeBag.increase(keyType);
		valTypeBag = valTypeBag.increase(valType);

		if (replaced != null) {
			final Type replacedType = replaced.getType();
			valTypeBag = valTypeBag.decrease(replacedType);
		}
	}

	@Override
	public void putAll(IMap map) {
		putAll(map.entryIterator());
	}

	@Override
	public void putAll(java.util.Map<IValue, IValue> map) {
		putAll(map.entrySet().iterator());
	}

	private void putAll(Iterator<Entry<IValue, IValue>> entryIterator) {
		checkMutation();

		while (entryIterator.hasNext()) {
			Entry<IValue, IValue> entry = entryIterator.next();
			IValue key = entry.getKey();
			IValue value = entry.getValue();

			this.put(key, value);
		}
	}

	@Override
	public void insert(IValue... values) {
		insertAll(Arrays.asList(values));
	}

	@Override
	public void insertAll(Iterable<? extends IValue> collection) {
		checkMutation();

		Iterator<? extends IValue> collectionIterator = collection.iterator();
		while (collectionIterator.hasNext()) {
			final Object item = collectionIterator.next();

			if (!(item instanceof ITuple)) {
				throw new IllegalArgumentException("Argument must be of ITuple type.");
			}

			final ITuple tuple = (ITuple) item;

			if (tuple.arity() != 2) {
				throw new IllegalArgumentException("Tuple must have an arity of 2.");
			}

			put(tuple.get(0), tuple.get(1));
		}
	}

	protected void checkMutation() {
		if (constructedMap != null) {
			throw new UnsupportedOperationException("Mutation of a finalized map is not supported.");
		}
	}

	@Override
	public IMap done() {
		if (constructedMap == null) {
			constructedMap = new PDBPersistentHashMap(keyTypeBag, valTypeBag, mapContent.freeze());
		}

		return constructedMap;
	}
}
