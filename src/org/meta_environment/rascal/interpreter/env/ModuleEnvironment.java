package org.meta_environment.rascal.interpreter.env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;
import org.meta_environment.rascal.interpreter.errors.TypeError;

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
	protected final Map<String, ModuleEnvironment> importedModules;
	protected final Map<String,Type> typeAliases;
	protected final Map<String, Type> adts;
	protected final Map<Type, List<Type>> signature;
	protected final Map<Type, List<Type>> extensions;
	protected final Map<Type, Map<String, Type>> annotations;
	
	public ModuleEnvironment(String name) {
		super(null);
		this.name = name;
		this.importedModules = new HashMap<String, ModuleEnvironment>();
		this.typeAliases = new HashMap<String,Type>();
		this.adts = new HashMap<String,Type>();
		this.signature = new HashMap<Type, List<Type>>();
		this.extensions = new HashMap<Type, List<Type>>();
		this.annotations = new HashMap<Type, Map<String, Type>>();
	}
	
	public boolean isModuleEnvironment() {
		return true;
	}
	
	public void addImport(String name, ModuleEnvironment env) {
		importedModules.put(name, env);
	}
	
	public Set<String> getImports() {
		return importedModules.keySet();
	}

	public String getName() {
		return name;
	}
	
	@Override
	public Result getVariable(QualifiedName name) {
		String modulename = Names.moduleName(name);
		
		if (modulename != null) {
			if (modulename.equals(getName())) {
				return getVariable(name, Names.name(Names.lastName(name)));
			}
			
			ModuleEnvironment imported = getImport(modulename);
			if (imported == null) {
				throw new TypeError("Module " + modulename + " is not visible in " + getName(), name);
			}
			return imported.getVariable(name);
		}
		else {
			return getVariable(name, Names.name(Names.lastName(name)));
		}
	
	}
	
	@Override
	public Result getVariable(AbstractAST ast, String name) {
		Result result = super.getVariable(ast, name);
		
		// if the local module scope does not contain the variable, it
		// may be visible in one of its imported modules.
		
		if (result == null) {
			List<Result> results = new ArrayList<Result>();
			for (String i : getImports()) {
				// imports are not transitive!
				ModuleEnvironment module = importedModules.get(i);
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
				throw new TypeError("Variable " + name + " is ambiguous, please qualify", ast);
			}
		}
		
		return result;
	}
	
	@Override
	public void storeVariable(String name, Result value) {
		Result result = super.getVariable(null, name);
		
		if (result != null) {
			super.storeVariable(name, value);
		}
		else {
			for (String i : getImports()) {
				ModuleEnvironment module = importedModules.get(i);
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
	public Lambda getFunction(String name, Type types) {
		Lambda result = super.getFunction(name, types);
		
		if (result == null) {
			List<Lambda> results = new ArrayList<Lambda>();
			for (String i : getImports()) {
				// imports are not transitive!
				ModuleEnvironment module = importedModules.get(i);
				result = module.getLocalPublicFunction(name, types);
				
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
				throw new TypeError("Function " + name + " is ambiguous, please qualify", result.getAst());
			}
		}
		
		return result;
	}
	
	public Lambda getLocalFunction(String name, Type types) {
		return super.getFunction(name, types);
	}
	
	public Lambda getLocalPublicFunction(String name, Type types) {
		Lambda decl = getLocalFunction(name, types);
		if (decl != null && decl.isPublic()) {
			return decl;
		}
		return null;
	}
	
	
	public Result getLocalVariable(String name) {
		return super.getVariable(null, name);
	}
	
	public Result getLocalPublicVariable(String name) {
		Result var = getLocalVariable(name);
		
		if (var != null && var.isPublic()) {
			return var;
		}
		
		return null;
	}

	@Override
	public Type getTypeAlias(String name) {
		return typeAliases.get(name);
	}
	
	@Override
	public Type getAbstractDataType(String sort) {
		return adts.get(sort);
	}
	
	@Override
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
			ModuleEnvironment mod = importedModules.get(i);
			Type found = mod.getConstructor(cons, args);
			
			if (found != null) {
				return found;
			}
		}
		
		return null;
	}
	
	@Override
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
			ModuleEnvironment mod = importedModules.get(i);
			Type found = mod.getConstructor(sort, cons, args);
			
			if (found != null) {
				return found;
			}
		}
		
		return null;
	}
	
	@Override
	public boolean isTreeConstructorName(QualifiedName name, Type signature) {
		java.util.List<Name> names = name.getNames();
		
		if (names.size() > 1) {
			String sort = Names.sortName(name);
			Type sortType = getAbstractDataType(sort);
			
			if (sortType != null) {
				String cons = Names.consName(name);
				
				if (getConstructor(sortType, cons, signature) != null) {
					return true;
				}
			}
		}
		else {
			String cons = Names.consName(name);
			if (getConstructor(cons, signature) != null) {
				return true;
			}
		}
		
		return false;
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
	
	// TODO deal with imports
	@Override
	public Type getAnnotationType(Type onType, String name) {
		Map<String, Type> annosFor = annotations.get(onType);
		
		if (annosFor != null) {
			return annosFor.get(name);
		}
		
		return null;
	}
	
	@Override
	public Map<String, Type> getAnnotations(Type onType) {
		return annotations.get(onType);
	}
	
	@Override
	public String toString() {
		return "Environment [ " + getName() + ":" + importedModules + "]"; 
	}

	@Override
	public ModuleEnvironment getImport(String moduleName) {
		return importedModules.get(moduleName);
	}
	
	private void checkModuleName(QualifiedName name) {
		String moduleName = Names.moduleName(name);
		
		if (moduleName != null && !moduleName.equals(getName())) {
			throw new ImplementationError("Attempt to access variable " + name + " of different module", name);
		}
	}
	
	@Override
	public void storeVariable(QualifiedName name, Result result) {
		checkModuleName(name);
		
		super.storeVariable(name, result);
	}
}
