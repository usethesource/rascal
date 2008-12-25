package org.meta_environment.rascal.interpreter.env;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.Declaration;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.ast.Rule;
import org.meta_environment.rascal.ast.Visibility;
import org.meta_environment.rascal.ast.Declaration.Function;
import org.meta_environment.rascal.ast.Declaration.Variable;
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
	
	public void pushModule(ModuleEnvironment module) {
		stack.pushModule(module);
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
	
	public void pushFrame(Environment env) {
		stack.pushFrame(env);
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
	
	public FunctionDeclaration getModuleFunction(String module, Name function, Type actuals) {
		return getModule(module).getFunction(Names.name(function), actuals, new EnvironmentHolder());
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
		EvalResult r = stack.getVariable(name);
		
		StringBuffer indent = new StringBuffer();
		for(int i = 0; i < stack.size(); i++){
			indent.append(" ");
		}
		
//		System.err.println(indent + "getVariable(" + name + ") -> " + r);
		return r;
	}
	
	public void storeVariable(String name, EvalResult value) {
		StringBuffer indent = new StringBuffer();
		for(int i = 0; i < stack.size(); i++){
			indent.append(" ");
		}

//		System.err.println(indent + "storeVariable(" + name + ", " + value + ")");
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
	
	public FunctionDeclaration getFunction(String name, Type actuals, EnvironmentHolder h) {
		return stack.getFunction(name, actuals, h);
	}
	
	public FunctionDeclaration getFunction(QualifiedName name, Type actuals, EnvironmentHolder h) {
		String module = Names.moduleName(name);
		Name function = Names.lastName(name);
		
		//System.err.println("getFunction: name=" + name + ", actuals=" + actuals);
		//System.err.println("getFunction: module=" + module + ", function=" + function);
		
		if (module != null) {
			h.setEnvironment(getModule(module));
			return getModuleFunction(module, function, actuals);
		}
		return stack.getFunction(name, actuals, h);
	}
	
	public void storeRule(Type forType, Rule rule) {
		List<Rule> rules = ruleEnvironment.get(forType);
		
		System.err.println("storeRule: type=" + forType + ",rule=" + rule);
		if (rules == null) {
			rules = new LinkedList<Rule>();
			ruleEnvironment.put(forType, rules);
		}
		
		rules.add(rule);
	}
	
	public List<Rule> getRules(Type forType) {
		List<Rule> rules = ruleEnvironment.get(forType);
		//System.err.println("getRules: type=" + forType + ",rules=" + rules);
		return rules != null ? rules : new LinkedList<Rule>();
	}

	public Type getParameterType(Type par) {
		return stack.getParameterType(par);
	}

	public Map<Type, Type> getTypeBindings() {
		return stack.getTypeBindings();
	}

	public void storeParameterType(Type par, Type type) {
		stack.storeParameterType(par, type);
	}

	public void storeTreeNodeType(Type decl) {
		stack.storeTreeNodeType(decl);
	}

	public void storeNamedTreeType(Type decl) {
		stack.storeNamedTreeType(decl);
	}

	public void storeNamedType(Type decl) {
		stack.storeNamedType(decl);
	}

	public void storeTypeBindings(Map<Type, Type> bindings) {
		stack.storeTypeBindings(bindings);
		
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

	public Type getNamedTreeType(String sort) {
		return stack.getNamedTreeType(sort);
	}

	public Type getTreeNodeType(Type sortType, String cons,
			Type signature) {
		return stack.getTreeNodeType(sortType, cons, signature);
	}

	public void storeAnnotation(Type onType, String name, Type annoType) {
		stack.storeAnnotation(onType, name, annoType);
	}

	public Type getTreeNodeType(String cons, Type args) {
		return stack.getTreeNodeType(cons, args);
	}

	public boolean existsModule(String name) {
		return moduleEnvironment.containsKey(name);
	}

	public EnvironmentStack copyStack() {
		return stack.copyStack();
	}

	public Environment top() {
		return stack.top();
	}
}
