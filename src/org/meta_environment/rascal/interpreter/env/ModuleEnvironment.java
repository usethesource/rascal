package org.meta_environment.rascal.interpreter.env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.result.Lambda;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.AmbiguousFunctionReferenceError;
import org.meta_environment.rascal.interpreter.staticErrors.AmbiguousVariableReferenceError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFunctionError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredModuleError;
import org.meta_environment.rascal.interpreter.utils.Names;


/**
 * A module environment represents a module object (i.e. a running module).
 * It manages imported modules and visibility of the
 * functions and variables it declares. 
 * 
 * TODO: add management of locally declared types and constructors
 * 
 */
public class ModuleEnvironment extends Environment {
	protected final Map<String, ModuleEnvironment> importedModules;
	protected final Map<Type, List<Type>> extensions;
	protected final TypeStore typeStore;
	protected final Map<String, ConcreteSyntaxType> concreteSyntaxTypes;
	private Set<String> importedSDFModules = new HashSet<String>();
	
	protected static final TypeFactory TF = TypeFactory.getInstance();
	
	public ModuleEnvironment(String name) {
		super(name);
		this.importedModules = new HashMap<String, ModuleEnvironment>();
		this.extensions = new HashMap<Type, List<Type>>();
		this.concreteSyntaxTypes = new HashMap<String, ConcreteSyntaxType>();
		this.typeStore = new TypeStore();
	}
	
	public boolean isModuleEnvironment() {
		return true;
	}
	
	public void addImport(String name, ModuleEnvironment env) {
		importedModules.put(name, env);
		typeStore.importStore(env.typeStore);
	}
	
	public void addSDFImport(String name) {
		importedSDFModules.add(name);	
	}
	
	public Set<String> getSDFImports() {
		return importedSDFModules;
	}
	
	@Override
	public Set<String> getImports() {
		return importedModules.keySet();
	}

	public String getName() {
		return name;
	}
	
	@Override
	public TypeStore getStore() {
		return typeStore;
	}
	
	@Override
	public Result<IValue> getVariable(QualifiedName name) {
		String modulename = Names.moduleName(name);
		
		if (getAbstractDataType(modulename) != null) {
			// sort names can not be module names?
			return null;
		}
		
		if (modulename != null) {
			if (modulename.equals(getName())) {
				return getVariable(name, Names.name(Names.lastName(name)));
			}
			
			ModuleEnvironment imported = getImport(modulename);
			if (imported == null) {
				throw new UndeclaredModuleError(modulename, name);
			}
			return imported.getVariable(name);
		}
		
		return getVariable(name, Names.name(Names.lastName(name)));
	}
	
	@Override
	public Result<IValue> getVariable(AbstractAST ast, String name) {
		Result<IValue> result = super.getVariable(ast, name);
		
		// if the local module scope does not contain the variable, it
		// may be visible in one of its imported modules.
		
		if (result == null) {
			List<Result<IValue>> results = new ArrayList<Result<IValue>>();
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
				throw new AmbiguousVariableReferenceError(name, ast);
			}
		}
		
