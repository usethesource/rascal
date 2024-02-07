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
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.type.Type;

public class NegativePattern extends AbstractMatchingResult {
	private IMatchingResult pat;

	public NegativePattern(IEvaluatorContext ctx, Expression.Negative neg, IMatchingResult pat) {
		super(ctx, neg);
		this.pat = pat;
	}

	public Type getType(Environment env, HashMap<String,IVarPattern> patternVars) {
		return pat.getType(env, patternVars);
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		pat.initMatch(subject.negative());
	}
	
	@Override
	public boolean mayMatch(Type subjectType, Environment env){
		return pat.mayMatch(subjectType, env);
	}
	
	@Override
	public boolean hasNext() {
		return pat.hasNext();
	}
	
	@Override
	public boolean next() {
		if(pat.next()) {
			if (subject.getDynamicType().isNumber()) {
				return ((INumber) subject.getValue()).lessEqual(IRascalValueFactory.getInstance().integer(0)).getValue();
			}
		}
		return false;
	}
}
