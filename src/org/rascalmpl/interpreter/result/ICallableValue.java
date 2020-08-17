/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import java.util.Arrays;
import java.util.Map;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public interface ICallableValue extends IExternalValue, IFunction {
	public int getArity();
	public boolean hasVarArgs();
	public boolean hasKeywordArguments();
	
	public Result<IValue> call(IRascalMonitor monitor, Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues);
	
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues);
	
	/**
     * For calls from other locations than the interpreter.
     */
	@Override
	default IValue call(IValue[] parameters, Map<String, IValue> keywordParameters) {
	    Type[] types = Arrays.stream(parameters).map(v -> v.getType()).toArray(Type[]::new);

	    Result<IValue> result = call(types, parameters, keywordParameters);

	    if (result.getType().isBottom()) {
	        return null;
	    }
	    else {
	        return result.getValue();
	    }
	}
	
	public abstract ICallableValue cloneInto(Environment env);
	
	public boolean isStatic();
	
	public IEvaluator<Result<IValue>> getEval();
}
