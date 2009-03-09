package org.meta_environment.rascal.interpreter.env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.ast.Rule;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.exceptions.ImplementationException;
import org.meta_environment.rascal.interpreter.exceptions.NoSuchModuleException;

/**
 * The global environment represents the stack and the heap of Rascal.
 * The stack is initialized with a bottom frame, which represent the shell
 * environment.
 * 
 */
public class GlobalEnvironment {
	/** The heap of Rascal */
	private final Map<String, ModuleEnvironment> moduleEnvironment = new HashMap<String, ModuleEnvironment>();
		
	/** Normalizing rules are a global feature */
	private final Map<Type, List<Rule>> ruleEnvironment = new HashMap<Type, List<Rule>>();
	
	/**
	 * Allocate a new module on the heap
	 * @param name
	 */
	public ModuleEnvironment addModule(ModuleEnvironment mod) {
		ModuleEnvironment env = moduleEnvironment.get(mod.getName());
		if (env == null) {
			moduleEnvironment.put(mod.getName(), mod);
			return mod;
		}
		else if (env == mod) {
			return mod;
		}
		else {
			throw new ImplementationException("Reinstantiating same module " + mod.getName());
		}
	}
	
		
	/**
	 * Retrieve a module from the heap
	 * @param ast TODO
	 */
	public ModuleEnvironment getModule(String name, AbstractAST ast) {
		ModuleEnvironment result = moduleEnvironment.get(Names.unescape(name));
		
		if (result == null) {
			throw new NoSuchModuleException("No such module " + name, ast);
		}
		
		return result;
	}

	public ModuleEnvironment getModule(QualifiedName name, AbstractAST ast) {
		return getModule(name.toString(), ast);
	}
	
	public void storeRule(Type forType, Rule rule) {
		List<Rule> rules = ruleEnvironment.get(forType);
		
		//System.err.println("storeRule: type=" + forType + ",rule=" + rule);
		if (rules == null) {
			rules = new ArrayList<Rule>();
			ruleEnvironment.put(forType, rules);
		}
		
		rules.add(rule);
	}
	
	public List<Rule> getRules(Type forType) {
		List<Rule> rules = ruleEnvironment.get(forType);
		//System.err.println("getRules: type=" + forType + ",rules=" + rules);
		return rules != null ? rules : new ArrayList<Rule>();
	}

	
	public boolean existsModule(String name) {
		return moduleEnvironment.containsKey(name);
	}

	
	@Override
	public String toString(){
		StringBuffer res = new StringBuffer();
		res.append("==== Module Environment ====\n").append(moduleEnvironment);
		res.append("==== Rule Environment ====\n");
		for(Type type : ruleEnvironment.keySet()){
			res.append(type).append(" : ").append(ruleEnvironment.get(type)).append("\n");
		}
		return res.toString();
	}

	
}
