/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Mark Hills - Mark.Hills@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.ImplementationError;


public class TypedMultiVariablePattern extends TypedVariablePattern {

	public TypedMultiVariablePattern(IEvaluatorContext ctx, Expression x, org.eclipse.imp.pdb.facts.type.Type type, org.rascalmpl.ast.Name name) {
		super(ctx, x, type, name);		
	}
	
	public TypedMultiVariablePattern(IEvaluatorContext ctx, Expression x, org.eclipse.imp.pdb.facts.type.Type type, String name) {
		super(ctx, x, type, name);
	}
	
	public void covertToListType() {
		if (!this.alreadyStored) {
			this.declaredType = TypeFactory.getInstance().listType(this.declaredType);
		} else {
			throw new ImplementationError("Cannot convert a typed multi variable to a list after it has already been stored at its current type");
		}
	}

	public void covertToSetType() {
		if (!this.alreadyStored) {
			this.declaredType = TypeFactory.getInstance().setType(this.declaredType);
		} else {
			throw new ImplementationError("Cannot convert a typed multi variable to a set after it has already been stored at its current type");
		}
	}

}
