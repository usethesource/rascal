package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.util.Map;
import java.util.function.BiFunction;

import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IValue;

public class RascalRuntimeValueFactory extends RascalValueFactory {
    private final $RascalModule module;

    public RascalRuntimeValueFactory($RascalModule currentModule) {
        this.module = currentModule;
    }

    @Override
    public IFunction function(io.usethesource.vallang.type.Type functionType,
            BiFunction<IValue[], Map<String, IValue>, IValue> func) {
        throw new UnsupportedOperationException("TODO:" + module);
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
