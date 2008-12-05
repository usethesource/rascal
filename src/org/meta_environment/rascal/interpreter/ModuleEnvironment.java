package org.meta_environment.rascal.interpreter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.type.TupleType;

/*package*/ class ModuleEnvironment extends EnvironmentStack {
	private final String name;
	protected final Set<String> importedModules;
	protected final Map<String, ModuleVisibility> nameVisibility;
	
	public ModuleEnvironment(String name) {
		this.name = name;
		this.importedModules = new HashSet<String>();
		this.nameVisibility = new HashMap<String, ModuleVisibility>();
		stack.push(this);
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean isModuleEnvironment() {
		return true;
	}
	
	public void setVisibility(String name, ModuleVisibility v) {
		nameVisibility.put(name, v);
	}
	
	protected ModuleVisibility getVisibility(String name) {
		ModuleVisibility v = nameVisibility.get(name);
		
		return v == null ? ModuleVisibility.PRIVATE : v;
	}
	
	public Set<String> getImportedModules() {
		return importedModules;
	}
	
	protected Environment getFunctionDefiningEnvironment(String name, TupleType formals) {
		Environment env = super.getFunctionDefiningEnvironment(name, formals);
		
		if (env.isGlobalEnvironment()) {
			for (String module : getImportedModules()) {
				env = getModule(module);

				if (env.getFunction(name, formals) != null) {
					return env;
				}
			}
		}
		
		return getGlobalEnvironment();
	}
	
	protected Environment getVariableDefiningEnvironment(String name) {
		Environment env = super.getVariableDefiningEnvironment(name);
		
		if (env.isGlobalEnvironment()) {
			for (String module : getImportedModules()) {
				ModuleEnvironment mod = getModule(module);

				if (mod.getVariable(name) != null) {
					return env;
				}
			}
		}
		
		return getGlobalEnvironment();
	}
}
