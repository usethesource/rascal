package org.meta_environment.rascal.interpreter;

import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.type.TupleType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Rule;

/*package*/ class EnvironmentStack extends Environment {
	private final Stack<Environment> stack = new Stack<Environment>();
	private final TypeEvaluator types = new TypeEvaluator();

	private class RootEnvironment extends Environment {
		@Override
		public boolean isRootEnvironment() {
			return true;
		}
	}
	
	public EnvironmentStack() {
		stack.push(new RootEnvironment());
	}

	public void push() {
		stack.push(new Environment());
	}

	public void pop() {
		stack.pop();
	}
	
	public void pushModule(String name) {
		stack.push(stack.get(0).moduleEnvironment.get(name));
	}

	@Override
	public FunctionDeclaration getFunction(String name, TupleType actuals) {
		return getFunctionDefiningEnvironment(name, actuals).getFunction(name, actuals);
	}

	@Override
	public EvalResult getVariable(String name) {
		return getVariableDefiningEnvironment(name).getVariable(name);
	}

	@Override
	public void storeFunction(String name, FunctionDeclaration function) {
		TupleType formals = (TupleType) function.getSignature().accept(types);
		
		getFunctionDefiningEnvironment(name, formals).storeFunction(name, function);
	}

	@Override
	public void storeVariable(String name, EvalResult value) {
		getVariableDefiningEnvironment(name).storeVariable(name, value);
	}
	
	private Environment getFunctionDefiningEnvironment(String name, TupleType formals) {
		int i;
		
		// first look on the scope stack
		for (i = stack.size() - 1; i >= 0; i--) {
			Environment environment = stack.get(i);
			
			if (environment.getFunction(name, formals) != null) {
				return environment;
			}
		}
		
		// then look through the imported modules
		for (String module : getImportedModules()) {
			Environment env = getModule(module);
			
			if (env.getFunction(name, formals) != null) {
				return env;
			}
		}
		
		return getRootEnvironment();
	}
	
	private Environment getVariableDefiningEnvironment(String name) {
		int i;
		
		// first look on the scope stack
		for (i = stack.size() - 1; i >= 0; i--) {
			Environment environment = stack.get(i);
			
            if (environment.getVariable(name) != null) {
            	return environment;
            }
		}
		
		// then look through the imported modules
		for (String module : getImportedModules()) {
			Environment env = getModule(module);
			
			if (env.getVariable(name) != null) {
				return env;
			}
		}
		
		return getRootEnvironment();
	}

	private Environment getRootEnvironment() {
		int i;
		for (i = stack.size() - 1; i >= 0 && !stack.get(i).isRootEnvironment(); i--);
		
		if (i == -1) {
			return stack.get(0);
		}
		else {
			return stack.get(i);
		}
	}
	
	@Override
	public Set<String> getImportedModules() {
		return getRootEnvironment().getImportedModules();
	}
	
	@Override
	public void storeRule(Type forType, Rule rule) {
		stack.get(0).storeRule(forType, rule);
	}
	
    @Override
    public List<Rule> getRules(Type forType) {
    	return stack.get(0).getRules(forType);
    } 
	
	@Override
	public void addModule(ModuleEnvironment m) {
		Environment env = getRootEnvironment();
		env.addModule(m);
		stack.get(0).addModule(m);
	}

	@Override
	public ModuleEnvironment getModule(String name) {
		return stack.get(0).getModule(name);
	}
	
	@Override
	public EvalResult getModuleVariable(String module, String variable) {
		ModuleEnvironment env = stack.get(0).getModule(module);
		if (env.getVisibility(variable) == ModuleVisibility.PRIVATE) {
			throw new RascalTypeError("Visibility of " + variable + " in " + module + " is private");
		}
		return stack.get(0).getModule(module).getVariable(variable);
	}
}
