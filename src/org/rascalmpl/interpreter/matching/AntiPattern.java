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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;


import java.util.HashMap;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

public class AntiPattern extends AbstractMatchingResult {
	private IMatchingResult pat;
	private boolean stop;

	public AntiPattern(IEvaluatorContext ctx, Expression.Anti anti, IMatchingResult pat) {
		super(ctx, anti);
		this.pat = pat;
	}

	public Type getType(Environment env, HashMap<String,IVarPattern> patternVars) {
		return pat.getType(env, patternVars);
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		pat.initMatch(subject);
		stop = false;
	}
	
	@Override
	public boolean mayMatch(Type subjectType, Environment env){
		return pat.mayMatch(subjectType, env);
	}
	
	@Override
	public boolean hasNext() {
		return !stop && pat.hasNext();
	}
	
	@Override
	public boolean next() {
		Environment old = ctx.getCurrentEnvt();

		while (pat.hasNext()) {
			try {
				ctx.pushEnv();
				if (pat.next()) {
					stop = true;
					return false;
				}
			}
			finally {
				ctx.unwind(old);
			}
		}
		
		return true;
	}
}
