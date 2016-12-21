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
import io.usethesource.capsule.api.deprecated.ImmutableSet;
import io.usethesource.capsule.api.deprecated.TransientSet;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetRelation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.impl.AbstractSet;
import org.rascalmpl.value.impl.DefaultRelationViewOnSet;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.util.AbstractTypeBag;

import java.util.Iterator;
import java.util.Objects;

import static org.rascalmpl.value.impl.persistent.EmptySet.EMPTY_SET;
import static org.rascalmpl.value.impl.persistent.SetWriter.*;
import static org.rascalmpl.value.util.AbstractTypeBag.toTypeBag;

public final class PersistentHashSet extends AbstractSet {

  private Type cachedSetType;
  private final AbstractTypeBag elementTypeBag;
  private final ImmutableSet<IValue> content;

  // TODO: make private
  public static final ISet from(final AbstractTypeBag elementTypeBag,
      final ImmutableSet<IValue> content) {
    if (content.isEmpty()) {
      return EMPTY_SET;
    } else {
      return new PersistentHashSet(elementTypeBag, content);
    }
  }

  private PersistentHashSet(AbstractTypeBag elementTypeBag, ImmutableSet<IValue> content) {
    this.elementTypeBag = Objects.requireNonNull(elementTypeBag);
    this.content = Objects.requireNonNull(content);

    assert checkDynamicType(elementTypeBag, content);
    assert !(elementTypeBag.lub() == getTypeFactory().voidType() || content.isEmpty());

    // final AbstractTypeBag expectedElementTypeBag =
    // content.stream().map(IValue::getType).collect(toTypeBag());
    //
    // boolean elementTypesEqual = expectedElementTypeBag.equals(elementTypeBag);
    // assert elementTypesEqual;

    // /*
    // * EXPERIMENTAL: Enforce that empty sets are always represented as a TrieSet.
    // */
    // if (content.isEmpty()) {
    // this.elementTypeBag = elementTypeBag;
    // this.content = DefaultTrieSet.of();
    // } else {
//    this.elementTypeBag = Objects.requireNonNull(elementTypeBag);
//    this.content = Objects.requireNonNull(content);
    // }

    /*
     * EXPERIMENTAL: Enforce that binary relations always are backed by multi-maps (instead of being
     * represented as a set of tuples).
     */
    // if (USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(elementTypeBag.lub())) {
    // assert this.content.getClass() == ImmutableSetMultimapAsImmutableSetView.class;
    // } else {
    assert this.content.getClass() == DefaultTrieSet.getTargetClass();
    // }
  }

  private static final boolean checkDynamicType(final AbstractTypeBag elementTypeBag,
      final ImmutableSet<IValue> content) {

    final AbstractTypeBag expectedElementTypeBag =
        content.stream().map(IValue::getType).collect(toTypeBag());

    boolean expectedTypesEqual = expectedElementTypeBag.equals(elementTypeBag);

    return expectedTypesEqual;
  }

  // internal use: introspecting backing implementation; TODO: reconsider visibility
  public AbstractTypeBag getElementTypeBag() {
    return elementTypeBag;
  }

  // internal use: introspecting backing implementation; TODO: reconsider visibility
  public ImmutableSet<IValue> getContent() {
    return content;
  }

  @Override
  public ISetRelation<ISet> asRelation() {
    validateIsRelation(this);

    // if (USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(elementTypeBag.lub())) {
    // return new DefaultRelationViewOnSetMultimap(getValueFactory(), this);
    // } else {
    return new DefaultRelationViewOnSet(getValueFactory(), this);
    // }
  }

  @Override
  protected IValueFactory getValueFactory() {
    return ValueFactory.getInstance();
  }

  @Override
  public Type getType() {
    if (cachedSetType == null) {
      final Type elementType = elementTypeBag.lub();

      // consists collection out of tuples?
      if (elementType.isFixedWidth()) {
        cachedSetType = getTypeFactory().relTypeFromTuple(elementType);
      } else {
        cachedSetType = getTypeFactory().setType(elementType);
      }
    }
    return cachedSetType;
  }

  @Override
  public boolean isEmpty() {
    return content.isEmpty();
  }

