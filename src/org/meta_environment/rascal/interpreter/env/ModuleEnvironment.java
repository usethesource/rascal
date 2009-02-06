package org.meta_environment.rascal.interpreter.env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Visibility;
import org.meta_environment.rascal.interpreter.exceptions.RascalTypeError;

/**
 * A module environment represents a module object (i.e. a running module).
 * It manages imported modules and visibility of the
 * functions and variables it declares. 
 * 
 * TODO: add management of locally declared types and constructors
 * 
 */
public class ModuleEnvironment extends Environment {
	private final String name;
	protected final Set<String> importedModules;
	protected final Map<FunctionDeclaration, Visibility> functionVisibility;
	protected final Map<String, Visibility> variableVisibility;
	protected final Map<String,Type> typeAliases;
	protected final Map<String, Type> adts;
	protected final Map<Type, List<Type>> signature;
	protected final Map<Type, List<Type>> extensions;
	protected final Map<Type, Map<String, Type>> annotations;
	
	public ModuleEnvironment(String name) {
		this.name = name;
		this.importedModules = new HashSet<String>();
		this.functionVisibility = new HashMap<FunctionDeclaration, Visibility>();
		this.variableVisibility = new HashMap<String, Visibility>();
		this.typeAliases = new HashMap<String,Type>();
		this.adts = new HashMap<String,Type>();
		this.signature = new HashMap<Type, List<Type>>();
		this.extensions = new HashMap<Type, List<Type>>();
		this.annotations = new HashMap<Type, Map<String, Type>>();
	}
	
	public void setFunctionVisibility(FunctionDeclaration decl, Visibility vis) {
		functionVisibility.put(decl, vis);
	}
	
	public void setVariableVisibility(String var, Visibility vis) {
		variableVisibility.put(var, vis);
	}
	
	public boolean isModuleEnvironment() {
		return true;
	}
	
	public void addImport(String name) {
		importedModules.add(name);
	}
	
