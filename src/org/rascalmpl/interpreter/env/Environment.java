package org.rascalmpl.interpreter.env;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Import.Syntax;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ConstructorFunction;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.utils.Names;

/**
 * A simple environment for variables and functions and types.
 * TODO: this class does not support shadowing of variables and functions yet, which is wrong.
 */
public class Environment {
	
	private class NameFlags {
		public final static int FINAL_NAME = 0x01;
		public final static int OVERLOADABLE_NAME = 0x02;
		private int flags = 0;

		public NameFlags(int flags) {
			this.flags = flags;
		}
		
		public int getFlags() {
			return flags;
		}
		
		public void setFlags(int flags) {
			this.flags = flags;
		}
	}
	
	protected Map<String, Result<IValue>> variableEnvironment;
	protected Map<String, OverloadedFunctionResult> functionEnvironment;
	protected Map<String,NameFlags> nameFlags;
	protected Map<Type, Type> typeParameters;
	protected final Environment parent;
	protected final Environment callerScope;
	protected final ISourceLocation callerLocation; // different from the scope location (more precise)
	protected final ISourceLocation loc;
	protected Cache cache = null;
	protected final String name;

	public Environment(String name) {
		this(null, null, null, null, null, name);
	}

	public Environment(Environment parent, String name) {
		this(parent, null, null, parent.getCache(), null, name);
	}

	public Environment(Environment parent, ISourceLocation loc, String name) {
		this(parent, null, null, parent.getCache(), loc, name);
	}

	public Environment(Environment parent, Environment callerScope, ISourceLocation callerLocation, ISourceLocation loc, String name) {
		this(parent, callerScope, callerLocation, parent.getCache(), loc, name);
	}
	
	protected Environment(Environment parent, Environment callerScope, ISourceLocation callerLocation, Cache cache, ISourceLocation loc, String name) {
		this.variableEnvironment = new HashMap<String, Result<IValue>>();
		this.functionEnvironment = new HashMap<String, OverloadedFunctionResult>();
		this.nameFlags = new HashMap<String, NameFlags>();
		this.typeParameters = new HashMap<Type, Type>();
		this.parent = parent;
		this.cache = cache;
		this.loc = loc;
		this.name = name;
		this.callerScope = callerScope;
		this.callerLocation = callerLocation;
		if (parent == this) {
			throw new ImplementationError("internal error: cyclic environment");
		}
	}

	/**
	 * @return the name of this environment/stack frame for use in tracing
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the location where this environment was created (i.e. call site) for use in tracing
	 */
	public ISourceLocation getLocation() {
		return loc;
	}

	private Cache getCache() {
		return cache;
	}

	public void checkPoint() {
		this.cache = new Cache();
	}

	public void rollback() {
		cache.rollback();
		cache = null;
	}

	public void commit() {
		cache = null;
	}


	private void updateVariableCache(String name, Map<String, Result<IValue>> env, Result<IValue> old) {
		if (cache == null) {
			return;
		}
		if (!cache.containsVariable(env, name)) {
			cache.save(env, name, old);
		}
	}

	private void updateFunctionCache(String name,
			Map<String, OverloadedFunctionResult> env, OverloadedFunctionResult list) {
		if (cache == null) {
			return;
		}
		if (!cache.containsFunction(env, name)) {
			cache.save(env, name, list);
		}
	}

	public boolean isRootScope() {
		if (!(this instanceof ModuleEnvironment)) {
			return false;
		}
		return parent == null;
	}

	public boolean isRootStackFrame() {
		return getCallerScope() == null;
	}

	/**
	 * Look up a variable, traversing the parents of the current scope
	 * until it is found.
	 * @param name
	 * @return
	 */
	public Result<IValue> getVariable(QualifiedName name) {
		if (name.getNames().size() > 1) {
			Environment current = this;
			while (!current.isRootScope()) {
				current = current.parent;
			}

			return current.getVariable(name);
		}

		String varName = Names.name(Names.lastName(name));
		return getVariable(varName);
	}

	public Result<IValue> getVariable(Name name) {
		return getVariable(Names.name(name));
	}
	
	public void storeVariable(QualifiedName name, Result<IValue> result) {
		if (name.getNames().size() > 1) {
			if (!isRootScope()) {
				parent.storeVariable(name, result);
			}
		}
		else {
			String varName = Names.name(Names.lastName(name));
			storeVariable(varName, result);
		}
	}