		return result;
	}
	
	@Override
	public void storeVariable(String name, Result<IValue> value) {
		Result<IValue> result = super.getVariable(null, name);
		
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
	public Lambda getFunction(String name, Type types, AbstractAST x) {
		Lambda result = super.getFunction(name, types, x);
		
		if (result == null) {
			List<Lambda> results = new ArrayList<Lambda>();
			for (String i : getImports()) {
				// imports are not transitive!
				ModuleEnvironment module = importedModules.get(i);
				result = module.getLocalPublicFunction(name, types, x);
				
				if (result != null) {
					results.add(result);
				}
			}
			if (results.size() == 1) {
				return results.get(0);
			}
			else if (results.size() == 0) {
				throw new UndeclaredFunctionError(signatureString(name, types), x);
			}
			else {
				throw new AmbiguousFunctionReferenceError(name, x);
			}
		}
		
		return result;
	}
	
	private String signatureString(String name, Type types) {
		StringBuffer sign = new StringBuffer();
		String sep = "";
		sign.append(name).append("(");
		for(Type t : types){
			sign.append(sep).append(t);
			sep = ",";
		}
		sign.append(")");
		return sign.toString();
	}
	
	public Lambda getLocalFunction(String name, Type types, AbstractAST x) {
		return super.getFunction(name, types, x);
	}
	
	public Lambda getLocalPublicFunction(String name, Type types, AbstractAST x) {
		Lambda decl = getLocalFunction(name, types, x);
		if (decl != null && decl.isPublic()) {
			return decl;
		}
		return null;
	}
	
	
	public Result<IValue> getInnermostVariable(String name) {
		return super.getInnermostVariable(name);
	}
	
	public Result<IValue> getLocalPublicVariable(String name) {
		Result<IValue> var = getInnermostVariable(name);
		
		if (var != null && var.isPublic()) {
			return var;
		}
		
		return null;
	}

	@Override
	public Type abstractDataType(String name, Type... parameters) {
		return TF.abstractDataType(typeStore, name, parameters);
	}
	
	@Override
	public Type concreteSyntaxType(String name, org.meta_environment.rascal.ast.Type type) {
		ConcreteSyntaxType sort = new ConcreteSyntaxType(type);
		concreteSyntaxTypes.put(name, sort);
		return sort;
	}
	
	public Type concreteSyntaxType(String name, IConstructor symbol) {
		ConcreteSyntaxType sort = new ConcreteSyntaxType(symbol);
		concreteSyntaxTypes.put(name, sort);
		return sort;
	}
	
	@Override
	public Type constructorFromTuple(Type adt, String name, Type tupleType) {
		return TF.constructorFromTuple(typeStore, adt, name, tupleType);
	}
	
	@Override
	public Type constructor(Type nodeType, String name,
			Object... childrenAndLabels) {
		return TF.constructor(typeStore, nodeType, name, childrenAndLabels);
	}
	
	@Override
	public Type constructor(Type nodeType, String name, Type... children) {
		return TF.constructor(typeStore, nodeType, name, children);
	}
	
	@Override
	public Type aliasType(String name, Type aliased, Type... parameters) {
		return TF.aliasType(typeStore, name, aliased, parameters);
	}
	
	@Override
	public void declareAnnotation(Type onType, String label, Type valueType) {
		typeStore.declareAnnotation(onType, label, valueType);
	}
	
	@Override
	public Type getAnnotationType(Type type, String label) {
		return typeStore.getAnnotationType(type, label);
	}
	
	@Override
	public Type getAbstractDataType(String sort) {
		return typeStore.lookupAbstractDataType(sort);
	}
	
	@Override
	public Type getConstructor(String cons, Type args) {
		return typeStore.lookupFirstConstructor(cons, args);
	}
	
	@Override
	public Type getConstructor(Type sort, String cons, Type args) {
		return typeStore.lookupConstructor(sort, cons, args);
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
	
	@Override
	public String toString() {
		return "Environment [ " + getName() + ":" + importedModules + "]"; 
	}

	@Override
	public ModuleEnvironment getImport(String moduleName) {
		return importedModules.get(moduleName);
	}
	
	@SuppressWarnings("unused")
	private void checkModuleName(QualifiedName name) {
		String moduleName = Names.moduleName(name);
		
		if (moduleName != null && !moduleName.equals(getName())) {
			throw new ImplementationError("Attempt to access variable " + name + " of different module");
		}
	}
	
	@Override
	public void storeVariable(QualifiedName name, Result<IValue> result) {
		String modulename = Names.moduleName(name);
		
		if (modulename != null) {
			if (modulename.equals(getName())) {
				storeVariable(Names.name(Names.lastName(name)), result);
				return;
			}
			
			ModuleEnvironment imported = getImport(modulename);
			if (imported == null) {
				throw new UndeclaredModuleError(modulename, name);
			}
			
			imported.storeVariable(name, result);
			return;
		}
		super.storeVariable(name, result);
	}
	
	@Override
	public boolean declaresAnnotation(Type type, String label) {
		return typeStore.getAnnotationType(type, label) != null;
	}
	
	@Override
	public Type lookupAbstractDataType(String name) {
		return typeStore.lookupAbstractDataType(name);
	}
	
	@Override
	public Type lookupConcreteSyntaxType(String name) {
		return concreteSyntaxTypes.get(name);
	}
	
	@Override
	public Type lookupAlias(String name) {
		return typeStore.lookupAlias(name);
	}
	
	@Override
	public Set<Type> lookupAlternatives(Type adt) {
		return typeStore.lookupAlternatives(adt);
	}
	
	@Override
	public Type lookupConstructor(Type adt, String cons, Type args) {
		return typeStore.lookupConstructor(adt, cons, args);
	}
	
	@Override
	public Set<Type> lookupConstructor(Type adt, String constructorName)
			throws FactTypeUseException {
		return typeStore.lookupConstructor(adt, constructorName);
	}
	
	@Override
	public Set<Type> lookupConstructors(String constructorName) {
		return typeStore.lookupConstructors(constructorName);
	}
	
	@Override
	public Type lookupFirstConstructor(String cons, Type args) {
		return typeStore.lookupFirstConstructor(cons, args);
	}

	
	
}
