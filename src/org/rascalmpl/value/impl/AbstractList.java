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

import java.util.Random;

import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListRelation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.IllegalOperationException;
import org.rascalmpl.value.impl.func.ListFunctions;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.visitors.IValueVisitor;

public abstract class AbstractList extends AbstractValue implements IList {

    public AbstractList() {
        super();
    }

    protected static TypeFactory getTypeFactory() {
        return TypeFactory.getInstance();
    }

    protected static Type inferListOrRelType(final Type elementType, final Iterable<IValue> content) {
    	return inferListOrRelType(elementType, content.iterator().hasNext() == false);
    }
    
    /*
     * TODO: get rid of code duplication (@see AbstractSet.inferSetOrRelType)
     */
    protected static Type inferListOrRelType(final Type elementType, final boolean isEmpty) {
        final Type inferredElementType;
        final Type inferredCollectionType;

        // is collection empty?
        if (isEmpty) {
            inferredElementType = getTypeFactory().voidType();
        } else {
            inferredElementType = elementType;
        }

        // consists collection out of tuples?
        if (inferredElementType.isFixedWidth()) {
            inferredCollectionType = getTypeFactory().lrelTypeFromTuple(inferredElementType);
        } else {
            inferredCollectionType = getTypeFactory().listType(inferredElementType);
        }

        return inferredCollectionType;
    }

    protected abstract IValueFactory getValueFactory();

    @Override
    public Type getElementType() {
        return getType().getElementType();
    }

    @Override
    public IList reverse() {
        return ListFunctions.reverse(getValueFactory(), this);
    }
    
    @Override
    public IList shuffle(Random rand) {
        return ListFunctions.shuffle(getValueFactory(), this, rand);
    }

    @Override
    public IList append(IValue e) {
        return ListFunctions.append(getValueFactory(), this, e);
    }

    @Override
    public IList insert(IValue e) {
        return ListFunctions.insert(getValueFactory(), this, e);
    }

    @Override
    public IList concat(IList that) {
        return ListFunctions.concat(getValueFactory(), this, that);
    }

    @Override
    public IList put(int i, IValue e) {
        return ListFunctions.put(getValueFactory(), this, i, e);
    }

    @Override
    public IList replace(int first, int second, int end, IList repl) {
        return ListFunctions.replace(getValueFactory(), this, first, second, end, repl);
    }

    @Override
    public IList sublist(int offset, int length) {
        return ListFunctions.sublist(getValueFactory(), this, offset, length);
    }

    @Override
    public boolean contains(IValue e) {
        return ListFunctions.contains(getValueFactory(), this, e);
    }

    @Override
    public IList delete(IValue e) {
        return ListFunctions.delete(getValueFactory(), this, e);
    }

    @Override
    public IList delete(int i) {
        return ListFunctions.delete(getValueFactory(), this, i);

    }

    @Override
    public IList product(IList that) {
        return ListFunctions.product(getValueFactory(), this, that);
    }

    @Override
    public IList intersect(IList that) {
        return ListFunctions.intersect(getValueFactory(), this, that);
    }

    @Override
    public IList subtract(IList that) {
        return ListFunctions.subtract(getValueFactory(), this, that);
    }

    @Override
    public boolean isSubListOf(IList that) {
        return ListFunctions.isSubListOf(getValueFactory(), this, that);
    }

    @Override
    public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
        if (getElementType().isFixedWidth()) {
            return v.visitListRelation(this);
        } else {
            return v.visitList(this);
        }
    }

	@Override
	public boolean isRelation() {
		return getType().isListRelation();
	}

	@Override
	public IListRelation<IList> asRelation() {
		if (!isRelation())
			throw new IllegalOperationException(
					"Cannot be viewed as a relation.", getType());

		return new DefaultRelationViewOnList(getValueFactory(), this);
	}    
    
}
