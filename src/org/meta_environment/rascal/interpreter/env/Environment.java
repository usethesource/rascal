package org.meta_environment.rascal.interpreter.env;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredFunctionError;
import org.meta_environment.rascal.interpreter.utils.Names;

/**
 * A simple environment for variables and functions and types.
 * TODO: this class does not support shadowing of variables and functions yet, which is wrong.
 */
public class Environment {
	protected final Map<String, Result<IValue>> variableEnvironment;
	protected final Map<String, List<Lambda>> functionEnvironment;
	protected final Map<Type, Type> typeParameters;
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
		this.functionEnvironment = new HashMap<String, List<Lambda>>();
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
			Map<String, List<Lambda>> env, List<Lambda> list) {
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
		/**
		if (callerScope != null) {
			return false;
		}

		if (getParent() != null) {
			return getParent().isRootStackFrame();
		}

		return true;
		 */
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

		return isRootScope() ? null : parent.getFunction(name, actuals, useSite);
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
	public Result<IValue> getInnermostVariable(Name name) {
		return getInnermostVariable(Names.name(name));
	}

	/**
	 * Returns a variable from the innermost scope, if it exists.
	 */
	public Result<IValue> getInnermostVariable(String name) {
		return variableEnvironment.get(name);
	}

	/**
	 * Look up a variable, traversing the parents of the current scope
	 * until it is found, but don't look in a module scope (which means
	 * stop in the shell or in the function scope). When the variable
	 * is qualified however, the variable is looked up in the named scope.
	 */
	public Result<IValue> getLocalVariable(QualifiedName name) {
		if (name.getNames().size() > 1) {
			Environment current = this;
			while (!current.isRootScope()) {
				current = current.parent;
			}

			return current.getVariable(name);
		}

		String varName = Names.name(Names.lastName(name));
		return getLocalVariable(varName);
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

		if (isRootScope() || parent.isRootScope()) {
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
			while (!current.isRootScope()) {
				current = current.parent;
			}

			return current.getVariable(name);
		}

		String varName = Names.name(Names.lastName(name));
		Result<IValue> r = getVariable(name, varName);

		if (r != null) {
			return r;
		}

		return isRootScope() ? null : parent.getVariable(name);
	}

	/**
	 * Look up a variable, traversing the parents of the current scope
	 * until it is found.
	 * @param name
	 * @return
	 */
	public Result<IValue> getVariable(String name) {
		Result<IValue> r = variableEnvironment.get(name);
		if (r != null) {
			return r;
		}
		return isRootScope() ? null : parent.getVariable(name);
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

	public Result<IValue> getVariable(AbstractAST ast, String name) {
		Result<IValue> t = variableEnvironment.get(name);
		if (t == null) {
			return isRootScope() ? null : parent.getVariable(ast, name);
		}
		return t;
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

	/**
	 * Store a variable the scope that it is declared in, down to the 
	 * module scope if needed.
	 */
	public void storeVariable(String name, Result<IValue> value) {
		//System.err.println("storeVariable: " + name + value.getValue());
		Map<String,Result<IValue>> env = getVariableDefiningEnvironment(name);
		if (env == null) {
			updateVariableCache(name, variableEnvironment, null);
			variableEnvironment.put(name, value);
			//System.out.println("Inferred: " + name);
			if (value != null) {
				value.setInferredType(true);
			}
		}
		else {
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

	public void storeLocalVariable(QualifiedName name, Result<IValue> result) {
		if (name.getNames().size() > 1) {
			storeVariable(name, result);
		}
		else {
			String varName = Names.name(Names.lastName(name));
			storeLocalVariable(varName, result);
		}
	}

	public void storeLocalVariable(Name name, Result<IValue> r) {
		storeLocalVariable(Names.name(name), r);
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

	public Type concreteSyntaxType(String name, org.meta_environment.rascal.ast.Type type) {
		return getRoot().concreteSyntaxType(name, type);
	}

	public Type concreteSyntaxType(String name, IConstructor symbol) {
		return getRoot().concreteSyntaxType(name, symbol);
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

	public Map<String, Result<IValue>> getVariables() {
		Map<String, Result<IValue>> vars = new HashMap<String, Result<IValue>>();
		if (parent != null) {
			vars.putAll(parent.getVariables());
		}
		vars.putAll(variableEnvironment);
		return vars;
	}

	public List<Entry<String, List<Lambda>>> getFunctions() {
		List<Entry<String, List<Lambda>>> functions = new ArrayList<Entry<String, List<Lambda>>>();
		if (parent != null) {
			functions.addAll(parent.getFunctions());
		}
		functions.addAll(functionEnvironment.entrySet());
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

	public boolean isDeclaredFunctionName(QualifiedName qualifiedName) {
		return getFunction(name, TypeFactory.getInstance().voidType(), qualifiedName) != null;
	}

}