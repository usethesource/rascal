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
 *   * Emilie Balland - (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.env;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.SyntaxDefinition;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ConstructorFunction;
import org.rascalmpl.interpreter.result.OverloadedFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModuleError;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

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
	protected Map<String, ModuleEnvironment> importedModules;
	protected Set<String> extended;
	protected Set<String> haveExtended;
	protected TypeStore typeStore;
	protected Set<IValue> productions;
	protected Map<String, NonTerminalType> concreteSyntaxTypes;
	private boolean initialized;
	private boolean syntaxDefined;
	private boolean bootstrap;
	private String cachedParser;
	private String deprecated;
	protected Map<String, AbstractFunction> resourceImporters;
	
	protected static final TypeFactory TF = TypeFactory.getInstance();

	public final static String SHELL_MODULE = "$shell$";
	
	public ModuleEnvironment(String name, GlobalEnvironment heap) {
		super(name);
		this.heap = heap;
		this.importedModules = new HashMap<String, ModuleEnvironment>();
		this.concreteSyntaxTypes = new HashMap<String, NonTerminalType>();
		this.productions = new HashSet<IValue>();
		this.typeStore = new TypeStore();
		this.initialized = false;
		this.syntaxDefined = false;
		this.bootstrap = false;
		this.resourceImporters = new HashMap<String, AbstractFunction>();
	}
	
	@Override
	public void reset() {
		super.reset();
		this.importedModules = new HashMap<String, ModuleEnvironment>();
		this.concreteSyntaxTypes = new HashMap<String, NonTerminalType>();
		this.typeStore = new TypeStore();
		this.productions = new HashSet<IValue>();
		this.initialized = false;
		this.syntaxDefined = false;
		this.bootstrap = false;
		this.extended = new HashSet<String>();
		this.haveExtended = new HashSet<String>();
		this.deprecated = null;
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
	
	@SuppressWarnings("deprecation")
	@Override
	public void declareProduction(SyntaxDefinition x) {
		productions.add(x.getTree());
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
		Type DefSort = RascalTypeFactory.getInstance().nonTerminalType((IConstructor) Factory.Symbol_Sort.make(VF, "SyntaxDefinition"));
		IMapWriter result = VF.mapWriter(TF.stringType(), TF.tupleType(TF.setType(TF.stringType()), TF.setType(TF.stringType()), TF.setType(DefSort)));
		
		while(!todo.isEmpty()){
			String m = todo.get(0);
			todo.remove(0);
			
			if(done.contains(m)) continue;
			
			done.add(m);
			
			ModuleEnvironment env = heap.getModule(m);
			
			if(env != null){
				ISetWriter importWriter = VF.setWriter(TF.stringType());
				for(String impname : env.getImports()){
					if(!done.contains(impname)) todo.add(impname);
					
					importWriter.insert(VF.string(impname));
				}
				
				ISetWriter extendWriter = VF.setWriter(TF.stringType());
				for(String impname : env.getExtends()){
					if(!done.contains(impname)) todo.add(impname);
					
					extendWriter.insert(VF.string(impname));
				}
				
				ISetWriter defWriter = VF.setWriter(DefSort);
				for(IValue def : env.productions){
					defWriter.insert(def);
				}
				
				ITuple t = VF.tuple(importWriter.done(), extendWriter.done(), defWriter.done());
				result.put(VF.string(m), t);
			}else if(m == getName()){ // This is the root scope.
				ISetWriter importWriter = VF.setWriter(TF.stringType());
				for(String impname : importedModules.keySet()){
					if(!done.contains(impname)) todo.add(impname);
					
					importWriter.insert(VF.string(impname));
				}
				
				ISetWriter extendWriter = VF.setWriter(TF.stringType());
				for(String impname : getExtends()){
					if(!done.contains(impname)) todo.add(impname);
					
					extendWriter.insert(VF.string(impname));
				}
				
				ISetWriter defWriter = VF.setWriter(DefSort);
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
		importedModules.put(name, env);
		typeStore.importStore(env.typeStore);
	}
	
	public void addExtend(String name) {
		if (extended == null) {
			extended = new HashSet<String>();
		}
		extended.add(name);
	}
	
	// register that this module has been extended with the named module (extension has executed)
	public void haveExtended(String name) {
		if (haveExtended == null) {
			haveExtended = new HashSet<String>();
		}
		haveExtended.add(name);
	}
	
	public boolean hasExtended(String name) {
		if (haveExtended != null) {
			return haveExtended.contains(name);
		}
		return false;
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
		return importedModules.keySet();
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
		   ModuleEnvironment env = heap.getModule(mod);
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
		ModuleEnvironment old = importedModules.remove(moduleName);
		if (old != null) {
			typeStore.unimportStores(new TypeStore[] { old.getStore() });
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
			
			return new OverloadedFunction(cons, result);
		}
		
		if (modulename != null) {
			if (modulename.equals(getName())) {
				return getVariable(cons);
			}
			
			ModuleEnvironment imported = getImport(modulename);
			if (imported == null) {
				throw new UndeclaredModuleError(modulename, name);
			}
			
			// TODO: will this not do a transitive closure? This should not happen...
			return imported.getVariable(name);
		}
		
		return getVariable(cons);
	}
	
	@Override
	public void storeVariable(String name, Result<IValue> value) {
		if (value instanceof AbstractFunction) {
			storeFunction(name, (AbstractFunction) value);
			return;
		}
		
		Result<IValue> result = super.getVariable(name);
		
		if (result != null) {
			super.storeVariable(name, value);
		}
		else {
			for (String i : getImports()) {
				ModuleEnvironment module = importedModules.get(i);
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
		
		for (String moduleName : getImports()) {
			ModuleEnvironment mod = getImport(moduleName);
			var = mod.getLocalPublicVariable(name);
			
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

		for (String moduleName : getImports()) {
			ModuleEnvironment mod = getImport(moduleName);
			Result<IValue> r = null;
			if (mod.variableEnvironment != null) 
				r = mod.variableEnvironment.get(name);
			
			if (r != null && r.isPublic()) {
				return mod.variableEnvironment;
			}
		}

		return null;
	}
	
	@Override
	public void getAllFunctions(String name, List<AbstractFunction> collection) {
		super.getAllFunctions(name, collection);
		
		for (String moduleName : getImports()) {
			ModuleEnvironment mod = getImport(moduleName);
			mod.getLocalPublicFunctions(name, collection);
		}
	}
	
	@Override
	public void getAllFunctions(Type returnType, String name, List<AbstractFunction> collection) {
		super.getAllFunctions(returnType, name, collection);
		
		for (String moduleName : getImports()) {
			ModuleEnvironment mod = getImport(moduleName);
			mod.getLocalPublicFunctions(returnType, name, collection);
		}
	}
	

	
	
	private Result<IValue> getLocalPublicVariable(String name) {
		Result<IValue> var = null;
		
		if (variableEnvironment != null) {
			var = variableEnvironment.get(name);
		}
		
		if (var != null && var.isPublic()) {
			return var;
		}
		
		return null;
	}
	
	private void getLocalPublicFunctions(String name, List<AbstractFunction> collection) {
		if (functionEnvironment != null) {
			List<AbstractFunction> lst = functionEnvironment.get(name);
			if (lst != null) {
				for (AbstractFunction func : lst) {
					if (func.isPublic()) {
						collection.add(func);
					}
				}
			}
		}
	}
	
	private void getLocalPublicFunctions(Type returnType, String name, List<AbstractFunction> collection) {
		if (functionEnvironment != null) {
			List<AbstractFunction> lst = functionEnvironment.get(name);
			
			if (lst != null) {
				for (AbstractFunction func : lst) {
					if (func.isPublic() && func.getReturnType().isSubtypeOf(returnType)) {
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
	
	@Override
	public ConstructorFunction constructorFromTuple(AbstractAST ast, Evaluator eval, Type adt, String name, Type tupleType) {
		Type cons = TF.constructorFromTuple(typeStore, adt, name, tupleType);
		ConstructorFunction function = new ConstructorFunction(ast, eval, this, cons);
		storeFunction(name, function);
		markNameFinal(name);
		markNameOverloadable(name);
		return function;
	}
	
	@Override
	public ConstructorFunction constructor(AbstractAST ast, Evaluator eval, Type nodeType, String name,
			Object... childrenAndLabels) {
		Type cons = TF.constructor(typeStore, nodeType, name, childrenAndLabels);
		ConstructorFunction function = new ConstructorFunction(ast, eval, this, cons);
		storeFunction(name, function);
		markNameFinal(name);
		markNameOverloadable(name);
		return function;
	}
	
	@Override
	public ConstructorFunction constructor(AbstractAST ast, Evaluator eval, Type nodeType, String name, Type... children) {
		Type cons = TF.constructor(typeStore, nodeType, name, children);
		ConstructorFunction function = new ConstructorFunction(ast, eval, this, cons);
		storeFunction(name, function);
		markNameFinal(name);
		markNameOverloadable(name);
		return function;
	}
	
	@Override
	public Type aliasType(String name, Type aliased, Type... parameters) {
		return TF.aliasType(typeStore, name, aliased, parameters);
	}
	
	@Override
	public void declareAnnotation(Type onType, String label, Type valueType) {
		typeStore.declareAnnotation(onType, label, valueType);
	}
	
	@Override
	public Type getAnnotationType(Type type, String label) {
		Type anno = typeStore.getAnnotationType(type, label);
		if (anno == null && type instanceof NonTerminalType) {
			return typeStore.getAnnotationType(Factory.Tree, label);
		}
		return anno;
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
		return "Environment [ " + getName() + ", imports: " + ((importedModules != null) ? importedModules.keySet() : "") + ", extends: " + ((extended != null) ? extended : "") + "]"; 
	}

	@Override
	public ModuleEnvironment getImport(String moduleName) {
		return importedModules.get(moduleName);
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
				throw new UndeclaredModuleError(modulename, name);
			}
			
			imported.storeVariable(name, result);
			return;
		}
		super.storeVariable(name, result);
	}
	
	@Override
	public boolean declaresAnnotation(Type type, String label) {
		return typeStore.getAnnotationType(type, label) != null;
	}
	
	@Override
	public Type lookupAbstractDataType(String name) {
		return typeStore.lookupAbstractDataType(name);
	}
	
	@Override
	public Type lookupConcreteSyntaxType(String name) {
		Type type = concreteSyntaxTypes.get(name);
		
		if (type == null) {
			for (String i : getImports()) {
				ModuleEnvironment mod = getImport(i);
				
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

	// todo: bootstrap must go and use this
	public void setCachedParser(String cachedParser) {
		this.cachedParser = cachedParser;
	}
	
	public String getCachedParser() {
		return cachedParser;
	}

	public boolean hasCachedParser() {
		return cachedParser != null;
	}

	@Override
	protected boolean isNameFlagged(QualifiedName name, int flags) {
		String modulename = Names.moduleName(name);
		String cons = Names.name(Names.lastName(name));
		if (modulename != null) {
			if (modulename.equals(getName())) {
				return isNameFlagged(cons, flags);
			}
			
			ModuleEnvironment imported = getImport(modulename);
			if (imported == null) {
				// this might happen if the name is actually a type name instead of a module name
				// when we replace the :: notation for . this issue should dissappear
				return false;
			}
			
			return imported.isNameFlagged(cons, flags);
		}
		
		return isNameFlagged(cons, flags);
	}

	@Override
	protected void flagName(QualifiedName name, int flags) {
		String modulename = Names.moduleName(name);
		String cons = Names.name(Names.lastName(name));
		if (modulename != null) {
			if (modulename.equals(getName())) {
				flagName(cons, flags);
			}
			
			ModuleEnvironment imported = getImport(modulename);
			if (imported == null) {
				throw new UndeclaredModuleError(modulename, name);
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
		
		for (String moduleName : getImports()) {
			ModuleEnvironment mod = getImport(moduleName);
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
