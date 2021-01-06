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
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;

@Deprecated
public interface ICallableValue extends IExternalValue, IFunction {
	public int getArity();
	public boolean hasVarArgs();
	public boolean hasKeywordArguments();
	
	public Result<IValue> call(IRascalMonitor monitor, Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues);
	
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues);

	/**
     * For calls from other locations than the interpreter.
     */
	@SuppressWarnings("unchecked")
    @Override
	default <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    Type[] types = Arrays.stream(parameters).map(v -> v.getType()).toArray(Type[]::new);

	    try {
	        Result<IValue> result = call(types, parameters, keywordParameters);

	        if (result.getStaticType().isBottom()) {
	            return null;
	        }
	        else {
	            return (T) result.getValue();
	        }
	    }
	    catch (MatchFailed e) {
	        IValueFactory vf = IRascalValueFactory.getInstance();
	        throw RuntimeExceptionFactory.callFailed(URIUtil.rootLocation("anonymous"), Arrays.stream(parameters).collect(vf.listWriter()));
	    }
	}

	@SuppressWarnings("unchecked")
    @Override
	default <T extends IValue> T monitoredCall(IRascalMonitor monitor, Map<String, IValue> keywordParameters,
	    IValue... parameters) {
	    Type[] types = Arrays.stream(parameters).map(v -> v.getType()).toArray(Type[]::new);

        try {
            Result<IValue> result = call(monitor, types, parameters, keywordParameters);

            if (result.getStaticType().isBottom()) {
                return null;
            }
            else {
                return (T) result.getValue();
            }
        }
        catch (MatchFailed e) {
            IValueFactory vf = IRascalValueFactory.getInstance();
            throw RuntimeExceptionFactory.callFailed(URIUtil.rootLocation("anonymous"), Arrays.stream(parameters).collect(vf.listWriter()));
        }
	}
	
	public abstract ICallableValue cloneInto(Environment env);
	
	public boolean isStatic();
	
	public IEvaluator<Result<IValue>> getEval();
}
