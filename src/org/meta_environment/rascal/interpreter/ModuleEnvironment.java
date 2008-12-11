package org.meta_environment.rascal.interpreter;

import java.util.HashSet;
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
 * A module environment represents a module object (i.e. a running module).
 * It has it's own stack  and manages imported modules and visibility of the
 * functions and variables it declares.
 * 
 */
public class ModuleEnvironment implements IEnvironment {
	private final String name;
	protected final EnvironmentStack stack;
	protected final Set<String> importedModules;
	
	public ModuleEnvironment(String name) {
		this.name = name;
		this.importedModules = new HashSet<String>();
		
		this.stack = new EnvironmentStack(new Environment() {
			@Override
			public boolean isRootEnvironment() {
				return true;
			}
		});
	}
	
	public void pushFrame(IEnvironment env) {
		stack.push(env);
	}
	
	public void pushFrame() {
		stack.pushFrame();
	}
	
	public void popFrame() {
		stack.popFrame();
	}
	
	public String getModuleName() {
		return name;
	}
	
	public boolean isRootEnvironment() {
		return true;
	}
	
	public void addImport(String name) {
		importedModules.add(name);
	}
	
	public Set<String> getImports() {
		return importedModules;
	}

	public FunctionDeclaration getFunction(String name, TupleType actuals) {
		FunctionDeclaration decl = stack.getFunction(name, actuals);
		if (decl == null) {
			for (String mod : getImports()) {
				IEnvironment mEnv = GlobalEnvironment.getInstance().getModule(mod);
				if (mEnv != null) {
					if ((decl = mEnv.getFunction(name, actuals)) != null) {
						return decl;
					}
				}
			}
		}
		
		return decl;
	}
	
	IEnvironment getFunctionDefiningEnvironment(Name name, TupleType formals) {
		FunctionDeclaration decl = stack.getFunction(name, formals);
		if (decl == null) {
			for (String mod : getImports()) {
				IEnvironment mEnv = GlobalEnvironment.getInstance().getModule(mod);
				if (mEnv != null) {
					if (mEnv.getFunction(name, formals) != null) {
						return mEnv;
					}
				}
			}
		}
		
		return stack.bottom();
	}

	
	
	public FunctionDeclaration getFunction(Name name, TupleType actuals) {
		return getFunction(Names.name(name), actuals);
	}

	public Type getType(ParameterType par) {
		return stack.getType(par);
	}

	public Map<ParameterType, Type> getTypes() {
		return stack.getTypes();
	}

	public EvalResult getVariable(Name name) {
		return stack.getVariable(name);
	}

	public void storeFunction(Name name, FunctionDeclaration function) {
		stack.storeFunction(name, function);
	}

	public void storeType(ParameterType par, Type type) {
		stack.storeType(par, type);
	}

	public void storeType(NamedType decl) {
		stack.storeType(decl);
	}

	public void storeType(NamedTreeType decl) {
		stack.storeType(decl);
	}

	public void storeType(TreeNodeType decl) {
		stack.storeType(decl);
	}

	public void storeTypes(Map<ParameterType, Type> bindings) {
		stack.storeTypes(bindings);
		
	}

	public void storeVariable(Name name, EvalResult value) {
		stack.storeVariable(name, value);
	}

	public FunctionDeclaration getFunction(QualifiedName name, TupleType actuals) {
		return stack.getFunction(name, actuals);
	}

	public EvalResult getVariable(QualifiedName name) {
		return stack.getVariable(name);
	}

	public void storeFunction(QualifiedName name, FunctionDeclaration function) {
		stack.storeFunction(name, function);
		
	}

	public void storeVariable(QualifiedName name, EvalResult value) {
		stack.storeVariable(name, value);
	}

	public EvalResult getVariable(String name) {
		return stack.getVariable(name);
	}

	public void storeFunction(String name, FunctionDeclaration function) {
		stack.storeFunction(name, function);
	}

	public void storeVariable(String name, EvalResult value) {
		stack.storeVariable(name, value);
	}
}
