package org.meta_environment.rascal.interpreter.env;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.exceptions.RascalTypeError;

/**
 * A simple environment for variables and functions and types.
 */
public class Environment {
	protected final Map<String, Result> variableEnvironment;
	protected final Map<String, List<Lambda>> functionEnvironment;
	protected final Map<Type, Type> typeParameters;
	

	public Environment() {
		this.variableEnvironment = new HashMap<String, Result>();
		this.functionEnvironment = new HashMap<String, List<Lambda>>();
		this.typeParameters = new HashMap<Type, Type>();
	}
	
	/**
	 * Used in EnvironmentStack to see if this environment is a root scope.
	 */
	public boolean isModuleEnvironment() {
		return false;
	}
	
	public Lambda getFunction(String name, Type actuals, EnvironmentHolder h) {
		List<Lambda> candidates = functionEnvironment.get(name);
		
		if (candidates != null) {
			for (Lambda candidate : candidates) {
				if (candidate.match(actuals)) {
					return candidate;
				}
			}
		}
		
		return null;
	}
	
	public Result getVariable(String name) {
		return variableEnvironment.get(name);
	}
	
	public void storeParameterType(Type par, Type type) {
		typeParameters.put(par, type);
	}
	
	public Type getParameterType(Type par) {
		return typeParameters.get(par);
	}

	public void storeVariable(String name, Result value) {
		Result old = variableEnvironment.get(name);
		if (old != null) {
			value.setPublic(old.isPublic());
		}
		variableEnvironment.put(name, value);
	}
	
	public void storeVariable(Name name, Result r) {
		storeVariable(Names.name(name), r);
	}
	
	public void storeFunction(String name, Lambda function) {
		List<Lambda> list = functionEnvironment.get(name);
		if (list == null) {
			list = new ArrayList<Lambda>();
			functionEnvironment.put(name, list);
		}
		
		for (Lambda other : list) {
			if (function.isAmbiguous(other)) {
				throw new RascalTypeError("Illegal redeclaration of function: " + other + "\n overlaps with new function: " + function);
			}
		}
		
		list.add(function);
	}
	
	public Map<Type, Type> getTypeBindings() {
		return Collections.unmodifiableMap(typeParameters);
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

	

	
}