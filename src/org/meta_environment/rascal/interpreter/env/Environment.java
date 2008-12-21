package org.meta_environment.rascal.interpreter.env;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.type.ParameterType;
import org.eclipse.imp.pdb.facts.type.TupleType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.interpreter.EvalResult;
import org.meta_environment.rascal.interpreter.RascalTypeError;
import org.meta_environment.rascal.interpreter.TypeEvaluator;

/**
 * A simple environment for variables and functions and types.
 */
public class Environment {
	protected final Map<String, EvalResult> variableEnvironment;
	protected final Map<String, List<FunctionDeclaration>> functionEnvironment;
	protected final Map<ParameterType, Type> typeParameters;
	

	public Environment() {
		this.variableEnvironment = new HashMap<String, EvalResult>();
		this.functionEnvironment = new HashMap<String, List<FunctionDeclaration>>();
		this.typeParameters = new HashMap<ParameterType, Type>();
		
	}
	
	/**
	 * Used in EnvironmentStack to see if this environment is a root scope.
	 */
	public boolean isModuleEnvironment() {
		return false;
	}
	
	public FunctionDeclaration getFunction(String name, TupleType actuals) {
		List<FunctionDeclaration> candidates = functionEnvironment.get(name);
		
		if (candidates != null) {
			for (FunctionDeclaration candidate : candidates) {
				//System.err.println("getFunction: candidate=" + candidate);
				TupleType formals = (TupleType) candidate.getSignature().accept(TypeEvaluator.getInstance());
				//System.err.println("formals = " + formals);
				//System.err.println("formals.basetype = " + formals.getBaseType());
				//System.err.println("formals.isNamedType = " + formals.isNamedType());
				//System.err.println("actuals = " + actuals);
				
				if (actuals.isSubtypeOf(formals)) {
					return candidate;
				}
			}
		}
		
		return null;
	}
	
	
	public EvalResult getVariable(String name) {
		return variableEnvironment.get(name);
	}
	
	public void storeType(ParameterType par, Type type) {
		typeParameters.put(par, type);
	}
	
	public Type getType(ParameterType par) {
		return typeParameters.get(par);
	}
	
	

	public void storeVariable(String name, EvalResult value) {
		variableEnvironment.put(name, value);
	}
	
	
	public void storeFunction(String name, FunctionDeclaration function) {
		TupleType formals = (TupleType) function.getSignature().getParameters().accept(TypeEvaluator.getInstance());
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
	
	public Map<ParameterType, Type> getTypes() {
		return Collections.unmodifiableMap(typeParameters);
	}
	
	public void storeTypes(Map<ParameterType, Type> bindings) {
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