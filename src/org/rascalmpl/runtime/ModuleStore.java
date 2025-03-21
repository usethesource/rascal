/*
 * Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
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
package org.rascalmpl.runtime;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * ModuleStore is a central store for all modules in a compiled Rascal application
 * and ensures that only a single copy of each module is created.
 *
 */
public final class ModuleStore {
	
    /**
     * The actual store of loaded (== imported or extended) modules
     */
    private final ConcurrentMap<Class<?>, Object> importedModules = new ConcurrentHashMap<>();
    private final ConcurrentMap<Entry<Class<?>, Class<?>>, Object> extendedModules = new ConcurrentHashMap<>();
    
    /**
     * Modules for which import is in progress, needed to break import cycles
     */
    private final HashSet<Class<?>> importInProgress = new HashSet<>();

    public ModuleStore() {
	}
    
    /**
     * Implement the import of a module, taking care of circular imports
     * @param <T> Module type, i.e. the generated interface for the module
     * @param module Class file for module
     * @param rex The RascalExecutionContext to be used
     * @param builder Constructor for the class
     */
    @SuppressWarnings("unchecked")
    public <T> void importModule(Class<T> module, RascalExecutionContext rex, Function<RascalExecutionContext, T> builder) {
    	T result = (T)importedModules.get(module);
        if (result == null) {
        	if(importInProgress.contains(module)) {
        		return;
        	}
        	importInProgress.add(module);
            // we have to compute and then merge, computeIfAbsent can not be used, as we'll have to use the map during the compute.
            T newResult = builder.apply(rex);
            // we merge, most cases we won't get a merge, but if we do, we keep the one in the store
            importedModules.merge(module, newResult, (a, b) -> (a == newResult) ? b : a);
            importInProgress.remove(module);
        }
    }
    
    /**
     * @param <T> Module type, i.e. the generated interface for the module
     * @param module Class file for module
     * @return the loaded module from the store
     */
	public <T> T getModule(Class<T> module) {
    	@SuppressWarnings("unchecked")
		T newResult = (T)importedModules.get(module);
    	assert newResult != null;
    	return newResult;
    }

	 /**
     * Implement the extension of a module.
     * @param <Base> Type of the base module, i.e. the generated interface for that module
     * @param <Extension> Type of the extending module, i.e. the generated interface for that module
     * @param baseModule Class file for the base module
     * @param rex RascalExecutionContext to be used
     * @param builder Constructor for the class
     * @param extension The extending class
     */
	@SuppressWarnings("unchecked")
    public <Base, Extension> Base extendModule(Class<Base> baseModule, RascalExecutionContext rex, BiFunction<RascalExecutionContext, Object, Base> builder, Object extension) {
    	Entry<Class<?>, Class<?>> entry = new AbstractMap.SimpleEntry<Class<?>,Class<?>> (baseModule, extension.getClass());
    	Extension ext =  (Extension) extendedModules.get(entry);
    	if (ext == null) {
    		Extension newResult =  (Extension) builder.apply(rex, (Extension)extension);
    		entry = new AbstractMap.SimpleEntry<Class<?>,Class<?>> (baseModule, extension.getClass());
    		extendedModules.put(entry, newResult);
            return (Base)newResult;
    	} else {
    		return (Base) ext;
    	}
    }
    
    public boolean notPresent(Class<?> module) {
    	return importedModules.get(module) == null && !importInProgress.contains(module);
    }

	public <T> void put(Class<T> module, T c) {
		importedModules.put(module, c);
	}
}