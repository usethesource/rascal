package org.meta_environment.rascal.interpreter;

import java.util.HashMap;
import java.util.Map;

import org.meta_environment.rascal.ast.FunctionBody;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Signature;

public class Environment {
	private final Map<String, EvalResult> variableEnvironment;
	private final Map<String, FunctionDeclaration> functionEnvironment;

	public Environment() {
		this.variableEnvironment = new HashMap<String, EvalResult>();
		this.functionEnvironment = new HashMap<String, FunctionDeclaration>();
	}
	
	// TODO overloading
	public FunctionDeclaration getFunction(String name) {
		return functionEnvironment.get(name);
	}
	
	public EvalResult getVariable(String name) {
		return variableEnvironment.get(name);
	}
	
	public void storeVariable(String name, EvalResult value) {
		variableEnvironment.put(name, value);
		System.err.println("put(" + name + ", " + value + ")");
	}
	
	public void storeFunction(String name, FunctionDeclaration function) {
		functionEnvironment.put(name, function);
	}
	
 // Function declarations -----------------------------------------
	
	class FunctionDeclarationX {
		Signature signature;
		FunctionBody body;
		
		FunctionDeclarationX(Signature s, FunctionBody b){
			signature = s;
			body = b;
		}
	}
}