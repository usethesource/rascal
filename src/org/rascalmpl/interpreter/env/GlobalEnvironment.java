package org.rascalmpl.interpreter.env;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.PatternWithAction;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModuleError;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.uptr.Factory;


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
	private final Map<Type, List<RewriteRule>> ruleEnvironment = new HashMap<Type, List<RewriteRule>>();
	
	/** Keeping track of module locations */
	private final Map<String, URI> moduleLocations = new HashMap<String,URI>();
	private final Map<URI, String> locationModules = new HashMap<URI,String>();
	
	public void clear() {
		moduleEnvironment.clear();
		ruleEnvironment.clear();
		moduleLocations.clear();
		locationModules.clear();
	}
	
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
			throw new ImplementationError("Reinstantiating same module " + mod.getName());
		}
	}
	
	public ModuleEnvironment resetModule(String name) {
		ModuleEnvironment mod = moduleEnvironment.get(name);
		mod.reset();
		return mod;
	}
		
	/**
	 * Retrieve a module from the heap
	 */
	public ModuleEnvironment getModule(String name) {
		return moduleEnvironment.get(Names.unescape(name));
	}

	public ModuleEnvironment getModule(QualifiedName name, AbstractAST ast) {
		ModuleEnvironment module = getModule(name.toString());
		if (module == null) {
			throw new UndeclaredModuleError(name.toString(), ast);
		}
		return module;
	}
	

	public void storeRule(Type forType, PatternWithAction rule, Environment env) {
		List<RewriteRule> rules = ruleEnvironment.get(forType);
		
		if (!forType.isConstructorType() && !forType.isNodeType() && forType != Factory.Tree && forType != Factory.Tree_Appl) {
			throw new IllegalArgumentException("forType should be a constructor type, instead it is :" + forType);
		}
		
		//System.err.println("storeRule: type=" + forType + ",rule=" + rule);
		if (rules == null) {
			rules = new ArrayList<RewriteRule>();
			ruleEnvironment.put(forType, rules);
		}
		
		rules.add(new RewriteRule(rule, env));
	}
	
	public List<RewriteRule> getRules(Type forType) {
		List<RewriteRule> rules = ruleEnvironment.get(forType);
		//System.err.println("getRules: type=" + forType + ",rules=" + rules);
		return rules != null ? rules : new ArrayList<RewriteRule>();
	}

	
	public List<RewriteRule> getRules() {
		List<RewriteRule> result = new LinkedList<RewriteRule>();
		
		for (Type t : ruleEnvironment.keySet()) {
			result.addAll(ruleEnvironment.get(t));
		}
		
		return result;
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

	public void removeModule(ModuleEnvironment env) {
		moduleEnvironment.remove(env.getName());
	}

	public void setModuleURI(String name, URI location) {
		moduleLocations.put(name, location);
		locationModules.put(location, name);
	}
	
	public URI getModuleURI(String name) {
		return moduleLocations.get(name);
	}
	
	public String getModuleForURI(URI location) {
		return locationModules.get(location);
	}
}
