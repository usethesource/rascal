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

import io.usethesource.capsule.DefaultTrieSetMultimap;
import io.usethesource.capsule.api.deprecated.ImmutableSetMultimap;
import io.usethesource.capsule.api.deprecated.TransientSetMultimap;
import org.rascalmpl.value.*;
import org.rascalmpl.value.impl.AbstractSet;
import org.rascalmpl.value.impl.DefaultRelationViewOnSetMultimap;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.util.AbstractTypeBag;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.rascalmpl.value.impl.persistent.SetWriter.equivalenceEqualityComparator;
import static org.rascalmpl.value.impl.persistent.SetWriter.isTupleOfArityTwo;

public final class PDBPersistentHashSetMultimap extends AbstractSet {

  public static final PDBPersistentHashSetMultimap EMPTY = new PDBPersistentHashSetMultimap();

  private Type cachedRelationType;
  private final AbstractTypeBag keyTypeBag;
  private final AbstractTypeBag valTypeBag;
  private final ImmutableSetMultimap<IValue, IValue> content;

  public PDBPersistentHashSetMultimap() {
    this.keyTypeBag = AbstractTypeBag.of();
    this.valTypeBag = AbstractTypeBag.of();
    this.content = DefaultTrieSetMultimap.of(equivalenceEqualityComparator);
  }

  public PDBPersistentHashSetMultimap(AbstractTypeBag keyTypeBag, AbstractTypeBag valTypeBag,
      ImmutableSetMultimap<IValue, IValue> content) {
    Objects.requireNonNull(keyTypeBag);
    Objects.requireNonNull(content);

    // assert USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(keyTypeBag.lub());

    // final AbstractTypeBag expectedKeyTypeBag =
    // content.entrySet().stream().map(Map.Entry::getKey).map(IValue::getType).collect(toTypeBag());
    //
    // final AbstractTypeBag expectedValTypeBag =
    // content.entrySet().stream().map(Map.Entry::getValue).map(IValue::getType).collect(toTypeBag());
    //
    // boolean keyTypesEqual = expectedKeyTypeBag.equals(keyTypeBag);
    // boolean valTypesEqual = expectedValTypeBag.equals(valTypeBag);
    // assert keyTypesEqual;
    // assert valTypesEqual;

    this.keyTypeBag = keyTypeBag;
    this.valTypeBag = valTypeBag;
    this.content = content;
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
  public ISetRelation<ISet> asRelation() {
    return new DefaultRelationViewOnSetMultimap(getValueFactory(), this);
  }

  @Override
  protected IValueFactory getValueFactory() {
    return ValueFactory.getInstance();
  }

  @Override
  public Type getType() {
    if (cachedRelationType == null) {
      // final Type elementType = getTypeFactory().tupleType(keyTypeBag.lub(), valTypeBag.lub());
      // cachedRelationType = getTypeFactory().relTypeFromTuple(elementType);
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

    return new PDBPersistentHashSetMultimap(keyTypeBagNew, valTypeBagNew, contentNew);
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

    return new PDBPersistentHashSetMultimap(keyTypeBagNew, valTypeBagNew, contentNew);
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

    if (other instanceof PDBPersistentHashSetMultimap) {
      PDBPersistentHashSetMultimap that = (PDBPersistentHashSetMultimap) other;

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

    if (other instanceof PDBPersistentHashSetMultimap) {
      PDBPersistentHashSetMultimap that = (PDBPersistentHashSetMultimap) other;

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
        return new PDBPersistentHashSetMultimap(keyTypeBagNew, valTypeBagNew, tmp.freeze());
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
      return EMPTY;

    if (other instanceof PDBPersistentHashSetMultimap) {
      PDBPersistentHashSetMultimap that = (PDBPersistentHashSetMultimap) other;

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
        return new PDBPersistentHashSetMultimap(keyTypeBagNew, valTypeBagNew, tmp.freeze());
      }
      return def;
    } else {
      return super.intersect(other);
    }
  }

  @Override
  public ISet subtract(ISet other) {
    if (other == this)
      return EMPTY;
    if (other == null)
      return this;

    if (other instanceof PDBPersistentHashSetMultimap) {
      PDBPersistentHashSetMultimap that = (PDBPersistentHashSetMultimap) other;

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
          return PDBPersistentHashSet.EMPTY;
        } else {
          return new PDBPersistentHashSetMultimap(keyTypeBagNew, valTypeBagNew, contentNew);
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

}
