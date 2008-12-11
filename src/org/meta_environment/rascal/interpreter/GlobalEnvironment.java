package org.meta_environment.rascal.interpreter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	private static GlobalEnvironment sInstance = new GlobalEnvironment();
    
	private GlobalEnvironment() {
		super("***global***");
		moduleEnvironment = new HashMap<String, ModuleEnvironment>();
		ruleEnvironment = new HashMap<Type, List<Rule>>();
	}

	public static GlobalEnvironment getInstance() {
		return sInstance;
	}
	
	public static void clean() {
		sInstance = new GlobalEnvironment();
	}
	
	@Override
	public void addImport(String name) {
		addModule(name);
		super.addImport(name);
	}
	
	public void addModule(String name) {
		ModuleEnvironment env = moduleEnvironment.get(name);
		if (env == null) {
			moduleEnvironment.put(name, new ModuleEnvironment(name));
		}
	}

	public void pushModule(QualifiedName name) {
		pushModule(Names.moduleName(name));
	}
	
	public void pushModule(String name) {
		stack.push(getModule(name));
	}
	
	public void popModule() {
		stack.pop();
	}
	
	public ModuleEnvironment getModule(String name) {
		return moduleEnvironment.get(name);
	}
	
	public EvalResult getModuleVariable(String module, Name variable) {
		return getModule(module).getVariable(variable);
	}
	
	private FunctionDeclaration getModuleFunction(String module, Name function, TupleType actuals) {
		ModuleEnvironment mod = getModule(module);
		
		if (mod == null) {
			return null;
		}
		return mod.getFunction(function, actuals);
	}
	
	private void storeModuleVariable(String module, Name variable, EvalResult value) {
		getModule(module).storeVariable(variable, value);
	}
	
	private void storeModuleFunction(String module, Name name, FunctionDeclaration function) {
		getModule(module).storeFunction(name, function);
	}
	
	public Name getLocalName(QualifiedName name) {
		return Names.lastName(name);
	}
	
	public EvalResult getVariable(QualifiedName name) {
		String module = Names.moduleName(name);
		Name variable = Names.lastName(name);
		
		if (module == null) {
			return getVariable(variable);
		}
		else {
			return getModuleVariable(module, variable);
		}
	}
	
	public FunctionDeclaration getFunction(QualifiedName name, TupleType actuals) {
		String module = Names.moduleName(name);
		Name function = Names.lastName(name);
		
		if (module == null) {
			return getFunction(function, actuals);
		}
		else {
			return getModuleFunction(module, function, actuals);
		}
	}
	
	public void storeVariable(QualifiedName name, EvalResult value) {
		String module = Names.moduleName(name);
		Name var = Names.lastName(name);
		
		if (module == null) {
			storeVariable(var, value);
		}
		else {
			storeModuleVariable(module, var, value);
		}
	}
	
	public void storeFunction(QualifiedName name, FunctionDeclaration function) {
		String module = Names.moduleName(name);
		Name func = Names.lastName(name);
		
		if (module == null) {
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

	public void pushModuleForFunction(QualifiedName name, TupleType formals) {
		String module = Names.moduleName(name);
		
		if (module != null) {
           pushModule(name);
		}
		else {
		   pushFrame(getFunctionDefiningEnvironment(Names.lastName(name), formals));
		}
	}
}
