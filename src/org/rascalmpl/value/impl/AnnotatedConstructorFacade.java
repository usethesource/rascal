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

import java.util.Iterator;

import org.rascalmpl.value.IAnnotatable;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.io.StandardTextWriter;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.value.visitors.IValueVisitor;

import io.usethesource.capsule.ImmutableMap;

public class AnnotatedConstructorFacade implements IConstructor {

	protected final IConstructor content;
	protected final ImmutableMap<String, IValue> annotations;
	
	public AnnotatedConstructorFacade(final IConstructor content, final ImmutableMap<String, IValue> annotations) {
		this.content = content;
		this.annotations = annotations;
	}

	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return v.visitConstructor(this);
	}

	public Type getType() {
		return content.getType();
	}

	public IValue get(int i) throws IndexOutOfBoundsException {
		return content.get(i);
	}

	public Type getConstructorType() {
		return content.getConstructorType();
	}
	
	public Type getUninstantiatedConstructorType() {
		return content.getUninstantiatedConstructorType();
	}

	public IValue get(String label) {
		return content.get(label);
	}

	public IConstructor set(String label, IValue newChild)
			throws FactTypeUseException {
		IConstructor newContent = content.set(label, newChild);
		return new AnnotatedConstructorFacade(newContent, annotations);	// TODO: introduce wrap() here as well			
	}

	public int arity() {
		return content.arity();
	}

	public boolean has(String label) {
		return content.has(label);
	}

	public String toString() {
		return StandardTextWriter.valueToString(this);
	}

	public IConstructor set(int index, IValue newChild)
			throws FactTypeUseException {
		IConstructor newContent = content.set(index, newChild);
		return new AnnotatedConstructorFacade(newContent, annotations);	// TODO: introduce wrap() here as well		
	}

	public String getName() {
		return content.getName();
	}

	public Iterable<IValue> getChildren() {
		return content.getChildren();
	}

	public Iterator<IValue> iterator() {
		return content.iterator();
	}

	public INode replace(int first, int second, int end, IList repl)
			throws FactTypeUseException, IndexOutOfBoundsException {
		return content.replace(first, second, end, repl);
	}

	public Type getChildrenTypes() {
		return content.getChildrenTypes();
	}

	public boolean declaresAnnotation(TypeStore store, String label) {
		return content.declaresAnnotation(store, label);
	}

	public boolean equals(Object o) {
		if(o == this) return true;
		if(o == null) return false;
		
		if(o.getClass() == getClass()){
			AnnotatedConstructorFacade other = (AnnotatedConstructorFacade) o;
		
			return content.equals(other.content) &&
					annotations.equals(other.annotations);
		}
		
		return false;
	}

	public boolean isEqual(IValue other) {
		return content.isEqual(other);
	}
	
	@Override
	public int hashCode() {
		return content.hashCode();
	}

	@Override
	public boolean isAnnotatable() {
		return true;
	}
	
	@Override
	public IAnnotatable<? extends IConstructor> asAnnotatable() {
		return new AbstractDefaultAnnotatable<IConstructor>(content, annotations) {

			@Override
			protected IConstructor wrap(IConstructor content,
					ImmutableMap<String, IValue> annotations) {
				return new AnnotatedConstructorFacade(content, annotations);
			}
		};
	}
	
	@Override
	public boolean mayHaveKeywordParameters() {
	  return false;
	}

	@Override
	public IWithKeywordParameters<IConstructor> asWithKeywordParameters() {
	  throw new UnsupportedOperationException("can not add keyword parameters to a node which already has annotations");
	}
}
