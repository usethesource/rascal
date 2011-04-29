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

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

public class AntiPattern extends AbstractMatchingResult {
	private IMatchingResult pat;
	private boolean stop;

	public AntiPattern(Expression.Anti anti, IMatchingResult pat) {
		super(anti);
		this.pat = pat;
	}

	public Type getType(Environment env) {
		return pat.getType(env);
	}
	
	@Override
	public void initMatch(IEvaluatorContext ctx, Result<IValue> subject){
		super.initMatch(ctx, subject);
		pat.initMatch(ctx, subject);
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
