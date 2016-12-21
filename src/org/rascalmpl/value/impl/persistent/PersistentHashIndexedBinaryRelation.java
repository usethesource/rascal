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

import io.usethesource.capsule.DefaultTrieSet;
import io.usethesource.capsule.DefaultTrieSetMultimap;
import io.usethesource.capsule.api.deprecated.ImmutableSet;
import io.usethesource.capsule.api.deprecated.ImmutableSetMultimap;
import io.usethesource.capsule.api.deprecated.TransientSetMultimap;
import io.usethesource.capsule.util.ArrayUtilsInt;
import io.usethesource.capsule.util.stream.CapsuleCollectors;
import org.rascalmpl.value.*;
import org.rascalmpl.value.exceptions.IllegalOperationException;
import org.rascalmpl.value.impl.AbstractSet;
import org.rascalmpl.value.impl.func.SetFunctions;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.util.AbstractTypeBag;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.rascalmpl.value.impl.persistent.EmptySet.EMPTY_SET;
import static org.rascalmpl.value.impl.persistent.SetWriter.*;
import static org.rascalmpl.value.util.AbstractTypeBag.toTypeBag;

public final class PersistentHashIndexedBinaryRelation extends AbstractSet {

  private Type cachedRelationType;
  private final AbstractTypeBag keyTypeBag;
  private final AbstractTypeBag valTypeBag;
  private final ImmutableSetMultimap<IValue, IValue> content;

  // TODO: make private
  public static final ISet from(final AbstractTypeBag keyTypeBag, final AbstractTypeBag valTypeBag,
      final ImmutableSetMultimap<IValue, IValue> content) {
    if (content.isEmpty()) {
      return EMPTY_SET;
    } else {
      return new PersistentHashIndexedBinaryRelation(keyTypeBag, valTypeBag, content);
    }
  }

  private PersistentHashIndexedBinaryRelation(AbstractTypeBag keyTypeBag,
      AbstractTypeBag valTypeBag, ImmutableSetMultimap<IValue, IValue> content) {
    this.keyTypeBag = Objects.requireNonNull(keyTypeBag);
    this.valTypeBag = Objects.requireNonNull(valTypeBag);
    this.content = Objects.requireNonNull(content);

    assert USE_MULTIMAP_BINARY_RELATIONS
        && isTupleOfArityTwo.test(getTypeFactory().tupleType(keyTypeBag.lub(), valTypeBag.lub()));
    assert USE_MULTIMAP_BINARY_RELATIONS && !content.isEmpty();
    assert USE_MULTIMAP_BINARY_RELATIONS && checkDynamicType(keyTypeBag, valTypeBag, content);
  }

  private static final boolean checkDynamicType(final AbstractTypeBag keyTypeBag,
      final AbstractTypeBag valTypeBag, final ImmutableSetMultimap<IValue, IValue> content) {

    final AbstractTypeBag expectedKeyTypeBag = content.entrySet().stream().map(Map.Entry::getKey)
        .map(IValue::getType).collect(toTypeBag());

    final AbstractTypeBag expectedValTypeBag = content.entrySet().stream().map(Map.Entry::getValue)
        .map(IValue::getType).collect(toTypeBag());

    boolean keyTypesEqual = expectedKeyTypeBag.equals(keyTypeBag);
    boolean valTypesEqual = expectedValTypeBag.equals(valTypeBag);

    return keyTypesEqual && valTypesEqual;
  }

  // internal use: introspecting backing implementation; TODO: reconsider visibility
  public AbstractTypeBag getKeyTypeBag() {
    return keyTypeBag;
  }

  // internal use: introspecting backing implementation; TODO: reconsider visibility
  public AbstractTypeBag getValTypeBag() {
    return valTypeBag;
  }

  // internal use: introspecting backing implementation; TODO: reconsider visibility
  public ImmutableSetMultimap<IValue, IValue> getContent() {
    return content;
  }

  @Override
  protected IValueFactory getValueFactory() {
    return ValueFactory.getInstance();
  }

  @Override
  public Type getType() {
    if (cachedRelationType == null) {
      cachedRelationType = getTypeFactory().relType(keyTypeBag.lub(), valTypeBag.lub());
    }
    return cachedRelationType;
  }

  @Override
  public boolean isEmpty() {
    return content.isEmpty();
  }

