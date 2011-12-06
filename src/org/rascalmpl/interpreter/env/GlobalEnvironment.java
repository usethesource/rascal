/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.PatternWithAction;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModuleError;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.parser.gtd.IGTD;
import org.rascalmpl.values.uptr.Factory;


/**
 * The global environment represents the stack and the heap of Rascal.
 * The stack is initialized with a bottom frame, which represent the shell
 * environment.
 * 
 */
public class GlobalEnvironment {
	/** The heap of Rascal */
	private final HashMap<String, ModuleEnvironment> moduleEnvironment = new HashMap<String, ModuleEnvironment>();
		
	/** Normalizing rules are a global feature */
	private final HashMap<Type, List<RewriteRule>> ruleEnvironment = new HashMap<Type, List<RewriteRule>>();
	
	/** Keeping track of module locations */
	private final HashMap<String, URI> moduleLocations = new HashMap<String,URI>();
	private final HashMap<URI, String> locationModules = new HashMap<URI,String>();
	
	/** Keeping track of generated parsers */
	private final HashMap<String,ParserTuple> objectParsersForModules = new HashMap<String,ParserTuple>();
	private final HashMap<String,ParserTuple> rascalParsersForModules = new HashMap<String,ParserTuple>();
	
	public void clear() {
		moduleEnvironment.clear();
		ruleEnvironment.clear();
		moduleLocations.clear();
		locationModules.clear();
	}
	
	/**
	 * Allocate a new module on the heap
	 * @param name
	 */
	public ModuleEnvironment addModule(ModuleEnvironment mod) {
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
		ModuleEnvironment module = getModule(name.toString());
		if (module == null) {
			throw new UndeclaredModuleError(name.toString(), ast);
		}
		return module;
	}
	

	public void storeRule(Type forType, PatternWithAction rule, Environment env) {
		List<RewriteRule> rules = ruleEnvironment.get(forType);
		
		if (!forType.isConstructorType() && !forType.isNodeType() && forType != Factory.Tree && forType != Factory.Tree_Appl) {
			throw new IllegalArgumentException("forType should be a constructor type, instead it is :" + forType);
		}
		
		//System.err.println("storeRule: type=" + forType + ",rule=" + rule);
		if (rules == null) {
			rules = new ArrayList<RewriteRule>();
			ruleEnvironment.put(forType, rules);
		}
		
		rules.add(new RewriteRule(rule, env));
	}
	
	public List<RewriteRule> getRules(Type forType) {
		List<RewriteRule> rules = ruleEnvironment.get(forType);
		//System.err.println("getRules: type=" + forType + ",rules=" + rules);
		return rules != null ? rules : new ArrayList<RewriteRule>();
	}

	
	public List<RewriteRule> getRules() {
		List<RewriteRule> result = new LinkedList<RewriteRule>();
		
		for (Type t : ruleEnvironment.keySet()) {
			result.addAll(ruleEnvironment.get(t));
		}
		
		return result;
	}

	public boolean existsModule(String name) {
		return moduleEnvironment.containsKey(name);
	}

	
	@Override
	public String toString(){
		StringBuffer res = new StringBuffer();
		res.append("==== Module Environment ====\n").append(moduleEnvironment);
		res.append("==== Rule Environment ====\n");
		for(Type type : ruleEnvironment.keySet()){
			res.append(type).append(" : ").append(ruleEnvironment.get(type)).append("\n");
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
				throw new UndeclaredModuleError(Names.moduleName(name), name);
			}
			return mod;
		}

		return current;
	}
	
	public Class<IGTD> getObjectParser(String module, IMap productions) {
		return getParser(objectParsersForModules, module, productions);
	}
	
	public Class<IGTD> getRascalParser(String module, IMap productions) {
		return getParser(rascalParsersForModules, module, productions);
	}
	
	/**
	 * Retrieves a parser for a module.
	 * 
	 * @param module
	 * @param productions
	 */
	private Class<IGTD> getParser(Map<String,ParserTuple> store, String module, IMap productions) {
		ParserTuple parser = store.get(module);
		if(parser != null && parser.getProductions().isEqual(productions)) {
			return parser.getParser();
		}
		
		return null;
	}
	
	public void storeObjectParser(String module, IMap productions, Class<IGTD> parser) {
		storeParser(objectParsersForModules, module, productions, parser);
	}
	
	public void storeRascalParser(String module, IMap productions, Class<IGTD> parser) {
		storeParser(rascalParsersForModules, module, productions, parser);
	}
	
	private static void storeParser(HashMap<String, ParserTuple> store, String module, IMap productions, Class<IGTD> parser) {
		ParserTuple newT = new ParserTuple(productions, parser);
		store.put(module, newT);
	}
	
	public Set<String> getImportingModules(String mod) {
		Set<String> result = new HashSet<String>();
		
		for (ModuleEnvironment env : moduleEnvironment.values()) {
			if (env.getImports().contains(mod)) {
				result.add(env.getName());
			}
		}
		
		return result;
	}
	
	public Set<String> getExtendingModules(String mod) {
		Set<String> result = new HashSet<String>();
		List<String> todo = new LinkedList<String>();
		todo.add(mod);
		
		while (!todo.isEmpty()) {
			String next = todo.remove(0);
			
			for (ModuleEnvironment env : moduleEnvironment.values()) {
				if (env.getExtends().contains(next)) {
					if (!result.contains(next)) {
						todo.add(env.getName());
						result.add(next);
						todo.removeAll(result);
					}
				}
			}
			result.add(next);
		}
		
		result.remove(mod);
		return result;
	}
	
	private static class ParserTuple {
		private final IMap production;
		private final Class<IGTD> parser;

		public ParserTuple(IMap productions, Class<IGTD> parser) {
			this.production = productions;
			this.parser = parser;
		}
		
		public IMap getProductions() {
			return production;
		}
		
		public Class<IGTD> getParser() {
			return parser;
		}
	}
}
