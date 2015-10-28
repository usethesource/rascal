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
import org.rascalmpl.value.IList;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.io.StandardTextWriter;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.visitors.IValueVisitor;

import io.usethesource.capsule.ImmutableMap;

public class AnnotatedNodeFacade implements INode {

	protected final INode content;
	protected final ImmutableMap<String, IValue> annotations;
	
	public AnnotatedNodeFacade(final INode content, final ImmutableMap<String, IValue> annotations) {
		this.content = content;
		this.annotations = annotations;
	}
	
	public Type getType() {
		return content.getType();
	}

	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return v.visitNode(this);
	}

	public IValue get(int i) throws IndexOutOfBoundsException {
		return content.get(i);
	}
	
	public INode set(int i, IValue newChild) throws IndexOutOfBoundsException {
		INode newContent = content.set(i, newChild);
		return new AnnotatedNodeFacade(newContent, annotations); // TODO: introduce wrap() here as well
	}

	public int arity() {
		return content.arity();
	}

	public String toString() {
		return StandardTextWriter.valueToString(this);
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
		INode newContent = content.replace(first, second, end, repl);
		return new AnnotatedNodeFacade(newContent, annotations); // TODO: introduce wrap() here as well
	}

	public boolean equals(Object o) {
		if(o == this) return true;
		if(o == null) return false;
		
		if(o.getClass() == getClass()){
			AnnotatedNodeFacade other = (AnnotatedNodeFacade) o;
		
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
	public IAnnotatable<? extends INode> asAnnotatable() {
		return new AbstractDefaultAnnotatable<INode>(content, annotations) {

			@Override
			protected INode wrap(INode content,
					ImmutableMap<String, IValue> annotations) {
				return new AnnotatedNodeFacade(content, annotations);
			}
		};
	}

  @Override
  public boolean mayHaveKeywordParameters() {
    return false;
  }

  @Override
  public IWithKeywordParameters<? extends INode> asWithKeywordParameters() {
    throw new UnsupportedOperationException("can not add keyword parameters to a node which already has annotations");
  }
}
