package org.meta_environment.rascal.interpreter.env;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.type.NamedTreeType;
import org.eclipse.imp.pdb.facts.type.NamedType;
import org.eclipse.imp.pdb.facts.type.ParameterType;
import org.eclipse.imp.pdb.facts.type.TreeNodeType;
import org.eclipse.imp.pdb.facts.type.TupleType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.Declaration;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.ast.Rule;
import org.meta_environment.rascal.ast.Visibility;
import org.meta_environment.rascal.ast.Declaration.Annotation;
import org.meta_environment.rascal.ast.Declaration.Function;
import org.meta_environment.rascal.ast.Declaration.Variable;
import org.meta_environment.rascal.interpreter.EvalResult;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.RascalBug;
import org.meta_environment.rascal.interpreter.RascalTypeError;

/**
 * The global environment represents the stack and the heap of Rascal.
 * The stack is initialized with a bottom frame, which represent the shell
 * environment.
 * 
 */
public class GlobalEnvironment {
	private static final String SHELL = "***root***";

	/** The heap of Rascal */
	private final Map<String, ModuleEnvironment> moduleEnvironment = new HashMap<String, ModuleEnvironment>();
	
	/** The stack of Rascal */
	private final EnvironmentStack stack  = new EnvironmentStack(new ModuleEnvironment(SHELL));
	
	/** Normalizing rules are a global feature */
	private final Map<Type, List<Rule>> ruleEnvironment = new HashMap<Type, List<Rule>>();
	
	/** There is only one global environment */
	private static final GlobalEnvironment sInstance = new GlobalEnvironment();
	
	private GlobalEnvironment() { }

	public static GlobalEnvironment getInstance() {
		return sInstance;
	}
	
	public static void clean() {
		GlobalEnvironment instance = getInstance();
		instance.moduleEnvironment.clear();
		instance.stack.clean(new ModuleEnvironment(SHELL));
		instance.ruleEnvironment.clear();
	}
	
	/**
	 * Adds an import to the module that is currently on the stack.
	 * @param name
	 */
	public void addImport(String name) {
		stack.getModuleEnvironment().addImport(name);
	}
	
	/**
	 * Allocate a new module on the heap
	 * @param name
	 */
	public void addModule(String name) {
		ModuleEnvironment env = moduleEnvironment.get(name);
		if (env == null) {
			moduleEnvironment.put(name, new ModuleEnvironment(name));
		}
	}
	
	/**
	 * Push a previously allocated module on the stack
	 * @param module
	 */
	public void pushModule(String module) {
		stack.pushModule(getModule(module));
	}
	
	/**
	 * Push a previously allocated module on the stack
	 * @param module
	 */
	public void pushModule(QualifiedName name) {
		pushModule(Names.moduleName(name));
	}
	
	
	/**
	 * Push a local scope on the stack
	 */
	public void pushFrame() {
		stack.pushFrame();
	}
	
	/**
	 * Pop the stack
	 */
	public void popFrame() {
		stack.popFrame();
	}
	
	public void popModule() {
		if (!stack.top().isModuleEnvironment()) {
			throw new RascalBug("Popping a local scope instead of a module scope?!");
		}
		stack.popModule();
	}
	
	/**
	 * Retrieve a module from the heap
	 */
	public ModuleEnvironment getModule(String name) {
		ModuleEnvironment result = moduleEnvironment.get(name);
		
		if (result == null) {
			throw new RascalTypeError("No such module " + name);
		}
		
		return result;
	}

	public ModuleEnvironment getModule(QualifiedName name) {
		return getModule(Names.moduleName(name));
	}
	
	public EvalResult getModuleVariable(String module, Name variable) {
		return getModule(module).getVariable(Names.name(variable));
	}
	
	public FunctionDeclaration getModuleFunction(String module, Name function, TupleType actuals) {
		return getModule(module).getFunction(Names.name(function), actuals);
	}
	
