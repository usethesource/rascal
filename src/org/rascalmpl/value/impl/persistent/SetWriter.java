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
import java.util.function.Predicate;

import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.exceptions.UnexpectedElementTypeException;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.util.AbstractTypeBag;
import org.rascalmpl.value.util.EqualityUtils;

import io.usethesource.capsule.DefaultTrieSet;
import io.usethesource.capsule.DefaultTrieSetMultimap;
import io.usethesource.capsule.api.deprecated.ImmutableSetMultimap;
import io.usethesource.capsule.api.deprecated.ImmutableSetMultimapAsImmutableSetView;
import io.usethesource.capsule.api.deprecated.TransientSet;

class SetWriter implements ISetWriter {

  /****************************************/

  static Predicate<Type> isTuple = (type) -> type.isTuple();
  static Predicate<Type> arityEqualsTwo = (type) -> type.getArity() == 2;
  static Predicate<Type> isTupleOfArityTwo = isTuple.and(arityEqualsTwo);

  private static final BiFunction<ITuple, Integer, Object> BINREL_TUPLE_ELEMENT_AT =
      (tuple, position) -> {
        if (position < 0 || position > 1)
          throw new IllegalStateException();

        return tuple.get(position);
      };

  private static final Function<ITuple, Boolean> BINREL_TUPLE_CHECKER =
      (tuple) -> tuple.arity() == 2;

  /*
   * Note, statically references persistent value factory.
   */
  private static final BiFunction<IValue, IValue, ITuple> TUPLE_OF =
      (first, second) -> ValueFactory.getInstance().tuple(first, second);

  /****************************************/

  @SuppressWarnings("unchecked")
  private static final Comparator<Object> equivalenceComparator =
      EqualityUtils.getEquivalenceComparator();

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
    // setContent = DefaultTrieSet.transientOf();
    constructedSet = null;
  }

  SetWriter() {
    super();

    this.checkUpperBound = false;
    this.upperBoundType = null;

    elementTypeBag = AbstractTypeBag.of();
    // setContent = DefaultTrieSet.transientOf();
    constructedSet = null;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void put(IValue element) {
    final Type elementType = element.getType();

    if (checkUpperBound && !elementType.isSubtypeOf(upperBoundType)) {
      throw new UnexpectedElementTypeException(upperBoundType, elementType);
    }

    if (setContent == null) {
      if (isTupleOfArityTwo.test(elementType)) {
        /*
         * EXPERIMENTAL: Enforce that binary relations always are backed by multi-maps (instead of
         * being represented as a set of tuples).
         */
        final ImmutableSetMultimap<IValue, IValue> multimap = DefaultTrieSetMultimap.of();

        setContent =
            (TransientSet) new ImmutableSetMultimapAsImmutableSetView<IValue, IValue, ITuple>(
                multimap, TUPLE_OF, BINREL_TUPLE_ELEMENT_AT, BINREL_TUPLE_CHECKER).asTransient();
      } else {
        setContent = DefaultTrieSet.transientOf();
      }
    }

    try {
      boolean result = setContent.__insertEquivalent(element, equivalenceComparator);
      if (result) {
        elementTypeBag = elementTypeBag.increase(elementType);
      }
    } catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
      /*
       * Conversion from ImmutableSetMultimapAsImmutableSetView to DefaultTrieSet.
       */
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
