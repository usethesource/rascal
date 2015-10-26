/*******************************************************************************
 * Copyright (c) 2014 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI  
 *******************************************************************************/
package org.rascalmpl.value.util;

import static io.usethesource.capsule.AbstractSpecialisedImmutableMap.mapOf;

import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

import io.usethesource.capsule.ImmutableMap;

/**
 * Stores mapping (Type -> Integer) to keep track of a collection's element
 * types. The least upper bound type of is calculated on basis of the map keys.
 */
public abstract class AbstractTypeBag implements Cloneable {

	public abstract AbstractTypeBag increase(Type t);

	public abstract AbstractTypeBag decrease(Type t);
	
	@Deprecated
	public abstract AbstractTypeBag setLabel(String label);	
	
	@Deprecated
	public abstract String getLabel();

	public abstract Type lub();

	public abstract AbstractTypeBag clone();
	
	public static AbstractTypeBag of(Type... ts) {
		return of(null, ts);
	}
	
	public static AbstractTypeBag of(String label, Type... ts) {
		return new TypeBag(label, ts);
	}

	public abstract int size();
	
	/**
	 * Implementation of <@link AbstractTypeBag/> that cached the current least
	 * upper bound.
	 */
	private static class TypeBag extends AbstractTypeBag {
		private final String label;
		private final ImmutableMap<Type, Integer> countMap;
		
		private Type cachedLub;

		private TypeBag(String label, ImmutableMap<Type, Integer> countMap) {
			this(label, countMap, null);
		}
		
		private TypeBag(String label, ImmutableMap<Type, Integer> countMap, Type cachedLub) {
			this.label = label;
			this.countMap = countMap;
			this.cachedLub = cachedLub;
		}
		
		private TypeBag(Type... ts) {
			this(null, ts);
		}
		
		private TypeBag(String label, Type... ts) {
			this.label = label;
			this.countMap = mapOf();
			
			for (Type t : ts) {
				this.increase(t);
			}
		}
		
		@Override
		public AbstractTypeBag increase(Type t) {	
			final Integer oldCount = countMap.get(t);
			final ImmutableMap<Type, Integer> newCountMap;
			
			if (oldCount == null) {
				newCountMap = countMap.__put(t, 1);
				
				if (cachedLub == null) {
					return new TypeBag(label, newCountMap);
				} else {
					// update cached type
					final Type newCachedLub = cachedLub.lub(t);
					return new TypeBag(label, newCountMap, newCachedLub);
				}
			} else {
				newCountMap = countMap.__put(t, oldCount + 1);
				return new TypeBag(label, newCountMap);
			}
		}

		@Override
		public AbstractTypeBag decrease(Type t) {		
			final Integer oldCount = countMap.get(t);
			
			if (oldCount == null) {
				throw new IllegalStateException(String.format("Type '%s' was not present.", t));
			} else if (oldCount > 1) {
				// update and decrease count; lub stays the same
				final ImmutableMap<Type, Integer> newCountMap = countMap.__put(t, oldCount - 1);
				return new TypeBag(label, newCountMap, cachedLub);
			} else {
				// count was zero, thus remove entry and invalidate cached type
				final ImmutableMap<Type, Integer> newCountMap = countMap.__remove(t);
				return new TypeBag(label, newCountMap);
			}			
		}
		
		@Deprecated
		@Override
		public AbstractTypeBag setLabel(String label) {
			return new TypeBag(label, countMap, cachedLub);
		}
		
		@Deprecated
		@Override
		public String getLabel() {
			return label;
		}
		
		@Override
		public Type lub() {
			if (cachedLub == null) {			
				Type inferredLubType = TypeFactory.getInstance().voidType();
				for (Type t : countMap.keySet()) {
					inferredLubType = inferredLubType.lub(t);
				}				
				cachedLub = inferredLubType;
			}
			return cachedLub;
		}	

		@Override
		public AbstractTypeBag clone() {
			return new TypeBag(label, countMap);
		}
		
		@Override
		public String toString() {
			return countMap.toString();
		}

		@Override
		public int size() {
			return countMap.size();
		}
	}
	
}
