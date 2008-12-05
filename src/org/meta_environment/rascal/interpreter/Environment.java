package org.meta_environment.rascal.interpreter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.type.TupleType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Rule;

/*package*/ class Environment {
	protected final Map<String, EvalResult> variableEnvironment;
	protected final Map<String, List<FunctionDeclaration>> functionEnvironment;
	protected final Map<String, ModuleEnvironment> moduleEnvironment;
	protected final Set<String> importedModules;
	protected final Map<String, ModuleVisibility> nameVisibility;
	protected final Map<Type, List<Rule>> ruleEnvironment;
	protected final TypeEvaluator types = new TypeEvaluator();

	public Environment() {
		this.variableEnvironment = new HashMap<String, EvalResult>();
		this.functionEnvironment = new HashMap<String, List<FunctionDeclaration>>();
		this.moduleEnvironment = new HashMap<String, ModuleEnvironment>();
		this.nameVisibility = new HashMap<String, ModuleVisibility>();
		this.ruleEnvironment = new HashMap<Type, List<Rule>>();
		this.importedModules = new HashSet<String>();
	}
	
	public void setVisibility(String name, ModuleVisibility v) {
		nameVisibility.put(name, v);
	}
	
	public ModuleVisibility getVisibility(String name) {
		ModuleVisibility v = nameVisibility.get(name);
		
		return v == null ? ModuleVisibility.PRIVATE : v;
	}
	
	public void storeRule(Type forType, Rule rule) {
		List<Rule> rules = ruleEnvironment.get(forType);
		
		if (rules == null) {
			rules = new LinkedList<Rule>();
			ruleEnvironment.put(forType, rules);
		}
		
		rules.add(rule);
	}
	
	public List<Rule> getRules(Type forType) {
		List<Rule> rules = ruleEnvironment.get(forType);
		return rules != null ? rules : new LinkedList<Rule>();
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
	
	public Set<String> getImportedModules() {
		return importedModules;
	}
	
	public void addModule(ModuleEnvironment m) {
		moduleEnvironment.put(m.getName(), m);
		importedModules.add(m.getName());
	}
	
	public ModuleEnvironment getModule(String name) {
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
	}
	
	public void storeFunction(String name, FunctionDeclaration function) {
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
	
	public boolean isRootEnvironment() {
		return false;
	}
}