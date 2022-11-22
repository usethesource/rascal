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
 *   * Emilie Balland - (CWI)
 *   * Anya Helene Bagge - (UiB)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.env;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ConstructorFunction;
import org.rascalmpl.interpreter.result.OverloadedFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModule;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.types.NonTerminalType;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/**
 * A module environment represents a module object (i.e. a running module).
 * It manages imported modules and visibility of the
 * functions and variables it declares. 
 * 
 * TODO: add management of locally declared types and constructors
 * 
 */
public class ModuleEnvironment extends Environment {
	protected final GlobalEnvironment heap;
	protected Set<String> importedModules;
	protected Set<String> extended;
	protected TypeStore typeStore;
	protected Set<IValue> productions;
	protected Map<Type, List<KeywordFormal>> generalKeywordParameters;
	protected Map<String, NonTerminalType> concreteSyntaxTypes;
	private boolean initialized;
	private boolean syntaxDefined;
	private boolean bootstrap;
	private String deprecated;
	protected Map<String, AbstractFunction> resourceImporters;
	
	protected static final TypeFactory TF = TypeFactory.getInstance();

	public final static String SHELL_MODULE = "$shell$";
	
	public ModuleEnvironment(String name, GlobalEnvironment heap) {
		super(ValueFactoryFactory.getValueFactory().sourceLocation(URIUtil.assumeCorrect("main", name, "")), name);
		this.heap = heap;
		this.importedModules = new HashSet<String>();
		this.concreteSyntaxTypes = new HashMap<String, NonTerminalType>();
		this.productions = new HashSet<IValue>();
		this.generalKeywordParameters = new HashMap<Type,List<KeywordFormal>>();
		this.typeStore = new TypeStore();
		this.initialized = false;
		this.syntaxDefined = false;
		this.bootstrap = false;
		this.resourceImporters = new HashMap<String, AbstractFunction>();
	}
	
	/**
	 * This constructor creates a shallow copy of the given environment
	 * 
	 * @param env
	 */
	protected ModuleEnvironment(ModuleEnvironment env) {
		super(env);
		this.heap = env.heap;
		this.importedModules = env.importedModules;
		this.concreteSyntaxTypes = env.concreteSyntaxTypes;
		this.productions = env.productions;
		this.typeStore = env.typeStore;
		this.initialized = env.initialized;
		this.syntaxDefined = env.syntaxDefined;
		this.bootstrap = env.bootstrap;
		this.resourceImporters = env.resourceImporters;
		this.deprecated = env.deprecated;
	}

	@Override
	public void reset() {
		super.reset();
		this.importedModules = new HashSet<String>();
		this.concreteSyntaxTypes = new HashMap<String, NonTerminalType>();
		this.typeStore = new TypeStore();
		this.productions = new HashSet<IValue>();
		this.initialized = false;
		this.syntaxDefined = false;
		this.bootstrap = false;
		this.extended = new HashSet<String>();
		this.deprecated = null;
	}
	
	public void extend(ModuleEnvironment other) {
//	  super.extend(other);
		extendNameFlags(other);
		  
	  // First extend the imports before functions and variables
      // so that types become available
	  if (other.importedModules != null) {
	    if (this.importedModules == null) {
	      this.importedModules = new HashSet<String>();
	    }
	    this.importedModules.addAll(other.importedModules);
	  }
	  
	  if (other.concreteSyntaxTypes != null) {
	    if (this.concreteSyntaxTypes == null) {
	      this.concreteSyntaxTypes = new HashMap<String,NonTerminalType>();
	    }
	    this.concreteSyntaxTypes.putAll(other.concreteSyntaxTypes);
	  }
	  
	  if (other.typeStore != null) {
	    if (this.typeStore == null) {
	      this.typeStore = new TypeStore();
	    }
	    this.typeStore.extendStore(other.typeStore);
	  }
	  
	  if (other.productions != null) {
	    if (this.productions == null) {
	      this.productions = new HashSet<IValue>();
	    }
	    this.productions.addAll(other.productions);
	  }
	  

	  if (other.extended != null) {
	    if (this.extended == null) {
	      this.extended = new HashSet<String>();
	    }
	    this.extended.addAll(other.extended);
	  }
	  
	  if (other.generalKeywordParameters != null) {
		  if (this.generalKeywordParameters == null) {
			  this.generalKeywordParameters = new HashMap<>();
		  }
		  this.generalKeywordParameters.putAll(other.generalKeywordParameters);
	  }
	  
	  extendTypeParams(other);
	  extendVariableEnv(other);
	  extendFunctionEnv(other);
	  
	  this.initialized &= other.initialized;
	  this.syntaxDefined |= other.syntaxDefined;
	  this.bootstrap |= other.bootstrap;
	  
	  addExtend(other.getName());
	}
	
	
	
