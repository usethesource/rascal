package org.meta_environment.rascal.interpreter.env;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.ast.Variable;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredFunctionError;

/**
 * A simple environment for variables and functions and types.
 * TODO: this class does not support shadowing of variables and functions yet, which is wrong.
 */
public class Environment {
	protected final Map<String, Result<IValue>> variableEnvironment;
	protected final Map<String, List<Lambda>> functionEnvironment;
	protected final Map<Type, Type> typeParameters;
	protected final Environment parent;
	protected Cache cache = null;

	public Environment(Environment parent) {
		this(parent, parent.getCache());
	}
	
	protected Environment(Environment parent, Cache cache) {
		this.variableEnvironment = new HashMap<String, Result<IValue>>();
		this.functionEnvironment = new HashMap<String, List<Lambda>>();
		this.typeParameters = new HashMap<Type, Type>();
		this.parent = parent;
		this.cache = cache;
		if (parent == this) {
			throw new ImplementationError("internal error: cyclic environment");
		}
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
			Map<String, List<Lambda>> env, List<Lambda> list) {
		if (cache == null) {
			return;
		}
		if (!cache.containsFunction(env, name)) {
			cache.save(env, name, list);
		}
	}
		
	public boolean isRoot() {
		assert this instanceof ModuleEnvironment: "roots should be instance of ModuleEnvironment";
		return parent == null;
	}
	
	public Lambda getFunction(String name, Type actuals, AbstractAST useSite) {
		List<Lambda> candidates = functionEnvironment.get(name);
		
		if (candidates != null) {
			for (Lambda candidate : candidates) {
				if (candidate.match(actuals)) {
					return candidate;
				}
			}
		}
		
		return isRoot() ? null : parent.getFunction(name, actuals, useSite);
	}
	
	/**
	 * Returns a variable from the innermost scope, if it exists.
	 */
	public Result<IValue> getInnermostVariable(QualifiedName name) {
		if (name.getNames().size() != 1) {
			throw new ImplementationError("Local variables should not be qualified");
		}
		return getInnermostVariable(name.getNames().get(0));
	}
	
	/**
	 * Returns a variable from the innermost scope, if it exists.
	 */
	private Result<IValue> getInnermostVariable(Name name) {
		return getInnermostVariable(Names.name(name));
	}

	/**
	 * Returns a variable from the innermost scope, if it exists.
	 */
	public Result<IValue> getInnermostVariable(String name) {
		//System.err.println("getLocalVariable: " + name);
		return variableEnvironment.get(name);
	}

	/**
	 * Look up a variable, traversing the parents of the current scope
	 * until it is found, but don't look in a module scope (which means
	 * stop in the shell or in the function scope). When the variable
	 * is qualified however, the variable is looked up in the named scope.
	 */
	public Result<IValue> getLocalVariable(QualifiedName name) {
		//System.err.println("getVariable: " + name);
		if (name.getNames().size() > 1) {
			Environment current = this;
			while (!current.isRoot()) {
				current = current.parent;
			}
			
			return current.getVariable(name);
		}
		else {
			String varName = Names.name(Names.lastName(name));
			return getLocalVariable(varName);
		}
	}

	/**
	 * Look up a variable, traversing the parents of the current scope
	 * until it is found, but don't look in a module scope (which means
	 * stop in the shell or in the function scope).
	 */
	public Result<IValue> getLocalVariable(Name name) {
		return getLocalVariable(Names.name(name));
	}
	
	/**
	 * Look up a variable, traversing the parents of the current scope
	 * until it is found, but don't look in a module scope (which means
	 * stop in the shell or in the function scope).
	 */
	public Result<IValue> getLocalVariable(String varName) {
		Result<IValue> r = getInnermostVariable(varName);

		if (r != null) {
			return r;
		}
		
		if (parent.isRoot()) {
			return null;
		}

		return parent.getLocalVariable(varName);
	}
	
	/**
	 * Look up a variable, traversing the parents of the current scope
	 * until it is found.
	 * @param name
	 * @return
	 */
	public Result<IValue> getVariable(QualifiedName name) {
		//System.err.println("getVariable: " + name);
		if (name.getNames().size() > 1) {
			Environment current = this;
			while (!current.isRoot()) {
				current = current.parent;
			}
			
			return current.getVariable(name);
		}
		else {
			String varName = Names.name(Names.lastName(name));
			Result<IValue> r = getVariable(name, varName);

			if (r != null) {
				return r;
			}

			return isRoot() ? null : parent.getVariable(name);
		}
	}
	
