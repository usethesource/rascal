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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.debug.IRascalFrame;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.ModuleEnvironment.GenericKeywordParameters;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ConstructorFunction;
import org.rascalmpl.interpreter.result.OverloadedFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.utils.Names;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeStore;

/**
 * A simple environment for variables and functions and types.
 */
public class Environment implements IRascalFrame {
	
	protected static class NameFlags {
		public final static int FINAL_NAME = 0x01;
		public final static int OVERLOADABLE_NAME = 0x02;
		public final static int PRIVATE_NAME = 0x04;
		private final int flags;

		public NameFlags() { 
			this.flags = 0;
		};

		private NameFlags(int flags) {
			this.flags = flags;
		}

		public NameFlags merge(NameFlags other) {
			return new NameFlags(flags | other.flags);
		}

		public NameFlags makeFinal() {
			return new NameFlags(flags | FINAL_NAME);
		}

		public NameFlags makePrivate() {
			return new NameFlags(flags | PRIVATE_NAME);
		}

		public NameFlags makeOverloadable() {
			return new NameFlags(flags | OVERLOADABLE_NAME);
		}

		boolean isFinal() {
			return (flags & FINAL_NAME) != 0;
		}

		boolean isPrivate() {
			return (flags & PRIVATE_NAME) != 0;
		}

		boolean isOverloadable() {
			return (flags & OVERLOADABLE_NAME) != 0;
		}
	}
	
	protected Map<String, Result<IValue>> variableEnvironment;
	protected Map<String, List<AbstractFunction>> functionEnvironment;
	protected Map<String, NameFlags> nameFlags;
	protected Map<Type, Type> staticTypeParameters;
	protected Map<Type, Type> dynamicTypeParameters;
	protected final Environment parent;
	protected final Environment callerScope;
	protected ISourceLocation callerLocation; // different from the scope location (more precise)
	protected final ISourceLocation loc;
	protected final String name;
	private Environment myRoot;
	private boolean isFunctionFrame;

	
	@Override
	public boolean equals(Object obj) {
	  if (obj instanceof Environment) {
	    Environment o = (Environment) obj;
	    
	    if (callerScope != o.callerScope 
          || !loc.equals(o.loc) 
          || !name.equals(o.name)) {
	      return false;
	    }
	    
	    if (!equalMaps(variableEnvironment, o.variableEnvironment)) {
	      return false;
	    }
	    
	    if (!equalMaps(functionEnvironment, o.functionEnvironment)) {
	      return false;
	    }
	    
	    return true;
	  }
	  
	  return false;
  }

	private <Key,Value> boolean equalMaps(Map<Key, Value> a, Map<Key, Value> b) {
	  if (b == null && a == null) {
	    return true;
	  }
	  
	  if (b == null || a == null) {
	    return false;
	  }
	  
	  if (a.size() != b.size()) {
	    return false;
	  }
	  
	  for (Key k : a.keySet()) {
	    if (!b.containsKey(k) || !a.get(k).equals(b.get(k))) {
	      return false;
	    }
	  }
	  
	  return true;
	}
	
	@Override
	public int hashCode() {
	  int code = 1331 * name.hashCode();
	  
	  if (variableEnvironment != null) {
	    code += variableEnvironment.hashCode() * 13;
	  }
	  
	  if (functionEnvironment != null) {
	    code += functionEnvironment.hashCode();
	  }
	  
	  return code;
	}
	
	
	public Environment(ISourceLocation loc, String name) {
		this(null, null, null, loc, name);
	}

	public Environment(Environment parent, ISourceLocation loc, String name) {
		this(parent, null, null, loc, name);
	}

	public Environment(Environment parent, Environment callerScope, ISourceLocation callerLocation, ISourceLocation loc, String name) {
		this.parent = parent;
		this.loc = loc;
		this.name = name;
		this.callerScope = callerScope;
		this.callerLocation = callerLocation;
		if (parent == this) {
			throw new ImplementationError("internal error: cyclic environment");
		}
	}

	protected Environment(Environment old) {
		this.parent = old.parent;
		this.loc = old.loc;
		this.name = old.name;
		this.callerScope = old.callerScope;
		this.callerLocation = old.callerLocation;
		this.variableEnvironment = old.variableEnvironment;
		this.functionEnvironment = old.functionEnvironment;
		this.staticTypeParameters = old.staticTypeParameters;
		this.dynamicTypeParameters = old.dynamicTypeParameters;
		this.nameFlags = old.nameFlags;
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
	 * @return value of variable
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
		return getFrameVariable(varName);
	}
	
