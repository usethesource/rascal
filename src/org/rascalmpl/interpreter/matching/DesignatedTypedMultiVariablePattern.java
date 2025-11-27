/*******************************************************************************
 * Copyright (c) 2018 NWO-I CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen Vinju - Jurgen.Vinju@cwi.nl - NWO-I CWO
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;

/**
 * Signals a * pattern variable for which the type is known (set or list var)
 */
public class DesignatedTypedMultiVariablePattern extends TypedVariablePattern {
	public DesignatedTypedMultiVariablePattern(IEvaluatorContext ctx, Expression x, io.usethesource.vallang.type.Type type, String name, boolean bindTypeParameters) {
		super(ctx, x, type, name, bindTypeParameters);
	}
	
	@Override
	public String toString(){
		return "*" + declaredType + " " + getName();
	}
}
