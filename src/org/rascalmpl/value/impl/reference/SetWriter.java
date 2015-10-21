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

import java.util.HashSet;

import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.exceptions.UnexpectedElementTypeException;
import org.rascalmpl.value.impl.AbstractWriter;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

/*package*/ class SetWriter extends AbstractWriter implements ISetWriter {
    protected final HashSet<IValue> setContent;
    protected final boolean inferred;
    protected Type eltType;
    protected Set constructedSet;

    /*package*/ SetWriter(Type eltType) {
        super();

        this.eltType = eltType;
        this.inferred = false;
        setContent = new HashSet<>();
    }

    /*package*/ SetWriter() {
        super();
        this.eltType = TypeFactory.getInstance().voidType();
        this.inferred = true;
        setContent = new HashSet<>();
    }

    private static void checkInsert(IValue elem, Type eltType) throws FactTypeUseException {
        Type type = elem.getType();
        if (!type.isSubtypeOf(eltType)) {
            throw new UnexpectedElementTypeException(eltType, type);
        }
    }

    private void put(IValue elem) {
        updateType(elem);
        checkInsert(elem, eltType);
        setContent.add(elem);
    }

    private void updateType(IValue elem) {
        if (inferred) {
            eltType = eltType.lub(elem.getType());
        }
    }

    @Override
	public void insert(IValue... elems) throws FactTypeUseException {
        checkMutation();

        for (IValue elem : elems) {
            put(elem);
        }
    }

    @Override
	public void insertAll(Iterable<? extends IValue> collection) throws FactTypeUseException {
        checkMutation();

        for (IValue v : collection) {
            put(v);
        }
    }

    @Override
	public ISet done() {
    	// Temporary fix of the static vs dynamic type issue
    	eltType = TypeFactory.getInstance().voidType();
    	for(IValue el : setContent)
    		eltType = eltType.lub(el.getType());
    	// ---
        if (constructedSet == null) {
            constructedSet = new Set(eltType, setContent);
        }

        return constructedSet;
    }

    private void checkMutation() {
        if (constructedSet != null)
            throw new UnsupportedOperationException("Mutation of a finalized set is not supported.");
    }

}
