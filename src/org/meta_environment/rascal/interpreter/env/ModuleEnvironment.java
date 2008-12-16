package org.meta_environment.rascal.interpreter.env;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.type.NamedTreeType;
import org.eclipse.imp.pdb.facts.type.NamedType;
import org.eclipse.imp.pdb.facts.type.TreeNodeType;
import org.eclipse.imp.pdb.facts.type.TupleType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Visibility;
import org.meta_environment.rascal.interpreter.EvalResult;
import org.meta_environment.rascal.interpreter.RascalTypeError;

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
	protected final Map<String,NamedType> namedTypes;
	protected final Map<String, NamedTreeType> namedTreeTypes;
	protected final Map<NamedTreeType, List<TreeNodeType>> signature;
	protected final Map<Type, Map<String, Type>> annotations;
	
	public ModuleEnvironment(String name) {
		this.name = name;
		this.importedModules = new HashSet<String>();
		this.functionVisibility = new HashMap<FunctionDeclaration, Visibility>();
		this.variableVisibility = new HashMap<String, Visibility>();
		this.namedTypes = new HashMap<String,NamedType>();
		this.namedTreeTypes = new HashMap<String,NamedTreeType>();
		this.signature = new HashMap<NamedTreeType, List<TreeNodeType>>();
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
	public EvalResult getVariable(String name) {
		EvalResult result = super.getVariable(name);
		
		if (result == null) {
			List<EvalResult> results = new LinkedList<EvalResult>();
			for (String i : getImports()) {
				// imports are not transitive!
				result = GlobalEnvironment.getInstance().getModule(i).getLocalVariable(name);
				
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
	public FunctionDeclaration getFunction(String name, TupleType types) {
		FunctionDeclaration result = super.getFunction(name, types);
		
		if (result == null) {
			List<FunctionDeclaration> results = new LinkedList<FunctionDeclaration>();
			for (String i : getImports()) {
				// imports are not transitive!
				result = GlobalEnvironment.getInstance().getModule(i).getLocalPublicFunction(name, types);
				
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
				throw new RascalTypeError("Function " + name + " is ambiguous, please qualify");
			}
		}
		
		return result;
	}
	
	public FunctionDeclaration getLocalFunction(String name, TupleType types) {
		return super.getFunction(name, types);
	}
	
	public FunctionDeclaration getLocalPublicFunction(String name, TupleType types) {
		FunctionDeclaration decl = getLocalFunction(name, types);
		if (decl != null) {
			Visibility vis = functionVisibility.get(decl);
			
			if (vis != null && vis.isPublic()) {
				return decl;
			}
		}
		return null;
	}
	
	public EvalResult getLocalVariable(String name) {
		return super.getVariable(name);
	}
	
	public EvalResult getLocalPublicVariable(String name) {
		EvalResult var = getLocalVariable(name);
		
		if (var != null) {
			Visibility vis = variableVisibility.get(name);
			
			if (vis != null && vis.isPublic()) {
				return var;
			}
		}
		
		return null;
	}

	public NamedType getNamedType(String name) {
		return namedTypes.get(name);
	}
	
	public NamedTreeType getNamedTreeType(String sort) {
		return namedTreeTypes.get(sort);
	}
	
	public TreeNodeType getTreeNodeType(NamedTreeType sort, String cons, TupleType args) {
		List<TreeNodeType> sig = signature.get(sort);
		
		if (sig != null) {
			for (TreeNodeType cand : sig) {
				if (cand.getName().equals(cons) && args.isSubtypeOf(cand.getChildrenTypes())) {
					return cand;
				}
			}
		}
		
		for (String i : getImports()) {
			ModuleEnvironment mod = GlobalEnvironment.getInstance().getModule(i);
			TreeNodeType found = mod.getTreeNodeType(sort, cons, args);
			
			if (found != null) {
				return found;
			}
		}
		
		return null;
	}
	
	public void storeType(NamedType decl) {
		namedTypes.put(decl.getName(), decl);
	}
	
	public void storeType(NamedTreeType decl) {
		List<TreeNodeType> tmp = signature.get(decl);
		
		if (tmp == null) {
			tmp = new LinkedList<TreeNodeType>();
			signature.put(decl, tmp);
		}
		
		namedTreeTypes.put(decl.getName(), decl);
	}
	
	public void storeType(TreeNodeType decl) {
		NamedTreeType sort = decl.getSuperType();
		
		storeType(sort);
		
		List<TreeNodeType> tmp = signature.get(sort);
		
		if (tmp == null) {
			tmp = new LinkedList<TreeNodeType>();
			signature.put(sort, tmp);
		}
		
		tmp.add(decl);
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
}
