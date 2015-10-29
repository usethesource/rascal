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
import java.util.Iterator;
import java.util.Objects;

import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.impl.AbstractSet;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.util.AbstractTypeBag;
import org.rascalmpl.value.util.EqualityUtils;

import io.usethesource.capsule.DefaultTrieSet;
import io.usethesource.capsule.ImmutableSet;
import io.usethesource.capsule.TransientSet;

public final class PDBPersistentHashSet extends AbstractSet {

	private static final PDBPersistentHashSet EMPTY = new PDBPersistentHashSet();

	@SuppressWarnings("unchecked")
	private static final Comparator<Object> equivalenceComparator = EqualityUtils.getEquivalenceComparator();

	private Type cachedSetType;
	private final AbstractTypeBag elementTypeBag;
	private final ImmutableSet<IValue> content;

	private PDBPersistentHashSet() {
		this.elementTypeBag = AbstractTypeBag.of();
		this.content = DefaultTrieSet.of();
	}

	public PDBPersistentHashSet(AbstractTypeBag elementTypeBag, ImmutableSet<IValue> content) {
		Objects.requireNonNull(elementTypeBag);
		Objects.requireNonNull(content);
		this.elementTypeBag = elementTypeBag;
		this.content = content;
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
		final ImmutableSet<IValue> contentNew = content.__insertEquivalent(value, equivalenceComparator);

		if (content == contentNew)
			return this;

		final AbstractTypeBag bagNew = elementTypeBag.increase(value.getType());

		return new PDBPersistentHashSet(bagNew, contentNew);
	}

	@Override
	public ISet delete(IValue value) {
		final ImmutableSet<IValue> contentNew = content.__removeEquivalent(value, equivalenceComparator);

		if (content == contentNew)
			return this;

		final AbstractTypeBag bagNew = elementTypeBag.decrease(value.getType());

		return new PDBPersistentHashSet(bagNew, contentNew);
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

		if (other instanceof PDBPersistentHashSet) {
			PDBPersistentHashSet that = (PDBPersistentHashSet) other;

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

		if (other instanceof PDBPersistentHashSet) {
			PDBPersistentHashSet that = (PDBPersistentHashSet) other;

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

			final TransientSet<IValue> tmp = one.asTransient();
			boolean modified = false;

			for (IValue key : two) {
				if (tmp.__insertEquivalent(key, equivalenceComparator)) {
					modified = true;
					bag = bag.increase(key.getType());
				}
			}

			if (modified) {
				return new PDBPersistentHashSet(bag, tmp.freeze());
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

		if (other instanceof PDBPersistentHashSet) {
			PDBPersistentHashSet that = (PDBPersistentHashSet) other;

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
				return new PDBPersistentHashSet(bag, tmp.freeze());
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

		if (other instanceof PDBPersistentHashSet) {
			PDBPersistentHashSet that = (PDBPersistentHashSet) other;

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
				return new PDBPersistentHashSet(bag, tmp.freeze());
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