  @Override
  public ISet insert(IValue value) {
    // TODO: check if binary tuple
    // assert USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(value.getType());
    if (!isTupleOfArityTwo.test(value.getType()))
      throw new UnsupportedOperationException("Conversion not supported yet.");

    final ITuple tuple = (ITuple) value;
    final IValue key = tuple.get(0);
    final IValue val = tuple.get(1);

    final ImmutableSetMultimap<IValue, IValue> contentNew;

    if (content.isEmpty()) {
      contentNew = DefaultTrieSetMultimap.<IValue, IValue>of(equivalenceEqualityComparator)
          .__insert(key, val);
    } else {
      contentNew = content.__insert(key, val);
    }

    if (content == contentNew)
      return this;

    final AbstractTypeBag keyTypeBagNew = keyTypeBag.increase(key.getType());
    final AbstractTypeBag valTypeBagNew = valTypeBag.increase(val.getType());

    return PersistentHashIndexedBinaryRelation.from(keyTypeBagNew, valTypeBagNew, contentNew);
  }

  @Override
  public ISet delete(IValue value) {
    // TODO: check if binary tuple
    // assert USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(value.getType());
    if (!isTupleOfArityTwo.test(value.getType()))
      return this;

    final ITuple tuple = (ITuple) value;
    final IValue key = tuple.get(0);
    final IValue val = tuple.get(1);

    final ImmutableSetMultimap<IValue, IValue> contentNew = content.__removeEntry(key, val);

    if (content == contentNew)
      return this;

    final AbstractTypeBag keyTypeBagNew = keyTypeBag.decrease(key.getType());
    final AbstractTypeBag valTypeBagNew = valTypeBag.decrease(val.getType());

    return PersistentHashIndexedBinaryRelation.from(keyTypeBagNew, valTypeBagNew, contentNew);
  }

  @Override
  public int size() {
    return content.size();
  }

  @Override
  public boolean contains(IValue value) {
    // TODO: check if binary tuple
    // assert USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(value.getType());
    if (!isTupleOfArityTwo.test(value.getType()))
      return false;

    final ITuple tuple = (ITuple) value;
    final IValue key = tuple.get(0);
    final IValue val = tuple.get(1);

    return content.containsEntry(key, val);
  }

  @Override
  public Iterator<IValue> iterator() {
    return content.tupleIterator(getValueFactory()::tuple);
  }

  @Override
  public int hashCode() {
    final int hashCode =
        StreamSupport.stream(spliterator(), false).mapToInt(tuple -> tuple.hashCode()).sum();

    return hashCode;

    // return content.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this)
      return true;
    if (other == null)
      return false;

    if (other instanceof PersistentHashIndexedBinaryRelation) {
      PersistentHashIndexedBinaryRelation that = (PersistentHashIndexedBinaryRelation) other;

      if (this.getType() != that.getType())
        return false;

      if (this.size() != that.size())
        return false;

      return content.equals(that.content);
    }

    if (other instanceof ISet) {
      ISet that = (ISet) other;

      if (this.getType() != that.getType())
        return false;

      if (this.size() != that.size())
        return false;

      for (IValue value : that) {
        // TODO: check if binary tuple
        // assert USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(value.getType());

        final ITuple tuple = (ITuple) value;
        final IValue key = tuple.get(0);
        final IValue val = tuple.get(1);

        if (!content.containsEntry(key, val))
          return false;
      }

      return true;
    }

    return false;
  }

  @Override
  public boolean isEqual(IValue other) {
    if (other == this)
      return true;
    if (other == null)
      return false;

    if (other instanceof ISet) {
      ISet that = (ISet) other;

      if (this.size() != that.size())
        return false;

      for (IValue value : that) {
        // TODO: check if binary tuple
        // assert USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(value.getType());

        if (!isTupleOfArityTwo.test(value.getType())) {
          return false;
        }

        final ITuple tuple = (ITuple) value;
        final IValue key = tuple.get(0);
        final IValue val = tuple.get(1);

        // TODO: reconsider hiding of comparator vs exposition via argument
        // TODO: containsEntry in isEquals does not use equivalence explicitly here
        // content.containsEntryEquivalent(key, val, equivalenceComparator);

        if (!content.containsEntry(key, val))
          return false;
      }

      return true;
    }

    return false;
  }

