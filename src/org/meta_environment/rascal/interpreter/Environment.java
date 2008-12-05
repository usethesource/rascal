package org.meta_environment.rascal.interpreter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.type.TupleType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.QualifiedName;
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
	
	protected ModuleVisibility getVisibility(String name) {
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
	
	public FunctionDeclaration getFunction(QualifiedName name, TupleType actuals) {
		String module = getModuleName(name);
		String function = getName(name);
		
		if (module.equals("")) {
			return getFunction(function, actuals);
		}
		else {
			return getModuleFunction(module, function, actuals);
		}
	}
	
	public Set<String> getImportedModules() {
		return importedModules;
	}
	
	public void addModule(ModuleEnvironment m) {
		moduleEnvironment.put(m.getName(), m);
		importedModules.add(m.getName());
	}
	
	protected ModuleEnvironment getModule(String name) {
		return moduleEnvironment.get(name);
	}
	
	protected EvalResult getVariable(String name) {
		return variableEnvironment.get(name);
	}
	
	private String getModuleName(QualifiedName name) {
		List<Name> names = name.getNames();
		java.util.List<Name> prefix = names.subList(0, names.size() - 1);
		
		StringBuilder tmp = new StringBuilder();
		Iterator<Name> iter = prefix.iterator();
		
		while (iter.hasNext()) {
			Name part = iter.next();
			tmp.append(part.toString());
			if (iter.hasNext()) {
				tmp.append("::");
			}
		}
		
		return tmp.toString();
	}
	
	public EvalResult getVariable(QualifiedName name) {
		String module = getModuleName(name);
		String variable = getName(name);
		
		if (module.equals("")) {
			return getVariable(variable);
		}
		else {
			return getModuleVariable(module, variable);
		}
	}
	
	public EvalResult getVariable(Name name) {
		return getVariable(name.toString());
	}

	private String getName(QualifiedName name) {
		return name.getNames().get(0).toString();
	}
	
	protected EvalResult getModuleVariable(String module, String variable) {
		return getModule(module).getVariable(variable);
	}
	
	protected FunctionDeclaration getModuleFunction(String module, String function, TupleType actuals) {
		return getModule(module).getFunction(function, actuals);
	}
	
	public void storeModuleVariable(String module, String variable, EvalResult value) {
		getModule(module).storeVariable(variable, value);
	}
	
	public void storeModuleFunction(String module, String name, FunctionDeclaration function) {
		getModule(module).storeFunction(name, function);
	}
	
	protected void storeVariable(String name, EvalResult value) {
		variableEnvironment.put(name, value);
	}
	
	public void storeVariable(QualifiedName name, EvalResult value) {
		String module = getModuleName(name);
		String var = getName(name);
		
		if (module.equals("")) {
			storeVariable(var, value);
		}
		else {
			storeModuleVariable(module, var, value);
		}
	}
	
	public void storeVariable(Name name, EvalResult value) {
		storeVariable(name.toString(), value);
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
	
	public void storeFunction(QualifiedName name, FunctionDeclaration function) {
		String module = getModuleName(name);
		String func = getName(name);
		
		if (module.equals("")) {
			storeFunction(func, function);
		}
		else {
			storeModuleFunction(module, func, function);
		}
	}
	
	public boolean isRootEnvironment() {
		return false;
	}
}