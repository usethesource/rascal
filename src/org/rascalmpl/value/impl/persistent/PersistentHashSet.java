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
import io.usethesource.capsule.api.Set;
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
import static org.rascalmpl.value.impl.persistent.SetWriter.equivalenceComparator;
import static org.rascalmpl.value.util.AbstractTypeBag.toTypeBag;

public final class PersistentHashSet extends AbstractSet {

  private Type cachedSetType;
  private final AbstractTypeBag elementTypeBag;
  private final Set.Immutable<IValue> content;

  /**
   * Construction of persistent hash-set.
   *
   * DO NOT CALL OUTSIDE OF {@link PersistentSetFactory}.
   *
   * @param elementTypeBag precise dynamic type
   * @param content immutable set
   */
  PersistentHashSet(AbstractTypeBag elementTypeBag, Set.Immutable<IValue> content) {
    this.elementTypeBag = Objects.requireNonNull(elementTypeBag);
    this.content = Objects.requireNonNull(content);

    assert checkDynamicType(elementTypeBag, content);
    assert !(elementTypeBag.lub() == getTypeFactory().voidType() || content.isEmpty());

    assert this.content.getClass() == DefaultTrieSet.getTargetClass();
  }

  private static final boolean checkDynamicType(final AbstractTypeBag elementTypeBag,
      final Set.Immutable<IValue> content) {

    final AbstractTypeBag expectedElementTypeBag =
        content.stream().map(IValue::getType).collect(toTypeBag());

    boolean expectedTypesEqual = expectedElementTypeBag.equals(elementTypeBag);

    return expectedTypesEqual;
  }

  @Override
  public ISetRelation<ISet> asRelation() {
    validateIsRelation(this);
    return new DefaultRelationViewOnSet(getValueFactory(), this);
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
    final Set.Immutable<IValue> contentNew =
        content.__insertEquivalent(value, equivalenceComparator);

    if (content == contentNew)
      return this;

    final AbstractTypeBag bagNew = elementTypeBag.increase(value.getType());

    return PersistentSetFactory.from(bagNew, contentNew);
  }

  @Override
  public ISet delete(IValue value) {
    final Set.Immutable<IValue> contentNew =
        content.__removeEquivalent(value, equivalenceComparator);

    if (content == contentNew)
      return this;

    final AbstractTypeBag bagNew = elementTypeBag.decrease(value.getType());

    return PersistentSetFactory.from(bagNew, contentNew);
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

      final Set.Immutable<IValue> one;
      final Set.Immutable<IValue> two;
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

      final Set.Transient<IValue> tmp = one.asTransient();
      boolean modified = false;

      for (IValue key : two) {
        if (tmp.__insertEquivalent(key, equivalenceComparator)) {
          modified = true;
          bag = bag.increase(key.getType());
        }
      }

      if (modified) {
        return PersistentSetFactory.from(bag, tmp.freeze());
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

      final Set.Immutable<IValue> one;
      final Set.Immutable<IValue> two;
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

      final Set.Transient<IValue> tmp = one.asTransient();
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
        return PersistentSetFactory.from(bag, tmp.freeze());
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

      final Set.Immutable<IValue> one;
      final Set.Immutable<IValue> two;
      AbstractTypeBag bag;
      final ISet def;

      def = this;
      one = this.content;
      bag = this.elementTypeBag;
      two = that.content;

      final Set.Transient<IValue> tmp = one.asTransient();
      boolean modified = false;

      for (IValue key : two) {
        if (tmp.__removeEquivalent(key, equivalenceComparator)) {
          modified = true;
          bag = bag.decrease(key.getType());
        }
      }

      if (modified) {
        return PersistentSetFactory.from(bag, tmp.freeze());
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