  @Override
  public ISet union(ISet other) {
    if (other == this)
      return this;
    if (other == null)
      return this;

    if (other instanceof PersistentHashIndexedBinaryRelation) {
      PersistentHashIndexedBinaryRelation that = (PersistentHashIndexedBinaryRelation) other;

      final ImmutableSetMultimap<IValue, IValue> one;
      final ImmutableSetMultimap<IValue, IValue> two;
      AbstractTypeBag keyTypeBagNew;
      AbstractTypeBag valTypeBagNew;
      final ISet def;

      if (that.size() >= this.size()) {
        def = that;
        one = that.content;
        keyTypeBagNew = that.keyTypeBag;
        valTypeBagNew = that.valTypeBag;
        two = this.content;
      } else {
        def = this;
        one = this.content;
        keyTypeBagNew = this.keyTypeBag;
        valTypeBagNew = this.valTypeBag;
        two = that.content;
      }

      TransientSetMultimap<IValue, IValue> tmp = one.asTransient(); // non-final due to
      // conversion
      boolean modified = false;

      for (Map.Entry<IValue, IValue> entry : two.entrySet()) {
        // try {
        final IValue key = entry.getKey();
        final IValue val = entry.getValue();

        if (tmp.__insert(key, val)) {
          modified = true;
          keyTypeBagNew = keyTypeBagNew.increase(key.getType());
          valTypeBagNew = valTypeBagNew.increase(val.getType());
        }
        // } catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
        // // Conversion from ImmutableSetMultimapAsImmutableSetView to
        // // DefaultTrieSet
        // // TODO: use keyTypeBag for deciding upon conversion and
        // // not exception
        //
        // TransientSetMultimap<IValue, IValue> convertedSetContent = DefaultTrieSet.transientOf();
        // convertedSetContent.__insertAll(tmp);
        // tmp = convertedSetContent;
        //
        // // retry
        // if (tmp.__insertEquivalent(key, equivalenceComparator)) {
        // modified = true;
        // keyTypeBagNew = keyTypeBagNew.increase(key.getType());
        // }
        // }
      }

      if (modified) {
        return PersistentHashIndexedBinaryRelation.from(keyTypeBagNew, valTypeBagNew, tmp.freeze());
      }
      return def;
    } else {
      return super.union(other);
    }
  }

  @Override
  public ISet intersect(ISet other) {
    if (other == this)
      return this;
    if (other == null)
      return EMPTY_SET;

    if (other instanceof PersistentHashIndexedBinaryRelation) {
      PersistentHashIndexedBinaryRelation that = (PersistentHashIndexedBinaryRelation) other;

      final ImmutableSetMultimap<IValue, IValue> one;
      final ImmutableSetMultimap<IValue, IValue> two;
      AbstractTypeBag keyTypeBagNew;
      AbstractTypeBag valTypeBagNew;
      final ISet def;

      if (that.size() >= this.size()) {
        def = this;
        one = this.content;
        keyTypeBagNew = this.keyTypeBag;
        valTypeBagNew = this.valTypeBag;
        two = that.content;
      } else {
        def = that;
        one = that.content;
        keyTypeBagNew = that.keyTypeBag;
        valTypeBagNew = that.valTypeBag;
        two = this.content;
      }

      final TransientSetMultimap<IValue, IValue> tmp = one.asTransient();
      boolean modified = false;

      for (Iterator<Map.Entry<IValue, IValue>> it = tmp.entryIterator(); it.hasNext();) {
        final Map.Entry<IValue, IValue> tuple = it.next();
        final IValue key = tuple.getKey();
        final IValue val = tuple.getValue();

        if (!two.containsEntry(key, val)) {
          it.remove();
          modified = true;
          keyTypeBagNew = keyTypeBagNew.decrease(key.getType());
          valTypeBagNew = valTypeBagNew.decrease(val.getType());
        }
      }

      if (modified) {
        final ImmutableSetMultimap<IValue, IValue> contentNew = tmp.freeze();

        // canonicalize
        if (contentNew.size() == 0) {
          return EMPTY_SET;
        } else {
          return PersistentHashIndexedBinaryRelation.from(keyTypeBagNew, valTypeBagNew, contentNew);
        }
      }
      return def;
    } else {
      return super.intersect(other);
    }
  }

  @Override
  public ISet subtract(ISet other) {
    if (other == this)
      return EMPTY_SET;
    if (other == null)
      return this;

    if (other instanceof PersistentHashIndexedBinaryRelation) {
      PersistentHashIndexedBinaryRelation that = (PersistentHashIndexedBinaryRelation) other;

      final ImmutableSetMultimap<IValue, IValue> one;
      final ImmutableSetMultimap<IValue, IValue> two;
      AbstractTypeBag keyTypeBagNew;
      AbstractTypeBag valTypeBagNew;
      final ISet def;

      def = this;
      one = this.content;
      keyTypeBagNew = this.keyTypeBag;
      valTypeBagNew = this.valTypeBag;
      two = that.content;

      final TransientSetMultimap<IValue, IValue> tmp = one.asTransient();
      boolean modified = false;

      for (Map.Entry<IValue, IValue> tuple : two.entrySet()) {
        final IValue key = tuple.getKey();
        final IValue val = tuple.getValue();

        if (tmp.__removeTuple(key, val)) {
          modified = true;
          keyTypeBagNew = keyTypeBagNew.decrease(key.getType());
          valTypeBagNew = valTypeBagNew.decrease(val.getType());
        }
      }

      if (modified) {
        final ImmutableSetMultimap<IValue, IValue> contentNew = tmp.freeze();

        // canonicalize
        if (contentNew.size() == 0) {
          return EMPTY_SET;
        } else {
          return PersistentHashIndexedBinaryRelation.from(keyTypeBagNew, valTypeBagNew, contentNew);
        }
      }
      return def;
    } else {
      return super.subtract(other);
    }
  }

