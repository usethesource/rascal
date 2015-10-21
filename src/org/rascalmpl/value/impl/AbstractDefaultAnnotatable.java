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
package org.rascalmpl.value.impl;

import java.util.Map;

import org.rascalmpl.value.IAnnotatable;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.FactTypeUseException;

import io.usethesource.capsule.AbstractSpecialisedImmutableMap;
import io.usethesource.capsule.ImmutableMap;

/**
 * A generic wrapper for an {@link IValue} that associates annotations to it. 
 *
 * @param <T> the interface over which this annotation wrapper closes
 */
public abstract class AbstractDefaultAnnotatable<T extends IValue> implements IAnnotatable<T> {

	protected final T content;
	protected final ImmutableMap<String, IValue> annotations;
		
	/**
	 * Creates an {@link IAnnotatable} view on {@literal content} with empty
	 * annotations.
	 * 
	 * @param content
	 *            is the wrapped object that supports annotations
	 */
	public AbstractDefaultAnnotatable(T content) {
		this.content = content;
		this.annotations = AbstractSpecialisedImmutableMap.mapOf();
	}
	
	/**
	 * Creates an {@link IAnnotatable} view on {@link #content} with already
	 * provided {@link #annotations}.
	 * 
	 * @param content
	 *            is the wrapped object that supports annotations
	 * @param annotations
	 *            is the map of annotations associated to {@link #content}
	 */
	public AbstractDefaultAnnotatable(T content, ImmutableMap<String, IValue> annotations) {
		this.content = content;
		this.annotations = annotations;
	}
	
	/**
	 * Wraps {@link #content} with other annotations. This methods is mandatory
	 * because of PDB's immutable value nature: Once annotations are modified, a
	 * new immutable view is returned.
	 * 
	 * @param content
	 *            is the wrapped object that supports annotations
	 * @param annotations
	 *            is the map of annotations associated to {@link #content}
	 * @return a new representations of {@link #content} with associated
	 *         {@link #annotations}
	 */
	protected abstract T wrap(final T content, final ImmutableMap<String, IValue> annotations);
	
	@Override
	public boolean hasAnnotations() {
		return annotations.size() > 0;
	}
	
	@Override
	public Map<String, IValue> getAnnotations() {
		return annotations;
	}
	
	@Override
	public T removeAnnotations() {
		return content;
	}
	
	@Override
	public boolean hasAnnotation(String label) throws FactTypeUseException {
		return annotations.containsKey(label);
	}
	
	@Override
	public IValue getAnnotation(String label) throws FactTypeUseException {
		return annotations.get(label);
	}

	@Override
	public T setAnnotation(String label, IValue newValue)
			throws FactTypeUseException {
		return wrap(content, annotations.__put(label, newValue));
	}

	@Override
	public T removeAnnotation(String label) {
		return wrap(content, annotations.__remove(label));
	}
	
	@Override
	public T setAnnotations(Map<String, IValue> otherAnnotations) {
		if (otherAnnotations.isEmpty())
			return content;
		
		return wrap(content, AbstractSpecialisedImmutableMap.mapOf(otherAnnotations));
	}

	@Override
	public T joinAnnotations(Map<String, IValue> otherAnnotations) {
		return wrap(content, annotations.__putAll(otherAnnotations));
	}
		
	@Override
	public String toString() {
		return content.toString();
	}
	
//	@Override
//	public int hashCode() {
//		// TODO
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		// TODO
//	}	
	
}
