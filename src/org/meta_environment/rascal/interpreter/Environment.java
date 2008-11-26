package org.meta_environment.rascal.interpreter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.type.TupleType;
import org.meta_environment.rascal.ast.FunctionBody;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Signature;

public class Environment {
	protected final Map<String, EvalResult> variableEnvironment;
	protected final Map<String, List<FunctionDeclaration>> functionEnvironment;
	protected final Map<String, Module> moduleEnvironment;
	protected final TypeEvaluator types = new TypeEvaluator();

	public Environment() {
		this.variableEnvironment = new HashMap<String, EvalResult>();
		this.functionEnvironment = new HashMap<String, List<FunctionDeclaration>>();
		this.moduleEnvironment = new HashMap<String, Module>();
	}
	
	public FunctionDeclaration getFunction(String name, TupleType actuals) {
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
	
	public void addModule(Module m) {
		moduleEnvironment.put(m.getName(), m);
	}
	
	public Module getModule(String name) {
		return moduleEnvironment.get(name);
	}
	
	public EvalResult getVariable(String name) {
		return variableEnvironment.get(name);
	}
	
	public EvalResult getModuleVariable(String module, String variable) {
		return getModule(module).getVariable(variable);
	}
	
	public void storeModuleVariable(String module, String variable, EvalResult value) {
		getModule(module).storeVariable(variable, value);
	}
	
	public void storeModuleFunction(String module, String name, FunctionDeclaration function) {
		getModule(module).storeFunction(name, function);
	}
	
	public void storeVariable(String name, EvalResult value) {
		variableEnvironment.put(name, value);
		System.err.println("put(" + name + ", " + value + ")");
	}
	
	public void storeFunction(String name, FunctionDeclaration function) {
		List<FunctionDeclaration> list = functionEnvironment.get(name);
		if (list == null) {
			list = new LinkedList<FunctionDeclaration>();
			functionEnvironment.put(name, list);
		}
		
		list.add(function);
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