	@Override
	public GlobalEnvironment getHeap() {
		return heap;
	}
	
	public boolean isSyntaxDefined() {
		return syntaxDefined;
	}
	
	public void setSyntaxDefined(boolean val) {
		this.syntaxDefined = val;
	}
	
	public void declareProduction(IConstructor sd) {
	  productions.add(sd);
	}
	
	public void clearProductions() {
		if (productions != null) {
			productions.clear();
		}
	}
	
	public boolean definesSyntax() {
		if (!productions.isEmpty()) {
			return true;
		}
		
		for (String mod : getExtendsTransitive()) {
			ModuleEnvironment env = heap.getModule(mod);
			if (env != null) {
				if (!env.productions.isEmpty()) {
					return true;
				}
			}
		}
		
		for(String mod : getImportsTransitive()){
			ModuleEnvironment env = heap.getModule(mod);
			if (env != null) {
				if (!env.productions.isEmpty()) {
					return true;
				}
			}
		}

		return false;
	}
	/** 
	 * Builds a map to communicate all relevant syntax definitions to the parser generator.
	 * See lang::rascal::grammar::definition::Modules.modules2grammar()
	 */
	public IMap getSyntaxDefinition() {
		List<String> todo = new LinkedList<String>();
		Set<String> done = new HashSet<String>();
		todo.add(getName());
		
		IValueFactory VF = ValueFactoryFactory.getValueFactory();
		IMapWriter result = VF.mapWriter();
		
		while(!todo.isEmpty()){
			String m = todo.get(0);
			todo.remove(0);
			
			if(done.contains(m)) 
			  continue;
			
			done.add(m);
			
			/* This allows the current module not to be loaded on the heap, for
			 * parsing in the IDE
			 */
			ModuleEnvironment env = m.equals(getName()) ? this : heap.getModule(m);
			
			if(env != null){
				ISetWriter importWriter = VF.setWriter();
				for(String impname : env.getImports()){
					if(!done.contains(impname)) todo.add(impname);
					
					importWriter.insert(VF.string(impname));
				}
				
				ISetWriter extendWriter = VF.setWriter();
				for(String impname : env.getExtends()){
					if(!done.contains(impname)) todo.add(impname);
					
					extendWriter.insert(VF.string(impname));
				}
				
				ISetWriter defWriter = VF.setWriter();
				for(IValue def : env.productions){
					defWriter.insert(def);
				}
				
				ITuple t = VF.tuple(importWriter.done(), extendWriter.done(), defWriter.done());
				result.put(VF.string(m), t);
			}else if(m.equals(getName())) { // This is the root scope.
				ISetWriter importWriter = VF.setWriter();
				for(String impname : importedModules){
					if(!done.contains(impname)) todo.add(impname);
					
					importWriter.insert(VF.string(impname));
				}
				
				ISetWriter extendWriter = VF.setWriter();
				for(String impname : getExtends()){
					if(!done.contains(impname)) todo.add(impname);
					
					extendWriter.insert(VF.string(impname));
				}
				
				ISetWriter defWriter = VF.setWriter();
				for(IValue def : productions){
					defWriter.insert(def);
				}
				
				ITuple t = VF.tuple(importWriter.done(), extendWriter.done(), defWriter.done());
				result.put(VF.string(m), t);
			}
		}
		
		
		return result.done();
	}
	
