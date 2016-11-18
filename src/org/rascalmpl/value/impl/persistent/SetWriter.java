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

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.exceptions.UnexpectedElementTypeException;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.util.AbstractTypeBag;
import org.rascalmpl.value.util.EqualityUtils;

import io.usethesource.capsule.DefaultTrieSet;
import io.usethesource.capsule.api.deprecated.ImmutableSetMultimap;
import io.usethesource.capsule.api.deprecated.ImmutableSetMultimapAsImmutableSetView;
import io.usethesource.capsule.api.deprecated.TransientSet;
import io.usethesource.capsule.experimental.multimap.TrieSetMultimap_ChampBasedPrototype;

class SetWriter implements ISetWriter {

	@SuppressWarnings("unchecked")
	private static final Comparator<Object> equivalenceComparator = EqualityUtils
					.getEquivalenceComparator();

	protected AbstractTypeBag elementTypeBag;
	protected TransientSet<IValue> setContent;

	protected final boolean checkUpperBound;
	protected final Type upperBoundType;
	protected ISet constructedSet;

	SetWriter(Type upperBoundType) {
		super();

		this.checkUpperBound = true;
		this.upperBoundType = upperBoundType;

		elementTypeBag = AbstractTypeBag.of();
//		setContent = DefaultTrieSet.transientOf();
		constructedSet = null;
	}

	SetWriter() {
		super();

		this.checkUpperBound = false;
		this.upperBoundType = null;

		elementTypeBag = AbstractTypeBag.of();
//		setContent = DefaultTrieSet.transientOf();
		constructedSet = null;
	}

	private IValueFactory getValueFactory() {
		return ValueFactory.getInstance();
	}
	
	private void put(IValue element) {
		final Type elementType = element.getType();

		if (checkUpperBound && !elementType.isSubtypeOf(upperBoundType)) {
			throw new UnexpectedElementTypeException(upperBoundType, elementType);
		}
		
		/*
		 * EXPERIMENTAL: Enforce that binary relations always are backed by
		 * multi-maps (instead of being represented as a set of tuples).
		 */
		if (setContent == null) {
			if ((elementType.isTuple() && elementType.getArity() == 2) == true) {
				final ImmutableSetMultimap<IValue, IValue> multimap = TrieSetMultimap_ChampBasedPrototype.<IValue, IValue>of();
	
				final BiFunction<IValue, IValue, ITuple> tupleOf = (first, second) -> getValueFactory().tuple(first,
						second);
	
				final BiFunction<ITuple, Integer, Object> tupleElementAt = (tuple, position) -> {
					switch (position) {
					case 0:
						return ((ITuple) tuple).get(0);
					case 1:
						return ((ITuple) tuple).get(1);
					default:
						throw new IllegalStateException();
					}
				};
	
				final Function<ITuple, Boolean> tupleChecker = (tuple) -> tuple.arity() == 2;
				
				setContent = (TransientSet) new ImmutableSetMultimapAsImmutableSetView<IValue, IValue, ITuple>(
								multimap, tupleOf, tupleElementAt, tupleChecker).asTransient();
			} else {
				setContent = DefaultTrieSet.transientOf();
			}
		}
		
		try {
			boolean result = setContent.__insertEquivalent(element, equivalenceComparator);
			if (result) {
				elementTypeBag = elementTypeBag.increase(elementType);
			}
		} catch(ClassCastException | ArrayIndexOutOfBoundsException e) {
			// Conversion from ImmutableSetMultimapAsImmutableSetView to DefaultTrieSet
			// TODO: use elementTypeBag for deciding upon conversion and not exception
			
			TransientSet<IValue> convertedSetContent = DefaultTrieSet.transientOf();
			convertedSetContent.__insertAll(setContent);			
			setContent = convertedSetContent;
			
			// repeat try-block
			boolean result = setContent.__insertEquivalent(element, equivalenceComparator);
			if (result) {
				elementTypeBag = elementTypeBag.increase(elementType);
			}			
		}
	}

	@Override
	public void insert(IValue... values) throws FactTypeUseException {
		checkMutation();

		for (IValue item : values) {
			put(item);
		}
	}

	@Override
	public void insertAll(Iterable<? extends IValue> collection) throws FactTypeUseException {
		checkMutation();

		for (IValue item : collection) {
			put(item);
		}
	}

	@Override
	public ISet done() {
		if (setContent == null) {
			setContent = DefaultTrieSet.transientOf();
		}
		
		if (constructedSet == null) {
			constructedSet = new PDBPersistentHashSet(elementTypeBag, setContent.freeze());
		}

		return constructedSet;
	}

	private void checkMutation() {
		if (constructedSet != null) {
			throw new UnsupportedOperationException("Mutation of a finalized set is not supported.");
		}
	}

	@Override
	public String toString() {
		return setContent.toString();
	}

}