  @Override
  public ISet product(ISet that) {
    // TODO Auto-generated method stub
    return super.product(that);
  }

  @Override
  public boolean isSubsetOf(ISet that) {
    // TODO Auto-generated method stub
    return super.isSubsetOf(that);
  }

  @Override
  public ISetRelation<ISet> asRelation() {
    IValueFactory vf1 = getValueFactory();
    PersistentHashIndexedBinaryRelation rel11 = this;
    return new ISetRelation<ISet>() {
      protected final IValueFactory vf = vf1;
      protected final PersistentHashIndexedBinaryRelation rel1 = rel11;

      private void validateSetMultimap(ISet... sets) {
        if (!isSetMultimap(sets)) {
          throw new IllegalArgumentException("Arguments are not multimap-backed binary relations.");
        }
      }

      private boolean isSetMultimap(ISet... sets) {
        boolean conditionHolds =
            Arrays.stream(sets).allMatch(set -> set instanceof PersistentHashIndexedBinaryRelation);

        return conditionHolds;
      }

      private ImmutableSetMultimap<IValue, IValue> extractSetMultimap(ISet set) {
        validateSetMultimap(set);
        return ((PersistentHashIndexedBinaryRelation) set).getContent();
      }

      @Override
      public ISet compose(ISetRelation<ISet> rel2) {
        // return SetFunctions.compose(vf, rel1, rel2.asSet());

        if (!isSetMultimap(rel2.asSet()))
          return SetFunctions.compose(vf, rel1, rel2.asSet());

        final ImmutableSetMultimap<IValue, IValue> xy = rel1.getContent();
        final ImmutableSetMultimap<IValue, IValue> yz = extractSetMultimap(rel2.asSet());

        final TransientSetMultimap<IValue, IValue> xz = xy.asTransient();

        for (IValue x : xy.keySet()) {
          final ImmutableSet<IValue> ys = xy.get(x);
          // TODO: simplify expression with nullable data
          final ImmutableSet<IValue> zs = ys.stream()
              .flatMap(y -> Optional.ofNullable(yz.get(y)).orElseGet(DefaultTrieSet::of).stream())
              .collect(CapsuleCollectors.toSet());

          if (zs == null) {
            xz.__remove(x);
          } else {
            // xz.__put(x, zs); // TODO: requires node batch update support

            xz.__remove(x);
            zs.forEach(z -> xz.__insert(x, z));
          }
        }

        //    // @formatter:off
        //    final Stream<BiConsumer<IValue, IValue>> localStream = null;
        //    final Node updatedNode = localStream
        //        .filter((x, y) -> yz.containsKey(y))
        //        .mapValues(y -> yz.get(y))
        //        .collect(toNode());
        //    // @formatter:on

        final ImmutableSetMultimap<IValue, IValue> data = xz.freeze();

        // Iterator<Map.Entry<IValue, IValue>> entryIterator = data.entryIterator();
        // entryIterator.forEachRemaining(tuple -> {
        // if (isInstanceOf(IValue.class).test(tuple.getValue())) {
        // return;
        // } else {
        // return;
        // }
        // });

        data.entrySet().stream().map(Map.Entry::getValue).map(IValue::getType).collect(toTypeBag());

        // final Function<Map.Entry<IValue, IValue>, Type> tupleToTypeMapper =
        // (tuple) -> TF.tupleType(tuple.getKey().getType(), tuple.getValue().getType());
        //
        // final AbstractTypeBag elementTypeBag =
        // data.entrySet().stream().map(tupleToTypeMapper).collect(toTypeBag());

        final AbstractTypeBag keyTypeBag = data.entrySet().stream().map(Map.Entry::getKey)
            .map(IValue::getType).collect(toTypeBag());

        final AbstractTypeBag valTypeBag = data.entrySet().stream().map(Map.Entry::getValue)
            .map(IValue::getType).collect(toTypeBag());

        // canonicalize
        if (data.size() == 0) {
          return EMPTY_SET;
        } else {
          /** TODO does not take into account {@link IValueFactory} */
          return from(keyTypeBag, valTypeBag, data);
        }
      }

      @Override
      public ISet closure() {
        return SetFunctions.closure(vf, rel1);
      }

      @Override
      public ISet closureStar() {
        return SetFunctions.closureStar(vf, rel1);
      }

      @Override
      public int arity() {
        return rel1.getElementType().getArity();
      }

      @Override
      public ISet project(int... fieldIndexes) {
        if (Arrays.equals(fieldIndexes, ArrayUtilsInt.arrayOfInt(0)))
          return domain();

        if (Arrays.equals(fieldIndexes, ArrayUtilsInt.arrayOfInt(1)))
          return range();

        if (Arrays.equals(fieldIndexes, ArrayUtilsInt.arrayOfInt(0, 1)))
          return rel1;

        // TODO: support fast inverse operator
        if (Arrays.equals(fieldIndexes, ArrayUtilsInt.arrayOfInt(1, 0)))
          return SetFunctions.project(vf, rel1, fieldIndexes);

        throw new IllegalStateException("Binary relation patterns exhausted.");
      }

      @Override
      public ISet projectByFieldNames(String... fieldNames) {
        final Type fieldTypeType = rel1.getType().getFieldTypes();

        if (!fieldTypeType.hasFieldNames())
          throw new IllegalOperationException("select with field names", rel1.getType());

        final int[] fieldIndices =
            Stream.of(fieldNames).mapToInt(fieldTypeType::getFieldIndex).toArray();

        return project(fieldIndices);
      }

      @Override
      public ISet carrier() {
        return rel1.asRelation().domain().union(rel1.asRelation().range());

        // return SetFunctions.carrier(vf, rel1);
      }

      @Override
      public ISet domain() {
        final ImmutableSetMultimap<IValue, IValue> multimap = rel1.getContent();

        /** TODO change {@link ImmutableSetMultimap#keySet()} to return {@link ImmutableSet} */
        final ImmutableSet<IValue> columnData = (ImmutableSet<IValue>) multimap.keySet();
        final AbstractTypeBag columnElementTypeBag =
            columnData.stream().map(IValue::getType).collect(toTypeBag());

        // // final AbstractTypeBag columnElementTypeBag = extractTypeBag(rel1).select(0);
        // final AbstractTypeBag columnElementTypeBag = rel1.getKeyTypeBag();

        // flattening Set[Tuple[K, V], _] to Multimap[K, V]
        if (USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(columnElementTypeBag.lub())) {
          /*
           * EXPERIMENTAL: Enforce that binary relations always are backed by multi-maps (instead of
           * being represented as a set of tuples).
           */
          final ISetWriter w = vf.setWriter();
          columnData.forEach(w::insert);
          return w.done();
        } else {
          /** TODO does not take into account {@link IValueFactory} */
          return PersistentHashSet.from(columnElementTypeBag, columnData);
        }

        // return SetFunctions.domain(vf, rel1);
      }

      @Override
      public ISet range() {
        final ImmutableSetMultimap<IValue, IValue> multimap = rel1.getContent();

        return multimap.values().stream().collect(ValueCollectors.toSet());

        // /** TODO change {@link ImmutableSetMultimap#keySet()} to return {@link ImmutableSet} */
        //
        // final TransientSet<IValue> tmp = DefaultTrieSet.transientOf();
        // multimap.values().forEach(tmp::__insert);
        // final ImmutableSet<IValue> columnData = tmp.freeze();
        //
        // // final AbstractTypeBag columnElementTypeBag = extractTypeBag(rel1).select(1);
        // final AbstractTypeBag columnElementTypeBag = rel1.getValTypeBag();
        //
        // // flattening Set[_, Tuple[K, V]] to Multimap[K, V]
        // if (USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(columnElementTypeBag.lub()))
        // {
        // /*
        // * EXPERIMENTAL: Enforce that binary relations always are backed by multi-maps (instead of
        // * being represented as a set of tuples).
        // */
        // final ISetWriter w = vf.setWriter();
        // columnData.forEach(w::insert);
        // return w.done();
        // } else {
        // /** TODO does not take into account {@link IValueFactory} */
        // return new PersistentHashSet(columnElementTypeBag, columnData);
        // }

        // return SetFunctions.range(vf, rel1);
      }

      @Override
      public ISet asSet() {
        return rel1;
      }

      @Override
      public String toString() {
        return rel1.toString();
      }
    };
  }

}
