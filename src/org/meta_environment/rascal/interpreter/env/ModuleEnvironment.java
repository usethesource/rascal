package org.meta_environment.rascal.interpreter.env;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.ast.Test;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ConstructorFunction;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredModuleError;
import org.meta_environment.rascal.interpreter.types.NonTerminalType;
import org.meta_environment.rascal.interpreter.types.RascalTypeFactory;
import org.meta_environment.rascal.interpreter.utils.Names;
import org.meta_environment.uptr.Factory;

/**
 * A module environment represents a module object (i.e. a running module).
 * It manages imported modules and visibility of the
 * functions and variables it declares. 
 * 
 * TODO: add management of locally declared types and constructors
 * 
 */
public class ModuleEnvironment extends Environment {
	protected final Map<String, ModuleEnvironment> importedModules;
	protected final Map<Type, List<Type>> extensions;
	protected final TypeStore typeStore;
	protected final Map<String, NonTerminalType> concreteSyntaxTypes;
	protected final List<Test> tests;
	private Set<String> importedSDFModules = new HashSet<String>();
	private boolean initialized;
	
	protected static final TypeFactory TF = TypeFactory.getInstance();
	
	public ModuleEnvironment(String name) {
		super(name);
		this.importedModules = new HashMap<String, ModuleEnvironment>();
		this.extensions = new HashMap<Type, List<Type>>();
		this.concreteSyntaxTypes = new HashMap<String, NonTerminalType>();
		this.typeStore = new TypeStore();
		this.tests = new LinkedList<Test>();
		this.initialized = false;
	}
	
	public boolean isModuleEnvironment() {
		return true;
	}
	
	public void addImport(String name, ModuleEnvironment env) {
		importedModules.put(name, env);
		typeStore.importStore(env.typeStore);
	}
	
	public void addTest(Test test) {
		tests.add(0, test);
	}
	
	public List<Test> getTests() {
		return Collections.unmodifiableList(tests);
	}
	
	public void addSDFImport(String name) {
		importedSDFModules.add(name);
	}
	
	public Set<String> getSDFImports() {
		return importedSDFModules;
	}
	
	@Override
	public Set<String> getImports() {
		return importedModules.keySet();
	}
	
	public void unImport(String moduleName) {
		importedModules.remove(moduleName);
	}

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
			OverloadedFunctionResult result = null;
			OverloadedFunctionResult candidates = getAllFunctions(cons);
			
			if(candidates != null){
				for(AbstractFunction candidate : candidates.iterable()){
					if (candidate.getReturnType() == adt) {
						result = result == null ? new OverloadedFunctionResult(candidate) : result.add(candidate);
					}
				}
			}
			
			return result;
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
	
	protected org.meta_environment.rascal.interpreter.result.Result<IValue> getSimpleVariable(String name) {
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
	
	@Override
	protected OverloadedFunctionResult getAllFunctions(String name) {
		OverloadedFunctionResult funs = super.getAllFunctions(name);
		
		for (String moduleName : getImports()) {
			ModuleEnvironment mod = getImport(moduleName);
			OverloadedFunctionResult locals = mod.getLocalPublicFunctions(name);
			if (locals != null && funs != null) {
				funs = locals.join(funs);
			}
			else if (funs == null && locals != null) {
				funs = locals;
			}
		}

		return funs;
	}
	
	private Result<IValue> getLocalPublicVariable(String name) {
		Result<IValue> var = variableEnvironment.get(name);
		
		if (var != null && var.isPublic()) {
			return var;
		}
		
		return null;
	}
	
	private OverloadedFunctionResult getLocalPublicFunctions(String name) {
		OverloadedFunctionResult all = functionEnvironment.get(name);
		OverloadedFunctionResult result = null;
		
		if (all == null) {
			return null;
		}
		
		for (AbstractFunction l : all.iterable()) {
			if (l.isPublic()) {
				result = result == null ? new OverloadedFunctionResult(l) : result.add(l);
			}
		}
		
		return result;
	}

	@Override
	public Type abstractDataType(String name, Type... parameters) {
		return TF.abstractDataType(typeStore, name, parameters);
	}
	
	@Override
	public Type concreteSyntaxType(String name, org.meta_environment.rascal.ast.Type type) {
		NonTerminalType sort = new NonTerminalType(type);
		concreteSyntaxTypes.put(name, sort);
		return sort;
	}
	
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
		return function;
	}
	
	@Override
	public ConstructorFunction constructor(AbstractAST ast, Evaluator eval, Type nodeType, String name,
			Object... childrenAndLabels) {
		Type cons = TF.constructor(typeStore, nodeType, name, childrenAndLabels);
		ConstructorFunction function = new ConstructorFunction(ast, eval, this, cons);
		storeFunction(name, function);
		return function;
	}
	
	@Override
	public ConstructorFunction constructor(AbstractAST ast, Evaluator eval, Type nodeType, String name, Type... children) {
		Type cons = TF.constructor(typeStore, nodeType, name, children);
		ConstructorFunction function = new ConstructorFunction(ast, eval, this, cons);
		storeFunction(name, function);
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
		return "Environment [ " + getName() + ":" + importedModules + "]"; 
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
				
				type = mod.lookupConcreteSyntaxType(name);
				
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
}
