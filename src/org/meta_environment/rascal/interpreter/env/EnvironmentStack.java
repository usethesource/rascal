package org.meta_environment.rascal.interpreter.env;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.exceptions.RascalBug;

/**
 * An environment that implements the scoping rules of Rascal.
 */
public class  EnvironmentStack implements Iterable<Environment>{
	protected final ArrayDeque<Environment> stack;
	
	public EnvironmentStack(){
		super();
		
		stack = new ArrayDeque<Environment>();
	}
	
	public EnvironmentStack(ModuleEnvironment bottom){
		super();
		
		stack = new ArrayDeque<Environment>();
		stack.offer(bottom);
	}
	
	public EnvironmentStack(ArrayDeque<Environment> stack){
		super();
		
		this.stack = stack;
	}
    
	public void clean(ModuleEnvironment bottom) {
		stack.clear();
		stack.offer(bottom);
	}
	
	public Iterator<Environment> iterator() {
		return stack.iterator();
	}

	public void pushFrame() {
		stack.offer(new Environment());
	}
	
	public void pushModule(ModuleEnvironment e) {
		if (e == null) { 
			throw new RascalBug("null environment");
		}
		stack.offer(e);
	}

	public void pushFrame(Environment env) {
		if (env == null) { 
			throw new RascalBug("null environment");
		}
		stack.offer(env);
	}

	public void popFrame() {
		stack.pollLast();
	}
	
	public void popModule() {
		stack.pollLast();
	}

	protected Environment bottom() {
		return stack.peekFirst();
	}
	
	protected Environment top() {
		return stack.peekLast();
	}
	
	protected int size(){
		return stack.size();
	}
	
	public void storeFunction(String name, FunctionDeclaration function) {
		top().storeFunction(name, function);
	}

	public void storeVariable(String name, EvalResult value) {
		getVariableDefiningEnvironment(name).storeVariable(name, value);
	}
	
	public ModuleEnvironment getModuleEnvironment() {
		Iterator<Environment> environmentIterator = stack.descendingIterator();
		while(environmentIterator.hasNext()){
			Environment env = environmentIterator.next();
			if(env.isModuleEnvironment()){
				return (ModuleEnvironment) env;
			}
		}
		
		return (ModuleEnvironment) bottom();
	}

	public Environment getFunctionDefiningEnvironment(String name, Type formals, EnvironmentHolder h) {
		//System.err.println("getFunctionDefiningEnvironment: stacksize=" + stack.size());
		Iterator<Environment> environmentIterator = stack.descendingIterator();
		while(environmentIterator.hasNext()){
			Environment env = environmentIterator.next();
			//System.err.println("stack(" + i + ")\n" + env);
			
			if (env.isModuleEnvironment() || env.getFunction(name, formals, h) != null) {
				h.setEnvironment(env);
				return env;
			}
		}
		
		return top();
	}
	
	public Environment getVariableDefiningEnvironment(String name) {
		// the first stack frame that contains a definition for this name
		// is returned. This allows inner scopes to override outer scopes.
		Iterator<Environment> environmentIterator = stack.descendingIterator();
		while(environmentIterator.hasNext()){
			Environment env = environmentIterator.next();
			
			// the outermost scope for plain variables is a module environment
            if (env.isModuleEnvironment() || env.getVariable(name) != null) {
            	return env;
            }
		}
		
		return top();
	}

	public Map<Type, Type> getTypeBindings() {
		Map<Type,Type> types = new HashMap<Type,Type>();
		
		Iterator<Environment> environmentIterator = stack.descendingIterator();
		while(environmentIterator.hasNext()){
			Environment env = environmentIterator.next();
			types.putAll(env.getTypeBindings());
			
			if (env.isModuleEnvironment()) {
				break;
			}
		}
		
		// result can not be given to a match
		return Collections.unmodifiableMap(types);
	}

	public void storeParameterType(Type par, Type type) {
		bottom().storeParameterType(par, type);
	}
	
	public void storeTypeBindings(Map<Type, Type> bindings) {
		top().storeTypeBindings(bindings);
	}

	public FunctionDeclaration getFunction(Name name, Type actuals, EnvironmentHolder h) {
		return getFunction(Names.name(name), actuals, h);
	}
	
	public FunctionDeclaration getFunction(String name, Type actuals, EnvironmentHolder h) {
		Environment env = getFunctionDefiningEnvironment(name, actuals, h);
		return env.getFunction(name, actuals, h);
	}
	
	public Type getParameterType(Type par) {
		return bottom().getParameterType(par);
	}

	public EvalResult getVariable(String name) {
		Environment env = getVariableDefiningEnvironment(name);
		return env.getVariable(name);
	}

	public void storeTypeAlias(Type decl) {
		getModuleEnvironment().storeTypeAlias(decl);
	}

	public void storeAbstractDataType(Type decl) {
		getModuleEnvironment().storeAbstractDataType(decl);
	}

	public void storeConstructor(Type decl) {
		getModuleEnvironment().storeConstructor(decl);
	}
	
	public void storeDefinition(Type adt, Type extension) {
		getModuleEnvironment().storeDefinition(adt, extension);
	}

	public FunctionDeclaration getFunction(QualifiedName name, Type actuals, EnvironmentHolder h) {
		return getFunction(Names.lastName(name), actuals, h);
	}

	public EvalResult getVariable(QualifiedName name) {
		return getVariable(Names.lastName(name));
	}

	public void storeFunction(QualifiedName name, FunctionDeclaration function) {
		storeFunction(Names.lastName(name), function);
	}

	public void storeVariable(QualifiedName name, EvalResult value) {
		storeVariable(Names.lastName(name), value);
	}

	public EvalResult getVariable(Name name) {
		return getVariable(Names.name(name));
	}

	public void storeFunction(Name name, FunctionDeclaration function) {
		storeFunction(Names.name(name), function);
	}

	public void storeVariable(Name name, EvalResult value) {
		storeVariable(Names.name(name), value);
	}

	public Type getAbstractDataType(String sort) {
		return getModuleEnvironment().getAbstractDataType(sort);
	}

	public Type getConstructor(Type sort, String cons,
			Type args) {
		return getModuleEnvironment().getConstructor(sort, cons, args);
	}

	public void storeAnnotation(Type onType, String name, Type annoType) {
		getModuleEnvironment().storeAnnotation(onType, name, annoType);
	}

	public Type getConstructor(String cons, Type args) {
		return getModuleEnvironment().getConstructor(cons, args);
	}

	public EnvironmentStack copyStack(){
		return new EnvironmentStack(stack.clone());
	}
	
	public String toString(){
		StringBuffer res = new StringBuffer();
		Iterator<Environment> environmentIterator = stack.iterator();
		int i = 0;
		while(environmentIterator.hasNext()){
			Environment env = environmentIterator.next();
			res.append("Stack Environment #" + i++ + ":\n").append(env.toString()).append("\n");
		}
		return res.toString();
	}
}
