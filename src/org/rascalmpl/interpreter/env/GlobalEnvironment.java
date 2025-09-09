/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.env;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModule;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.capsule.SetMultimap;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IString;


/**
 * The global environment represents the heap of Rascal.
 * The stack is initialized with a bottom frame, which represent the shell
 * environment.
 * 
 */
public class GlobalEnvironment {
	private final Deque<String> loadStack = new ArrayDeque<>();

	/** The heap of Rascal */
	private final HashMap<String, ModuleEnvironment> moduleEnvironment = new HashMap<String, ModuleEnvironment>();
		
	/** Keeping track of module locations */
	private final HashMap<String, URI> moduleLocations = new HashMap<String,URI>();
	private final HashMap<URI, String> locationModules = new HashMap<URI,String>();
	
	/**
	 * Source location resolvers map user defined schemes to primitive schemes
	 */
	private final HashMap<String, ICallableValue> sourceResolvers = new HashMap<String, ICallableValue>();
	
	private boolean bootstrapper;

	public void clear() {
		moduleEnvironment.clear();
		moduleLocations.clear();
		locationModules.clear();
		sourceResolvers.clear();
	}
	
	/**
	 * Push a module on the load stack and
	 * return true iff its already on the stack.
	 * @param name
	 * @return
	 */
	public boolean pushModuleLoading(String name) {
		var preExists = loadStack.contains(name);
		loadStack.push(name);
		return preExists;
	}

	public String popModuleLoading() {
		return loadStack.pop();
	}

	public boolean onLoadingStack(String name) {
		return loadStack.contains(name);
	}

	/**
	 * Register a source resolver for a specific scheme. Will overwrite the previously
	 * registered source resolver for this scheme.
	 * 
	 * @param scheme   intended be a scheme name without + or :
	 * @param function a Rascal function of type `loc (loc)`
	 */
	public void registerSourceResolver(String scheme, ICallableValue function) {
		sourceResolvers.put(scheme, function);
	}
	
	/**
	 * Allocate a new module on the heap
	 * @param name
	 */
	public ModuleEnvironment addModule(ModuleEnvironment mod) {
		assert mod != null;
		ModuleEnvironment env = moduleEnvironment.get(mod.getName());
		if (env == null) {
			moduleEnvironment.put(mod.getName(), mod);
			return mod;
		}
		else if (env == mod) {
			return mod;
		}
		else {
			throw new ImplementationError("Reinstantiating same module " + mod.getName());
		}
	}
	
	public ModuleEnvironment resetModule(String name) {
		ModuleEnvironment mod = moduleEnvironment.get(name);
		mod.reset();
		return mod;
	}
		
	/**
	 * Retrieve a module from the heap
	 */
	public ModuleEnvironment getModule(String name) {
		return moduleEnvironment.get(name);
	}

	public ModuleEnvironment getModule(QualifiedName name, AbstractAST ast) {
		ModuleEnvironment module = getModule(Names.fullName(name));
		if (module == null) {
			throw new UndeclaredModule(Names.fullName(name), ast);
		}
		return module;
	}
	

	public boolean existsModule(String name) {
		return moduleEnvironment.containsKey(name);
	}

	
	@Override
	public String toString(){
		StringBuffer res = new StringBuffer();
		res.append("heap.modules: ");
		for (String mod : moduleEnvironment.keySet()) {
			res.append(mod + ",");
		}
		return res.toString();
	}

	public void removeModule(ModuleEnvironment env) {
		moduleEnvironment.remove(env.getName());
	}

	public void setModuleURI(String name, URI location) {
		moduleLocations.put(name, location);
		locationModules.put(location, name);
	}
	
	public URI getModuleURI(String name) {
		return moduleLocations.get(name);
	}
	
	public String getModuleForURI(URI location) {
		return locationModules.get(location);
	}

