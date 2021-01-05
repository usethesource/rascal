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
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.Iterator;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.values.iterators.IteratorFactory;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class EnumeratorResult extends BasicBooleanResult {
	private IMatchingResult pat;
	private Iterator<?> iterator;
	private Expression expression;
	private Type elementType;
	private boolean firstTime;

	/*
	 * Constructor for a standard enumerator
	 */
	
	public EnumeratorResult(IEvaluatorContext ctx, IMatchingResult matchPattern, Expression expression){
		super(ctx, expression);
		
		this.pat = matchPattern;
		this.expression = expression;
	}
	
	@Override
	public void init() {
		firstTime = true;
	}
	
	@Override
	public boolean hasNext(){
		if (firstTime) {
			hasNext = true;
			return true;
		}
		if (hasNext) {
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
		if (firstTime) {
			firstTime = false;
			Result<IValue> result = expression.interpret(ctx.getEvaluator());
			elementType = IteratorFactory.elementType(ctx, result);
			iterator = IteratorFactory.make(ctx, pat, result, true);
		}
		/*
		 * First, explore alternatives that remain to be matched by the current pattern
		 */
		while (pat.hasNext()){
			if(pat.next()){
				return true;
			}
		}
		/*
		 * Next, fetch a new data element (if any) and create a new pattern.
		 */
		while (iterator.hasNext()){
			IValue v = (IValue) iterator.next();
			
			pat.initMatch(ResultFactory.makeResult(elementType, v, ctx));
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

