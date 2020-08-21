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
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UndeclaredNonTerminal;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.parser.gtd.exception.UndeclaredNonTerminalException;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

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
    
    @Override
    public IFunction parser(IValue reifiedGrammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity) {
        RascalTypeFactory rtf = RascalTypeFactory.getInstance();
        TypeFactory tf = TypeFactory.getInstance();
        
        Type functionType = rtf.functionType(RascalValueFactory.Tree, 
            tf.tupleType(tf.valueType()), 
            tf.tupleType(new Type[] { tf.sourceLocationType()}, new String[] { "origin"}));
        
        return function(functionType, new ParseFunction(ctx, reifiedGrammar, allowAmbiguity, hasSideEffects, firstAmbiguity));
    }
    
    /**
     * This class wraps the parseObject methods of the Evaluator by presenting it as an implementation of IFunction.
     * In this way library builtins can use the parser generator functionality of the Evaluator without knowing about
     * the internals of parser generation and parser caching.
     */
    static private class ParseFunction implements BiFunction<IValue[], Map<String, IValue>, IValue> {
        private final IEvaluatorContext ctx;
        private final IValue grammar;
        private final IValueFactory vf;
        private final boolean allowAmbiguity;
        private final boolean hasSideEffects;
        private final boolean firstAmbiguity;
        
        public ParseFunction(IEvaluatorContext ctx, IValue grammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity) {
            this.ctx = ctx;
            this.vf = ctx.getValueFactory();
            this.grammar = grammar;
            this.allowAmbiguity = allowAmbiguity.getValue() || firstAmbiguity.getValue();
            this.hasSideEffects = hasSideEffects.getValue();
            this.firstAmbiguity = firstAmbiguity.getValue();
        }
        
        @Override
        public IValue apply(IValue[] parameters, Map<String, IValue> keywordParameters) {
            if (parameters.length != 1) {
                throw fail(parameters);
            }

            if (firstAmbiguity) {
                if (parameters[0].getType().isString()) {
                    return firstAmbiguity(grammar, (IString) parameters[0], ctx);
                }
                else if (parameters[0].getType().isSourceLocation()) {
                    return firstAmbiguity(grammar, (ISourceLocation) parameters[0], ctx);
                }
            }
            else {
                if (parameters[0].getType().isString()) {
                    return parse(grammar, (IString) parameters[0], (ISourceLocation) keywordParameters.get("origin"), allowAmbiguity, hasSideEffects, ctx);
                }
                else if (parameters[0].getType().isSourceLocation()) {
                    return parse(grammar, (ISourceLocation) parameters[0], (ISourceLocation) keywordParameters.get("origin"), allowAmbiguity, hasSideEffects, ctx);
                }
            }
            
            throw fail(parameters);
        }

        private Throw fail(IValue... parameters) {
            return RuntimeExceptionFactory.callFailed(URIUtil.rootLocation("unknown"), Arrays.stream(parameters).collect(ctx.getValueFactory().listWriter()));
        }
        
        public IValue parse(IValue start, IString input,  ISourceLocation origin, boolean allowAmbiguity, boolean hasSideEffects, IEvaluatorContext ctx) {
            try {
                Type reified = start.getType();
                if (origin == null) {
                    origin = URIUtil.rootLocation("unknown");
                }
                
                IConstructor grammar = checkPreconditions(start, reified);
                IMap emptyMap = ctx.getValueFactory().map();
                return ctx.getEvaluator().parseObject(ctx.getEvaluator().getMonitor(), grammar, emptyMap, input.getValue(), origin, allowAmbiguity, hasSideEffects);
            }
            catch (ParseError pe) {
                ISourceLocation errorLoc = pe.getLocation();
                throw RuntimeExceptionFactory.parseError(errorLoc);
            }
            catch (Ambiguous e) {
                ITree tree = e.getTree();
                throw RuntimeExceptionFactory.ambiguity(e.getLocation(), printSymbol(TreeAdapter.getType(tree)), vf.string(TreeAdapter.yield(tree)));
            }
            catch (UndeclaredNonTerminalException e){
                throw new UndeclaredNonTerminal(e.getName(), e.getClassName(), ctx.getCurrentAST());
            }
        }
        
        public IValue firstAmbiguity(IValue start, IString input, IEvaluatorContext ctx) {
            Type reified = start.getType();
            IConstructor grammar = checkPreconditions(start, reified);
            
            try {
                return ctx.getEvaluator().parseObject(ctx.getEvaluator().getMonitor(), grammar, vf.map(), input.getValue(), false, false);
            }
            catch (ParseError pe) {
                ISourceLocation errorLoc = pe.getLocation();
                throw RuntimeExceptionFactory.parseError(errorLoc, ctx.getCurrentAST(), ctx.getStackTrace());
            }
            catch (Ambiguous e) {
                return e.getTree();
            }
            catch (UndeclaredNonTerminalException e){
                throw new UndeclaredNonTerminal(e.getName(), e.getClassName(), ctx.getCurrentAST());
            }
        }
        
        public IValue firstAmbiguity(IValue start, ISourceLocation input, IEvaluatorContext ctx) {
            Type reified = start.getType();
            IConstructor grammar = checkPreconditions(start, reified);
            
            try {
                return ctx.getEvaluator().parseObject(ctx.getEvaluator().getMonitor(), grammar, vf.map(), input, false, false);
            }
            catch (ParseError pe) {
                ISourceLocation errorLoc = pe.getLocation();
                throw RuntimeExceptionFactory.parseError(errorLoc, ctx.getCurrentAST(), ctx.getStackTrace());
            }
            catch (Ambiguous e) {
                return e.getTree();
            }
            catch (UndeclaredNonTerminalException e){
                throw new UndeclaredNonTerminal(e.getName(), e.getClassName(), ctx.getCurrentAST());
            }
        }
        
        private IString printSymbol(IConstructor symbol) {
            return vf.string(SymbolAdapter.toString(symbol, false));
        }

        private IValue parse(IValue start, ISourceLocation input, ISourceLocation origin, boolean allowAmbiguity, boolean hasSideEffects, IEvaluatorContext ctx) {
            Type reified = start.getType();
            IConstructor grammar = checkPreconditions(start, reified);
            
            if (origin == null) {
                origin = input;
            }
            
            try {
                IMap emptyMap = ctx.getValueFactory().map();
                return ctx.getEvaluator().parseObject(ctx.getEvaluator().getMonitor(), grammar, emptyMap, input, allowAmbiguity, hasSideEffects);
            }
            catch (ParseError pe) {
                ISourceLocation errorLoc = pe.getLocation();
                throw RuntimeExceptionFactory.parseError(errorLoc, ctx.getCurrentAST(), ctx.getStackTrace());
            }
            catch (Ambiguous e) {
                ITree tree = e.getTree();
                throw RuntimeExceptionFactory.ambiguity(e.getLocation(), printSymbol(TreeAdapter.getType(tree)), vf.string(TreeAdapter.yield(tree)));
            }
            catch (UndeclaredNonTerminalException e){
                throw new UndeclaredNonTerminal(e.getName(), e.getClassName(), ctx.getCurrentAST());
            }
        }

        private static IConstructor checkPreconditions(IValue start, Type reified) {
            if (!(reified instanceof ReifiedType)) {
               throw RuntimeExceptionFactory.illegalArgument(start, "A reified type is required instead of " + reified);
            }
            
            Type nt = reified.getTypeParameters().getFieldType(0);
            
            if (!(nt instanceof NonTerminalType)) {
                throw RuntimeExceptionFactory.illegalArgument(start, "A non-terminal type is required instead of  " + nt);
            }
            
            return (IConstructor) start;
        }

       
    }
}
