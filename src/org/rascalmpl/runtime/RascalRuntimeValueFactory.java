package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

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
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.parser.ParserGenerator;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IValue;

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
            });
        case 1:
            return new TypedFunctionInstance1<>(new TypedFunction1<IValue,IValue>() {
                @Override
                public IValue typedCall(IValue arg) {
                    return func.apply(new IValue[] { arg }, Collections.emptyMap());
                }
            });
        case 2:
            return new TypedFunctionInstance2<>(new TypedFunction2<IValue,IValue,IValue>() {
                @Override
                public IValue typedCall(IValue arg, IValue arg2) {
                    return func.apply(new IValue[] { arg, arg2 }, Collections.emptyMap());
                }
            });
        case 3:
            return new TypedFunctionInstance3<>(new TypedFunction3<IValue,IValue,IValue,IValue>() {
                @Override
                public IValue typedCall(IValue arg, IValue arg2, IValue arg3) {
                    return func.apply(new IValue[] { arg, arg2, arg3 }, Collections.emptyMap());
                }
            });
        case 4:
            return new TypedFunctionInstance4<>(new TypedFunction4<IValue,IValue,IValue,IValue,IValue>() {
                @Override
                public IValue typedCall(IValue arg, IValue arg2, IValue arg3, IValue arg4) {
                    return func.apply(new IValue[] { arg, arg2, arg3, arg4 }, Collections.emptyMap());
                }
            });
        case 5:
            return new TypedFunctionInstance5<>(new TypedFunction5<IValue,IValue,IValue,IValue,IValue,IValue>() {
                @Override
                public IValue typedCall(IValue arg, IValue arg2, IValue arg3, IValue arg4, IValue arg5) {
                    return func.apply(new IValue[] { arg, arg2, arg3, arg4, arg5 }, Collections.emptyMap());
                }
            });
        }
        
        throw new UnsupportedOperationException("do not support functions with arity higher than 6 yet?");
    }
    
    @Override
    public IFunction parser(IValue reifiedGrammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity) {
        throw new UnsupportedOperationException("TODO");
    }
    
    @Override
    public IFunction parsers(IValue reifiedGrammar, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity) {
        throw new UnsupportedOperationException("TODO");
    }
}
