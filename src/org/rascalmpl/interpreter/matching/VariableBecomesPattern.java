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
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

public class VariableBecomesPattern extends AbstractMatchingResult {
	private IMatchingResult pat;
	private IMatchingResult var;
	
	private boolean firstTime;

	public VariableBecomesPattern(IEvaluatorContext ctx, Expression x, IMatchingResult var, IMatchingResult pat){
		super(ctx, x);
		
		this.pat = pat;
		this.var = var;
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		var.initMatch(subject);
		if (var.hasNext()) { 
			pat.initMatch(subject);
			hasNext = pat.hasNext();
		}
		else {
			hasNext = false;
		}
	}
	
	@Override
	public void init() {
		super.init();
		firstTime = true;
	}
	
	@Override
	public List<IVarPattern> getVariables() {
		List<IVarPattern> first = var.getVariables();
		List<IVarPattern> second = pat.getVariables();
		List<IVarPattern> vars = new ArrayList<IVarPattern>(first.size() + second.size());
		vars.addAll(first);
		vars.addAll(second);
		return vars;
	}
	
	@Override
	public Type getType(Environment env, HashMap<String,IVarPattern> patternVars) {
		return pat.getType(env, patternVars);
	}
	
	@Override
	public boolean hasNext(){
		return hasNext && pat.hasNext();
	}

	@Override
	public boolean next() {
		if(hasNext && pat.hasNext() && pat.next()) {
			if(firstTime) {
				if(var.hasNext() && var.next()) {
					firstTime = false;
					return true;
				} else {
					hasNext = false;
					return false;
				}
			}
			return true;
		}
		hasNext = false;
		return false;
	}
}
