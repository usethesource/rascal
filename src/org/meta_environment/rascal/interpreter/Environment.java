package org.meta_environment.rascal.interpreter;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.type.NamedTreeType;
import org.eclipse.imp.pdb.facts.type.NamedType;
import org.eclipse.imp.pdb.facts.type.ParameterType;
import org.eclipse.imp.pdb.facts.type.TreeNodeType;
import org.eclipse.imp.pdb.facts.type.TupleType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.QualifiedName;

/**
 * A simple environment for variables and functions. Does not have support
 * for scopes or modules, which are a features of EnvironmentStack, 
 * GlobalEnvironment and ModuleEnvironment.
 */
public class Environment implements IEnvironment {
	protected final Map<String, EvalResult> variableEnvironment;
	protected final Map<String, List<FunctionDeclaration>> functionEnvironment;
	protected final Map<ParameterType, Type> typeParameters;
	protected final TypeEvaluator types = TypeEvaluator.getInstance();
	protected final Set<Type> namedTypes;
	protected final Map<NamedTreeType, List<TreeNodeType>> signature;

	public Environment() {
		this.variableEnvironment = new HashMap<String, EvalResult>();
		this.functionEnvironment = new HashMap<String, List<FunctionDeclaration>>();
		this.typeParameters = new HashMap<ParameterType, Type>();
		this.namedTypes = new HashSet<Type>();
		this.signature = new HashMap<NamedTreeType, List<TreeNodeType>>();
	}
	
	public boolean isRootEnvironment() {
		return false;
	}
	
	
	
	public FunctionDeclaration getFunction(QualifiedName name, TupleType actuals) {
		return getFunction(Names.lastName(name), actuals);
	}
	
	public FunctionDeclaration getFunction(Name name, TupleType actuals) {
		return getFunction(Names.name(name), actuals);
	}
	
	public FunctionDeclaration getFunction(String name, TupleType actuals) {
		List<FunctionDeclaration> candidates = functionEnvironment.get(name);
		
		if (candidates != null) {
			for (FunctionDeclaration candidate : candidates) {
				TupleType formals = (TupleType) candidate.getSignature().accept(types);
			
				if (actuals.isSubtypeOf(formals)) {
					return candidate;
				}
			}
		}
		
		return null;
	}
	
	public EvalResult getVariable(QualifiedName name) {
		return getVariable(Names.lastName(name));
	}
	
	public EvalResult getVariable(Name name) {
		return getVariable(Names.name(name));
	}
	
	public EvalResult getVariable(String name) {
		return variableEnvironment.get(name);
	}
	
	public void storeType(ParameterType par, Type type) {
		typeParameters.put(par, type);
	}
	
	public Type getType(ParameterType par) {
		return typeParameters.get(par);
	}
	
	public void storeType(NamedType decl) {
		namedTypes.add(decl);
	}
	
	public void storeType(NamedTreeType decl) {
		List<TreeNodeType> tmp = signature.get(decl);
		
		if (tmp == null) {
			tmp = new LinkedList<TreeNodeType>();
			signature.put(decl, tmp);
		}
	}
	
	public void storeType(TreeNodeType decl) {
		NamedTreeType sort = decl.getSuperType();
		List<TreeNodeType> tmp = signature.get(sort);
		
		if (tmp == null) {
			tmp = new LinkedList<TreeNodeType>();
			signature.put(sort, tmp);
		}
		
		tmp.add(decl);
	}

	public void storeVariable(QualifiedName name, EvalResult value) {
		storeVariable(Names.lastName(name), value);
		
	}
	
	public void storeVariable(Name name, EvalResult value) {
		storeVariable(Names.name(name), value);
	}
	
	public void storeVariable(String name, EvalResult value) {
		variableEnvironment.put(name, value);
	}
	
	public void storeFunction(QualifiedName name, FunctionDeclaration function) {
		storeFunction(Names.lastName(name), function);
	}
	
	public void storeFunction(Name name, FunctionDeclaration function) {
		storeFunction(Names.name(name), function);
	}
	
	public void storeFunction(String name, FunctionDeclaration function) {
		TupleType formals = (TupleType) function.getSignature().getParameters().accept(types);
		FunctionDeclaration definedEarlier = getFunction(name, formals);
		
		if (definedEarlier != null) {
			throw new RascalTypeError("Illegal redeclaration of function: " + definedEarlier + "\n overlaps with new function: " + function);
		}
		
		List<FunctionDeclaration> list = functionEnvironment.get(name);
		if (list == null) {
			list = new LinkedList<FunctionDeclaration>();
			functionEnvironment.put(name, list);
		}
		
		list.add(function);
	}
	
	public Map<ParameterType, Type> getTypes() {
		return Collections.unmodifiableMap(typeParameters);
	}
	
	public void storeTypes(Map<ParameterType, Type> bindings) {
		typeParameters.putAll(bindings);
	}
}