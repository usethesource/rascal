/** 
 * Copyright (c) 2020, Jurgen J. Vinju, Centrum Wiskunde & Informatica (NWOi - CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.values;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;

import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class RascalFunctionValueFactory extends RascalValueFactory {
    private final IEvaluatorContext ctx;

    /** 
     * This factory is not a singleton since functions in the interpreter must know
     * in which interpreter they are executing. This is a design flaw of the interpreter.
     * @param ctx the current evaluator context which functions depend on for executing in.
     */
    public RascalFunctionValueFactory(IEvaluatorContext ctx) {
        super();
        this.ctx = ctx;
    }
    
    @Override
    public IFunction function(io.usethesource.vallang.type.Type functionType, BiFunction<IValue[], Map<String, IValue>, IValue> func) {
        return new RascalFunctionValue((FunctionType) functionType, func, ctx.getEvaluator());
    }
    
    private static final class RascalFunctionValue extends AbstractFunction implements IFunction {
        private final BiFunction<IValue[], Map<String, IValue>, IValue> func;

        public RascalFunctionValue(FunctionType functionType, BiFunction<IValue[], Map<String, IValue>, IValue> func, IEvaluator<Result<IValue>> eval) {
            super(eval.getCurrentAST(), eval, functionType, Collections.emptyList(), false, eval.getCurrentEnvt());
            this.func = func;
        }

        @Override
        public boolean isStatic() {
          return false;
        }
        
        @Override
        public ICallableValue cloneInto(Environment env) {
          // this can not happen because the function is not present in an environment
          return null;
        }
        
        @Override
        public boolean isDefault() {
          return false;
        }
        
        /** 
         * For calls directly from the interpreter:
         */
        public org.rascalmpl.interpreter.result.Result<IValue> call(Type[] argTypes, IValue[] argValues, java.util.Map<String,IValue> keyArgValues) {
            IValue returnValue = func.apply(argValues, keyArgValues);
            
            if (functionType.isBottom()) {
                return ResultFactory.nothing();
            }
            else if (returnValue == null) {
                throw RuntimeExceptionFactory.callFailed(ctx.getCurrentAST().getLocation(), Arrays.stream(argValues).collect(vf.listWriter()));
            }
            else {
                return ResultFactory.makeResult(functionType.getReturnType(), returnValue, ctx);
            }
        };
    }
}
