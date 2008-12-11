package org.meta_environment.rascal.interpreter;

import java.util.Map;

import org.eclipse.imp.pdb.facts.type.NamedTreeType;
import org.eclipse.imp.pdb.facts.type.NamedType;
import org.eclipse.imp.pdb.facts.type.ParameterType;
import org.eclipse.imp.pdb.facts.type.TreeNodeType;
import org.eclipse.imp.pdb.facts.type.TupleType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.QualifiedName;

public interface IEnvironment {
	public FunctionDeclaration getFunction(String name, TupleType actuals);

	public FunctionDeclaration getFunction(Name name, TupleType actuals);

	public FunctionDeclaration getFunction(QualifiedName name, TupleType actuals);

	public EvalResult getVariable(String name);

	public EvalResult getVariable(QualifiedName name);

	public EvalResult getVariable(Name name);

	public void storeType(ParameterType par, Type type);

	public Type getType(ParameterType par);

	public void storeType(NamedType decl);

	public void storeType(NamedTreeType decl);

	public void storeType(TreeNodeType decl);

	public void storeVariable(String name, EvalResult value);

	public void storeVariable(QualifiedName name, EvalResult value);

	public void storeVariable(Name name, EvalResult value);

	public void storeFunction(String name, FunctionDeclaration function);

	public void storeFunction(QualifiedName name, FunctionDeclaration function);

	public void storeFunction(Name name, FunctionDeclaration function);

	public Map<ParameterType, Type> getTypes();

	public void storeTypes(Map<ParameterType, Type> bindings);
	
	public boolean isRootEnvironment();

}