	public Result<IValue> getVariable(String name) {
		Result<IValue> t = getSimpleVariable(name);
		
		if (t != null) {
			return t;
		}
		
		OverloadedFunctionResult funs = getAllFunctions(name);
		
		if (funs == null || funs.size() == 0) {
			return null;
		}
		
		return funs;
	}
	
	protected Result<IValue> getSimpleVariable(String name) {
		Result<IValue> t = variableEnvironment.get(name);
		
		if (t != null) {
			return t;
		}
		
		if (!isRootScope()) {
			return parent.getSimpleVariable(name);
		}
		
		return null;
	}
	
	protected OverloadedFunctionResult getAllFunctions(String name) {
		OverloadedFunctionResult result = functionEnvironment.get(name);
		
		if (isRootScope()) {
			return result;
		}
		
		if (result == null) {
			return parent.getAllFunctions(name);
		}
		
		OverloadedFunctionResult resultFromParent = parent.getAllFunctions(name);
		if(resultFromParent == null) return result;
		
		return result.join(resultFromParent);
	}

	private boolean isNameFlagged(QualifiedName name, int flags) {
		if (name.getNames().size() > 1) {
			Environment current = this;
			while (!current.isRootScope()) {
				current = current.parent;
			}
			
			return current.isNameFlagged(name, flags);
		}
		
		String simpleName = Names.name(Names.lastName(name));
		return isNameFlagged(simpleName, flags);
		
	}
	
	private boolean isNameFlagged(String name, int flags) {
			return nameFlags.containsKey(name) && (0 != (nameFlags.get(name).getFlags() & flags));
	}

	public boolean isNameFinal(QualifiedName name) {
		return isNameFlagged(name, NameFlags.FINAL_NAME);
	}

	public boolean isNameOverloadable(QualifiedName name) {
		return isNameFlagged(name, NameFlags.OVERLOADABLE_NAME);
	}

	private void flagName(QualifiedName name, int flags) {
		if (name.getNames().size() > 1) {
			Environment current = this;
			while (!current.isRootScope()) {
				current = current.parent;
			}
			
			current.flagName(name, flags);
		}
		
		String simpleName = Names.name(Names.lastName(name));
		flagName(simpleName, flags);
		
	}
	
	private void flagName(String name, int flags) {
			if (nameFlags.containsKey(name))
				nameFlags.get(name).setFlags(nameFlags.get(name).getFlags() | flags);
			else
				nameFlags.put(name, new NameFlags(flags));
	}
	
	public void markNameFinal(QualifiedName name) {
		flagName(name, NameFlags.FINAL_NAME);
	}
	
	public void markNameFinal(String name) {
		flagName(name, NameFlags.FINAL_NAME);
	}

	public void markNameOverloadable(QualifiedName name) {
		flagName(name, NameFlags.OVERLOADABLE_NAME);
	}

	public void markNameOverloadable(String name) {
		flagName(name, NameFlags.OVERLOADABLE_NAME);
	}
	
	public void storeParameterType(Type par, Type type) {
		typeParameters.put(par, type);
	}

	public Type getParameterType(Type par) {
		return typeParameters.get(par);
	}

	/**
	 * Search for the environment that declared a variable, down to the module
	 * scope.
	 */
	private Map<String,Result<IValue>> getVariableDefiningEnvironment(String name) {
		Result<IValue> r = variableEnvironment.get(name);

		if (r != null) {
			return variableEnvironment;
		}

		return isRootScope() ? null : parent.getVariableDefiningEnvironment(name);
	}

	/**
	 * Search for the environment that declared a variable, but stop just above
	 * the module scope such that we end up in a function scope or a shell scope.
	 */
	private Map<String,Result<IValue>> getLocalVariableDefiningEnvironment(String name) {
		Result<IValue> r = variableEnvironment.get(name);

		if (r != null) {
			return variableEnvironment;
		}

		if (parent == null || parent.isRootScope()) {
			return variableEnvironment;
		}

		return parent.getLocalVariableDefiningEnvironment(name);
	}