	public void storeVariable(QualifiedName name, Result<IValue> result) {
 		if (name.getNames().size() > 1) {
 			if (!isRoot()) {
 				parent.storeVariable(name, result);
 			}
 		}
 		else {
 			String varName = Names.name(Names.lastName(name));
 			storeVariable(varName, result);
 		}
	}
	
	public Result<IValue> getVariable(AbstractAST ast, String name) {
		Result<IValue> t = variableEnvironment.get(name);
		if (t == null) {
			return isRoot() ? null : parent.getVariable(ast, name);
		}
		return t;
	}
	
	public void storeParameterType(Type par, Type type) {
		typeParameters.put(par, type);
	}
	
	public Type getParameterType(Type par) {
		return typeParameters.get(par);
	}

	private Map<String,Result<IValue>> getVariableDefiningEnvironment(String name) {
		Result<IValue> r = variableEnvironment.get(name);
		
		if (r != null) {
			return variableEnvironment;
		}
		
		return isRoot() ? null : parent.getVariableDefiningEnvironment(name);
	}
	
	/**
	 * Store a variable in the innermost (current) scope.
	 */
	public void storeInnermostVariable(QualifiedName name, Result<IValue> value) {
		if (name.getNames().size() != 1) {
			throw new ImplementationError("Local variables should not be qualified");
		}
		storeInnermostVariable(name.getNames().get(0), value);
	}

	/**
	 * Store a variable in the innermost (current) scope.
	 */
	public void storeInnermostVariable(Name name, Result<IValue> value) {
		storeInnermostVariable(Names.name(name), value);
	}

	/**
	 * Store a variable in the innermost (current) scope.
	 */
	public void storeInnermostVariable(String name, Result<IValue> value) {
		updateVariableCache(name, variableEnvironment, null); // TODO check if this is correct?
		variableEnvironment.put(name, value);
	}
	
	public void storeVariable(String name, Result<IValue> value) {
		//System.err.println("storeVariable: " + name + value.getValue());
		Map<String,Result<IValue>> env = getVariableDefiningEnvironment(name);
		if (env == null) {
			updateVariableCache(name, variableEnvironment, null);
			variableEnvironment.put(name, value);
		}
		else {
			Result<IValue> old = env.get(name);
			updateVariableCache(name, env, old);
			value.setPublic(old.isPublic());
			env.put(name, value);
		}
	}

	public void storeVariable(Name name, Result<IValue> r) {
		storeVariable(Names.name(name), r);
	}
	
	public void storeFunction(String name, Lambda function) {
		List<Lambda> list = functionEnvironment.get(name);
		if (list == null) {
			// NB: we store null in the cache so that rollback will remove the table entry on name
			// instead of restoring an empty list.
			updateFunctionCache(name, functionEnvironment, null);
			list = new ArrayList<Lambda>();
			functionEnvironment.put(name, list);
		}
		else {
			updateFunctionCache(name, functionEnvironment, list);
		}
		
		for (Lambda other : list) {
			if (function.isAmbiguous(other)) {
				throw new RedeclaredFunctionError(function.getHeader(), other.getHeader(), function.getAst());
			}
		}
		
		list.add(function);
	}
	
	
	public Map<Type, Type> getTypeBindings() {
		Environment env = this;
		Map<Type, Type> result = new HashMap<Type,Type>();
		
		while (env != null) {
			result.putAll(env.typeParameters);
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
		Environment target = parent;
		while (!target.isRoot()) {
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
	
	public Type constructorFromTuple(Type adt, String name, Type tupleType) {
		return getRoot().constructorFromTuple(adt, name, tupleType);
	}
		   
	public Type constructor(Type nodeType, String name, Object... childrenAndLabels ) {
		return getRoot().constructor(nodeType, name, childrenAndLabels);
	}
	
	public Type constructor(Type nodeType, String name, Type... children ) {
		return getRoot().constructor(nodeType, name, children);
	}
	
	public Type aliasType(String name, Type aliased, Type...parameters) {
		return getRoot().aliasType(name, aliased, parameters);
	}
	
	public TypeStore getStore() {
		return getRoot().getStore();
	}

	
		   
		   
		  
}