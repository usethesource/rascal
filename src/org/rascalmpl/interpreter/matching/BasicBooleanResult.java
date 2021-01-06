/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import io.usethesource.vallang.IValue;

public class BasicBooleanResult extends AbstractBooleanResult {
	private org.rascalmpl.ast.Expression expr;
	private boolean firstTime = true;

	public BasicBooleanResult(IEvaluatorContext ctx, Expression expr) {
		super(ctx);
		
		this.expr = expr;
	}

	@Override
	public void init() {
		super.init();
		firstTime = true;
	}
	
	@Override
	public boolean hasNext() {
		return firstTime;
	}

	@Override
	public boolean next() {
		if (firstTime) {
			/* Evaluate expression only once */
			firstTime = false;
			Result<IValue> result = expr.interpret(ctx.getEvaluator());
			if (result.getStaticType().isBool() && result.getValue() != null) {
				if (result.getValue().equals(ctx.getValueFactory().bool(true))) {
					return true;
				}
				
				return false;
			}

			throw new UnexpectedType(tf.boolType(), result.getStaticType(), expr);
		}
		
		return false;
	}
}
