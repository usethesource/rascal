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
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *
 * Based on code by:
 *
 *   * Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation
 *******************************************************************************/
package org.rascalmpl.value.impl.reference;

import java.util.LinkedList;

import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.exceptions.UnexpectedElementTypeException;
import org.rascalmpl.value.impl.AbstractWriter;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

/**
 * This class does not guarantee thread-safety. Users must lock the writer object for thread safety.
 * It is thread-friendly however.
 */
/*package*/ class ListWriter extends AbstractWriter implements IListWriter {
    protected Type eltType;
    protected final java.util.List<IValue> listContent;

    protected IList constructedList;
    private final boolean inferred;

    /*package*/ ListWriter(Type eltType){
        super();

        this.eltType = eltType;
        this.inferred = false;
        listContent = new LinkedList<>();

        constructedList = null;
    }

    /*package*/ ListWriter(){
        super();

        this.eltType = TypeFactory.getInstance().voidType();
        inferred = true;
        listContent = new LinkedList<>();

        constructedList = null;
    }

    private void checkMutation(){
        if(constructedList != null) throw new UnsupportedOperationException("Mutation of a finalized list is not supported.");
    }

    private static void checkInsert(IValue elem, Type eltType) throws FactTypeUseException{
        Type type = elem.getType();
        if(!type.isSubtypeOf(eltType)){
            throw new UnexpectedElementTypeException(eltType, type);
        }
    }

    private void put(int index, IValue elem){
        if (inferred) {
            eltType = eltType.lub(elem.getType());
        }
        else {
            checkInsert(elem, eltType);
        }
        listContent.add(index, elem);
    }

    public void insert(IValue elem) throws FactTypeUseException {
        checkMutation();
        put(0, elem);
    }

    @Override
	public void insert(IValue[] elems, int start, int length) throws FactTypeUseException{
        checkMutation();
        checkBounds(elems, start, length);

        for(int i = start + length - 1; i >= start; i--){
            updateType(elems[i]);
            put(0, elems[i]);
        }
    }

    @Override
	public IValue replaceAt(int index, IValue elem) throws FactTypeUseException, IndexOutOfBoundsException {
        checkMutation();
        updateType(elem);
        checkInsert(elem, eltType);
        return listContent.set(index, elem);
    }

    @Override
	public void insert(IValue... elems) throws FactTypeUseException{
        insert(elems, 0, elems.length);
    }

    @Override
	public void insertAt(int index, IValue[] elems, int start, int length) throws FactTypeUseException{
        checkMutation();
        checkBounds(elems, start, length);

        for(int i = start + length - 1; i >= start; i--) {
            if (inferred) {
                eltType = eltType.lub(elems[i].getType());
            }
            put(index, elems[i]);
        }
    }

    @Override
	public void insertAt(int index, IValue... elems) throws FactTypeUseException{
        insertAt(index,  elems, 0, 0);
    }

    public void append(IValue elem) throws FactTypeUseException{
        checkMutation();
        updateType(elem);
        put(listContent.size(), elem);
    }

    @Override
	public void append(IValue... elems) throws FactTypeUseException{
        checkMutation();

        for(IValue elem : elems){
            updateType(elem);
            put(listContent.size(), elem);
        }
    }

    @Override
	public void appendAll(Iterable<? extends IValue> collection) throws FactTypeUseException{
        checkMutation();

        for(IValue v : collection){
            updateType(v);
            put(listContent.size(), v);
        }
    }

    private void updateType(IValue v) {
        if (inferred) {
            eltType = eltType.lub(v.getType());
        }
    }

    @Override
	public IList done() {
    	// Temporary fix of the static vs dynamic type issue
    	eltType = TypeFactory.getInstance().voidType();
    	for(IValue el : listContent)
    		eltType = eltType.lub(el.getType());
    	// ---
        if (constructedList == null) {
            constructedList = new List(eltType, listContent);
        }

        return constructedList;
    }

    private void checkBounds(IValue[] elems, int start, int length) {
        if(start < 0) throw new ArrayIndexOutOfBoundsException("start < 0");
        if((start + length) > elems.length) throw new ArrayIndexOutOfBoundsException("(start + length) > elems.length");
    }
    
    @Override
    public IValue get(int i) throws IndexOutOfBoundsException {
        return listContent.get(i);
    }
    
    @Override
    public int length() {
    	return listContent.size();
    }

}