	public Result<IValue> getFunctionForReturnType(Type returnType, String name) {
	    List<AbstractFunction> candidates = new LinkedList<>();
        
        getAllFunctions(name, candidates);
        
        if (candidates.isEmpty()) {
            return null;
        }
        
        List<AbstractFunction> filtered = new LinkedList<>();
        
        for (AbstractFunction candidate : candidates) {
            if (candidate.getReturnType().isAbstractData() && candidate.getReturnType() == returnType) {
                filtered.add(candidate);
            }
        }

        if (filtered.isEmpty()) {
            return null;  
        }
        
        return new OverloadedFunction(name, filtered);
	}

	public Result<IValue> getVariable(Name name) {
		return getFrameVariable(Names.name(name));
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

	public Result<IValue> getSimpleVariable(QualifiedName name) {
		if (name.getNames().size() > 1) {
			Environment current = this;
			while (!current.isRootScope()) {
				current = current.parent;
			}

			return current.getSimpleVariable(name);
		}

		String varName = Names.name(Names.lastName(name));
		return getSimpleVariable(varName);
	}
	
	public Result<IValue> getFrameVariable(String name) {
		Result<IValue> t = getSimpleVariable(name);
		
		if (t != null) {
			return t;
		}
		
		List<AbstractFunction> funcs = new LinkedList<AbstractFunction>(); 
		getAllFunctions(name, funcs);
		
		if (funcs.isEmpty()) {
			return null;
		}
		else if (funcs.size() == 1) {
			return funcs.get(0);
		}
		else {
			return new OverloadedFunction(name, funcs);
		}
	}
	
	public Result<IValue> getSimpleVariable(Name name) {
		return getSimpleVariable(Names.name(name));
	}
	
	public Result<IValue> getSimpleVariable(String name) {
		Result<IValue> t = null;
		
		if (variableEnvironment != null) {
			t = variableEnvironment.get(name);
		}
		
		if (t != null) {
			return t;
		}
		
		if (!isRootScope()) {
			return parent.getSimpleVariable(name);
		}
		
		return null;
	}
	
	public void getAllFunctions(String name, List<AbstractFunction> collection) {
		if (functionEnvironment != null) {
			List<AbstractFunction> locals = functionEnvironment.get(name);
			
			if (locals != null) {
				collection.addAll(locals);
			}
		}
		
		if (parent != null) {
			parent.getAllFunctions(name, collection);
		}
	}
	
	public ConstructorFunction getConstructorFunction(Type constructorType) {
		List<AbstractFunction> list = new LinkedList<>();
		getAllFunctions(constructorType.getAbstractDataType(), constructorType.getName(), list);
		
		for (AbstractFunction candidate : list) {
			if (candidate instanceof ConstructorFunction) {
				ConstructorFunction func = (ConstructorFunction) candidate;
				
				if (func.getName().equals(constructorType.getName()) 
						&& constructorType.isSubtypeOf(func.getConstructorType())) {
					return func;
				}
			}
		}
		
		return null; // not found
	}
	
	public void getAllFunctions(Type returnType, String name, List<AbstractFunction> collection) {
		if (functionEnvironment != null) {
			List<AbstractFunction> locals = functionEnvironment.get(name);
			
			if (locals != null) {
				for (AbstractFunction func : locals) {
					if (func.getReturnType().comparable(returnType)) {
						collection.add(func);
					}
				}
			}
		}
		
		if (parent != null) {
			parent.getAllFunctions(returnType, name, collection);
		}
	}

	protected boolean isNameFlagged(QualifiedName name, Predicate<NameFlags> tester) {
		if (name.getNames().size() > 1) {
			Environment current = this;
			while (!current.isRootScope()) {
				current = current.parent;
			}
			
			return current.isNameFlagged(name, tester);
		}
		
		String simpleName = Names.name(Names.lastName(name));
		return isNameFlagged(simpleName, tester);
		
	}
	
	protected boolean isNameFlagged(String name, Predicate<NameFlags> tester) {
		Environment flaggingEnvironment = getFlagsEnvironment(name);
		if (flaggingEnvironment == null) {
			return false;
		}

		return flaggingEnvironment.nameFlags.containsKey(name) 
			&& tester.test(flaggingEnvironment.nameFlags.get(name));
	}

	public boolean isNameFinal(QualifiedName name) {
		return isNameFlagged(name, NameFlags::isFinal);
	}

	public boolean isNamePrivate(String name) {
		return isNameFlagged(name, NameFlags::isPrivate);
	}

	public boolean isNamePrivate(QualifiedName name) {
		return isNameFlagged(name, NameFlags::isPrivate);
	}

	public boolean isNameOverloadable(String name) {
		return isNameFlagged(name, NameFlags::isOverloadable);
	}

	public boolean isNameOverloadable(QualifiedName name) {
		return isNameFlagged(name, NameFlags::isOverloadable);
	}

	protected void flagName(QualifiedName name, NameFlags flags) {
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

	/** 
	 * The assumption is that the environment level is already correct, i.e.,
     * we are not requested to mark a name that is higher up in the hierarchy.
     */
	protected void flagName(String name, NameFlags flags) {
		if (nameFlags == null) {
			nameFlags = new HashMap<String,NameFlags>();
		}
		
		if (nameFlags.containsKey(name)) {
			nameFlags.put(name, nameFlags.get(name).merge(flags));
		}
		else {
			nameFlags.put(name, flags);
		}
	}
	
	public void markNameFinal(QualifiedName name) {
		flagName(name, new NameFlags().makeFinal());
	}
	
	public void markNameFinal(String name) {
		flagName(name, new NameFlags().makeFinal());
	}

	public void markNamePrivate(QualifiedName name) {
		flagName(name, new NameFlags().makePrivate());
	}
	
	public void markNamePrivate(String name) {
		flagName(name, new NameFlags().makePrivate());
	}

	public void markNameOverloadable(QualifiedName name) {
		flagName(name, new NameFlags().makeOverloadable());
	}

	public void markNameOverloadable(String name) {
		flagName(name, new NameFlags().makeOverloadable());
	}
	
	public void storeStaticParameterType(Type par, Type type) {
		if (staticTypeParameters == null) {
			staticTypeParameters = new HashMap<Type,Type>();
		}
		staticTypeParameters.put(par, type);
	}
	
	public void storeDynamicParameterType(Type par, Type type) {
        if (dynamicTypeParameters == null) {
            dynamicTypeParameters = new HashMap<Type,Type>();
        }
        dynamicTypeParameters.put(par, type);
    }

	public Type getStaticParameterType(Type par) {
		if (staticTypeParameters != null) {
			return staticTypeParameters.get(par);
		}
		return null;
	}
	
	public Type getDynamicParameterType(Type par) {
        if (dynamicTypeParameters != null) {
            return dynamicTypeParameters.get(par);
        }
        return null;
    }

	/**
	 * Search for the environment that declared a variable.
	 */
	protected Map<String,Result<IValue>> getVariableDefiningEnvironment(String name) {
		if (variableEnvironment != null) {
			Result<IValue> r = variableEnvironment.get(name);

			if (r != null) {
				return variableEnvironment;
			}
		}

		return (parent == null) ? null : parent.getVariableDefiningEnvironment(name);
	}

	/**
	 * Search for the environment that declared a variable, but stop just above
	 * the module scope such that we end up in a function scope or a shell scope.
	 */
	private Map<String,Result<IValue>> getLocalVariableDefiningEnvironment(String name) {
		if (variableEnvironment != null) {
			Result<IValue> r = variableEnvironment.get(name);

			if (r != null) {
				return variableEnvironment;
			}
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
		Map<String,Result<IValue>> env = getVariableDefiningEnvironment(name);
		
		if (env == null) {
			// an undeclared variable, which gets an inferred type
			if (variableEnvironment == null) {
				variableEnvironment = new HashMap<String,Result<IValue>>();
			}
			variableEnvironment.put(name, value);
			//System.out.println("Inferred: " + name);
			if (value != null) {
				// this is a dangerous design decision, even typed declared variables will not be "inferred"
				// it could lead to subtle bugs
				value.setInferredType(true);
			}
		}
		else {
			// a declared variable
			env.put(name, value);
		}
	}

	public void declareAndStoreInferredInnerScopeVariable(String name, Result<IValue> value) {
		declareVariable(value.getStaticType(), name);
		// this is dangerous to do and should be rethought, it could lead to variabled being inferred while they should not be.
		value.setInferredType(true);
		variableEnvironment.put(name, value);
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

		if (old == null) {
			value.setInferredType(true);
		}

		env.put(name, value);
	}

	public void storeVariable(Name name, Result<IValue> r) {
		storeVariable(Names.name(name), r);
	}
 
	public void storeFunction(String name, AbstractFunction function) {
		List<AbstractFunction> list = null;
		
		if (functionEnvironment != null) {
			list = functionEnvironment.get(name);
		}
		
		if (list == null || !this.isNameOverloadable(name)) {
			list = new ArrayList<>(1); // we allocate an array list of 1, since most cases it's only a single overload, and we want to avoid allocating more memory than needed
			
			if (functionEnvironment == null) {
				functionEnvironment = new HashMap<String, List<AbstractFunction>>();
			}
			functionEnvironment.put(name, list);
		}

		if (!list.contains(function)) {
			list.add(function);
			functionEnvironment.put(name, list);
		
			if (function.hasResourceScheme()) {
				if (getRoot() instanceof ModuleEnvironment) {
					((ModuleEnvironment)getRoot()).addResourceImporter(function);
				}
			}

			if (function.hasResolverScheme()) {
				getRoot().getHeap().registerSourceResolver(function.getResolverScheme(), function);
			}
		}
	}

	public boolean declareVariable(Type type, Name name) {
		return declareVariable(type, Names.name(name));
	}

	public boolean declareVariable(Type type, String name) {
		if (variableEnvironment != null) {
			if (variableEnvironment.get(name) != null) {
				return false;
			}
		}
		if (variableEnvironment == null) {
			variableEnvironment = new HashMap<String,Result<IValue>>();
		}
		variableEnvironment.put(name, ResultFactory.nothing(type));
		return true;
	}
	
	public Map<Type, Type> getStaticTypeBindings() {
		Environment env = this;
		Map<Type, Type> result = null;

		while (env != null) {
			if (env.staticTypeParameters != null) {
				for (Type key : env.staticTypeParameters.keySet()) {
				    if (result ==  null) {
				        result = new HashMap<>();
				    }
					if (!result.containsKey(key)) {
						result.put(key, env.staticTypeParameters.get(key));
					}
				}
			}
			env = env.parent;
		}

		if (result == null) {
		    return Collections.emptyMap();
		}
		return Collections.unmodifiableMap(result);
	}

	public Map<Type, Type> getDynamicTypeBindings() {
        Environment env = this;
        Map<Type, Type> result = null;

        while (env != null) {
            if (env.dynamicTypeParameters != null) {
                for (Type key : env.dynamicTypeParameters.keySet()) {
                    if (result ==  null) {
                        result = new HashMap<>();
                    }
                    if (!result.containsKey(key)) {
                        result.put(key, env.dynamicTypeParameters.get(key));
                    }
                }
            }
            env = env.parent;
        }

        if (result == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(result);
    }
	
	public void storeStaticTypeBindings(Map<Type, Type> bindings) {
		if (staticTypeParameters == null) {
			staticTypeParameters = new HashMap<Type, Type>();
		}
		staticTypeParameters.putAll(bindings);
	}
	
	public void storeDynamicTypeBindings(Map<Type, Type> bindings) {
        if (dynamicTypeParameters == null) {
            dynamicTypeParameters = new HashMap<Type, Type>();
        }
        dynamicTypeParameters.putAll(bindings);
    }

	@Override
	public String toString(){
		StringBuffer res = new StringBuffer();
		if (functionEnvironment != null) {
			for(String name : functionEnvironment.keySet()){
				res.append(name).append(": ").append(functionEnvironment.get(name)).append("\n");
			}
		}
		if (variableEnvironment != null) {
			for(String name : variableEnvironment.keySet()){
				res.append(name).append(": ").append(variableEnvironment.get(name)).append("\n");
			}
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
		if (this.isRootScope()) {
			return this;
		}
		if (myRoot == null) {
			myRoot = parent.getRoot();
		}
		return myRoot;
	}
	
	public GlobalEnvironment getHeap() {
		return getRoot().getHeap();
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

	public Type concreteSyntaxType(String name, IConstructor symbol) {
		return getRoot().concreteSyntaxType(name, symbol);
	}

	public ConstructorFunction constructorFromTuple(AbstractAST ast, Evaluator eval, Type adt, String name, Type tupleType, List<KeywordFormal> initializers) {
		return getRoot().constructorFromTuple(ast, eval, adt, name, tupleType, initializers);
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
		if (variableEnvironment != null) {
			vars.putAll(variableEnvironment);
		}
		return vars;
	}

	public List<Pair<String, List<AbstractFunction>>> getFunctions() {
		ArrayList<Pair<String, List<AbstractFunction>>> functions = new ArrayList<Pair<String, List<AbstractFunction>>>();
		if (parent != null) {
			functions.addAll(parent.getFunctions());
		}
		if (functionEnvironment != null) {
			// don't just add the Map.Entries, as they may not live outside the iteration
			for (Entry<String, List<AbstractFunction>> entry : functionEnvironment.entrySet()) {
				functions.add(new Pair<String, List<AbstractFunction>>(entry.getKey(), entry.getValue()));
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
		this.variableEnvironment = null;
		this.functionEnvironment = null;
		this.staticTypeParameters = null;
		this.nameFlags = null;
	}
	
	protected void extendNameFlags(Environment other) {
		// note that the flags need to be extended before other things, since
		// they govern how overloading is handled in functions.
		if (other.nameFlags != null) {
			if (this.nameFlags == null) {
				this.nameFlags = new HashMap<String, NameFlags>();
			}

			for (String name : other.nameFlags.keySet()) {
				NameFlags flags = this.nameFlags.get(name);

				if (flags != null) {
					this.nameFlags.put(name, flags.merge(other.nameFlags.get(name)));
				}
				else {
					this.nameFlags.put(name, other.nameFlags.get(name));
				}
			}
		}
	}
	
	public void extend(Environment other) {
	  extendNameFlags(other);
	  extendVariableEnv(other);
	  extendFunctionEnv(other);
	  extendTypeParams(other);
	}

	protected void extendTypeParams(Environment other) {
		if (other.staticTypeParameters != null) {
		    if (this.staticTypeParameters == null) {
		      this.staticTypeParameters = new HashMap<Type, Type>();
		    }
		    this.staticTypeParameters.putAll(other.staticTypeParameters);
		  }
	}
	
	protected void extendFunctionEnv(Environment other) {
		if (other.functionEnvironment != null) {
		    if (this.functionEnvironment == null) {
		      this.functionEnvironment = new HashMap<String, List<AbstractFunction>>();
		    }
		    
		    for (String name : other.functionEnvironment.keySet()) {
		      List<AbstractFunction> otherFunctions = other.functionEnvironment.get(name);
		      
		      if (otherFunctions != null) {
		        for (AbstractFunction function : otherFunctions) {
		          storeFunction(name, (AbstractFunction) function.cloneInto(this));
		        }
		      }
		    }
		  }
	}

	protected void extendVariableEnv(Environment other) {
		if (other.variableEnvironment != null) {
		    if (this.variableEnvironment == null) {
		      this.variableEnvironment = new HashMap<String, Result<IValue>>();
		    }
		    // TODO: if variables are bound to closures
		    // we have to cloneInto those values to.
		    this.variableEnvironment.putAll(other.variableEnvironment);
		  }
	}

	// TODO: We should have an extensible environment model that doesn't
	// require this type of checking, but instead stores all the info on
	// a name in one location...
	protected Environment getFlagsEnvironment(String name) {
		if (this.variableEnvironment != null) {
			if (this.variableEnvironment.get(name) != null) {
				if (this.nameFlags != null) {
					if (nameFlags.get(name) != null) {
						return this; // Found it at this level, return the environment
					}
				}
				return null; // Found the name, but no flags, so return null
			}
		}
		
		if (this.functionEnvironment != null) {
			if (this.functionEnvironment.get(name) != null) {
				if (this.nameFlags != null) {
					if (nameFlags.get(name) != null) {
						return this; // Found it at this level, return the environment
					}
				}
				return null; // Found the name, but no flags, so return null
			}
		}
		
		if (!isRootScope()) {
			return parent.getFlagsEnvironment(name);
		}

		return null;
	}

	public void declareConstructorKeywordParameter(Type onType, String label,
			Type valueType) {
		getRoot().declareConstructorKeywordParameter(onType, label, valueType);
	}

	public Set<GenericKeywordParameters> lookupGenericKeywordParameters(Type adt) {
		return getRoot().lookupGenericKeywordParameters(adt);
	}

	public void declareGenericKeywordParameters(Type adt, Type kwTypes,
			List<KeywordFormal> formals) {
		getRoot().declareGenericKeywordParameters(adt, kwTypes, formals);
	}

	public Map<String, Type> getKeywordParameterTypes(Type ontype) {
		return getRoot().getKeywordParameterTypes(ontype);
	}

    @Override
    public Set<String> getFrameVariables() {
        return getVariables().keySet();
    }

    public void markAsFunctionFrame() {
		this.isFunctionFrame = true;
    }

	public boolean isFunctionFrame() {
		return isFunctionFrame;
	}
}

