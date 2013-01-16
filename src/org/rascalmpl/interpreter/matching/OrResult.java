/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;

/**
 * The or boolean operator backtracks for both the lhs and the rhs. This means
 * that if the lhs or rhs have multiple ways of assigning a value to a variable,
 * this and operator will be evaluated as many times.
 * 
 * Note that variables introduced in the left hand side will NOT be visible in the
 * right hand side. The right hand side is only evaluated if the left hand side is false,
 * which means that no variables have been bound. Also note that both sides of a
 * disjunction are required to introduce exactly the same variables of exactly the same
 * type
 * 
 * @author jurgenv
 *
 */
public class OrResult extends AbstractBooleanResult {
	private final IBooleanResult left;
	private final IBooleanResult right;
	private boolean atRight;
	private Environment old;

	public OrResult(IEvaluatorContext ctx, IBooleanResult left, IBooleanResult right) {
		super(ctx);
		this.left = left;
		this.right = right;
	}

	@Override
	public void init() {
		super.init();
		left.init();
		old = ctx.getCurrentEnvt();
		ctx.pushEnv();
		atRight = false;
	}

	@Override
	public boolean hasNext() {
		if (left.hasNext()) {
			return true;
		}
		else {
			if (!atRight) {
				right.init();
				atRight = true;
			}
			
			return right.hasNext();
		}
	}
	
	@Override
	public boolean next() {
		if (atRight) {
			return right.next();
		}
		else {
	      if (left.next()) {
	    	  return true;
	      }
	      else {
	    	  ctx.unwind(old);
	    	  right.init();
	    	  atRight = true;
	    	  return next();
	      }
		}
	}
}
