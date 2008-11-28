package org.meta_environment.rascal.interpreter;

import java.util.List;
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
		for (int i = stack.size() - 1; i >= 0; i--) {
			FunctionDeclaration result = stack.get(i)
					.getFunction(name, actuals);

			if (result != null) {
				return result;
			}
			
			if (stack.get(i).isRootEnvironment()) {
				break;
			}
		}

		throw new RascalTypeError("Call to undefined function " + name
				+ " with argument types " + actuals);
	}

	@Override
	public EvalResult getVariable(String name) {
		for (int i = stack.size() - 1; i >= 0; i--) {
			EvalResult result = stack.get(i).getVariable(name);

			if (result != null) {
				return result;
			}
			
			if (stack.get(i).isRootEnvironment()) {
				break;
			}
		}

		return null;
	}

	@Override
	public void storeFunction(String name, FunctionDeclaration function) {
		TupleType formals = (TupleType) function.getSignature().accept(types);
		int i;
		for (i = stack.size() - 1; i >= 0; i--) {
			FunctionDeclaration result = stack.get(i)
					.getFunction(name, formals);

			if (result != null) {
				break;
			}
			
			if (stack.get(i).isRootEnvironment()) {
				break;
			}
		}

		if (i == -1) {
			stack.peek().storeFunction(name, function);
		} else {
			stack.get(i).storeFunction(name, function);
		}
	}

	@Override
	public void storeVariable(String name, EvalResult value) {
		int i;
		for (i = stack.size() - 1; i >= 0; i--) {
			EvalResult result = stack.get(i).getVariable(name);

			if (result != null) {
				break;
			}
			
			if (stack.get(i).isRootEnvironment()) {
				break;
			}
		}

		if (i == -1) {
			stack.peek().storeVariable(name, value);
		} else {
			stack.get(i).storeVariable(name, value);
		}
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
		stack.get(0).addModule(m);
	}

	@Override
	public ModuleEnvironment getModule(String name) {
		return stack.get(0).getModule(name);
	}
	
	@Override
	public EvalResult getModuleVariable(String module, String variable) {
		return stack.get(0).getModule(module).getVariable(variable);
	}
}
