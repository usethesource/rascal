package org.meta_environment.rascal.interpreter;

import org.eclipse.imp.pdb.facts.type.TupleType;
import org.meta_environment.rascal.ast.FunctionDeclaration;

public class Module {
	private final Environment env = new Environment();
	private final String name;

	public Module(String name) {
		this.name = name;
	}
	
	public FunctionDeclaration getFunction(String name, TupleType actuals) {
		return env.getFunction(name, actuals);
	}
	
	public EvalResult getVariable(String name) {
		return env.getVariable(name);
	}
	
	public void storeFunction(String name, FunctionDeclaration function) {
		env.storeFunction(name, function);
	}
	
	public void storeVariable(String name, EvalResult value) {
		env.storeVariable(name, value);
	}
	
	String getName() {
		return name;
	}
}
