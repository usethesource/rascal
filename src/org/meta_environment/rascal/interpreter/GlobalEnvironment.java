package org.meta_environment.rascal.interpreter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.type.ParameterType;
import org.eclipse.imp.pdb.facts.type.TupleType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.ast.Rule;

/**
 * A global environment is an environment that can also store modules
 * and rules, since these are both globally scoped. It behaves as an
 * anonymous module environment.
 * 
 * It is the ``heap'' of the Rascal interpreter.
 */
public class GlobalEnvironment extends ModuleEnvironment {
	private final Map<String, ModuleEnvironment> moduleEnvironment;
	private final Map<Type, List<Rule>> ruleEnvironment;

	public GlobalEnvironment(TypeEvaluator te) {
		super("***global***", te);
		stack.pop();
		stack.push(this);
		moduleEnvironment = new HashMap<String, ModuleEnvironment>();
		ruleEnvironment = new HashMap<Type, List<Rule>>();
	}
	
	@Override
	public boolean isModuleEnvironment() {
		return true;
	}
	
	@Override
	public boolean isGlobalEnvironment() {
		return true;
	}
	
	public void addModule(String name) {
		moduleEnvironment.put(name, new ModuleEnvironment(name, types));
		importedModules.add(name);
	}
	
	public ModuleEnvironment getModule(String name) {
		return moduleEnvironment.get(name);
	}
	
	public EvalResult getModuleVariable(String module, String variable) {
		return getModule(module).getVariable(variable);
	}
	
	public FunctionDeclaration getModuleFunction(String module, String function, TupleType actuals) {
		ModuleEnvironment mod = getModule(module);
		
		if (mod == null) {
			return null;
		}
		return mod.getFunction(function, actuals);
	}
	
	public void storeModuleVariable(String module, String variable, EvalResult value) {
		getModule(module).storeVariable(variable, value);
	}
	
	public void storeModuleFunction(String module, String name, FunctionDeclaration function) {
		getModule(module).storeFunction(name, function);
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
	
	public String getLocalName(QualifiedName name) {
		List<Name> names = name.getNames();
		String str = names.get(names.size() - 1).toString();
		if (str.startsWith("\\")) {
			str = str.substring(1);
		}
		return str;
	}
	
	public EvalResult getVariable(QualifiedName name) {
		String module = getModuleName(name);
		String variable = getLocalName(name);
		
		if (module.equals("")) {
			return getVariable(variable);
		}
		else {
			return getModuleVariable(module, variable);
		}
	}
	
	public FunctionDeclaration getFunction(QualifiedName name, TupleType actuals) {
		String module = getModuleName(name);
		String function = getLocalName(name);
		
		if (module.equals("")) {
			return getFunction(function, actuals);
		}
		else {
			return getModuleFunction(module, function, actuals);
		}
	}
	
	public ModuleEnvironment getModuleFor(QualifiedName name) {
		ModuleEnvironment mod = getModule(getModuleName(name));
		return mod != null ? mod : this;
	}
	
	public void storeVariable(QualifiedName name, EvalResult value) {
		String module = getModuleName(name);
		String var = getLocalName(name);
		
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
	
	public void storeFunction(QualifiedName name, FunctionDeclaration function) {
		String module = getModuleName(name);
		String func = getLocalName(name);
		
		if (module.equals("")) {
			storeFunction(func, function);
		}
		else {
			storeModuleFunction(module, func, function);
		}
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

	
	
	
}
