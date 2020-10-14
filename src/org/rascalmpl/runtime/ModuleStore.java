package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class ModuleStore {
    
    private final ConcurrentMap<Class<?>, Object> loadedModules = new ConcurrentHashMap<>();

    public ModuleStore() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
    public <T> T importModule(Class<T> module, Function<ModuleStore, T> builder) {
        T result = (T)loadedModules.get(module);
        if (result == null) {
            // we have to compute and then merge, computeIfAbstent can not be used, as we'll have to use the map during the compute.
            T newResult = builder.apply(this);
            // we merge, most cases we won't get a merge, but if we do, we keep the one in the store
            return (T)loadedModules.merge(module, newResult, (a, b) -> (a == newResult) ? b : a);
        }
        return result;
    }

    public <T1, T2> T1 extendModule(Class<T1> module, BiFunction<ModuleStore, T2, T1> builder, T2 extension) {
		return builder.apply(this, extension);
    }
}