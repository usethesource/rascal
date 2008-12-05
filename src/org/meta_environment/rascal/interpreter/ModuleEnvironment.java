package org.meta_environment.rascal.interpreter;

import org.eclipse.imp.pdb.facts.type.TupleType;
import org.meta_environment.rascal.ast.FunctionDeclaration;

/*package*/ class ModuleEnvironment extends EnvironmentStack {
	private final Environment env = new Environment();
	private final String name;

	public ModuleEnvironment(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public void addModule(ModuleEnvironment m) {
		importedModules.add(m.getName());
		super.addModule(m);
	}
	
	@Override
	protected FunctionDeclaration getFunction(String name, TupleType actuals) {
		return env.getFunction(name, actuals);
	}
	
	@Override
	protected EvalResult getVariable(String name) {
		return env.getVariable(name);
	}
	
	@Override
	protected void storeFunction(String name, FunctionDeclaration function) {
		env.storeFunction(name, function);
	}
	
	@Override
	protected void storeVariable(String name, EvalResult value) {
		env.storeVariable(name, value);
	}
	
	@Override
	public boolean isRootEnvironment() {
		return true;
	}
}
