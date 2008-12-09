package org.meta_environment.rascal.interpreter;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.type.ParameterType;
import org.eclipse.imp.pdb.facts.type.TupleType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Name;

/**
 * A simple environment for variables and functions. Does not have support
 * for scopes or modules, which are a features of EnvironmentStack, 
 * GlobalEnvironment and ModuleEnvironment.
 */
public class Environment {
	protected final Map<String, EvalResult> variableEnvironment;
	protected final Map<String, List<FunctionDeclaration>> functionEnvironment;
	protected final Map<ParameterType, Type> typeEnvironment;
	protected final TypeEvaluator types;

	public Environment(TypeEvaluator te) {
		this.variableEnvironment = new HashMap<String, EvalResult>();
		this.functionEnvironment = new HashMap<String, List<FunctionDeclaration>>();
		this.typeEnvironment = new HashMap<ParameterType, Type>();
		this.types = te;
	}
	
	protected FunctionDeclaration getFunction(String name, TupleType actuals) {
		List<FunctionDeclaration> candidates = functionEnvironment.get(name);
		
		if (candidates != null) {
			for (FunctionDeclaration candidate : candidates) {
				TupleType formals = (TupleType) candidate.getSignature().accept(types);
			
				if (actuals.isSubtypeOf(formals)) {
					return candidate;
				}
			}
		}
		
		return null;
	}
	
	protected EvalResult getVariable(String name) {
		//System.err.println("getVariable " + name + " => " + variableEnvironment.get(name));
		return variableEnvironment.get(name);
	}
	
	public EvalResult getVariable(Name name) {
		return getVariable(name.toString());
	}
	
	protected void storeType(ParameterType par, Type type) {
		typeEnvironment.put(par, type);
	}
	
	protected Type getType(ParameterType par) {
		return typeEnvironment.get(par);
	}
	
	protected void storeVariable(String name, EvalResult value) {
		//System.err.println("storeVariable " + name + " => " + value);
		variableEnvironment.put(name, value);
	}
	
	protected void storeFunction(String name, FunctionDeclaration function) {
		TupleType formals = (TupleType) function.getSignature().getParameters().accept(types);
		FunctionDeclaration definedEarlier = getFunction(name, formals);
		
		if (definedEarlier != null) {
			throw new RascalTypeError("Illegal redeclaration of function: " + definedEarlier + "\n overlaps with new function: " + function);
		}
		
		List<FunctionDeclaration> list = functionEnvironment.get(name);
		if (list == null) {
			list = new LinkedList<FunctionDeclaration>();
			functionEnvironment.put(name, list);
		}
		
		list.add(function);
	}
	
	public boolean isModuleEnvironment() {
		return false;
	}
	
	public boolean isGlobalEnvironment() {
		return false;
	}
	
	public Map<ParameterType, Type> getTypes() {
		return Collections.unmodifiableMap(typeEnvironment);
	}
	
	public void storeTypes(Map<ParameterType, Type> bindings) {
		typeEnvironment.putAll(bindings);
	}

}