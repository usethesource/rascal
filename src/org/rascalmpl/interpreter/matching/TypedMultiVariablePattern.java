/*******************************************************************************
 * Copyright (c) 2009-2018 CWI, NWO-I CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Mark Hills - Mark.Hills@cwi.nl - CWI
 *   * Jurgen Vinju - Jurgen.Vinju@cwi.nl - NWO-I CWO
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;

import io.usethesource.vallang.IValue;


/**
 * This is a place holder pattern for * Type Name variables which is useful during type inference
 * at pattern construction time, since it will emulate the element type of the container. However,
 * during pattern matching it is not useful and must be replace by a normal DesignatedTypedMultiVariablePattern
 * with the type wrapped by a set or a list appropriately
 */
public class TypedMultiVariablePattern extends TypedVariablePattern {
	public TypedMultiVariablePattern(IEvaluatorContext ctx, Expression x, io.usethesource.vallang.type.Type elementType, org.rascalmpl.ast.Name name, boolean bindTypeParameters) {
		super(ctx, x, elementType, name, bindTypeParameters);		
	}
	
	public TypedMultiVariablePattern(IEvaluatorContext ctx, Expression x, io.usethesource.vallang.type.Type elementType, String name, boolean bindTypeParameters) {
		super(ctx, x, elementType, name, bindTypeParameters);
	}
	
	@Override
	public String toString(){
		return "*" + declaredType + " " + getName();
	}
	
	@Override
	public void initMatch(Result<IValue> subject) {
	    throw new ImplementationError("TypedMultiVariablePattern is a placeholder not to be used for matching");
	}
	
	@Override
	public boolean hasNext() {
	    throw new ImplementationError("TypedMultiVariablePattern is a placeholder not to be used for matching");
	}
	
	@Override
	public boolean next() {
	    throw new ImplementationError("TypedMultiVariablePattern is a placeholder not to be used for matching");
	}
}
