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

import io.usethesource.capsule.api.deprecated.Set;
import io.usethesource.capsule.util.EqualityComparator;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.exceptions.UnexpectedElementTypeException;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.util.AbstractTypeBag;
import org.rascalmpl.value.util.EqualityUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.rascalmpl.value.impl.persistent.EmptySet.EMPTY_SET;

/*
 * TODO: visibility is currently public to allow set-multimap experiments. Must be set back to
 * `protected` when experiments are finished.
 */
public class SetWriter implements ISetWriter {

  /****************************************/

  public static final boolean USE_MULTIMAP_BINARY_RELATIONS = true;
  // static final boolean USE_MULTIMAP_BINARY_RELATIONS = Boolean.getBoolean(String.format("%s.%s",
  // "org.rascalmpl.value", "useMultimapBinaryRelations"));

  /****************************************/

  @SuppressWarnings("unchecked")
  static final Comparator<Object> equivalenceComparator = EqualityUtils.getEquivalenceComparator();

  static final EqualityComparator<Object> equivalenceEqualityComparator =
      (a, b) -> equivalenceComparator.compare(a, b) == 0;

  public static Predicate<Type> isTuple = (type) -> type.isTuple();
  public static Predicate<Type> arityEqualsTwo = (type) -> type.getArity() == 2;
  public static Predicate<Type> isTupleOfArityTwo = isTuple.and(arityEqualsTwo);

  /****************************************/

  protected AbstractTypeBag elementTypeBag;
  protected Set.TransientSet<IValue> setContent;

  protected final boolean checkUpperBound;
  protected final Type upperBoundType;
  protected ISet constructedSet;

  private Type leastUpperBound = TypeFactory.getInstance().voidType();
  private Stream.Builder<Type> typeStreamBuilder = Stream.builder();
  private Stream.Builder<IValue> dataStreamBuilder = Stream.builder();

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

    dataStreamBuilder.accept(element);
    typeStreamBuilder.accept(elementType);
    leastUpperBound = leastUpperBound.lub(elementType);
  }

  @Override
  public void insert(IValue... values) throws FactTypeUseException {
    checkMutation();
    Arrays.stream(values).forEach(this::put);
  }

  @Override
  public void insertAll(Iterable<? extends IValue> collection) throws FactTypeUseException {
    checkMutation();
    collection.forEach(this::put);
  }

  @Override
  public ISet done() {
    if (constructedSet != null)
      return constructedSet;

    final Stream<Type> typeStream = typeStreamBuilder.build();
    final Stream<IValue> dataStream = dataStreamBuilder.build();

    if (leastUpperBound == TypeFactory.getInstance().voidType()) {
      constructedSet = EMPTY_SET;
      return constructedSet;
    }

    if (USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(leastUpperBound)) {
      // collect to multimap

//      final java.util.List<Type> tupleTypes = typeStream.collect(Collectors.toList());
//
//      final AbstractTypeBag keyTypeBag =
//          tupleTypes.stream().map(type -> type.getFieldType(0)).collect(toTypeBag());
//
//      final AbstractTypeBag valTypeBag =
//          tupleTypes.stream().map(type -> type.getFieldType(1)).collect(toTypeBag());
//
//      final Immutable<IValue, IValue> data = dataStream.map(asInstanceOf(ITuple.class))
//          .collect(CapsuleCollectors.toSetMultimap(tuple -> tuple.get(0), tuple -> tuple.get(1)));
//
//      constructedSet = new PersistentHashIndexedBinaryRelation(keyTypeBag, valTypeBag, data);
//      return constructedSet;

      constructedSet = dataStream.map(asInstanceOf(ITuple.class))
          .collect(ValueCollectors.toSetMultimap(tuple -> tuple.get(0), tuple -> tuple.get(1)));

      return constructedSet;
    } else {
      // collect to set

//      final AbstractTypeBag elementTypeBag = typeStream.collect(toTypeBag());
//      final ImmutableSet<IValue> data = dataStream.collect(CapsuleCollectors.toSet());
//
//      constructedSet = new PersistentHashSet(elementTypeBag, data);
//      return constructedSet;

      constructedSet = dataStream.collect(ValueCollectors.toSet());
      return constructedSet;
    }
  }

  // TODO: extract to a utilities class
  @SuppressWarnings("unchecked")
  public static <T, R> Function<T, R> asInstanceOf(Class<R> resultClass) {
    return item -> (R) item;
  }

  // TODO: extract to a utilities class
  public static <T> Predicate<T> isInstanceOf(Class<T> inputClass) {
    return item -> inputClass.isInstance(item);
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
