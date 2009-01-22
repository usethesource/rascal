package org.meta_environment.rascal.interpreter.env;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.TypeEvaluator;
import org.meta_environment.rascal.interpreter.exceptions.RascalTypeError;

/**
 * A simple environment for variables and functions and types.
 */
public class Environment {
	protected final Map<String, EvalResult> variableEnvironment;
	protected final Map<String, List<FunctionDeclaration>> functionEnvironment;
	protected final Map<Type, Type> typeParameters;
	

	public Environment() {
		this.variableEnvironment = new HashMap<String, EvalResult>();
		this.functionEnvironment = new HashMap<String, List<FunctionDeclaration>>();
		this.typeParameters = new HashMap<Type, Type>();
	}
	
	/**
	 * Used in EnvironmentStack to see if this environment is a root scope.
	 */
	public boolean isModuleEnvironment() {
		return false;
	}
	
	public FunctionDeclaration getFunction(String name, Type actuals, EnvironmentHolder h) {
		List<FunctionDeclaration> candidates = functionEnvironment.get(name);
		
		if (candidates != null) {
			for (FunctionDeclaration candidate : candidates) {
				Type formals = candidate.getSignature().accept(TypeEvaluator.getInstance());
				//System.err.println("getFunction: formals=" + formals + ", " + "actuals=" + actuals);
				if (actuals.isSubtypeOf(formals)) {
					h.setEnvironment(this);
					return candidate;
				}
				else if (isVarArgsFunction(candidate)) {
					if (matchVarArgsFunction(candidate, formals, actuals)) {
						return candidate;
					}
				}
			}
		}
		
		return null;
	}
	
	private boolean matchVarArgsFunction(FunctionDeclaration candidate,
			Type formals, Type actuals) {
		int arity = formals.getArity();
		int i;
		
		for (i = 0; i < arity - 1; i++) {
			if (!actuals.getFieldType(i).isSubtypeOf(formals.getFieldType(i))) {
				return false;
			}
		}
		
		if (i > actuals.getArity()) {
			return false;
		}

		Type elementType = formals.getFieldType(i).getElementType();

		for (; i < actuals.getArity(); i++) {
			if (!actuals.getFieldType(i).isSubtypeOf(elementType)) {
				return false;
			}
		}
		
		return true;
	}

	private boolean isVarArgsFunction(FunctionDeclaration candidate) {
		return candidate.getSignature().getParameters().isVarArgs();
	}
	
	public EvalResult getVariable(String name) {
		return variableEnvironment.get(name);
	}
	
	public void storeParameterType(Type par, Type type) {
		typeParameters.put(par, type);
	}
	
	public Type getParameterType(Type par) {
		return typeParameters.get(par);
	}

	public void storeVariable(String name, EvalResult value) {
		variableEnvironment.put(name, value);
	}
	
	public void storeVariable(Name name, EvalResult r) {
		storeVariable(Names.name(name), r);
	}
	
	public void storeFunction(String name, FunctionDeclaration function) {
		Type formals = function.getSignature().getParameters().accept(TypeEvaluator.getInstance());
		FunctionDeclaration definedEarlier = getFunction(name, formals, new EnvironmentHolder());
		
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