  @Override
  public ISet insert(IValue value) {
    if (content.isEmpty()) {
      final ImmutableSet<IValue> contentNew;

      if (USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(value.getType())) {
        // TODO: directly construct set-multimap
        return EMPTY_SET.insert(value);
      } else {
        contentNew = DefaultTrieSet.<IValue>of().__insertEquivalent(value, equivalenceComparator);
      }

      // if (USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(value.getType())) {
      // /*
      // * EXPERIMENTAL: Enforce that binary relations always are backed by multi-maps (instead of
      // * being represented as a set of tuples).
      // */
      //
      // final ImmutableSetMultimap<IValue, IValue> multimap =
      // DefaultTrieSetMultimap.of(equivalenceEqualityComparator);
      //
      // // NOTE: tuple untyped
      // final BiFunction<IValue, IValue, IValue> tupleOf =
      // (first, second) -> getValueFactory().tuple(first, second);
      //
      // // NOTE: tuple untyped
      // final BiFunction<IValue, Integer, Object> tupleElementAt = (tuple, position) -> {
      // switch (position) {
      // case 0:
      // return ((ITuple) tuple).get(0);
      // case 1:
      // return ((ITuple) tuple).get(1);
      // default:
      // throw new IllegalStateException();
      // }
      // };
      //
      // // NOTE: tuple untyped
      // final Function<IValue, Boolean> tupleChecker =
      // (argument) -> argument instanceof ITuple && ((ITuple) argument).arity() == 2;
      //
      // contentNew = new ImmutableSetMultimapAsImmutableSetView<IValue, IValue, IValue>(multimap,
      // tupleOf, tupleElementAt, tupleChecker).__insertEquivalent(value, equivalenceComparator);
      // } else {
      // contentNew = DefaultTrieSet.<IValue>of().__insertEquivalent(value, equivalenceComparator);
      // }

      if (content == contentNew)
        return this;

      final AbstractTypeBag bagNew = elementTypeBag.increase(value.getType());

      return PersistentHashSet.from(bagNew, contentNew);
    } else {
      final ImmutableSet<IValue> contentNew =
          content.__insertEquivalent(value, equivalenceComparator);

      if (content == contentNew)
        return this;

      final AbstractTypeBag bagNew = elementTypeBag.increase(value.getType());

      return PersistentHashSet.from(bagNew, contentNew);
    }
  }

  @Override
  public ISet delete(IValue value) {
    final ImmutableSet<IValue> contentNew =
        content.__removeEquivalent(value, equivalenceComparator);

    if (content == contentNew)
      return this;

    final AbstractTypeBag bagNew = elementTypeBag.decrease(value.getType());

    return PersistentHashSet.from(bagNew, contentNew);
  }

  @Override
  public int size() {
    return content.size();
  }

  @Override
  public boolean contains(IValue value) {
    return content.containsEquivalent(value, equivalenceComparator);
  }

  @Override
  public Iterator<IValue> iterator() {
    return content.iterator();
  }

  @Override
  public int hashCode() {
    return content.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this)
      return true;
    if (other == null)
      return false;

    if (other instanceof PersistentHashSet) {
      PersistentHashSet that = (PersistentHashSet) other;

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

      for (IValue e : that)
        if (!content.contains(e))
          return false;

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

      for (IValue e : that)
        if (!content.containsEquivalent(e, equivalenceComparator))
          return false;

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

    if (other instanceof PersistentHashSet) {
      PersistentHashSet that = (PersistentHashSet) other;

      final ImmutableSet<IValue> one;
      final ImmutableSet<IValue> two;
      AbstractTypeBag bag;
      final ISet def;

      if (that.size() >= this.size()) {
        def = that;
        one = that.content;
        bag = that.elementTypeBag;
        two = this.content;
      } else {
        def = this;
        one = this.content;
        bag = this.elementTypeBag;
        two = that.content;
      }

      TransientSet<IValue> tmp = one.asTransient(); // non-final due to
                                                    // conversion
      boolean modified = false;

      for (IValue key : two) {
        // try {
        if (tmp.__insertEquivalent(key, equivalenceComparator)) {
          modified = true;
          bag = bag.increase(key.getType());
        }
        // } catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
        // // Conversion from ImmutableSetMultimapAsImmutableSetView to
        // // DefaultTrieSet
        // // TODO: use elementTypeBag for deciding upon conversion and
        // // not exception
        //
        // TransientSet<IValue> convertedSetContent = DefaultTrieSet.transientOf();
        // convertedSetContent.__insertAll(tmp);
        // tmp = convertedSetContent;
        //
        // // retry
        // if (tmp.__insertEquivalent(key, equivalenceComparator)) {
        // modified = true;
        // bag = bag.increase(key.getType());
        // }
        // }
      }

      if (modified) {
        return PersistentHashSet.from(bag, tmp.freeze());
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

    if (other instanceof PersistentHashSet) {
      PersistentHashSet that = (PersistentHashSet) other;

      final ImmutableSet<IValue> one;
      final ImmutableSet<IValue> two;
      AbstractTypeBag bag;
      final ISet def;

      if (that.size() >= this.size()) {
        def = this;
        one = this.content;
        bag = this.elementTypeBag;
        two = that.content;
      } else {
        def = that;
        one = that.content;
        bag = that.elementTypeBag;
        two = this.content;
      }

      final TransientSet<IValue> tmp = one.asTransient();
      boolean modified = false;

      for (Iterator<IValue> it = tmp.iterator(); it.hasNext();) {
        final IValue key = it.next();
        if (!two.containsEquivalent(key, equivalenceComparator)) {
          it.remove();
          modified = true;
          bag = bag.decrease(key.getType());
        }
      }

      if (modified) {
        return PersistentHashSet.from(bag, tmp.freeze());
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

    if (other instanceof PersistentHashSet) {
      PersistentHashSet that = (PersistentHashSet) other;

      final ImmutableSet<IValue> one;
      final ImmutableSet<IValue> two;
      AbstractTypeBag bag;
      final ISet def;

      def = this;
      one = this.content;
      bag = this.elementTypeBag;
      two = that.content;

      final TransientSet<IValue> tmp = one.asTransient();
      boolean modified = false;

      for (IValue key : two) {
        if (tmp.__removeEquivalent(key, equivalenceComparator)) {
          modified = true;
          bag = bag.decrease(key.getType());
        }
      }

      if (modified) {
        return PersistentHashSet.from(bag, tmp.freeze());
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

}