	/**
	 * A utility method for getting the right environment for a qualified name in a certain context.
	 * 
	 * @param current  the current environment
	 * @return current if the given name is not qualifed, otherwise it returns the environment that the qualified name points to
	 */
	public Environment getEnvironmentForName(QualifiedName name, Environment current) {
		if (Names.isQualified(name)) {
			ModuleEnvironment mod = getModule(Names.moduleName(name));
			if (mod == null) {
				throw new UndeclaredModule(Names.moduleName(name), name);
			}
			return mod;
		}

		return current;
	}
	
	public Set<String> getImportingModules(String mod) {
		Set<String> result = new HashSet<>();
		
		for (ModuleEnvironment env : moduleEnvironment.values()) {
			if (env.getImports().contains(mod)) {
				result.add(env.getName());
			}
		}
		
		return result;
	}

	public SetMultimap.Transient<String, String> getExtendGraphFrom(String mod) {
		return getModule(mod).collectExtendsGraph();
	}

	public Set<String> getExtendingModules(String mod) {
		Set<String> result = new HashSet<>();
		Deque<String> todo = new ArrayDeque<>();
		todo.add(mod);
		
		while (!todo.isEmpty()) {
			String next = todo.remove();
			
			for (ModuleEnvironment env : moduleEnvironment.values()) {
				if (env.getExtends().contains(next)) {
					String extending = env.getName();
					
					if (!todo.contains(extending) && !result.contains(extending) /*cuts infinite extend loops*/) {
						// add transitive depending modules
					    todo.addFirst(extending);
						result.add(extending);
					}
				}
			}
			result.add(next);
		}
		
		result.remove(mod);
		return result;
	}
	
	public AbstractFunction getResourceImporter(String resourceScheme) {
		for (ModuleEnvironment menv : this.moduleEnvironment.values()) {
			if (menv.hasImporterForResource(resourceScheme))
				return menv.getResourceImporter(resourceScheme);
		}
		return null;
	}

	public void isBootstrapper(boolean b) {
	    this.bootstrapper = b;
	}

	public boolean isBootstrapper() {
	    return bootstrapper;
	}

	/**
	 * This starts with an edge that is not yet in the graph because we are loading these
	 * modules. Then the rest of graph is loaded from what we already have in memory.
	 * @param parent the first and last node of a detected cycle.
	 * @return
	 */
    public List<String> findCyclicExtendPathFrom(String parent, String child) {
        SetMultimap.Transient<String, String> graph = getExtendGraphFrom(child);
		graph.__put(parent, child); // add the last edge that was not yet registered
		IRascalValueFactory vf = IRascalValueFactory.getInstance();
		return depthFirstSearch(parent, new HashSet<>(), vf.list(vf.string(parent)), graph)
			.stream()
			.map(IString.class::cast)
			.map(IString::getValue)
			.collect(Collectors.toList());
    }

	/*
	 * dfs uses an immutable IList for the path such that we don't have to maintain a stack, next to the 
	 * recursion.
	 * otherwise this is a standard depth-first search for a directed graph
	 */
	public IList depthFirstSearch(String parent, Set<String> visited, IList path,  SetMultimap.Transient<String, String> graph) {
		visited.add(parent);
		var vf = IRascalValueFactory.getInstance();

		for (String child : graph.get(parent)) {
			if (!visited.contains(child)) {
				// a new node but not cyclic (yet) 
				IList result = depthFirstSearch(child, visited, path.append(vf.string(child)), graph);
				if (!result.isEmpty()) {
					// then this is the first cycle we found, so we're done
					return result;
				}
				else {
					continue; // searching with the other nodes
				}
			}
			else if (path.contains(vf.string(child))) {
				// cycle detected, drop the prefix and report it!
				return path.stream().dropWhile(e -> !e.equals(vf.string(child))).collect(vf.listWriter());
			}
			else {
				// already visited but not cyclic
				continue; // with the neighbors
			}
		}

		// no cycle found here
		return vf.list();
	}

	public List<String> nonInitializedModules() {
		return moduleEnvironment.entrySet().stream()
			.filter(e -> !e.getValue().isInitialized())
			.map(e -> e.getKey())
			.collect(Collectors.toList());
	}
}
