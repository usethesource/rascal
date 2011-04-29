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
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

// TODO: jurgen: i don't understand why this class exists, the boolean negation is not a pattern,
// it should be like AndResult and abstract BooleanResult
public class NotPattern extends AbstractMatchingResult {
	private final IMatchingResult arg;

	public NotPattern(Expression x, IMatchingResult arg) {
		super(x);
		this.arg = arg;
	}

	@Override
	public void initMatch(IEvaluatorContext ctx, Result<IValue> subject) {
		arg.initMatch(ctx, subject);
	}

	@Override
	public boolean hasNext() {
		return arg.hasNext();
	}
	
	@Override
	public boolean next() {
		Environment old = ctx.getCurrentEnvt();
		ctx.pushEnv();
		try {
			return !arg.next();
		}
		finally {
			ctx.unwind(old);
		}
	}

	@Override
	public Type getType(Environment env) {
		return arg.getType(env);
	}
}