	public boolean isModuleEnvironment() {
		return true;
	}
	
	public void addImport(String name, ModuleEnvironment env) {
		assert heap.getModule(name).equals(env);
		importedModules.add(name);
		typeStore.importStore(env.typeStore);
	}
	
	public void addExtend(String name) {
		if (extended == null) {
			extended = new HashSet<String>();
		}
		extended.add(name);
	}
	
	public List<AbstractFunction> getTests() {
		List<AbstractFunction> result = new LinkedList<AbstractFunction>();
		
		if (functionEnvironment != null) {
			for (List<AbstractFunction> f : functionEnvironment.values()) {
				for (AbstractFunction c : f) {
					if (c.isTest()) {
						result.add(c);
					}
				}
			}
		}
		
		return result;
	}
	
	@Override
	public Set<String> getImports() {
		return Collections.unmodifiableSet(importedModules);
	}
	
	public Set<String> getImportsTransitive() {
		List<String> todo = new LinkedList<String>();
		Set<String> done = new HashSet<String>();
		Set<String> result = new HashSet<String>();
		todo.add(this.getName());
		GlobalEnvironment heap = getHeap();
		
		while (!todo.isEmpty()) {
		   String mod = todo.remove(0);	
		   done.add(mod);
		   ModuleEnvironment env = mod.equals(getName())? this : heap.getModule(mod);
		   if (env != null) {
			  for (String e : env.getImports()) {
				  result.add(e);
				  if (!done.contains(e)) {
					  todo.add(e);
				  }
			  }
		   }
		}
		
		return result;
	}
	
	public void unImport(String moduleName) {
		if(importedModules.remove(moduleName)) {
			ModuleEnvironment old = heap.getModule(moduleName);
			if (old != null) {
				typeStore.unimportStores(new TypeStore[] { old.getStore() });
			}
		}
	}
	
