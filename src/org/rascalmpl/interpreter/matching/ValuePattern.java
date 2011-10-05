/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.matching;


import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

/**
 * For matching a closed pattern (i.e. a value that simply needs to be checked for equality.
 * This pattern can be used to short-cut pattern matching in several cases.
 */
public class ValuePattern extends AbstractMatchingResult {
	private Result<?> val;

	public ValuePattern(IEvaluatorContext ctx, Expression x, Result<?> val) {
		super(ctx, x);
		this.val = val;
	}

	@Override
	public Type getType(Environment env, HashMap<String,IVarPattern> patternVars) {
		return val.getType();
	}
	
	@Override
	public void initMatch(Result<IValue> subject) {
		super.initMatch(subject);
		hasNext = ((IBool) subject.equals(val).getValue()).getValue();
	}

	@Override
	public boolean next() {
		boolean result = hasNext;
		hasNext = false;
		return result;
	}
}