	/**
	 * Store a variable the scope that it is declared in, down to the 
	 * module scope if needed.
	 */
	public void storeVariable(String name, Result<IValue> value) {
		//System.err.println("storeVariable: " + name + value.getValue());
		Map<String,Result<IValue>> env = getVariableDefiningEnvironment(name);
		
		if (env == null) {
			// an undeclared variable, which gets an inferred type
			updateVariableCache(name, variableEnvironment, null);
			variableEnvironment.put(name, value);
			//System.out.println("Inferred: " + name);
			if (value != null) {
				value.setInferredType(true);
			}
		}
		else {
			// a declared variable
			Result<IValue> old = env.get(name);
			updateVariableCache(name, env, old);
			value.setPublic(old.isPublic());
			env.put(name, value);
		}
	}

	/**
	 * Store a variable the scope that it is declared in, but not in the
	 * module (root) scope. This should result in storing variables in either
	 * the function scope or the shell scope. If a variable is not declared yet,
	 * this function will store it in the function scope.
	 */
	public void storeLocalVariable(String name, Result<IValue> value) {
		Map<String,Result<IValue>> env = getLocalVariableDefiningEnvironment(name);

		if (env == null) {
			throw new ImplementationError("storeVariable should always find a scope");
		}

		Result<IValue> old = env.get(name);

		if (old != null) {
			updateVariableCache(name, env, old);
		}
		else {
			value.setInferredType(true);
		}

		env.put(name, value);
	}

	public void storeVariable(Name name, Result<IValue> r) {
		storeVariable(Names.name(name), r);
	}
 
	public void storeFunction(String name, AbstractFunction function) {
		OverloadedFunctionResult list = functionEnvironment.get(name);
		if (list == null || !this.isNameFlagged(name,NameFlags.OVERLOADABLE_NAME)) {
			// NB: we store null in the cache so that rollback will remove the table entry on name
			// instead of restoring an empty list.
			updateFunctionCache(name, functionEnvironment, null);
			
			list = new OverloadedFunctionResult(function);
			functionEnvironment.put(name, list);
		}
		else {
			updateFunctionCache(name, functionEnvironment, list);
		}

		functionEnvironment.put(name, list.add(function));
	}

	public boolean declareVariable(Type type, Name name) {
		return declareVariable(type, Names.name(name));
	}

	public boolean declareVariable(Type type, String name) {
		if (variableEnvironment.get(name) != null) {
			return false;
		}
		variableEnvironment.put(name, ResultFactory.nothing(type));
		return true;
	}
	
	public Map<Type, Type> getTypeBindings() {
		Environment env = this;
		Map<Type, Type> result = new HashMap<Type,Type>();

		while (env != null) {
			for (Type key : env.typeParameters.keySet()) {
				if (!result.containsKey(key)) {
					result.put(key, env.typeParameters.get(key));
				}
			}
			env = env.parent;
		}

		return Collections.unmodifiableMap(result);
	}

	public void storeTypeBindings(Map<Type, Type> bindings) {
		typeParameters.putAll(bindings);
	}

	@Override
	public String toString(){
		StringBuffer res = new StringBuffer();
		for(String name : functionEnvironment.keySet()){
			res.append(name).append(": ").append(functionEnvironment.get(name)).append("\n");
		}
		for(String name : variableEnvironment.keySet()){
			res.append(name).append(": ").append(variableEnvironment.get(name)).append("\n");
		}
		return res.toString();
	}

	public ModuleEnvironment getImport(String moduleName) {
		return getRoot().getImport(moduleName);
	}

	public boolean isTreeConstructorName(QualifiedName name, Type signature) {
		return getRoot().isTreeConstructorName(name, signature);
	}

	public Environment getRoot() {
		if (parent == null) {
			return this;
		}
		
		Environment target = parent;
		while (target!=null && !target.isRootScope()) {
			target = target.parent;
		}
		return target;
	}

	public Type getConstructor(Type sort, String cons, Type args) {
		return getRoot().getConstructor(sort, cons, args);
	}

	public Type getConstructor(String cons, Type args) {
		return getRoot().getConstructor(cons, args);
	}

	public Type getAbstractDataType(String sort) {
		return getRoot().getAbstractDataType(sort);
	}

	public Type lookupAbstractDataType(String name) {
		return getRoot().lookupAbstractDataType(name);
	}

	public Type lookupConcreteSyntaxType(String name) {
		return getRoot().lookupConcreteSyntaxType(name);
	}


	public Type lookupAlias(String name) {
		return getRoot().lookupAlias(name);
	}

	public Set<Type> lookupAlternatives(Type adt) {
		return getRoot().lookupAlternatives(adt);
	}

