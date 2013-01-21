/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.experiments.resource.results.buffers;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListRelation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

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
		LazyIterator bi = new LazyIterator(filler.getBufferedFiller(), bufferSize);
		bi.init();
		return bi;
	}

	@Override
	public Type getType() {
		return TypeFactory.getInstance().listType(elementType);
	}

	@Override
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEqual(IValue other) {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}

	@Override
	public Type getElementType() {
		return elementType;
	}

	@Override
	public int length() {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}

	@Override
	public IList reverse() {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}

	@Override
	public IList append(IValue e) {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}

	@Override
	public IList insert(IValue e) {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}

	@Override
	public IList concat(IList o) {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}

	@Override
	public IList put(int i, IValue e) throws FactTypeUseException, IndexOutOfBoundsException {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}

	@Override
	public IValue get(int i) throws IndexOutOfBoundsException {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}

	@Override
	public IList sublist(int offset, int length) {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}

	@Override
	public boolean isEmpty() {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}

	@Override
	public boolean contains(IValue e) {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}

	@Override
	public IList delete(IValue e) {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}

	@Override
	public IList delete(int i) {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}
	
	@Override
	public IListRelation product(IList e) {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}
	
	@Override
	public IListRelation subtract(IList e) {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}
	
	@Override
	public IListRelation intersect(IList e) {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}
	
	@Override
	public boolean isSubListOf(IList e) {
		throw new IllegalOperationException("isEqual over buffered list", getType());
	}

	@Override
	public <ListOrRel extends IList> ListOrRel replace(int first, int second, int end, IList repl)
			throws FactTypeUseException, IndexOutOfBoundsException {
		throw new IllegalOperationException("replace over buffered list", getType());
	}


}
