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
import java.util.Map.Entry;

import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.impl.AbstractMap;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.util.AbstractTypeBag;
import org.rascalmpl.value.util.EqualityUtils;

import java.util.Objects;

import io.usethesource.capsule.ImmutableMap;
import io.usethesource.capsule.TransientMap;

public final class PDBPersistentHashMap extends AbstractMap {
		
	@SuppressWarnings("unchecked")
	private static final Comparator<Object> equivalenceComparator = EqualityUtils.getEquivalenceComparator();
	
	private Type cachedMapType;
	private final AbstractTypeBag keyTypeBag; 
	private final AbstractTypeBag valTypeBag;
	private final ImmutableMap<IValue,IValue> content; 
	
	/* 
	 * Passing an pre-calulated map type is only allowed from inside this class.
	 */
	protected PDBPersistentHashMap(AbstractTypeBag keyTypeBag,
			AbstractTypeBag valTypeBag, ImmutableMap<IValue, IValue> content) {
		Objects.requireNonNull(content);
		this.cachedMapType = null;
		this.keyTypeBag = keyTypeBag;
		this.valTypeBag = valTypeBag;
		this.content = content;
	}
	
	@Override
	protected IValueFactory getValueFactory() {
		return ValueFactory.getInstance();
	}

	@Override
	public Type getType() {
		if (cachedMapType == null) {
			final Type keyType = keyTypeBag.lub();
			final Type valType = valTypeBag.lub();
	
			final String keyLabel = keyTypeBag.getLabel();
			final String valLabel = valTypeBag.getLabel();

			if (keyLabel != null && valLabel != null) {
				cachedMapType = getTypeFactory().mapType(keyType, keyLabel, valType, valLabel);
			} else { 
				cachedMapType = getTypeFactory().mapType(keyType, valType);
			}
		}
		return cachedMapType;		
	}

	@Override
	public boolean isEmpty() {
		return content.isEmpty();
	}

	@Override
	public IMap put(IValue key, IValue value) {
		final ImmutableMap<IValue,IValue> contentNew = 
				content.__putEquivalent(key, value, equivalenceComparator);
		
		if (content == contentNew)
			return this;

		final AbstractTypeBag keyBagNew;
		final AbstractTypeBag valBagNew;

		if (content.size() == contentNew.size()) {
			// value replaced
			final IValue replaced = content.getEquivalent(key, equivalenceComparator);
			keyBagNew = keyTypeBag;
			valBagNew = valTypeBag.decrease(replaced.getType()).increase(value.getType());
		} else {
			// pair added
			keyBagNew = keyTypeBag.increase(key.getType());			
			valBagNew = valTypeBag.increase(value.getType());
		}
		
		return new PDBPersistentHashMap(keyBagNew, valBagNew, contentNew);
	}
	
	@Override
	public int size() {
		return content.size();
	}

	@Override
	public boolean containsKey(IValue key) {
		return content.containsKeyEquivalent(key, equivalenceComparator);
	}

	@Override
	public boolean containsValue(IValue value) {
		return content.containsValueEquivalent(value, equivalenceComparator);
	}
	
	@Override
	public IValue get(IValue key) {
		return content.getEquivalent(key, equivalenceComparator);
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
		
		if (other instanceof PDBPersistentHashMap) {
			PDBPersistentHashMap that = (PDBPersistentHashMap) other;

			if (this.size() != that.size())
				return false;

			return content.equals(that.content);
		}
		
		if (other instanceof IMap) {
			IMap that = (IMap) other;

			if (this.getType() != that.getType())
				return false;
			
			if (this.size() != that.size())
				return false;
			
			for (IValue e : that) {
	            if (!content.containsKey(e)) {
	                return false;
	            } else if (!content.get(e).equals(that.get(e))) {
	            	return false;
	            }
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

		if (other instanceof IMap) {
			IMap that = (IMap) other;

			if (this.size() != that.size())
				return false;

			for (IValue e : that) {
				if (!containsKey(e)) {
					return false;
				} else if (!get(e).isEqual(that.get(e))) {
					return false;
				}
			}

			return true;
		}

		return false;
	}

	@Override
	public Iterator<IValue> iterator() {
		return content.keyIterator();
	}
	
	@Override
	public Iterator<IValue> valueIterator() {
		return content.valueIterator();
	}

	@Override
	public Iterator<Entry<IValue, IValue>> entryIterator() {
		return content.entryIterator();
	}

	@Deprecated
	private static String mergeLabels(String one, String two) {
		if (one != null && two != null && one.equals(two)) {
			// both are the same
			return one;
		} else {
			// only one is not null
			return one != null ? one : two;
		}
	}	
	
	@Override
	public IMap join(IMap other) {
		if (other instanceof PDBPersistentHashMap) {
			PDBPersistentHashMap that = (PDBPersistentHashMap) other;

			final TransientMap<IValue, IValue> transientContent = content.asTransient();

			boolean isModified = false;
			int previousSize = size();

			AbstractTypeBag keyBagNew = null;
			if (that.keyTypeBag.getLabel() != null) {
				keyBagNew = keyTypeBag.setLabel(mergeLabels(keyTypeBag.getLabel(),
								that.keyTypeBag.getLabel()));

				isModified |= (keyBagNew.getLabel() != keyTypeBag.getLabel());
			} else {
				keyBagNew = keyTypeBag;
			}

			AbstractTypeBag valBagNew = null;
			if (that.valTypeBag.getLabel() != null) {
				valBagNew = valTypeBag.setLabel(mergeLabels(valTypeBag.getLabel(),
								that.valTypeBag.getLabel()));

				isModified |= (valBagNew.getLabel() != valTypeBag.getLabel());
			} else {
				valBagNew = valTypeBag;
			}

			for (Iterator<Entry<IValue, IValue>> it = that.entryIterator(); it.hasNext();) {
				Entry<IValue, IValue> tuple = it.next();
				IValue key = tuple.getKey();
				IValue value = tuple.getValue();

				final IValue replaced = transientContent.__putEquivalent(key, value,
								equivalenceComparator);

				if (replaced != null) {
					// value replaced
					valBagNew = valBagNew.decrease(replaced.getType()).increase(value.getType());

					isModified = true;
				} else if (previousSize != transientContent.size()) {
					// pair added
					keyBagNew = keyBagNew.increase(key.getType());
					valBagNew = valBagNew.increase(value.getType());

					isModified = true;
					previousSize++;
				}
			}

			if (isModified) {
				return new PDBPersistentHashMap(keyBagNew, valBagNew, transientContent.freeze());
			} else {
				return this;
			}
		} else {
			return super.join(other);
		}
	}
	
	@Override
	public IMap remove(IMap that) {
		// TODO Auto-generated method stub
		return super.remove(that);
	}

	@Override
	public IMap compose(IMap that) {
		// TODO Auto-generated method stub
		return super.compose(that);
	}

	@Override
	public IMap common(IMap that) {
		// TODO Auto-generated method stub
		return super.common(that);
	}

	@Override
	public boolean isSubMap(IMap that) {
		// TODO Auto-generated method stub
		return super.isSubMap(that);
	}

}
