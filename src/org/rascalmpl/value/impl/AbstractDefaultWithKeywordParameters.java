/*******************************************************************************
 * Copyright (c) 2013 CWI
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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.value.exceptions.FactTypeUseException;

import io.usethesource.capsule.AbstractSpecialisedImmutableMap;
import io.usethesource.capsule.ImmutableMap;


/**
 * A generic wrapper for an {@link IValue} that associates keyword parameters to it. 
 *
 * @param <T> the interface over which this parameter wrapper closes
 */
public abstract class AbstractDefaultWithKeywordParameters<T extends IValue> implements IWithKeywordParameters<T> {
	protected final T content;
	protected final ImmutableMap<String, IValue> parameters;

	/**
	 * Creates an {@link IWithKeywordParameters} view on {@link #content} with already
	 * provided {@link #parameters}.
	 * 
	 * @param content
	 *            is the wrapped object that supports annotations
	 * @param parameters
	 *            is the map of annotations associated to {@link #content}
	 */
	public AbstractDefaultWithKeywordParameters(T content, ImmutableMap<String, IValue> parameters) {
		this.content = content;
		this.parameters = parameters;
	}

	/**
	 * Wraps {@link #content} with other parameters. This methods is mandatory
	 * because of PDB's immutable value nature: Once parameters are modified, a
	 * new immutable view is returned.
	 * 
	 * @param content
	 *            is the wrapped object that supports annotations
	 * @param annotations
	 *            is the map of annotations associated to {@link #content}
	 * @return a new representations of {@link #content} with associated
	 *         {@link #parameters}
	 */
	protected abstract T wrap(final T content, final ImmutableMap<String, IValue> parameters);

	@Override
	public String toString() {
		return content.toString();
	}

	@Override
	public IValue getParameter(String label) throws FactTypeUseException {
		return parameters.get(label);
	}

	@Override
	public T setParameter(String label, IValue newValue) throws FactTypeUseException {
		return wrap(content, parameters.__put(label, newValue));
	}
	
	@Override
	public T unsetParameter(String label) {
		ImmutableMap<String, IValue> removed = parameters.__remove(label);
		
		if (removed.size() == 0) {
			return content;
		} else {
			return wrap(content, removed);
		}
	}
	
	@Override
	public T unsetAll() {
		return content;
	}

	@Override
	public boolean hasParameter(String label) throws FactTypeUseException {
		return parameters.containsKey(label);
	}

	@Override
	public boolean hasParameters() {
		return parameters.size() > 0;
	}

	@Override
	public Set<String> getParameterNames() {
		return parameters.keySet();
	}

	@Override
	public Map<String,IValue> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

	@Override
	public boolean equals(Object other) {
		if (!getClass().equals(other.getClass())) {
			return false;
		}

		AbstractDefaultWithKeywordParameters<? extends IValue> o = (AbstractDefaultWithKeywordParameters<?>) other;

		if (!content.isEqual(o.content)) {
			return false;
		}

		if (parameters.size() != o.parameters.size()) {
			return false;
		}

		for (String key : parameters.keySet()) {
			if (!getParameter(key).equals(o.getParameter(key))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public <U extends IWithKeywordParameters<? extends IValue>> boolean equalParameters(U other) {
		if (!(other instanceof AbstractDefaultWithKeywordParameters<?>)) {
			return false;
		}

		AbstractDefaultWithKeywordParameters<? extends IValue> o = (AbstractDefaultWithKeywordParameters<?>) other;

		if (parameters.size() != o.parameters.size()) {
			return false;
		}

		for (String key : parameters.keySet()) {
			// TODO: isEqual should become equals when annotations have been removed.
			IValue parameter = getParameter(key);
			if (parameter == null && o.getParameter(key) != null) {
				return false;
			}
			else if (parameter != null && !parameter.isEqual(o.getParameter(key))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public T setParameters(Map<String, IValue> params) {
		return wrap(content, AbstractSpecialisedImmutableMap.mapOf(params));
	}
}
