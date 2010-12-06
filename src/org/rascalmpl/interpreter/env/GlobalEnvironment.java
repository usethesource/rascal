package org.rascalmpl.interpreter.env;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.PatternWithAction;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModuleError;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.parser.gtd.IGTD;
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
	
	/** Keeping track of generated parsers */
	private final Map<String,ParserTuple> objectParsersForModules = new HashMap<String,ParserTuple>();
	private final Map<String,ParserTuple> rascalParsersForModules = new HashMap<String,ParserTuple>();
	
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
	
	public Class<IGTD> getObjectParser(String module, ISet productions) {
		return getParser(objectParsersForModules, module, productions);
	}
	
	public Class<IGTD> getRascalParser(String module, ISet productions) {
		return getParser(rascalParsersForModules, module, productions);
	}
	
	/**
	 * Retrieves a parser for a module. If a similar parser exists for another module,
	 * based on an equal set of productions, this parser is returned instead.
	 * 
	 * @param module
	 * @param productions
	 */
	private Class<IGTD> getParser(Map<String,ParserTuple> store, String module, ISet productions) {
		ParserTuple parser = store.get(module);
		if (parser == null) {
			for (ParserTuple g : store.values()) {
				if (g.getProductions().isEqual(productions)) {
					// there is a parser for the same syntax, but stored with a different module
					store.put(module, g);
					return g.getParser();
				}
			}
			
			// there is no parser, not even for another module with the same productions
			return null;
		}
		else if (!parser.getProductions().isEqual(productions)) {
			// there is a parser, but it is not for the same syntax
			return null;
		}
		
		// there is a parser and its for the same syntax
		return parser.getParser();
	}
	
	public void storeObjectParser(String module, ISet productions, Class<IGTD> parser) {
		storeParser(objectParsersForModules, module, productions, parser);
	}
	
	private void storeParser(Map<String,ParserTuple> store, String module, ISet productions, Class<IGTD> parser) {
		ParserTuple newT = new ParserTuple(productions, parser);
		ParserTuple old = store.get(module);
		
		if (old != null) {
			// all modules for the old production set can be updated now
			for (String m : store.keySet()) {
				ParserTuple mt = store.get(m);
				
				if (mt != null && mt.getProductions().isEqual(old.getProductions())) {
					// there is a parser for the same syntax, but stored with a different module
					store.put(m, newT);
				}
			}
		}
		else {
			store.put(module, newT);
		}
	}
	
	public void storeRascalParser(String module, ISet productions, Class<IGTD> parser) {
		storeParser(rascalParsersForModules, module, productions, parser);
	}
	
	private class ParserTuple {
		private ISet production;
		private Class<IGTD> parser;

		public ParserTuple(ISet productions, Class<IGTD> parser) {
			this.production = productions;
			this.parser = parser;
		}
		
		public ISet getProductions() {
			return production;
		}
		
		public Class<IGTD> getParser() {
			return parser;
		}
	}
}
