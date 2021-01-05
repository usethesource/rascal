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
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.types.TypeReachability;
import org.rascalmpl.values.iterators.IteratorFactory;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class DescendantPattern extends AbstractMatchingResult  {
	private IMatchingResult pat;
	private Iterator<?> iterator;

	public DescendantPattern(IEvaluatorContext ctx, Expression.Descendant x, IMatchingResult pat) {
		super(ctx, x);
		
		this.pat = pat;
	}

	@Override
	public Type getType(Environment env, HashMap<String,IVarPattern> patternVars) {
		return TypeFactory.getInstance().valueType();
	}
	
	@Override
	public boolean mayMatch(Type subjectType, Environment env){
		return TypeReachability.mayOccurIn(getType(env, null), subjectType, env);
	}
	
	@Override
	public List<IVarPattern> getVariables() {
		return pat.getVariables();
	}
	
	@Override
	public void initMatch(Result<IValue> subject) {
		super.initMatch(subject);
		// note how we switch to a dynamic type here!
		iterator = IteratorFactory.make(ctx, pat, ResultFactory.makeResult(subject.getValue().getType(), subject.getValue(), ctx), false);
		hasNext = true;
	}
	
	@Override
	public boolean hasNext(){
		if(!initialized)
			return false;
		
		if(hasNext){
			boolean hn = pat.hasNext() || iterator.hasNext();
			if(!hn){
				hasNext = false;
			}
			return hn;
		}
		return false;
	}
	
	@Override
	public boolean next() {
		/*
		 * First, explore alternatives that remain to be matched by the current pattern
		 */
		while(pat.hasNext()){
			if(pat.next()){
				return true;
			}
		}
		/*
		 * Next, fetch a new data element (if any) and create a new pattern.
		 */
		while(iterator.hasNext()){
			IValue v = (IValue) iterator.next();
			
			pat.initMatch(ResultFactory.makeResult(v.getType(), v, ctx));
			while(pat.hasNext()){
				if(pat.next()){
					return true;						
				}	
			}
		}
		hasNext = false;
		return false;
	}
}
