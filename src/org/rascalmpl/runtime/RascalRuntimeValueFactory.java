package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;

import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction0;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction1;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction2;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction3;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction4;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunction5;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance0;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance1;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance2;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance3;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance4;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.TypedFunctionInstance5;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.Configuration;
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

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class RascalRuntimeValueFactory extends RascalValueFactory {
    private final $RascalModule module;
    private ParserGenerator generator;

    public RascalRuntimeValueFactory($RascalModule currentModule) {
        this.module = currentModule;
    }

    private ParserGenerator getParserGenerator() {
        if (this.generator == null) {
            this.generator = new ParserGenerator(module.$MONITOR, module.$OUT, Collections.singletonList(module.getClass().getClassLoader()), this, new Configuration());
        }
        
        return generator;
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
        }
        
        throw new UnsupportedOperationException("do not support functions with arity higher than 6 yet?");
    }
    
    @Override
    public IFunction parser(IValue reifiedGrammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity) {
        TypeFactory tf = TypeFactory.getInstance();
        
        // the return type of the generated parse function is instantiated here to the start nonterminal of
        // the provided grammar:
        Type functionType = tf.functionType(reifiedGrammar.getType().getTypeParameters().getFieldType(0),
            tf.tupleType(tf.valueType(), tf.sourceLocationType()), 
            tf.tupleEmpty());
        
        return function(functionType, new ParseFunction(getParserGenerator(), module.$MONITOR, this, reifiedGrammar, allowAmbiguity, hasSideEffects, firstAmbiguity));
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
        
        return function(functionType, new ParametrizedParseFunction(getParserGenerator(), module.$MONITOR, this, reifiedGrammar, allowAmbiguity, hasSideEffects, firstAmbiguity));
    }
    
    /**
     * This class wraps the parseObject methods of this factory by presenting it them as an implementation of IFunction.
     * In this way library builtins can use the embedded parser generator functionalitywithout knowing about
     * the internals of parser generation and parser caching.
     */
    static private class ParseFunction implements BiFunction<IValue[], Map<String, IValue>, IValue> {
        protected final ParserGenerator generator;
        protected final IRascalMonitor monitor;
        protected final IValue grammar;
        protected final IValueFactory vf;
        protected final boolean allowAmbiguity;
        protected final boolean hasSideEffects;
        protected final boolean firstAmbiguity;
        
        public ParseFunction(ParserGenerator generator, IRascalMonitor monitor, IRascalValueFactory vf, IValue grammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity) {
            this.generator = generator;
            this.monitor = monitor;
            this.vf = vf;
            this.grammar = grammar;
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
                    return firstAmbiguity(grammar, (IString) parameters[0], generator);
                }
                else if (parameters[0].getType().isSourceLocation()) {
                    return firstAmbiguity(grammar, (ISourceLocation) parameters[0], generator);
                }
            }
            else {
                if (!parameters[1].getType().isSourceLocation()) {
                    throw fail(parameters); 
                }

                if (parameters[0].getType().isString()) {
                    return parse(grammar, (IString) parameters[0], (ISourceLocation) parameters[1], allowAmbiguity, hasSideEffects, generator);
                }
                else if (parameters[0].getType().isSourceLocation()) {
                    return parse(grammar, (ISourceLocation) parameters[0], (ISourceLocation) parameters[1], allowAmbiguity, hasSideEffects, generator);
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
                return parseObject(grammar, input.getValue(), URIUtil.rootLocation("unknown"), false, false);
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
                return parseObject(grammar, input, false, false);
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

        protected IValue parse(IValue start, IString input, ISourceLocation origin, boolean allowAmbiguity, boolean hasSideEffects, ParserGenerator generator) {
            Type reified = start.getType();
            IConstructor grammar = checkPreconditions(start, reified);
            
            if (origin == null) {
                origin = URIUtil.rootLocation("unknown");
            }
            
            try {
                return parseObject(grammar, input.getValue(), origin, allowAmbiguity, hasSideEffects);
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
        
        protected IValue parse(IValue start, ISourceLocation input, ISourceLocation origin, boolean allowAmbiguity, boolean hasSideEffects, ParserGenerator generator) {
            Type reified = start.getType();
            IConstructor grammar = checkPreconditions(start, reified);
            
            if (origin == null) {
                origin = input;
            }
            
            try {
                return parseObject(grammar, input, allowAmbiguity, hasSideEffects);
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
        
        private ITree parseObject(IConstructor grammar, ISourceLocation location, char[] input,  boolean allowAmbiguity, boolean hasSideEffects) {
            IConstructor startSort = (IConstructor) grammar.get("symbol");
            IGTD<IConstructor, ITree, ISourceLocation> parser = getObjectParser(generator, (IMap) grammar.get("definitions"));
            String name = "";
            if (SymbolAdapter.isStartSort(startSort)) {
                name = "start__";
                startSort = SymbolAdapter.getStart(startSort);
            }

            if (SymbolAdapter.isSort(startSort) || SymbolAdapter.isLex(startSort) || SymbolAdapter.isLayouts(startSort)) {
                name += SymbolAdapter.getName(startSort);
            }

            // TODO: here should be an Action executor which can call generated functions to normalize and
            // filter the produced tree.
            IActionExecutor<ITree> exec = new NoActionExecutor();

            return (ITree) parser.parse(name, location.getURI(), input, exec, new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(allowAmbiguity), (IRecoverer<IConstructor>) null);
        }
        
        private IConstructor parseObject(IConstructor startSort, ISourceLocation location,  boolean allowAmbiguity, boolean hasSideEffects){
            try {
                char[] input = getResourceContent(location);
                return parseObject(startSort, location, input, allowAmbiguity, hasSideEffects);
            }
            catch (IOException e) {
                throw RuntimeExceptionFactory.io(vf.string(e.getMessage()));
            }
        }

        private IConstructor parseObject(IConstructor startSort, String input, ISourceLocation loc,  boolean allowAmbiguity, boolean hasSideEffects) {
            return parseObject(startSort, loc, input.toCharArray(), allowAmbiguity, hasSideEffects);
        }

        // TODO: implement parser cache!
        private IGTD<IConstructor, ITree, ISourceLocation> getObjectParser(ParserGenerator generator, IMap iMap) {
            // TODO: is this classname ok?
            Class<IGTD<IConstructor, ITree, ISourceLocation>> parser = generator.getNewParser(monitor, URIUtil.rootLocation("parser-generator"), "$ANY_NAME$", iMap);
            try {
                return parser.newInstance();
            } catch (InstantiationException e) {
                throw new ImplementationError(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                throw new ImplementationError(e.getMessage(), e);
            } catch (ExceptionInInitializerError e) {
                throw new ImplementationError(e.getMessage(), e);
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
        
        private char[] getResourceContent(ISourceLocation location) throws IOException{
            char[] data;
            Reader textStream = null;

            try {
                textStream = URIResolverRegistry.getInstance().getCharacterReader(location);
                data = InputConverter.toChar(textStream);
            }
            finally{
                if(textStream != null){
                    textStream.close();
                }
            }

            return data;
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
        
        public ParametrizedParseFunction(ParserGenerator generator, IRascalMonitor monitor, IRascalValueFactory vf, IValue grammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity) {
            super(generator, monitor, vf, grammar, allowAmbiguity, hasSideEffects, firstAmbiguity);
        }
        
        @Override
        public IValue apply(IValue[] parameters, Map<String, IValue> keywordParameters) {
            if (parameters.length != 3) {
                throw fail(parameters);
            }

            if (firstAmbiguity) {
                if (parameters[1].getType().isString()) {
                    return firstAmbiguity(parameters[0], (IString) parameters[1], generator);
                }
                else if (parameters[1].getType().isSourceLocation()) {
                    return firstAmbiguity(parameters[0], (ISourceLocation) parameters[1], generator);
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
                    return parse(parameters[0], ((IString) parameters[1]), (ISourceLocation) parameters[2], allowAmbiguity, hasSideEffects, generator);
                }
                else if (parameters[1].getType().isSourceLocation()) {
                    return parse(parameters[0], (ISourceLocation) parameters[1], (ISourceLocation) parameters[2], allowAmbiguity, hasSideEffects, generator);
                }
            }

            throw fail(parameters);
        } 
    }
}
