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
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UndeclaredNonTerminal;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.parser.gtd.exception.UndeclaredNonTerminalException;
import org.rascalmpl.types.NonTerminalType;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.types.ReifiedType;
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
        return new RascalFunctionValue(functionType, func, ctx.getEvaluator());
    }
    
    private static final class RascalFunctionValue extends AbstractFunction implements IFunction {
        private final BiFunction<IValue[], Map<String, IValue>, IValue> func;

        public RascalFunctionValue(Type functionType, BiFunction<IValue[], Map<String, IValue>, IValue> func, IEvaluator<Result<IValue>> eval) {
            super(eval.getCurrentAST(), eval, functionType, functionType, Collections.emptyList(), false, eval.getCurrentEnvt());
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
            Environment old = ctx.getCurrentEnvt();

            try {
                ctx.pushEnv(getName());
                Environment env = ctx.getCurrentEnvt();

                if (argValues.length != getArity()) {
                    throw new MatchFailed();
                }
                
                Map<Type, Type> renamings = new HashMap<>();
                Map<Type, Type> dynamicRenamings = new HashMap<>();
                bindTypeParameters(TypeFactory.getInstance().tupleType(argTypes), argValues, staticFunctionType.getFieldTypes(), renamings, dynamicRenamings, env); 

               
                IValue returnValue = func.apply(argValues, keyArgValues);

                Type resultType = getReturnType().instantiate(env.getStaticTypeBindings());
                resultType = unrenameType(renamings, resultType);
                
                if (!getReturnType().isBottom() && getReturnType().instantiate(env.getStaticTypeBindings()).isBottom()) {
                    // type parameterized functions are not allowed to return void,
                    // so they are never called if this happens (if void is bound to the return type parameter)
                    throw RuntimeExceptionFactory.callFailed(ctx.getCurrentAST().getLocation(), Arrays.stream(argValues).collect(vf.listWriter()));
                }

                if (staticFunctionType.isBottom()) {
                    return ResultFactory.nothing();
                }
                else if (returnValue == null) {
                    throw RuntimeExceptionFactory.callFailed(ctx.getCurrentAST().getLocation(), Arrays.stream(argValues).collect(vf.listWriter()));
                }
                else {
                    return ResultFactory.makeResult(resultType, returnValue, ctx);
                }
            }
            finally {
                ctx.unwind(old);
            }
        };
    }
    
    @Override
    public IFunction parser(IValue reifiedGrammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity) {
        TypeFactory tf = TypeFactory.getInstance();
        
        // the return type of the generated parse function is instantiated here to the start nonterminal of
        // the provided grammar:
        Type functionType = tf.functionType(reifiedGrammar.getType().getTypeParameters().getFieldType(0),
            tf.tupleType(tf.valueType(), tf.sourceLocationType()), 
            tf.tupleEmpty());
        
        return function(functionType, new ParseFunction(ctx, reifiedGrammar, allowAmbiguity, hasSideEffects, firstAmbiguity));
    }
    
    @Override
    public IFunction parsers(IValue reifiedGrammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity) {
        RascalTypeFactory rtf = RascalTypeFactory.getInstance();
        TypeFactory tf = TypeFactory.getInstance();
        
        // here the return type is parametrized and instantiated when the parser function is called with the
        // given start non-terminal:
        
        Type parameterType = tf.parameterType("U", RascalValueFactory.Tree);
        
        Type functionType = tf.functionType(parameterType,
            tf.tupleType(rtf.reifiedType(parameterType), tf.valueType(), tf.sourceLocationType()), 
            tf.tupleEmpty());
        
        return function(functionType, new ParametrizedParseFunction(ctx, reifiedGrammar, allowAmbiguity, hasSideEffects, firstAmbiguity));
    }
    
    /**
     * This class wraps the parseObject methods of the Evaluator by presenting it as an implementation of IFunction.
     * In this way library builtins can use the parser generator functionality of the Evaluator without knowing about
     * the internals of parser generation and parser caching.
     */
    static private class ParseFunction implements BiFunction<IValue[], Map<String, IValue>, IValue> {
        protected final IEvaluatorContext ctx;
        protected final IValue grammar;
        protected final IValueFactory vf;
        protected final boolean allowAmbiguity;
        protected final boolean hasSideEffects;
        protected final boolean firstAmbiguity;
        protected final ModuleEnvironment callingModule;
        
        public ParseFunction(IEvaluatorContext ctx, IValue grammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity) {
            this.ctx = ctx;
            this.callingModule = retrieveCallingModuleEnvironment(ctx);
            this.vf = ctx.getValueFactory();
            this.grammar = grammar;
            this.allowAmbiguity = allowAmbiguity.getValue() || firstAmbiguity.getValue();
            this.hasSideEffects = hasSideEffects.getValue();
            this.firstAmbiguity = firstAmbiguity.getValue();
        }
        
        /**
         * This workaround retrieves the user module that called the parser functions such that
         * the right normalizing overloads can be executed after parsing. 
         * @return
         */
        private ModuleEnvironment retrieveCallingModuleEnvironment(IEvaluatorContext ctx) {
            Environment current = ctx.getCurrentEnvt();
            Environment previous = current;
            
            while (current != null && "parser".equals(current.getName())) {
                previous = current;
                current = current.getCallerScope();
            }
            
            return (ModuleEnvironment) (current != null ?  current.getRoot() : previous.getRoot());
        }

        @Override
        public IValue apply(IValue[] parameters, Map<String, IValue> keywordParameters) {
            Environment old = ctx.getCurrentEnvt();
            
            try {
                ctx.setCurrentEnvt(callingModule);
                
                if (parameters.length != 2) {
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
                    if (!parameters[1].getType().isSourceLocation()) {
                        throw fail(parameters); 
                    }

                    if (parameters[0].getType().isString()) {
                        return parse(grammar, (IString) parameters[0], (ISourceLocation) parameters[1], allowAmbiguity, hasSideEffects, ctx);
                    }
                    else if (parameters[0].getType().isSourceLocation()) {
                        return parse(grammar, (ISourceLocation) parameters[0], (ISourceLocation) parameters[1], allowAmbiguity, hasSideEffects, ctx);
                    }
                }

                throw fail(parameters);
            }
            finally {
                ctx.setCurrentEnvt(old);
            }
        }

        protected Throw fail(IValue... parameters) {
            return RuntimeExceptionFactory.callFailed(URIUtil.rootLocation("unknown"), Arrays.stream(parameters).collect(ctx.getValueFactory().listWriter()));
        }
        
        protected IValue parse(IValue start, IString input,  ISourceLocation origin, boolean allowAmbiguity, boolean hasSideEffects, IEvaluatorContext ctx) {
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
        
        protected IValue firstAmbiguity(IValue start, IString input, IEvaluatorContext ctx) {
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
        
        protected IValue firstAmbiguity(IValue start, ISourceLocation input, IEvaluatorContext ctx) {
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

        protected IValue parse(IValue start, ISourceLocation input, ISourceLocation origin, boolean allowAmbiguity, boolean hasSideEffects, IEvaluatorContext ctx) {
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
    
    /**
     * This class wraps the parseObject methods of the Evaluator by presenting it as an implementation of IFunction.
     * In this way library builtins can use the parser generator functionality of the Evaluator without knowing about
     * the internals of parser generation and parser caching.
     * 
     * It generates different parse functions from @see {@link ParseFunction}; they have an additional first
     * parameter for the start-nonterminal.
     */
    static private class ParametrizedParseFunction extends ParseFunction {
        
        public ParametrizedParseFunction(IEvaluatorContext ctx, IValue grammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity) {
            super(ctx, grammar, allowAmbiguity, hasSideEffects, firstAmbiguity);
        }
        
        @Override
        public IValue apply(IValue[] parameters, Map<String, IValue> keywordParameters) {
            Environment old = ctx.getCurrentEnvt();
            try {
                ctx.setCurrentEnvt(callingModule);
                
                if (parameters.length != 3) {
                    throw fail(parameters);
                }

                if (firstAmbiguity) {
                    if (parameters[1].getType().isString()) {
                        return firstAmbiguity(parameters[0], (IString) parameters[1], ctx);
                    }
                    else if (parameters[1].getType().isSourceLocation()) {
                        return firstAmbiguity(parameters[0], (ISourceLocation) parameters[1], ctx);
                    }
                }
                else {
                    if (!(parameters[0].getType() instanceof ReifiedType)) {
                        throw fail(parameters);
                    }

                    if (!parameters[2].getType().isSourceLocation()) {
                        throw fail(parameters); 
                    }

                    if (parameters[1].getType().isString()) {
                        return parse(parameters[0], (IString) parameters[1], (ISourceLocation) parameters[2], allowAmbiguity, hasSideEffects, ctx);
                    }
                    else if (parameters[1].getType().isSourceLocation()) {
                        return parse(parameters[0], (ISourceLocation) parameters[1], (ISourceLocation) parameters[2], allowAmbiguity, hasSideEffects, ctx);
                    }
                }

                throw fail(parameters);
            }
            finally {
                ctx.setCurrentEnvt(old);
            }
        } 
    }
}