	private void storeModuleFunction(String module, Name name, FunctionDeclaration function) {
		getModule(module).storeFunction(Names.name(name), function);
	}
	
	public void storeVariable(QualifiedName name, EvalResult value) {
		String module = Names.moduleName(name);
		Name var = Names.lastName(name);
		
		if (module == null) {
			stack.storeVariable(var, value);
		}
		else {
			getModule(module).storeVariable(Names.name(var), value);
		}
	}
	
	public void storeVariable(Name name, EvalResult value) {
		storeVariable(Names.name(name), value);
	}
	
	public void storeFunction(Name name, FunctionDeclaration function) {
		storeFunction(Names.name(name), function);
	}
	
	public EvalResult getVariable(QualifiedName name) {
		String module = Names.moduleName(name);
		Name var = Names.lastName(name);
		
		if (module != null) {
			return getModuleVariable(module, var);
		}
		else {
			return getVariable(var);
		}
	}
	
	public EvalResult getVariable(Name name) {
		return getVariable(Names.name(name));
	}
	
	public EvalResult getVariable(String name) {
		return stack.getVariable(name);
	}
	
	public void storeVariable(String name, EvalResult value) {
		stack.storeVariable(name, value);
	}
	
	public void storeFunction(QualifiedName name, FunctionDeclaration function) {
		String module = Names.moduleName(name);
		Name func = Names.lastName(name);
		
		if (module == null) {
			storeFunction(func, function);
		}
		else {
			storeModuleFunction(module, func, function);
		}
	}
	
	public void storeFunction(String name, FunctionDeclaration function) {
		stack.storeFunction(name, function);
	}
	
	public FunctionDeclaration getFunction(String name, TupleType actuals) {
		return stack.getFunction(name, actuals);
	}
	
	public FunctionDeclaration getFunction(QualifiedName name, TupleType actuals) {
		String module = Names.moduleName(name);
		Name function = Names.lastName(name);
		
		if (module != null) {
			return getModuleFunction(module, function, actuals);
		}
		return stack.getFunction(name, actuals);
	}
	
	public void storeRule(Type forType, Rule rule) {
		List<Rule> rules = ruleEnvironment.get(forType);
		
		if (rules == null) {
			rules = new LinkedList<Rule>();
			ruleEnvironment.put(forType, rules);
		}
		
		rules.add(rule);
	}
	
	public List<Rule> getRules(Type forType) {
		List<Rule> rules = ruleEnvironment.get(forType);
		return rules != null ? rules : new LinkedList<Rule>();
	}

	public Type getType(ParameterType par) {
		return stack.getType(par);
	}

	public Map<ParameterType, Type> getTypes() {
		return stack.getTypes();
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
	
	public void setVisibility(Declaration decl, final Visibility vis) {
		NullASTVisitor<Declaration> dispatcher = new NullASTVisitor<Declaration>() {
			@Override
			public Declaration visitDeclarationFunction(Function x) {
				stack.getModuleEnvironment().setFunctionVisibility(x.getFunctionDeclaration(), vis);
				return x;
			}
			
			@Override
			public Declaration visitDeclarationVariable(Variable x) {
				for (org.meta_environment.rascal.ast.Variable y : x.getVariables()) {
					stack.getModuleEnvironment().setVariableVisibility(Names.name(y.getName()), vis);
				}
				return x;
			}
		};
		
		decl.accept(dispatcher);
	}

	public NamedTreeType getNamedTreeType(String sort) {
		return stack.getNamedTreeType(sort);
	}

	public TreeNodeType getTreeNodeType(NamedTreeType sortType, String cons,
			TupleType signature) {
		return stack.getTreeNodeType(sortType, cons, signature);
	}

	public void storeAnnotation(Type onType, String name, Type annoType) {
		stack.storeAnnotation(onType, name, annoType);
	}

	public TreeNodeType getTreeNodeType(String cons, TupleType args) {
		return stack.getTreeNodeType(cons, args);
	}
}