	public Set<String> getImports() {
		return importedModules;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public Result getVariable(String name) {
		Result result = super.getVariable(name);
		
		// if the local module scope does not contain the variable, it
		// may be visible in one of its imported modules.
		
		if (result == null) {
			List<Result> results = new ArrayList<Result>();
			for (String i : getImports()) {
				// imports are not transitive!
				ModuleEnvironment module = GlobalEnvironment.getInstance().getModule(i);
				result = module.getLocalPublicVariable(name);
				
				if (result != null) {
					results.add(result);
				}
			}
			
			if (results.size() == 1) {
				return results.get(0);
			}
			else if (results.size() == 0) {
				return null;
			}
			else {
				throw new RascalTypeError("Variable " + name + " is ambiguous, please qualify");
			}
		}
		
		return result;
	}
	
	@Override
	// NEW
	public void storeVariable(String name, Result value) {
		Result result = super.getVariable(name);
		
		if (result != null) {
			super.storeVariable(name, value);
		}
		else {
			for (String i : getImports()) {
				ModuleEnvironment module = GlobalEnvironment.getInstance().getModule(i);
				result = module.getLocalPublicVariable(name);

				if (result != null) {
					module.storeVariable(name, value);
					return;
				}
			}
			
			super.storeVariable(name, value);
		}
	}
	
	@Override
	public Lambda getFunction(String name, Type types, EnvironmentHolder h) {
		Lambda result = super.getFunction(name, types, h);
		
		if (result == null) {
			List<Lambda> results = new ArrayList<Lambda>();
			for (String i : getImports()) {
				// imports are not transitive!
				ModuleEnvironment module = GlobalEnvironment.getInstance().getModule(i);
				result = module.getLocalPublicFunction(name, types);
				
				if (result != null) {
					h.setEnvironment(module);
					results.add(result);
				}
			}
			
			if (results.size() == 1) {
				return results.get(0);
			}
			else if (results.size() == 0) {
				return null;
			}
			else {
				throw new RascalTypeError("Function " + name + " is ambiguous, please qualify");
			}
		}
		
		return result;
	}
	
	public Lambda getLocalFunction(String name, Type types) {
		return super.getFunction(name, types, new EnvironmentHolder());
	}
	
	public Lambda getLocalPublicFunction(String name, Type types) {
		Lambda decl = getLocalFunction(name, types);
		if (decl != null) {
			Visibility vis = functionVisibility.get(decl);
			
			if (vis != null && vis.isPublic()) {
				return decl;
			}
		}
		return null;
	}
	
	public Result getLocalVariable(String name) {
		return super.getVariable(name);
	}
	
	public Result getLocalPublicVariable(String name) {
		Result var = getLocalVariable(name);
		
		if (var != null) {
			Visibility vis = variableVisibility.get(name);
			
			if (vis != null && vis.isPublic()) {
				return var;
			}
		}
		
		return null;
	}

	public Type getTypeAlias(String name) {
		return typeAliases.get(name);
	}
	
	public Type getAbstractDataType(String sort) {
		return adts.get(sort);
	}
	
	public Type getConstructor(String cons, Type args) {
		for (List<Type> sig : signature.values()) {
			for (Type cand : sig) {
				String candCons = cand.getName();
				
				if (candCons == null) {
					if (cons == null && args.isSubtypeOf(cand.getFieldTypes())) {
						return cand;
					}
				}
				else if (candCons.equals(cons)
				      && args.isSubtypeOf(cand.getFieldTypes())) {
					return cand;
				}
			}
		}
		
		for (String i : getImports()) {
			ModuleEnvironment mod = GlobalEnvironment.getInstance().getModule(i);
			Type found = mod.getConstructor(cons, args);
			
			if (found != null) {
				return found;
			}
		}
		
		return null;
	}
	
	public Type getConstructor(Type sort, String cons, Type args) {
		List<Type> sig = signature.get(sort);
		
		if (sig != null) {
			for (Type cand : sig) {
				String candName = cand.getName();
				if (candName != null && candName.equals(cons) && args.isSubtypeOf(cand.getFieldTypes())) {
					return cand;
				}
			}
		}
		
		for (String i : getImports()) {
			ModuleEnvironment mod = GlobalEnvironment.getInstance().getModule(i);
			Type found = mod.getConstructor(sort, cons, args);
			
			if (found != null) {
				return found;
			}
		}
		
		return null;
	}
	
	public void storeTypeAlias(Type decl) {
		typeAliases.put(decl.getName(), decl);
	}
	
	public void storeAbstractDataType(Type decl) {
		List<Type> tmp = signature.get(decl);
		
		if (tmp == null) {
			tmp = new ArrayList<Type>();
			signature.put(decl, tmp);
		}
		
		adts.put(decl.getName(), decl);
	}
	
	public void storeConstructor(Type decl) {
		Type adt = decl.getAbstractDataType();
		
		storeAbstractDataType(adt);
		
		List<Type> tmp = signature.get(adt);
		
		if (tmp == null) {
			tmp = new ArrayList<Type>();
			signature.put(adt, tmp);
		}
		
		tmp.add(decl);
	}
	
	public void storeDefinition(Type adt, Type extension) {
		List<Type> tmp = extensions.get(adt);
		
		if (tmp == null) {
			tmp = new ArrayList<Type>();
			extensions.put(adt, tmp);
		}
		
		tmp.add(extension);
	}


	public void storeAnnotation(Type onType, String name, Type annoType) {
		Map<String, Type> annosFor = annotations.get(onType);
		
		if (annosFor == null) {
			annosFor = new HashMap<String,Type>();
			annotations.put(onType, annosFor);
		}

		annosFor.put(name, annoType);
	}
	
	public Type getAnnotationType(Type onType, String name) {
		Map<String, Type> annosFor = annotations.get(onType);
		
		if (annosFor != null) {
			return annosFor.get(name);
		}
		
		return null;
	}
	
	public Map<String, Type> getAnnotations(Type onType) {
		return annotations.get(onType);
	}
	
	@Override
	public String toString() {
		return "Environment [ " + getName() + ":" + importedModules + "]"; 
	}
}
