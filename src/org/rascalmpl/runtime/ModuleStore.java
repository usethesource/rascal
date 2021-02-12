package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class ModuleStore {
    
	
    /**
     * The actual store of loaded (== imported or extended) modules
     */
    private final ConcurrentMap<Class<?>, Object> loadedModules = new ConcurrentHashMap<>();
    /**
     * Modules for which import is in progress, needed to break import cycles
     */
    private final HashSet<Class<?>> importInProgress = new HashSet<>();

    public ModuleStore() {
	}
    
    /**
     * Initiate the import of a module, taking care of circular imports
     * @param <T> Module type, i.e. the generated interface for the module
     * @param module Class file for module
     * @param builder Constructor for the class
     */
    @SuppressWarnings("unchecked")
    public <T> void importModule(Class<T> module, Function<ModuleStore, T> builder) {
        T result = (T)loadedModules.get(module);
        if (result == null) {
        	if(importInProgress.contains(module)) {
        		return;
        	}
        	importInProgress.add(module);
            // we have to compute and then merge, computeIfAbstent can not be used, as we'll have to use the map during the compute.
            T newResult = builder.apply(this);
            // we merge, most cases we won't get a merge, but if we do, we keep the one in the store
            loadedModules.merge(module, newResult, (a, b) -> (a == newResult) ? b : a);
            importInProgress.remove(module);
        }
    }
    
    /**
     * @param <T> Module type, i.e. the generated interface for the module
     * @param module Class file for module
     * @return the loaded moduled from the store
     */
    @SuppressWarnings("unchecked")
	public <T> T getModule(Class<T> module) {
    	return (T)loadedModules.get(module);
    }

    public <T1, T2> T1 extendModule(Class<T1> module, BiFunction<ModuleStore, T2, T1> builder, T2 extension) {
		return builder.apply(this, extension);
    }
}