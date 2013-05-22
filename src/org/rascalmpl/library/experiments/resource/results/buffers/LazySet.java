/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.experiments.resource.results.buffers;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IRelationalAlgebra;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetRelation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;

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

//	@Override
//	public int arity() {
//		return this.elementType.getArity();
//	}
//
//	@Override
//	public ISet compose(ISet rel) throws FactTypeUseException {
//		throw new IllegalOperationException("compose over buffered relation", getType());
//	}
//
//	@Override
//	public ISet closure() throws FactTypeUseException {
//		throw new IllegalOperationException("transitive closure over buffered relation", getType());
//	}
//
//	@Override
//	public ISet closureStar() throws FactTypeUseException {
//		throw new IllegalOperationException("reflexive transitive closure over buffered relation", getType());
//	}
//
//	@Override
//	public ISet carrier() {
//		throw new IllegalOperationException("carrier over buffered relation", getType());
//	}
//
//	@Override
//	public Type getFieldTypes() {
//		return this.elementType;
//	}
//
//	@Override
//	public ISet domain() {
//		throw new IllegalOperationException("domain over buffered relation", getType());
//	}
//
//	@Override
//	public ISet range() {
//		throw new IllegalOperationException("range over buffered relation", getType());
//	}
//
//	@Override
//	public ISet select(int... fields) {
//		throw new IllegalOperationException("select over buffered relation", getType());
//	}
//
//	@Override
//	public ISet selectByFieldNames(String... fields) throws FactTypeUseException {
//		throw new IllegalOperationException("select over buffered relation", getType());
//	}

	@Override
	public String toString() {
		return "Buffered Relation";
	}

	@Override
	public boolean isRelation() {
		return false;
	}

	@Override
	public ISetRelation<ISet> asRelation() {
		throw new IllegalOperationException(
				"Relational operations are not supported on lazy representation.",
				getType());
	}
}
