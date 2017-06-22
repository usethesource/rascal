package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.Type;

public class OverloadedFunctionInstanceCall {
	
	private final int[] functions;
	private final int[] constructors;
	final Frame cf;
	final RascalExecutionContext rex;
	
	final Frame previousScope;
	final Type types;
	
	final int arity;
	final Object[] stack;
	final int sp;
	
	int alternative = 0;
	
	public OverloadedFunctionInstanceCall(final Frame cf, final int[] functions, final int[] constructors, final Frame previousScope, final Type types, final int arity, RascalExecutionContext rex) {
		this.cf = cf;
		assert functions.length + constructors.length > 0;
		assert functions.length == 0 && types == null ? constructors.length > 0 : true;
		this.functions = functions;
		this.constructors = constructors;
		this.previousScope = previousScope;
		this.types = types;
		this.arity = arity;
		this.stack = cf.stack;
		this.sp = cf.sp;
		this.rex = rex;
	}
	
	int[] getFunctions() {
		return functions;
	}

	int[] getConstructors() {
		return constructors;
	}

	public String toString(List<Function> functionStore, List<Type> constructorStore){
		StringBuilder sb = new StringBuilder("OverloadedFunctionInstanceCall[");
		if(getFunctions().length > 0){
			sb.append("functions:");
			for(int i = 0; i < getFunctions().length; i++){
				int fi = getFunctions()[i];
				sb.append(" ").append(functionStore.get(fi).getName()).append("/").append(fi);
			}
		}
		if(getConstructors().length > 0){
			if(getFunctions().length > 0){
				sb.append("; ");
			}
			sb.append("constructors:");
			for(int i = 0; i < getConstructors().length; i++){
				int ci = getConstructors()[i];
				sb.append(" ").append(constructorStore.get(ci).getName()).append("/").append(ci);
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Assumption: scopeIn != -1; 
	 * @param rex TODO
	 */
	public static OverloadedFunctionInstanceCall computeOverloadedFunctionInstanceCall(final Frame cf, final int[] functions, final int[] constructors, final int scopeIn, final Type types, final int arity, RascalExecutionContext rex) {
		assert scopeIn != -1 : "OverloadedFunctionInstanceCall, scopeIn should not be -1";
		for(Frame previousScope = cf; previousScope != null; previousScope = previousScope.previousScope) {
			if (previousScope.scopeId == scopeIn) {
				return new OverloadedFunctionInstanceCall(cf, functions, constructors, previousScope, types, arity, rex);
			}
		}
		throw new InternalCompilerError("Could not find a matching scope when computing a nested overloaded function instance: " + scopeIn, cf);
	}
	
	public Frame nextFrame(final Function[] functionStore) {
		Function f = this.nextFunction(functionStore);
		if(f == null) {
			return null;
		}
		return cf.getFrame(f, previousScope, arity, sp);
	}
	
	public Function nextFunction(final Function[] functionStore) {
		if(types == null) {
			return alternative < getFunctions().length ? functionStore[getFunctions()[alternative++]] : null;
		} else {
			while(alternative < getFunctions().length) {
				Function fun = functionStore[getFunctions()[alternative++]];
				for(Type type : types) {
					try {
						Map<Type,Type> bindings = new HashMap<Type,Type>();
						type.match(fun.ftype, bindings);
						return fun;
					} catch(FactTypeUseException e) {
						;
					}
				}
			}
		}
		return null;
	}
	
	public Type nextConstructor(final List<Type> constructorStore) {
		if(types == null) {
			if(getConstructors().length == 0){
			    StringBuffer sb = new StringBuffer("No alternative found for (overloaded) function or constructor\n");	
				if(getFunctions().length > 0){
					sb.append("Function(s):\n");
					for(int i = 0; i < getFunctions().length; i++){
						sb.append(" " + getFunctions()[i]);
					}
					sb.append("\n");
				}
				if(getConstructors().length > 0){
					sb.append("Constructor(s):\n");
					for(int i = 0; i < getConstructors().length; i++){
						sb.append(" " + getConstructors()[i]);
					}
					sb.append("\n");
				}
				rex.getFrameObserver().exception(cf, RascalRuntimeException.incompletelyDefinedFunction(sb.toString(), cf));
				return null;
			}
			assert getConstructors().length >= 1;
			return constructorStore.get(getConstructors()[0]);
		} else {
			for(int index : getConstructors()) {
				Type constructor = constructorStore.get(index);
				for(Type type : types) {
					try {
						Map<Type,Type> bindings = new HashMap<Type,Type>();
						type.match(constructor, bindings);
						return constructor;
					} catch(FactTypeUseException e) {
						;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Assumptions: 
	 * - constructors do not permit var args,
	 * - only constructors without keyword parameters may be directly called within an overloaded function call, 
	 * - constructors with keyword parameters are indirectly called via companion functions.
	 */
	public IValue[] getConstructorArguments(final int arity) {
		IValue[] args = new IValue[arity];
		for(int i = 0; i < arity; i++) {
			args[i] = (IValue) stack[sp - this.arity + i]; 
		}
		return args;
	}
	
}
