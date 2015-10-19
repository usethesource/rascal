/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.result;


import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

public class CollectionResult<T extends IValue> extends ElementResult<T> {
	/*
	 * These methods are called for expressions like:
	 * 1 + [2,3]:   1.add([2,3]) --> [2,3].addInt(1) --> [2,3].insertElement(1) --> [1,2,3]
	 * etc.
	 */

	CollectionResult(Type type, T value, IEvaluatorContext ctx) {
		super(type, value, null, ctx);
	}


	@Override
	protected <U extends IValue> Result<U> addRational(RationalResult n) {
		return insertElement(n);
	}

	@Override
	protected <U extends IValue> Result<U> addReal(ElementResult<IReal> n) {
		return insertElement(n);
	}
	
	@Override
	protected <U extends IValue> Result<U> addInteger(IntegerResult n) {
		return insertElement(n);
	}
	
	@Override
	protected <U extends IValue> Result<U> addNumber(NumberResult n) {
		return insertElement(n);
	}

	@Override
	protected <U extends IValue> Result<U> addString(StringResult n) {
		return insertElement(n);
	}
	
	@Override
	protected <U extends IValue> Result<U> addBool(BoolResult n) {
		return insertElement(n);
	}
	
	@Override
	protected <U extends IValue> Result<U> addDateTime(DateTimeResult n) {
		return insertElement(n);
	}
	
	@Override
	protected <U extends IValue> Result<U> addSourceLocation(SourceLocationResult n) {
		return insertElement(n);
	}
	
	@Override 
	protected <U extends IValue> Result<U> addTuple(TupleResult t) {
		return insertElement(t);
	}
	
	@Override 
	protected <U extends IValue> Result<U> addMap(MapResult t) {
		return insertElement(t);
	}


//	protected <U extends IValue> Type resultTypeWhenAddingElement(ElementResult<U> that) {
//		Type t1 = getType().getElementType();
//		Type t2 = that.getType();
//		getType().
//		return getTypeFactory().(t1.lub(t2));
//	}

	
}