	public Type lookupConstructor(Type adt, String cons, Type args) {
		return getRoot().lookupConstructor(adt, cons, args);
	}

	public Set<Type> lookupConstructor(Type adt, String constructorName)
	throws FactTypeUseException {
		return getRoot().lookupConstructor(adt, constructorName);
	}

	public Set<Type> lookupConstructors(String constructorName) {
		return getRoot().lookupConstructors(constructorName);
	}

	public Type lookupFirstConstructor(String cons, Type args) {
		return getRoot().lookupFirstConstructor(cons, args);
	}

	public boolean declaresAnnotation(Type type, String label) {
		return getRoot().declaresAnnotation(type, label);
	}

	public Type getAnnotationType(Type type, String label) {
		return getRoot().getAnnotationType(type, label);
	}

	public void declareAnnotation(Type onType, String label, Type valueType) {
		getRoot().declareAnnotation(onType, label, valueType);
	}

	public Type abstractDataType(String name, Type...parameters) {
		return getRoot().abstractDataType(name, parameters);
	}

	public Type concreteSyntaxType(String name, org.rascalmpl.ast.Type type) {
		return getRoot().concreteSyntaxType(name, type);
	}

	public Type concreteSyntaxType(String name, IConstructor symbol) {
		return getRoot().concreteSyntaxType(name, symbol);
	}

	public ConstructorFunction constructorFromTuple(AbstractAST ast, Evaluator eval, Type adt, String name, Type tupleType) {
		return getRoot().constructorFromTuple(ast, eval, adt, name, tupleType);
	}

	public ConstructorFunction constructor(AbstractAST ast, Evaluator eval, Type nodeType, String name, Object... childrenAndLabels ) {
		return getRoot().constructor(ast, eval, nodeType, name, childrenAndLabels);
	}

	public ConstructorFunction constructor(AbstractAST ast, Evaluator eval, Type nodeType, String name, Type... children ) {
		return getRoot().constructor(ast, eval, nodeType, name, children);
	}

	public Type aliasType(String name, Type aliased, Type...parameters) {
		return getRoot().aliasType(name, aliased, parameters);
	}

	public TypeStore getStore() {
		return getRoot().getStore();
	}

	public Map<String, Result<IValue>> getVariables() {
		Map<String, Result<IValue>> vars = new HashMap<String, Result<IValue>>();
		if (parent != null) {
			vars.putAll(parent.getVariables());
		}
		vars.putAll(variableEnvironment);
		return vars;
	}

	public List<Entry<String, OverloadedFunctionResult>> getFunctions() {
		ArrayList<Entry<String, OverloadedFunctionResult>> functions = new ArrayList<Entry<String, OverloadedFunctionResult>>();
		if (parent != null) {
			functions.addAll(parent.getFunctions());
		}
		functions.addAll(functionEnvironment.entrySet());
		return functions;
	}
	
	public List<Entry<String, OverloadedFunctionResult>> getAllFunctions() {
		ArrayList<Entry<String, OverloadedFunctionResult>> functions = new ArrayList<Entry<String, OverloadedFunctionResult>>();
		functions.addAll(getFunctions());
		
		for (String i : getImports()) {
			next:for (Entry<String, OverloadedFunctionResult> cand : getImport(i).getFunctions()) {
				for (AbstractFunction func : cand.getValue().iterable()) {
					if (func.isPublic()) {
						functions.add(cand);
						continue next;
					}
				}
			}
		}
		return functions;
	}
	

	public Environment getParent() {
		return parent;
	}

	public Environment getCallerScope() {
		if (callerScope != null) {
			return callerScope;
		} else if (parent != null) {
			return parent.getCallerScope();
		}
		return null;
	}

	public ISourceLocation getCallerLocation() {
		if (callerLocation != null) {
			return callerLocation;
		} else if (parent != null) {
			return parent.getCallerLocation();
		}
		return null;
	}

	public Set<String> getImports() {
		return getRoot().getImports();
	}

	public void reset() {
		this.variableEnvironment = new HashMap<String, Result<IValue>>();
		this.functionEnvironment = new HashMap<String, OverloadedFunctionResult>();
		this.typeParameters = new HashMap<Type, Type>();
	}

	public void declareProduction(Syntax x) {
		getRoot().declareProduction(x);
	}
	
	public ISet getProductions() {
		return getRoot().getProductions();
	}
}