/*
 * Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction0;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction1;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction2;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction3;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction4;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction5;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction6;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction7;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction8;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction9;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance0;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance1;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance2;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance3;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance4;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance5;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance6;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance7;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance8;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance9;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.parser.ParserGenerator;
import org.rascalmpl.parser.gtd.IGTD;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.parser.gtd.exception.UndeclaredNonTerminalException;
import org.rascalmpl.parser.gtd.io.InputConverter;
import org.rascalmpl.parser.gtd.recovery.IRecoverer;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.types.NonTerminalType;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.types.ReifiedType;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class RascalRuntimeValueFactory extends RascalValueFactory {
    
    private final RascalExecutionContext rex;
    
    private LoadingCache<IMap, Class<IGTD<IConstructor, ITree, ISourceLocation>>> parserCache = Caffeine.newBuilder()
        .softValues()
        .maximumSize(100) // a 100 cached parsers is quit a lot, put this in to make debugging such a case possible
        .expireAfterAccess(30, TimeUnit.MINUTES) // we clean up unused parsers after 30 minutes
        .build(grammar -> generateParser(grammar));

    public RascalRuntimeValueFactory(RascalExecutionContext rex) {
    	this.rex = rex;
    }

    private ParserGenerator getParserGenerator() {
    	return ParserGeneratorFactory.getInstance(rex).getParserGenerator(this);
    }

    private Class<IGTD<IConstructor, ITree, ISourceLocation>> generateParser(IMap grammar) {
        try {
        	System.err.println("generateParser: " + grammar.hashCode());
//        	for(IValue key : grammar) {
//        		System.err.println(key + ":\n\t" + grammar.get(key));
//        	}
        	
            return getParserGenerator().getNewParser(rex, URIUtil.rootLocation("parser-generator"), "$GENERATED_PARSER$" + Math.abs(grammar.hashCode()), grammar);
        } 
        catch (ExceptionInInitializerError e) {
            throw new ImplementationError(e.getMessage(), e);
        }
    }

    private IGTD<IConstructor, ITree, ISourceLocation> getObjectParser(IMap iMap) {
        Class<IGTD<IConstructor, ITree, ISourceLocation>> parser = parserCache.get(iMap);
        try {
            return parser.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new ImplementationError("could not instantiate generated parser", e);
        } 
    }

    @Override
    public IFunction function(io.usethesource.vallang.type.Type functionType, BiFunction<IValue[], Map<String, IValue>, IValue> func) {
        switch (functionType.getArity()) {
        case 0:
            return new TypedFunctionInstance0<>(new TypedFunction0<IValue> () {
                @Override
                public IValue typedCall() {
                    return func.apply(new IValue[0], Collections.emptyMap());
                }
            }, functionType);
        case 1:
            return new TypedFunctionInstance1<>(new TypedFunction1<IValue,IValue>() {
                @Override
                public IValue typedCall(IValue arg) {
                    return func.apply(new IValue[] { arg }, Collections.emptyMap());
                }
            }, functionType);
        case 2:
            return new TypedFunctionInstance2<>(new TypedFunction2<IValue,IValue,IValue>() {
                @Override
                public IValue typedCall(IValue arg, IValue arg2) {
                    return func.apply(new IValue[] { arg, arg2 }, Collections.emptyMap());
                }
            }, functionType);
        case 3:
            return new TypedFunctionInstance3<>(new TypedFunction3<IValue,IValue,IValue,IValue>() {
                @Override
                public IValue typedCall(IValue arg, IValue arg2, IValue arg3) {
                    return func.apply(new IValue[] { arg, arg2, arg3 }, Collections.emptyMap());
                }
            }, functionType);
        case 4:
            return new TypedFunctionInstance4<>(new TypedFunction4<IValue,IValue,IValue,IValue,IValue>() {
                @Override
                public IValue typedCall(IValue arg, IValue arg2, IValue arg3, IValue arg4) {
                    return func.apply(new IValue[] { arg, arg2, arg3, arg4 }, Collections.emptyMap());
                }
            }, functionType);
        case 5:
            return new TypedFunctionInstance5<>(new TypedFunction5<IValue,IValue,IValue,IValue,IValue,IValue>() {
                @Override
                public IValue typedCall(IValue arg, IValue arg2, IValue arg3, IValue arg4, IValue arg5) {
                    return func.apply(new IValue[] { arg, arg2, arg3, arg4, arg5 }, Collections.emptyMap());
                }
            }, functionType);
        case 6:
            return new TypedFunctionInstance6<>(new TypedFunction6<IValue,IValue,IValue,IValue,IValue,IValue,IValue>() {
                @Override
                public IValue typedCall(IValue arg, IValue arg2, IValue arg3, IValue arg4, IValue arg5, IValue arg6) {
                    return func.apply(new IValue[] { arg, arg2, arg3, arg4, arg5, arg6 }, Collections.emptyMap());
                }
            }, functionType);
        case 7:
            return new TypedFunctionInstance7<>(new TypedFunction7<IValue,IValue,IValue,IValue,IValue,IValue,IValue,IValue>() {
                @Override
                public IValue typedCall(IValue arg, IValue arg2, IValue arg3, IValue arg4, IValue arg5, IValue arg6, IValue arg7) {
                    return func.apply(new IValue[] { arg, arg2, arg3, arg4, arg5, arg6, arg7 }, Collections.emptyMap());
                }
            }, functionType);
        case 8:
            return new TypedFunctionInstance8<>(new TypedFunction8<IValue,IValue,IValue,IValue,IValue,IValue,IValue,IValue,IValue>() {
                @Override
                public IValue typedCall(IValue arg, IValue arg2, IValue arg3, IValue arg4, IValue arg5, IValue arg6, IValue arg7, IValue arg8) {
                    return func.apply(new IValue[] { arg, arg2, arg3, arg4, arg5, arg6, arg7, arg8 }, Collections.emptyMap());
                }
            }, functionType);
        case 9:
            return new TypedFunctionInstance9<>(new TypedFunction9<IValue,IValue,IValue,IValue,IValue,IValue,IValue,IValue,IValue,IValue>() {
                @Override
                public IValue typedCall(IValue arg, IValue arg2, IValue arg3, IValue arg4, IValue arg5, IValue arg6, IValue arg7, IValue arg8, IValue arg9) {
                    return func.apply(new IValue[] { arg, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9 }, Collections.emptyMap());
                }
            }, functionType);
        }
        
        throw new UnsupportedOperationException("do not support functions with arity higher than 6 yet?");
    }
    
    @Override
    public IFunction parser(IValue reifiedGrammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity, ISet filters) {
        TypeFactory tf = TypeFactory.getInstance();
        
        // the return type of the generated parse function is instantiated here to the start nonterminal of
        // the provided grammar:
        Type functionType = tf.functionType(reifiedGrammar.getType().getTypeParameters().getFieldType(0),
            tf.tupleType(tf.valueType(), tf.sourceLocationType()), 
            tf.tupleEmpty());
        
        return function(functionType, new ParseFunction(this, reifiedGrammar, allowAmbiguity, hasSideEffects, firstAmbiguity, filters));
    }
    
    @Override
    public IFunction parsers(IValue reifiedGrammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity, ISet filters) {
        RascalTypeFactory rtf = RascalTypeFactory.getInstance();
        TypeFactory tf = TypeFactory.getInstance();
        
        // here the return type is parametrized and instantiated when the parser function is called with the
        // given start non-terminal:
        
        Type parameterType = tf.parameterType("U", RascalValueFactory.Tree);
        
        Type functionType = tf.functionType(parameterType,
            tf.tupleType(rtf.reifiedType(parameterType), tf.valueType(), tf.sourceLocationType()), 
            tf.tupleEmpty());
        
        return function(functionType, new ParametrizedParseFunction(this, reifiedGrammar, allowAmbiguity, hasSideEffects, firstAmbiguity, filters));
    }
    
    /**
     * This class wraps the parseObject methods of this factory by presenting it them as an implementation of IFunction.
     * In this way library builtins can use the embedded parser generator functionalitywithout knowing about
     * the internals of parser generation and parser caching.
     */
    private class ParseFunction implements BiFunction<IValue[], Map<String, IValue>, IValue> {
        protected final IValue grammar;
        protected final ISet filters;
        protected final IValueFactory vf;
        protected final boolean allowAmbiguity;
        protected final boolean hasSideEffects;
        protected final boolean firstAmbiguity;
        
        public ParseFunction(IRascalValueFactory vf, IValue grammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity, ISet filters) {
            this.vf = vf;
            this.grammar = grammar;
            this.filters = filters;
            this.allowAmbiguity = allowAmbiguity.getValue() || firstAmbiguity.getValue();
            this.hasSideEffects = hasSideEffects.getValue();
            this.firstAmbiguity = firstAmbiguity.getValue();
        }
        
        @Override
        public IValue apply(IValue[] parameters, Map<String, IValue> keywordParameters) {
            if (parameters.length != 2) {
                throw fail(parameters);
            }

            if (firstAmbiguity) {
                if (parameters[0].getType().isString()) {
                    return firstAmbiguity(grammar, (IString) parameters[0], getParserGenerator());
                }
                else if (parameters[0].getType().isSourceLocation()) {
                    return firstAmbiguity(grammar, (ISourceLocation) parameters[0], getParserGenerator());
                }
            }
            else {
                if (!parameters[1].getType().isSourceLocation()) {
                    throw fail(parameters); 
                }

                if (parameters[0].getType().isString()) {
                    return parse(grammar, (IString) parameters[0], (ISourceLocation) parameters[1], allowAmbiguity, hasSideEffects, filters, getParserGenerator());
                }
                else if (parameters[0].getType().isSourceLocation()) {
                    return parse(grammar, (ISourceLocation) parameters[0], (ISourceLocation) parameters[1], allowAmbiguity, hasSideEffects, filters, getParserGenerator());
                }
            }

            throw fail(parameters);
        }

        protected Throw fail(IValue... parameters) {
            return RuntimeExceptionFactory.callFailed(URIUtil.rootLocation("unknown"), Arrays.stream(parameters).collect(vf.listWriter()));
        }
        
        protected IValue firstAmbiguity(IValue start, IString input, ParserGenerator generator) {
            Type reified = start.getType();
            IConstructor grammar = checkPreconditions(start, reified);
            
            try {
                return parseObject(grammar, input.getValue(), URIUtil.rootLocation("unknown"), false, false, filters);
            }
            catch (ParseError pe) {
                ISourceLocation errorLoc = pe.getLocation();
                throw RuntimeExceptionFactory.parseError(errorLoc);
            }
            catch (Ambiguous e) {
                return e.getTree();
            }
            catch (UndeclaredNonTerminalException e){
                throw RuntimeExceptionFactory.illegalArgument(vf.string(e.getName()));
            }
        }
        
        protected IValue firstAmbiguity(IValue start, ISourceLocation input, ParserGenerator generator) {
            Type reified = start.getType();
            IConstructor grammar = checkPreconditions(start, reified);
            
            try {
                return parseObject(grammar, filters, input, false, false);
            }
            catch (ParseError pe) {
                ISourceLocation errorLoc = pe.getLocation();
                throw RuntimeExceptionFactory.parseError(errorLoc);
            }
            catch (Ambiguous e) {
                return e.getTree();
            }
            catch (UndeclaredNonTerminalException e){
                throw RuntimeExceptionFactory.illegalArgument(vf.string(e.getName()));
            }
        }
        
        private IString printSymbol(IConstructor symbol) {
            return vf.string(SymbolAdapter.toString(symbol, false));
        }

        protected IValue parse(IValue start, IString input, ISourceLocation origin, boolean allowAmbiguity, boolean hasSideEffects, ISet filters, ParserGenerator generator) {
            Type reified = start.getType();
            IConstructor grammar = checkPreconditions(start, reified);
            //System.err.println("parse uses grammar:"); System.err.println(grammar);
            if (origin == null) {
                origin = URIUtil.rootLocation("unknown");
            }
            
            try {
                return parseObject(grammar, input.getValue(), origin, allowAmbiguity, hasSideEffects, filters);
            }
            catch (ParseError pe) {
                ISourceLocation errorLoc = pe.getLocation();
                //System.err.println(grammar);
                throw RuntimeExceptionFactory.parseError(errorLoc);
            }
            catch (Ambiguous e) {
                ITree tree = e.getTree();
                throw RuntimeExceptionFactory.ambiguity(e.getLocation(), printSymbol(TreeAdapter.getType(tree)), vf.string(TreeAdapter.yield(tree)));
            }
            catch (UndeclaredNonTerminalException e){
                throw RuntimeExceptionFactory.illegalArgument(vf.string(e.getName()));
            }
        }
        
        protected IValue parse(IValue start, ISourceLocation input, ISourceLocation origin, boolean allowAmbiguity, boolean hasSideEffects, ISet filters, ParserGenerator generator) {
            Type reified = start.getType();
            IConstructor grammar = checkPreconditions(start, reified);
            
            if (origin == null) {
                origin = input;
            }
            
            try {
                return parseObject(grammar, filters, input, allowAmbiguity, hasSideEffects);
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
                throw RuntimeExceptionFactory.illegalArgument(vf.string(e.getName()));
            }
        }
        
        private ITree parseObject(IConstructor grammar,  ISourceLocation location, char[] input,  boolean allowAmbiguity, boolean hasSideEffects,  ISet filters) {
            IConstructor startSort = (IConstructor) grammar.get("symbol");
            IGTD<IConstructor, ITree, ISourceLocation> parser = getObjectParser((IMap) grammar.get("definitions"));
            String name = getParserGenerator().getParserMethodName(startSort);

            IActionExecutor<ITree> exec = filters.isEmpty() ?  new NoActionExecutor() : new RascalFunctionActionExecutor(filters, !hasSideEffects);

            return (ITree) parser.parse(name, location.getURI(), input, exec, new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(allowAmbiguity), (IRecoverer<IConstructor>) null);
        }
        
        private IConstructor parseObject(IConstructor startSort, ISet filters, ISourceLocation location,  boolean allowAmbiguity, boolean hasSideEffects){
            try {
                char[] input = getResourceContent(location);
                return parseObject(startSort, location, input, allowAmbiguity, hasSideEffects, filters);
            }
            catch (IOException e) {
                throw RuntimeExceptionFactory.io(vf.string(e.getMessage()));
            }
        }

        private IConstructor parseObject(IConstructor startSort, String input, ISourceLocation loc,  boolean allowAmbiguity, boolean hasSideEffects, ISet filters) {
            return parseObject(startSort, loc, input.toCharArray(), allowAmbiguity, hasSideEffects, filters);
        }

        private IConstructor checkPreconditions(IValue start, Type reified) {
            if (!(reified instanceof ReifiedType)) {
               throw RuntimeExceptionFactory.illegalArgument(start, "A reified type is required instead of " + reified);
            }
            
            Type nt = reified.getTypeParameters().getFieldType(0);
            
            if (!(nt instanceof NonTerminalType)) {
                throw RuntimeExceptionFactory.illegalArgument(start, "A non-terminal type is required instead of  " + nt);
            }
            
            return (IConstructor) start;
        }
        
        private char[] getResourceContent(ISourceLocation location) throws IOException{
            try (Reader textStream = URIResolverRegistry.getInstance().getCharacterReader(location)) {
                return InputConverter.toChar(textStream);
            }
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
    private class ParametrizedParseFunction extends ParseFunction {
        
        public ParametrizedParseFunction(IRascalValueFactory vf, IValue grammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity, ISet filters) {
            super(vf, grammar, allowAmbiguity, hasSideEffects, firstAmbiguity, filters);
        }
        
        @Override
        public IValue apply(IValue[] parameters, Map<String, IValue> keywordParameters) {
            if (parameters.length != 3) {
                throw fail(parameters);
            }

            if (firstAmbiguity) {
                if (parameters[1].getType().isString()) {
                    return firstAmbiguity(parameters[0], (IString) parameters[1], getParserGenerator());
                }
                else if (parameters[1].getType().isSourceLocation()) {
                    return firstAmbiguity(parameters[0], (ISourceLocation) parameters[1], getParserGenerator());
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
                    return parse(parameters[0], ((IString) parameters[1]), (ISourceLocation) parameters[2], allowAmbiguity, hasSideEffects, filters, getParserGenerator());
                }
                else if (parameters[1].getType().isSourceLocation()) {
                    return parse(parameters[0], (ISourceLocation) parameters[1], (ISourceLocation) parameters[2], allowAmbiguity, hasSideEffects, filters, getParserGenerator());
                }
            }

            throw fail(parameters);
        } 
    }
}
