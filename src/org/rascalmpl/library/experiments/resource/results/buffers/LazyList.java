/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.experiments.resource.results.buffers;

import java.util.Iterator;
import java.util.Random;

import org.rascalmpl.values.uptr.IRascalValueFactory;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IRelation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.exceptions.IllegalOperationException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class LazyList implements IList {

	private final int bufferSize;
	private ILazyFiller filler;
	private Type elementType;

	public LazyList(int bufferSize, ILazyFiller filler, Type elementType) {
		this.bufferSize = bufferSize;
		this.filler = filler;
		this.elementType = elementType;
	}

	@Override
	public Iterator<IValue> iterator() {
		LazyIterator bi = new LazyIterator(filler.getBufferedFiller(),
				bufferSize);
		bi.init();
		return bi;
	}

	@Override
	public Type getType() {
		return TypeFactory.getInstance().listType(elementType);
	}

	@Override
    public boolean match(IValue other) {
        throw new IllegalOperationException("match over buffered list",
                getType());
    }

	@Override
	public Type getElementType() {
		return elementType;
	}

	@Override
	public int length() {
		throw new IllegalOperationException("length over buffered list",
				getType());
	}

	@Override
	public int size() {
	    throw new IllegalOperationException("size over buffered list",
                getType());
	}
	
	@Override
	public IList reverse() {
		throw new IllegalOperationException("reverse over buffered list",
				getType());
	}
	
	@Override
	public IList shuffle(Random rand) {
		throw new IllegalOperationException("shuffle over buffered list",
				getType());
	}

	@Override
	public IList append(IValue e) {
		throw new IllegalOperationException("append over buffered list",
				getType());
	}

	@Override
	public IList insert(IValue e) {
		throw new IllegalOperationException("insert over buffered list",
				getType());
	}

	@Override
	public IList concat(IList o) {
		throw new IllegalOperationException("concat over buffered list",
				getType());
	}

	@Override
	public IList put(int i, IValue e) throws FactTypeUseException,
			IndexOutOfBoundsException {
		throw new IllegalOperationException("put over buffered list",
				getType());
	}

	@Override
	public IValue get(int i) throws IndexOutOfBoundsException {
		throw new IllegalOperationException("get over buffered list",
				getType());
	}

	@Override
	public IList sublist(int offset, int length) {
		throw new IllegalOperationException("sublist over buffered list",
				getType());
	}

	@Override
	public boolean isEmpty() {
		throw new IllegalOperationException("isEmpty over buffered list",
				getType());
	}

	@Override
	public boolean contains(IValue e) {
		throw new IllegalOperationException("contains over buffered list",
				getType());
	}

	@Override
	public IList delete(IValue e) {
		throw new IllegalOperationException("isEqual over buffered list",
				getType());
	}

	@Override
	public IList delete(int i) {
		throw new IllegalOperationException("delete over buffered list",
				getType());
	}

	@Override
	public IList product(IList e) {
		throw new IllegalOperationException("product over buffered list",
				getType());
	}

	@Override
	public IList subtract(IList e) {
		throw new IllegalOperationException("subtract over buffered list",
				getType());
	}

	@Override
	public IList intersect(IList e) {
		throw new IllegalOperationException("intersect over buffered list",
				getType());
	}

	@Override
	public boolean isSubListOf(IList e) {
		throw new IllegalOperationException("isSublistOf over buffered list",
				getType());
	}

	@Override
	public IList replace(int first, int second, int end, IList repl)
			throws FactTypeUseException, IndexOutOfBoundsException {
		throw new IllegalOperationException("replace over buffered list",
				getType());
	}

	@Override
	public boolean isRelation() {
		return false;
	}

	@Override
	public IRelation<IList> asRelation() {
		throw new IllegalOperationException(
				"Relational operations are not supported on lazy representation.",
				getType());
	}

    @Override
    public IListWriter writer() {
        return IRascalValueFactory.getInstance().listWriter();
    }
}
