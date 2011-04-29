/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - emilie.balland@inria.fr (INRIA)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import org.rascalmpl.interpreter.IEvaluatorContext;

/**
 * The and boolean operator backtracks for both the lhs and the rhs. This means
 * that if the lhs or rhs have multiple ways of assigning a value to a variable,
 * this and operator will be evaluated as many times.
 * 
 * Also note that variables introduced in the lhs of the and can be used directly
 * in the rhs of the and, but not vice versa.
 * 
 * @author jurgenv
 *
 */
public class AndResult extends AbstractBooleanResult {
	private final IBooleanResult left;
	private final IBooleanResult right;
	private boolean firstMatch = true;
	private boolean leftResult;

	public AndResult(IBooleanResult left, IBooleanResult right) {
		this.left = left;
		this.right = right;
	}

	public void init(IEvaluatorContext ctx) {
		super.init(ctx);
		leftResult = false;
		left.init(ctx);
		// do not right.init() yet since it may use variables introduced by the first left.next();
		firstMatch = true;
	}

	public boolean hasNext() {
		if (firstMatch) {
			return left.hasNext();
		}
		
		return left.hasNext() || (leftResult == true && right.hasNext());
	}
	
	@Override
	public boolean next() {
		if (firstMatch) {
			firstMatch = false;
			ctx.pushEnv();
			leftResult = left.next();
			
			if (leftResult) {
				right.init(ctx);
				return right.next();
			}
			return false;
		}
		
		if (leftResult && right.hasNext()) {
			// first do the right.next because && would short cut it which leads to an infinite loop 
			// because right will always have a true hasNext() then.
			boolean rightResult = right.next();
			return leftResult && rightResult;
		}

		ctx.pushEnv();
		leftResult = left.next();

		if (leftResult) {
			right.init(ctx);
			return right.next();
		}
		return false;
	}
}
