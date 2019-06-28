/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.experiments.resource.results.buffers;

import java.util.Iterator;

import org.rascalmpl.values.uptr.IRascalValueFactory;

import io.usethesource.vallang.IAnnotatable;
import io.usethesource.vallang.IRelation;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.exceptions.IllegalOperationException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.visitors.IValueVisitor;

public class LazySet implements ISet {
	private final int bufferSize;
	private ILazyFiller filler;
	private Type elementType;
	
	public LazySet(int bufferSize, ILazyFiller filler, Type elementType) {
		this.bufferSize = bufferSize;
		this.filler = filler;
		this.elementType = elementType;
	}

	@Override
	public ISetWriter writer() {
	    return IRascalValueFactory.getInstance().setWriter();
	}
	
	@Override
	public Type getElementType() {
		return elementType;
	}

	@Override
	public boolean isEmpty() {
		throw new IllegalOperationException("isEmpty over buffered relation", getType());
	}

	@Override
	public int size() {
		throw new IllegalOperationException("size over buffered relation", getType());
	}

	@Override
	public boolean contains(IValue element) {
		throw new IllegalOperationException("contains over buffered relation", getType());
	}

	@Override
	public ISet insert(IValue element) {
		throw new IllegalOperationException("insert over buffered relation", getType());
	}

	@Override
	public ISet union(ISet set) {
		throw new IllegalOperationException("union over buffered relation", getType());
	}

	@Override
	public ISet intersect(ISet set) {
		throw new IllegalOperationException("intersect over buffered relation", getType());
	}

	@Override
	public ISet subtract(ISet set) {
		throw new IllegalOperationException("subtract over buffered relation", getType());
	}

	@Override
	public ISet delete(IValue elem) {
		throw new IllegalOperationException("delete over buffered relation", getType());
	}

	@Override
	public ISet product(ISet set) {
		throw new IllegalOperationException("product over buffered relation", getType());
	}

	@Override
	public boolean isSubsetOf(ISet other) {
		throw new IllegalOperationException("subset over buffered relation", getType());
	}

	@Override
	public Iterator<IValue> iterator() {
		LazyIterator bi = new LazyIterator(filler.getBufferedFiller(), bufferSize);
		bi.init();
		return bi;
	}

	@Override
	public Type getType() {
		return TypeFactory.getInstance().relTypeFromTuple(this.elementType);
	}

	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEqual(IValue other) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
    public boolean match(IValue other) {
        // TODO Auto-generated method stub
        return false;
    }

	@Override
	public String toString() {
		return "Buffered Relation";
	}

	@Override
	public boolean isRelation() {
		return false;
	}

	@Override
	public IRelation<ISet> asRelation() {
		throw new IllegalOperationException(
				"Relational operations are not supported on lazy representation.",
				getType());
	}
	
	@Override
	public boolean isAnnotatable() {
		return false;
	}

	@Override
	public IAnnotatable<? extends IValue> asAnnotatable() {
		throw new IllegalOperationException(
				"Cannot be viewed as annotatable.", getType());
	}
	
	 @Override
   public boolean mayHaveKeywordParameters() {
     return false;
   }
   
   @Override
   public IWithKeywordParameters<? extends IValue> asWithKeywordParameters() {
     throw new IllegalOperationException(
         "Cannot be viewed as with keyword parameters", getType());
   }
	
}
