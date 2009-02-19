package org.meta_environment.rascal.interpreter.env;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;
import org.meta_environment.rascal.interpreter.errors.TypeError;
import org.meta_environment.rascal.interpreter.result.Result;

/**
 * A simple environment for variables and functions and types.
 * TODO: this class does not support shadowing of variables and functions yet, which is wrong.
 */
public class Environment {
	protected final Map<String, Result> variableEnvironment;
	protected final Map<String, List<Lambda>> functionEnvironment;
	protected final Map<Type, Type> typeParameters;
	protected final Environment parent;
	protected Cache cache = null;

	public Environment(Environment parent) {
		this(parent, parent.getCache());
	}
	
	protected Environment(Environment parent, Cache cache) {
		this.variableEnvironment = new HashMap<String, Result>();
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

	
	private void updateVariableCache(String name, Map<String, Result> env, Result old) {
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
	
	public Lambda getFunction(String name, Type actuals) {
		List<Lambda> candidates = functionEnvironment.get(name);
		
		if (candidates != null) {
			for (Lambda candidate : candidates) {
				if (candidate.match(actuals)) {
					return candidate;
				}
			}
		}
		
		return isRoot() ? null : parent.getFunction(name, actuals);
	}
	
	public Result getVariable(QualifiedName name) {
		String varName = Names.name(Names.lastName(name));
		Result r = getVariable(name, varName);
		
		if (r != null) {
			return r;
		}
		
		return isRoot() ? null : parent.getVariable(name);
	}
	
	public void storeVariable(QualifiedName name, Result result) {
		String varName = Names.name(Names.lastName(name));
		storeVariable(varName, result);
	}
	
	public Result getVariable(AbstractAST ast, String name) {
		Result t = variableEnvironment.get(name);
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

	private Map<String,Result> getVariableDefiningEnvironment(String name) {
		Result r = variableEnvironment.get(name);
		
		if (r != null) {
			return variableEnvironment;
		}
		
		return isRoot() ? null : parent.getVariableDefiningEnvironment(name);
	}
	
	
	
	public void storeVariable(String name, Result value) {
		Map<String,Result> env = getVariableDefiningEnvironment(name);
		if (env == null) {
			updateVariableCache(name, variableEnvironment, null);
			variableEnvironment.put(name, value);
		}
		else {
			Result old = env.get(name);
			updateVariableCache(name, env, old);
			value.setPublic(old.isPublic());
			env.put(name, value);
		}
	}


	public void storeVariable(Name name, Result r) {
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
				throw new TypeError("Declaration `" + function.getHeader() + "' overlaps with `" + other.getHeader() + "`", function.getAst());
			}
		}
		
		list.add(function);
	}
	
	
	public Map<Type, Type> getTypeBindings() {
		Environment env = this;
		Map<Type, Type> result = new HashMap<Type,Type>();
		
		while (env != null) {
			result.putAll(typeParameters);
			env = env.parent;
		}
		
		return Collections.unmodifiableMap(result);
	}
	
	public void storeTypeBindings(Map<Type, Type> bindings) {
		typeParameters.putAll(bindings);
	}
	
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

	public Map<String, Type> getAnnotations(Type onType) {
		return getRoot().getAnnotations(onType);
	}

	public Type getAnnotationType(Type onType, String name) {
		return getRoot().getAnnotationType(onType, name);
	}

	public boolean isTreeConstructorName(QualifiedName name, Type signature) {
		return getRoot().isTreeConstructorName(name, signature);
	}

	private Environment getRoot() {
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

	public Type getTypeAlias(String name) {
		return getRoot().getTypeAlias(name);
	}

}