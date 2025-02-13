/** 
 * Copyright (c) 2020, Jurgen J. Vinju, Centrum Wiskunde & Informatica (NWO-I - CWI) 
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

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UndeclaredNonTerminal;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.library.util.ParseErrorRecovery;
import org.rascalmpl.parser.ParserGenerator;
import org.rascalmpl.parser.gtd.IGTD;
import org.rascalmpl.parser.gtd.debug.IDebugListener;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.parser.gtd.exception.UndeclaredNonTerminalException;
import org.rascalmpl.parser.gtd.io.InputConverter;
import org.rascalmpl.parser.gtd.recovery.IRecoverer;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.util.StackNodeIdDispenser;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.parser.uptr.action.RascalFunctionActionExecutor;
import org.rascalmpl.parser.uptr.recovery.ToTokenRecoverer;
import org.rascalmpl.types.NonTerminalType;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.types.ReifiedType;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.SymbolFactory;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class RascalFunctionValueFactory extends RascalValueFactory {
    private ParserGenerator generator;
    private final LoadingCache<IMap, Class<IGTD<IConstructor, ITree, ISourceLocation>>> parserCache = Caffeine.newBuilder()
        .softValues()
        .maximumSize(100) // a 100 cached parsers is quit a lot, put this in to make debugging such a case possible
        .expireAfterAccess(30, TimeUnit.MINUTES) // we clean up unused parsers after 30 minutes
        .build(grammar -> generateParser(grammar));
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
    
    private ParserGenerator getParserGenerator() {
        if (this.generator == null) {
            this.generator = ctx.getEvaluator().getParserGenerator();
        }
        
        return generator;
    }

    private Class<IGTD<IConstructor, ITree, ISourceLocation>> generateParser(IMap grammar) {
        try {
            return getParserGenerator().getNewParser(ctx.getEvaluator().getMonitor(), URIUtil.rootLocation("parser-generator"), "$GENERATED_PARSER$" + Math.abs(grammar.hashCode()), grammar);
        } 
        catch (ExceptionInInitializerError e) {
            throw new ImplementationError(e.getMessage(), e);
        }
    }

    protected Class<IGTD<IConstructor, ITree, ISourceLocation>> getParserClass(IMap grammar) {
        return parserCache.get(grammar);
    }

    protected void writeParserClass(IMap grammar, ISourceLocation target) throws IOException {
        getParserGenerator().writeNewParser(
            ctx.getEvaluator().getMonitor(), 
            URIUtil.rootLocation("parser-generator"), 
            "$GENERATED_PARSER$" + Math.abs(grammar.hashCode()), 
            grammar, 
            target
        );
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
        
        @Override
        @SuppressWarnings("unchecked")
        public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
            // if we call this function from outside the interpreter, we might as well just
            // call it immediately without locking the evaluator. This is beneficial for
            // situations such as the LSP and the Eclipse IDE which use callbacks.
            // in particular generated parsers do not need locking per se.
            return (T) func.apply(parameters, keywordParameters);
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

                if (staticFunctionType.getReturnType().isBottom()) {
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
    public IFunction parser(IValue reifiedGrammar, IBool allowAmbiguity, IBool allowRecovery, IBool hasSideEffects, IBool firstAmbiguity, ISet filters) {
        TypeFactory tf = TypeFactory.getInstance();
        
        // the return type of the generated parse function is instantiated here to the start nonterminal of
        // the provided grammar:
        Type functionType = !firstAmbiguity.getValue() 
            ? tf.functionType(reifiedGrammar.getType().getTypeParameters().getFieldType(0),
                tf.tupleType(tf.valueType(), tf.sourceLocationType()), 
                tf.tupleEmpty())
            : tf.functionType(RascalFunctionValueFactory.Tree,
                tf.tupleType(tf.valueType(), tf.sourceLocationType()), 
                tf.tupleEmpty())
            ;
        
        Class<IGTD<IConstructor, ITree, ISourceLocation>>parser = getParserClass((IMap) ((IConstructor) reifiedGrammar).get("definitions"));
        IConstructor startSort = (IConstructor) ((IConstructor) reifiedGrammar).get("symbol");

        checkPreconditions(startSort, reifiedGrammar.getType());
        AbstractAST current = ctx.getCurrentAST();
        ISourceLocation caller = current != null ? current.getLocation() : URIUtil.rootLocation("unknown");

        String name = getParserMethodName(startSort);
        if (name == null) {
            name = generator.getParserMethodName(startSort);
        }

        return function(functionType, new ParseFunction(ctx.getValueFactory(), caller, parser, name, allowAmbiguity, allowRecovery, hasSideEffects, firstAmbiguity, filters));
    }
    
    protected static String getParserMethodName(IConstructor symbol) {
		// we use a fast non-synchronized path for simple cases; 
		// this is to prevent locking the evaluator in IDE contexts
		// where many calls into the evaluator/parser are fired in rapid
		// succession.

		switch (symbol.getName()) {
			case "start":
				return "start__" + getParserMethodName(SymbolAdapter.getStart(symbol));
			case "layouts":
				return "layouts_" + SymbolAdapter.getName(symbol);
			case "sort":
			case "lex":
			case "keywords":
				return SymbolAdapter.getName(symbol);
		}

        return null;
    }

    @Override
    public IFunction parsers(IValue reifiedGrammar, IBool allowAmbiguity, IBool allowRecovery, IBool hasSideEffects, IBool firstAmbiguity, ISet filters) {
        RascalTypeFactory rtf = RascalTypeFactory.getInstance();
        TypeFactory tf = TypeFactory.getInstance();
        
        // here the return type is parametrized and instantiated when the parser function is called with the
        // given start non-terminal:
        Type parameterType = tf.parameterType("U", RascalValueFactory.Tree);
        
        Type functionType = tf.functionType(parameterType,
            tf.tupleType(rtf.reifiedType(parameterType), tf.valueType(), tf.sourceLocationType()), 
            tf.tupleEmpty());

        Class<IGTD<IConstructor, ITree, ISourceLocation>> parser = getParserClass((IMap) ((IConstructor) reifiedGrammar).get("definitions"));
        IConstructor startSort = (IConstructor) ((IConstructor) reifiedGrammar).get("symbol");

        checkPreconditions(startSort, reifiedGrammar.getType());    
        
        AbstractAST current = ctx.getCurrentAST();
        ISourceLocation caller = current != null ? current.getLocation() : URIUtil.rootLocation("unknown");
        
        return function(functionType, new ParametrizedParseFunction(() -> getParserGenerator(), this, caller, parser, allowAmbiguity, allowRecovery, hasSideEffects, firstAmbiguity, filters));
    }

    @Override
    public void storeParsers(IValue reifiedGrammar, ISourceLocation saveLocation) throws IOException {
        IMap grammar = (IMap) ((IConstructor) reifiedGrammar).get("definitions");
        getParserGenerator().writeNewParser(new NullRascalMonitor(), URIUtil.rootLocation("parser-generator"), "$GENERATED_PARSER$" + Math.abs(grammar.hashCode()), grammar, saveLocation);
    }

    @Override
    public IFunction loadParsers(ISourceLocation saveLocation, IBool allowAmbiguity, IBool allowRecovery, IBool hasSideEffects, IBool firstAmbiguity, ISet filters) throws IOException, ClassNotFoundException {
        RascalTypeFactory rtf = RascalTypeFactory.getInstance();
        TypeFactory tf = TypeFactory.getInstance();
        
        // here the return type is parametrized and instantiated when the parser function is called with the
        // given start non-terminal:
        Type parameterType = tf.parameterType("U", RascalValueFactory.Tree);
        
        Type functionType = tf.functionType(parameterType,
            tf.tupleType(rtf.reifiedType(parameterType), tf.valueType(), tf.sourceLocationType()), 
            tf.tupleEmpty());

        @SuppressWarnings({"unchecked"})
        final Class<IGTD<IConstructor, ITree, ISourceLocation>> parser 
            = (Class<IGTD<IConstructor, ITree, ISourceLocation>>) ctx.getEvaluator()
                .__getJavaBridge().loadClass(URIResolverRegistry.getInstance().getInputStream(saveLocation));
          
        AbstractAST current = ctx.getCurrentAST();
        ISourceLocation caller = current != null ? current.getLocation() : URIUtil.rootLocation("unknown");
                
        return function(
            functionType, 
            new ParametrizedParseFunction(() -> getParserGenerator(), 
            this, 
            caller, 
            parser, 
            allowAmbiguity, allowRecovery, hasSideEffects, firstAmbiguity, filters)
        );
    }

    @Override
    public IFunction loadParser(IValue reifiedGrammar, ISourceLocation saveLocation, IBool allowAmbiguity, IBool allowRecovery, IBool hasSideEffects, IBool firstAmbiguity, ISet filters) throws IOException, ClassNotFoundException {
        TypeFactory tf = TypeFactory.getInstance();
        
        Type functionType = tf.functionType(reifiedGrammar.getType().getTypeParameters().getFieldType(0),
            tf.tupleType(tf.valueType(), tf.sourceLocationType()), 
            tf.tupleEmpty());
      
        @SuppressWarnings({"unchecked"})
        final Class<IGTD<IConstructor, ITree, ISourceLocation>> parser 
            = (Class<IGTD<IConstructor, ITree, ISourceLocation>>) ctx.getEvaluator()
                .__getJavaBridge().loadClass(URIResolverRegistry.getInstance().getInputStream(saveLocation));
          
        AbstractAST current = ctx.getCurrentAST();
        ISourceLocation caller = current != null ? current.getLocation() : URIUtil.rootLocation("unknown");
                
        IConstructor startSort = (IConstructor) ((IConstructor) reifiedGrammar).get("symbol");

        checkPreconditions(startSort, reifiedGrammar.getType());
        
        String name = getParserMethodName(startSort);
        if (name == null) {
            name = generator.getParserMethodName(startSort);
        }

        return function(functionType, new ParseFunction(ctx.getValueFactory(), caller, parser, name, allowAmbiguity, allowRecovery, hasSideEffects, firstAmbiguity, filters));
    }

    /**
     * This function mimicks `parsers(#start[Module]) inside lang::rascal::\syntax::Rascal.
     * so it produces a parse function for the Rascal language, where non-terminal is the
     * first parameter as a reified type and a str (IString) is the second parameter.
     */
    public IFunction bootstrapParsers() {
        RascalTypeFactory rtf = RascalTypeFactory.getInstance();
        TypeFactory tf = TypeFactory.getInstance();
        IValueFactory vf = ctx.getValueFactory();

        Type parameterType = tf.parameterType("U", RascalValueFactory.Tree);

        Type functionType = tf.functionType(parameterType,
            tf.tupleType(rtf.reifiedType(parameterType), tf.valueType(), tf.sourceLocationType()), 
            tf.tupleEmpty());

        // Funny diamond issue with multiple interface inheritance requires double cast to avoid compiler error here.
        @SuppressWarnings("unchecked")
        final Class<IGTD<IConstructor, ITree, ISourceLocation>> parser = (Class<IGTD<IConstructor, ITree, ISourceLocation>>) (Class<?>) RascalParser.class;

        AbstractAST current = ctx.getCurrentAST();
        ISourceLocation caller = current != null ? current.getLocation() : URIUtil.rootLocation("unknown");
        return function(functionType, new ParametrizedParseFunction(() -> getParserGenerator(), this, caller, parser, vf.bool(false), vf.bool(false), vf.bool(false), vf.bool(false), ctx.getValueFactory().set()));
    }

    public IString createHole(ITree part, IInteger index) {
        ITree hole = TreeAdapter.getArg(part, "hole");
        ITree sym = TreeAdapter.getArg(hole, "symbol");
        IConstructor symbol = SymbolFactory.typeToSymbol(sym , false, null);

        IString result =  ctx.getValueFactory().string("\u0000" + symbol.toString() + ":" + index + "\u0000");

        return result;
    }

    public IConstructor sym2symbol(ITree parsedSym) {
        if ("nonterminal".equals(TreeAdapter.getConstructorName(parsedSym))) {
            String nonterminalName = TreeAdapter.yield(parsedSym);
            return constructor(Symbol_Sort, string(nonterminalName));
        }
        
        return getParserGenerator().symbolTreeToSymbol(parsedSym);
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
    
    /**
     * This class wraps generated parser classes by presenting them as implementations of IFunction.
     * In this way library builtins can use the parser generator functionality of the Evaluator without knowing about
     * the internals of parser generation and parser caching.
     */
    static private class ParseFunction implements BiFunction<IValue[], Map<String, IValue>, IValue> {
        protected final ISet filters;
        protected final IValueFactory vf;
        protected final boolean allowAmbiguity;
        protected final boolean allowRecovery;
        protected final boolean hasSideEffects;
        protected final boolean firstAmbiguity;
        protected final Class<IGTD<IConstructor, ITree, ISourceLocation>> parser;
        protected final String methodName;
        protected final ISourceLocation caller;
        
        public ParseFunction(IValueFactory vf, ISourceLocation caller, Class<IGTD<IConstructor, ITree, ISourceLocation>> parser, String methodName, IBool allowAmbiguity, IBool allowRecovery, IBool hasSideEffects, IBool firstAmbiguity, ISet filters) {
            this.vf = vf;
            this.caller = caller;
            this.parser = parser;
            this.methodName = methodName;
            this.filters = filters;
            this.allowAmbiguity = allowAmbiguity.getValue() || firstAmbiguity.getValue();
            this.allowRecovery = allowRecovery.getValue();
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
                    return firstAmbiguity(methodName, (IString) parameters[0]);
                }
                else if (parameters[0].getType().isSourceLocation()) {
                    return firstAmbiguity(methodName, (ISourceLocation) parameters[0]);
                }
            }
            else {
                if (!parameters[1].getType().isSourceLocation()) {
                    throw fail(parameters); 
                }

                if (parameters[0].getType().isString()) {
                    return parse(methodName, filters, (IString) parameters[0], (ISourceLocation) parameters[1], allowAmbiguity, allowRecovery, hasSideEffects);
                }
                else if (parameters[0].getType().isSourceLocation()) {
                    return parse(methodName, filters, (ISourceLocation) parameters[0], (ISourceLocation) parameters[1], allowAmbiguity, allowRecovery, hasSideEffects);
                }
            }

            throw fail(parameters);
        }

        protected Throw fail(IValue... parameters) {
            return RuntimeExceptionFactory.callFailed(caller, Arrays.stream(parameters).collect(vf.listWriter()));
        }
        
        private IGTD<IConstructor, ITree, ISourceLocation> getParser() {
            try {
                return parser.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                throw new ImplementationError("could not instantiate generated parser", e);
            } 
        }

        protected IValue parse(String methodName, ISet filters, IString input,  ISourceLocation origin, boolean allowAmbiguity, boolean allowRecovery, boolean hasSideEffects) {
            try {
                if (origin == null) {
                    origin = URIUtil.rootLocation("unknown");
                }
                
                return parseObject(methodName, origin, input.getValue().toCharArray(), allowAmbiguity, allowRecovery, hasSideEffects, filters);
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
                throw new UndeclaredNonTerminal(e.getName(), e.getClassName(), caller);
            }
        }
        
        protected IValue firstAmbiguity(String methodName, IString input) {
            try {
                return parseObject(methodName, URIUtil.invalidLocation(), input.getValue().toCharArray(), false, false, false, vf.set());
            }
            catch (ParseError pe) {
                ISourceLocation errorLoc = pe.getLocation();
                throw RuntimeExceptionFactory.parseError(errorLoc);
            }
            catch (Ambiguous e) {
                return e.getTree();
            }
            catch (UndeclaredNonTerminalException e){
                throw new UndeclaredNonTerminal(e.getName(), e.getClassName(), caller);
            }
        }
        
        protected IValue firstAmbiguity(String methodName, ISourceLocation input) {
            try {
                return parseObject(methodName, input, readAll(input), false, false, false, vf.set());
            }
            catch (ParseError pe) {
                ISourceLocation errorLoc = pe.getLocation();
                throw RuntimeExceptionFactory.parseError(errorLoc);
            }
            catch (IOException e) {
                throw RuntimeExceptionFactory.io("IO error: " + e);
            }
            catch (Ambiguous e) {
                return e.getTree();
            }
            catch (UndeclaredNonTerminalException e){
                throw new UndeclaredNonTerminal(e.getName(), e.getClassName(), caller);
            }
        }

        private static char[] readAll(ISourceLocation loc) throws IOException {
            try (Reader chars = URIResolverRegistry.getInstance().getCharacterReader(loc)) {
                return InputConverter.toChar(chars);
            }
        }
        
        private IString printSymbol(IConstructor symbol) {
            return vf.string(SymbolAdapter.toString(symbol, false));
        }

        protected IValue parse(String methodName, ISet filters, ISourceLocation input, ISourceLocation origin, boolean allowAmbiguity, boolean allowRecovery, boolean hasSideEffects) {
            if (origin != null && !origin.equals(input)) {
                throw new IllegalArgumentException("input and origin should be equal: <input> != <origin>");
            }
            
            try {
                return parseObject(methodName, input, readAll(input), allowAmbiguity, allowRecovery, hasSideEffects, filters);
            }
            catch (ParseError pe) {
                ISourceLocation errorLoc = pe.getLocation();
                throw RuntimeExceptionFactory.parseError(errorLoc);
            }
            catch (IOException e) {
                throw RuntimeExceptionFactory.io("IO error: " + e);
            }
            catch (Ambiguous e) {
                ITree tree = e.getTree();
                throw RuntimeExceptionFactory.ambiguity(e.getLocation(), printSymbol(TreeAdapter.getType(tree)), vf.string(TreeAdapter.yield(tree)));
            }
            catch (UndeclaredNonTerminalException e){
                throw new UndeclaredNonTerminal(e.getName(), e.getClassName(), caller);
            }
        }

        private ITree parseObject(String methodName, ISourceLocation location, char[] input,  boolean allowAmbiguity, boolean allowRecovery, boolean hasSideEffects,  ISet filters) {
            IActionExecutor<ITree> exec = filters.isEmpty() ?  new NoActionExecutor() : new RascalFunctionActionExecutor(filters, !hasSideEffects);
            IGTD<IConstructor, ITree, ISourceLocation> parserInstance = getParser();
            IRecoverer<IConstructor> recoverer = null;
            IDebugListener<IConstructor> debugListener = null;
            URI uri = location.getURI();
            if (allowRecovery) {
                recoverer = new ToTokenRecoverer(uri, parserInstance, new StackNodeIdDispenser(parserInstance));
            }
            ITree parseForest = (ITree) parserInstance.parse(methodName, uri, input, exec, new DefaultNodeFlattener<>(), new UPTRNodeFactory(allowRecovery || allowAmbiguity), recoverer, debugListener);

            if (!allowAmbiguity && allowRecovery) {
                // Check for 'regular' (non-error) ambiguities
                RascalValueFactory valueFactory = (RascalValueFactory) ValueFactoryFactory.getValueFactory();
                new ParseErrorRecovery(valueFactory).checkForRegularAmbiguities(parseForest);
            }

            return parseForest;
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
        private Supplier<ParserGenerator> generator;

        public ParametrizedParseFunction(Supplier<ParserGenerator> generator, IValueFactory vf, ISourceLocation caller, Class<IGTD<IConstructor, ITree, ISourceLocation>> parser, IBool allowAmbiguity, IBool allowRecovery, IBool hasSideEffects, IBool firstAmbiguity, ISet filters) {
            super(vf, caller, parser, null, allowAmbiguity, allowRecovery, hasSideEffects, firstAmbiguity, filters);
            this.generator = generator;
        }
        
        @Override
        public IValue apply(IValue[] parameters, Map<String, IValue> keywordParameters) {
            if (parameters.length != 3) {
                throw fail(parameters);
            }

            IConstructor startSort = (IConstructor) ((IConstructor) parameters[0]).get("symbol");

            if (!(parameters[0].getType() instanceof ReifiedType)) {
                throw fail(parameters);
            }

            String name = getParserMethodName(startSort);
            if (name == null) {
                name = generator.get().getParserMethodName(startSort);
            }

            if (firstAmbiguity) {
                if (parameters[1].getType().isString()) {
                    return firstAmbiguity(name, (IString) parameters[1]);
                }
                else if (parameters[1].getType().isSourceLocation()) {
                    return firstAmbiguity(name, (ISourceLocation) parameters[1]);
                }
            }
            else {
                if (!parameters[2].getType().isSourceLocation()) {
                    throw fail(parameters); 
                }

                if (parameters[1].getType().isString()) {
                    return parse(name, filters, (IString) parameters[1], (ISourceLocation) parameters[2], allowAmbiguity, allowRecovery, hasSideEffects);
                }
                else if (parameters[1].getType().isSourceLocation()) {
                    return parse(name, filters, (ISourceLocation) parameters[1], (ISourceLocation) parameters[2], allowAmbiguity, allowRecovery, hasSideEffects);
                }
            }

            throw fail(parameters);
        } 
    }
}