	public void unExtend(String moduleName) {
		extended.remove(moduleName);
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public TypeStore getStore() {
		return typeStore;
	}
	
	@Override
	public Result<IValue> getVariable(QualifiedName name) {
		String modulename = Names.moduleName(name);
		
		String cons = Names.name(Names.lastName(name));
		Type adt = getAbstractDataType(modulename);
		
		if (adt != null) {
			List<AbstractFunction> result = new LinkedList<AbstractFunction>();
			getAllFunctions(adt, cons, result);
			
			if (result.isEmpty()) {
				return null;
			}
			
			if (result.size() == 1) {
				return result.get(0);
			}
			else {
				return new OverloadedFunction(cons, result);
			}
		}
		
		if (modulename != null) {
			if (modulename.equals(getName())) {
				return getFrameVariable(cons);
			}
			
			ModuleEnvironment imported = getImport(modulename);
			if (imported == null) {
				throw new UndeclaredModule(modulename, name);
			}
			
			// TODO: will this not do a transitive closure? This should not happen...
			return imported.getVariable(name);
		}
		
		return getFrameVariable(cons);
	}
	
	@Override
	public void storeVariable(String name, Result<IValue> value) {
//		if (value instanceof AbstractFunction) {
//			storeFunction(name, (AbstractFunction) value);
//			return;
//		}
		
		Result<IValue> result = super.getFrameVariable(name);
		
		if (result != null) {
			super.storeVariable(name, value);
		}
		else {
			for (String i : importedModules) {
				ModuleEnvironment module = heap.getModule(i);
				result = module.getLocalPublicVariable(name);

				if (result != null) {
					module.storeVariable(name, value);
					return;
				}
			}
			
			super.storeVariable(name, value);
		}
	}
	
	@Override
	public org.rascalmpl.interpreter.result.Result<IValue> getSimpleVariable(String name) {
		Result<IValue> var = super.getSimpleVariable(name);
		
		if (var != null) {
			return var;
		}
		
		for (String moduleName : importedModules) {
			ModuleEnvironment mod = getImport(moduleName);
			
			if (mod != null) { 
			  var = mod.getLocalPublicVariable(name);
			}
			
			if (var != null) {
				return var;
			}
		}

		return null;
	}

	/**
	 * Search for the environment that declared a variable.
	 */
	@Override
	protected Map<String,Result<IValue>> getVariableDefiningEnvironment(String name) {
		if (variableEnvironment != null) {
			Result<IValue> r = variableEnvironment.get(name);

			if (r != null) {
				return variableEnvironment;
			}
		}

		for (String moduleName : importedModules) {
			ModuleEnvironment mod = getImport(moduleName);
			Result<IValue> r = null;
			if (mod != null && mod.variableEnvironment != null) 
				r = mod.variableEnvironment.get(name);
			
			if (r != null && !mod.isNamePrivate(name)) {
				return mod.variableEnvironment;
			}
		}

		return null;
	}
	
	@Override
	public void getAllFunctions(String name, List<AbstractFunction> collection) {
		super.getAllFunctions(name, collection);
		
		for (String moduleName : importedModules) {
			ModuleEnvironment mod = getImport(moduleName);
			
			if (mod != null) {
			  mod.getLocalPublicFunctions(name, collection);
			}
		}
	}
	
	@Override
	public void getAllFunctions(Type returnType, String name, List<AbstractFunction> collection) {
		super.getAllFunctions(returnType, name, collection);
		
		for (String moduleName : importedModules) {
			ModuleEnvironment mod = getImport(moduleName);
			
			if (mod != null) {
			  mod.getLocalPublicFunctions(returnType, name, collection);
			}
		}
	}
	

	
	
	private Result<IValue> getLocalPublicVariable(String name) {
		Result<IValue> var = null;
		
		if (variableEnvironment != null) {
			var = variableEnvironment.get(name);
		}
		
		if (var != null && !isNamePrivate(name)) {
			return var;
		}
		
		return null;
	}
	
	private void getLocalPublicFunctions(String name, List<AbstractFunction> collection) {
		if (functionEnvironment != null) {
			List<AbstractFunction> lst = functionEnvironment.get(name);
			if (lst != null) {
				if (!isNamePrivate(name)) {
					collection.addAll(lst);
				}
			}
		}
	}
	
	private void getLocalPublicFunctions(Type returnType, String name, List<AbstractFunction> collection) {
		if (functionEnvironment != null && !isNamePrivate(name)) {
			List<AbstractFunction> lst = functionEnvironment.get(name);
			
			if (lst != null) {
				for (AbstractFunction func : lst) {
					if (returnType.isSubtypeOf(func.getReturnType())) {
						collection.add(func);
					}
				}
			}
		}
	}

	@Override
	public Type abstractDataType(String name, Type... parameters) {
		return TF.abstractDataType(typeStore, name, parameters);
	}
	
	@Override
	public Type concreteSyntaxType(String name, IConstructor symbol) {
		NonTerminalType sort = (NonTerminalType) RascalTypeFactory.getInstance().nonTerminalType(symbol);
		concreteSyntaxTypes.put(name, sort);
		return sort;
	}
	
	private Type makeTupleType(Type adt, String name, Type tupleType) {
	  return TF.constructorFromTuple(typeStore, adt, name, tupleType);
	}
	
	@Override
	public ConstructorFunction constructorFromTuple(AbstractAST ast, Evaluator eval, Type adt, String name, Type tupleType, List<KeywordFormal> initializers) {
		Type cons = makeTupleType(adt, name, tupleType);
		ConstructorFunction function = new ConstructorFunction(ast, eval, this, cons, initializers);
		storeFunction(name, function);
		markNameFinal(name);
		markNameOverloadable(name);
		return function;
	}
	
//	@Override
//	public ConstructorFunction constructor(AbstractAST ast, Evaluator eval, Type nodeType, String name,
//			Map<String, Type> kwArgs, Map<String, IValue> kwDefaults, Object... childrenAndLabels) {
//		Type cons = TF.constructor(typeStore, nodeType, name, childrenAndLabels, kwArgs, kwDefaults);
//		ConstructorFunction function = new ConstructorFunction(ast, eval, this, cons);
//		storeFunction(name, function);
//		markNameFinal(name);
//		markNameOverloadable(name);
//		return function;
//	}
	
	@Override
	public Type aliasType(String name, Type aliased, Type... parameters) {
		return TF.aliasType(typeStore, name, aliased, parameters);
	}
	
	@Override
	public void declareAnnotation(Type onType, String label, Type valueType) {
	    // TODO: simulating annotations still here
	    if (RascalValueFactory.isLegacySourceLocationAnnotation(onType, label)) {
	        label = RascalValueFactory.Location;
	    }
		typeStore.declareKeywordParameter(onType, label, valueType);
	}
	
	private boolean keywordFormalExists(List<KeywordFormal> haystack, KeywordFormal needle) {
	    String label = ((Name.Lexical) needle.getName()).getString();
	    
	    for (KeywordFormal candidate : haystack) {
	        if (((Name.Lexical) candidate.getName()).getString().equals(label)) {
	            return true;
	        }
	    }
	    
	    return false;
	}
	
	@Override
	public void declareGenericKeywordParameters(Type adt, Type kwTypes, List<KeywordFormal> formals) {
		List<KeywordFormal> list = generalKeywordParameters.get(adt);
		if (list == null) {
			list = new LinkedList<KeywordFormal>();
			generalKeywordParameters.put(adt, list);
		}

		// due to the `extend` feature we might redeclare many formals, so this loop is to avoid duplicate declaration.
		// it is important to retain the declaration order, due to the way default expressions can depend on previously declared formals.
		// this is why the list is a list and not a possibly faster set. 
		for (KeywordFormal f : formals) {
		    if (!keywordFormalExists(list, f)) {
		        list.add(f);
		    }
		}
		
		for (String label : kwTypes.getFieldNames()) {
			typeStore.declareKeywordParameter(adt, label, kwTypes.getFieldType(label));
		}
	}
	
	@Override
	public Map<String, Type> getKeywordParameterTypes(Type ontype) {
		return typeStore.getKeywordParameters(ontype);
	}
	
	public static class GenericKeywordParameters {
		// kw params with default expressions:
		final List<KeywordFormal> formals;
		// environment in which they are declared:
		final ModuleEnvironment env; 
		final Map<String, Type> types;
		
		public GenericKeywordParameters(ModuleEnvironment env, List<KeywordFormal> formals, Map<String,Type> types) {
			this.env = env;
			this.formals = formals;
			this.types = types;
		}
		
		public Map<String, Type> getTypes() {
			return types;
		}
		
		public ModuleEnvironment getEnv() {
			return env;
		}
		
		public List<KeywordFormal> getFormals() {
			return formals;
		}
	}
	
	@Override
	public Set<GenericKeywordParameters> lookupGenericKeywordParameters(Type adt) {
		Set<GenericKeywordParameters> result = new HashSet<>();
		List<KeywordFormal> list = generalKeywordParameters.get(adt);
		if (list != null) {
			result.add(new GenericKeywordParameters(this, list, getStore().getKeywordParameters(adt)));
		}
		
		for (String moduleName : importedModules) {
			ModuleEnvironment mod = getImport(moduleName);
			
			list = mod.generalKeywordParameters.get(adt);
			if (list != null) {
				result.add(new GenericKeywordParameters(mod, list, mod.getStore().getKeywordParameters(adt)));
			}
		}
		
		return result;
	}
	
	
	@Override
	public void declareConstructorKeywordParameter(Type onType, String label, Type valueType) {
		typeStore.declareKeywordParameter(onType, label, valueType);
	}
	
	@Override
	public Type getAnnotationType(Type type, String label) {
		Type anno = typeStore.getKeywordParameterType(type, label);
		if (anno == null && type instanceof NonTerminalType) {
			return typeStore.getKeywordParameterType(RascalValueFactory.Tree, label);
		}
		return anno;
	}

	public Collection<Type> getAbstractDatatypes() {
		return typeStore.getAbstractDataTypes();
	}

	public Collection<Type> getAliases() {
		return typeStore.getAliases();
	}

	public Map<Type, Map<String, Type>> getAnnotations() {
	    // TODO: simulating annotations here
		return typeStore.getKeywordParameters();
	}

	@Override
	public Type getAbstractDataType(String sort) {
		return typeStore.lookupAbstractDataType(sort);
	}
	
	@Override
	public Type getConstructor(String cons, Type args) {
		return typeStore.lookupFirstConstructor(cons, args);
	}
	
	@Override
	public Type getConstructor(Type sort, String cons, Type args) {
		return typeStore.lookupConstructor(sort, cons, args);
	}
	
	@Override
	public boolean isTreeConstructorName(QualifiedName name, Type signature) {
		java.util.List<Name> names = name.getNames();
		
		if (names.size() > 1) {
			String sort = Names.sortName(name);
			Type sortType = getAbstractDataType(sort);
			
			if (sortType != null) {
				String cons = Names.consName(name);
				
				if (getConstructor(sortType, cons, signature) != null) {
					return true;
				}
			}
		}
		else {
			String cons = Names.consName(name);
			if (getConstructor(cons, signature) != null) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "Environment [ " + getName() + ", imports: " + ((importedModules != null) ? importedModules : "") + ", extends: " + ((extended != null) ? extended : "") + "]"; 
	}

	
	
	@Override
	public ModuleEnvironment getImport(String moduleName) {
		if(importedModules.contains(moduleName)) {
			return heap.getModule(moduleName);
		}
		else {
			return null;    
		}
	}
	
	@Override
	public void storeVariable(QualifiedName name, Result<IValue> result) {
		String modulename = Names.moduleName(name);
		
		if (modulename != null) {
			if (modulename.equals(getName())) {
				storeVariable(Names.name(Names.lastName(name)), result);
				return;
			}
			
			ModuleEnvironment imported = getImport(modulename);
			if (imported == null) {
				throw new UndeclaredModule(modulename, name);
			}
			
			imported.storeVariable(name, result);
			return;
		}
		super.storeVariable(name, result);
	}
	
	@Override
	public boolean declaresAnnotation(Type type, String label) {
	    // TODO: we simulate annotations using kw fields here
		return typeStore.getKeywordParameterType(type, label) != null;
	}
	
	@Override
	public Type lookupAbstractDataType(String name) {
		return typeStore.lookupAbstractDataType(name);
	}
	
	@Override
	public Type lookupConcreteSyntaxType(String name) {
		Type type = concreteSyntaxTypes.get(name);
		
		if (type == null) {
			for (String i : importedModules) {
				ModuleEnvironment mod = getImport(i);
				
				if (mod == null) {
				  continue;
				}
				// don't recurse here (cyclic imports!)
				type = mod.concreteSyntaxTypes.get(name);
				
				if (type != null) {
					return type;
				}
			}
		}
		
		return type;
	}
	
	@Override
	public Type lookupAlias(String name) {
		return typeStore.lookupAlias(name);
	}
	
	@Override
	public Set<Type> lookupAlternatives(Type adt) {
		return typeStore.lookupAlternatives(adt);
	}
	
	@Override
	public Type lookupConstructor(Type adt, String cons, Type args) {
		return typeStore.lookupConstructor(adt, cons, args);
	}
	
	@Override
	public Set<Type> lookupConstructor(Type adt, String constructorName)
			throws FactTypeUseException {
		return typeStore.lookupConstructor(adt, constructorName);
	}
	
	@Override
	public Set<Type> lookupConstructors(String constructorName) {
		return typeStore.lookupConstructors(constructorName);
	}
	
	@Override
	public Type lookupFirstConstructor(String cons, Type args) {
		return typeStore.lookupFirstConstructor(cons, args);
	}

	public boolean isInitialized() {
		return initialized;
	}
	
	public void setInitialized() {
		this.initialized = true;
	}
	
	public void setInitialized(boolean init) {
		this.initialized = init;
	}

	public void setBootstrap(boolean needBootstrapParser) {
		this.bootstrap = needBootstrapParser;
	}
	
	public boolean getBootstrap() {
		return bootstrap;
	}

	@Override
	protected boolean isNameFlagged(QualifiedName name, Predicate<NameFlags> tester) {
		String modulename = Names.moduleName(name);
		String cons = Names.name(Names.lastName(name));
		if (modulename != null) {
			if (modulename.equals(getName())) {
				return isNameFlagged(cons, tester);
			}
			
			ModuleEnvironment imported = getImport(modulename);
			if (imported == null) {
				// this might happen if the name is actually a type name instead of a module name
				// when we replace the :: notation for . this issue should dissappear
				return false;
			}
			
			return imported.isNameFlagged(cons, tester);
		}
		
		return isNameFlagged(cons, tester);
	}

	@Override
	protected void flagName(QualifiedName name, NameFlags flags) {
		String modulename = Names.moduleName(name);
		String cons = Names.name(Names.lastName(name));
		if (modulename != null) {
			if (modulename.equals(getName())) {
				flagName(cons, flags);
			}
			
			ModuleEnvironment imported = getImport(modulename);
			if (imported == null) {
				throw new UndeclaredModule(modulename, name);
			}
			
			imported.flagName(cons, flags);
		}
		
		flagName(cons, flags);
	}

	@Override
	protected Environment getFlagsEnvironment(String name) {
		Environment env = super.getFlagsEnvironment(name);
		
		if (env != null) {
			return env;
		}
		
		for (String moduleName : importedModules) {
			ModuleEnvironment mod = getImport(moduleName);
			if(mod == null)	{
				throw new RuntimeException("getFlagsEnvironment");
			}
			env = mod.getLocalFlagsEnvironment(name);
			
			if (env != null) {
				return env;
			}
		}

		return null;
	}

	private Environment getLocalFlagsEnvironment(String name) {
		if (this.nameFlags != null && nameFlags.get(name) != null)
			return this;
		return null;
	}

	public Set<String> getExtends() {
		if (extended != null) {
			return Collections.unmodifiableSet(extended);
		}
		
		return Collections.<String>emptySet();
	}
	
	public Set<String> getExtendsTransitive() {
		List<String> todo = new LinkedList<String>();
		Set<String> done = new HashSet<String>();
		Set<String> result = new HashSet<String>();
		todo.add(this.getName());
		GlobalEnvironment heap = getHeap();
		
		while (!todo.isEmpty()) {
		   String mod = todo.remove(0);	
		   done.add(mod);
		   ModuleEnvironment env = heap.getModule(mod);
		   if (env != null) {
			  for (String e : env.getExtends()) {
				  result.add(e);
				  if (!done.contains(e)) {
					  todo.add(e);
				  }
			  }
		   }
		}
		
		return result;
	}

	public void removeExtend(String name) {
		extended.remove(name);
	}

	public void setDeprecatedMessage(String deprecatedMessage) {
		this.deprecated = deprecatedMessage;
	}
	
	public boolean isDeprecated() {
		return deprecated != null;
	}
	
	public String getDeprecatedMessage() {
		return deprecated;
	}
	
	public boolean hasImporterForResource(String resourceScheme) {
		return this.resourceImporters.containsKey(resourceScheme);
	}

	public void addResourceImporter(AbstractFunction fun) {
		// TODO: Need checking to make sure an importer for this scheme does not already exist
		this.resourceImporters.put(fun.getResourceScheme(), fun);
	}
	
	public AbstractFunction getResourceImporter(String resourceScheme) {
		if (this.hasImporterForResource(resourceScheme))
			return this.resourceImporters.get(resourceScheme);
		else
			return null; // TODO: May be better to throw an exception here
	}

	public void resetProductions() {
		this.productions = new HashSet<IValue>(productions.size());
	}
}
