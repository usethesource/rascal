package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;

public class OverloadedFunctionInstanceCall {
	
	final int[] functions;
	final int[] constructors;
	final Frame cf;
	
	final Frame previousScope;
	final Type types;
	
	final int arity;
	final Object[] stack;
	final int sp;
	
	int alternative = 0;
	
	public OverloadedFunctionInstanceCall(final Frame cf, final int[] functions, final int[] constructors, final Frame previousScope, final Type types, final int arity) {
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
	}
	
	public String toString(List<Function> functionStore, List<Type> constructorStore){
		StringBuilder sb = new StringBuilder("OverloadedFunctionInstanceCall[");
		if(functions.length > 0){
			sb.append("functions:");
			for(int i = 0; i < functions.length; i++){
				int fi = functions[i];
				sb.append(" ").append(functionStore.get(fi).getName()).append("/").append(fi);
			}
		}
		if(constructors.length > 0){
			if(functions.length > 0){
				sb.append("; ");
			}
			sb.append("constructors:");
			for(int i = 0; i < constructors.length; i++){
				int ci = constructors[i];
				sb.append(" ").append(constructorStore.get(ci).getName()).append("/").append(ci);
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Assumption: scopeIn != -1; 
	 */
	public static OverloadedFunctionInstanceCall computeOverloadedFunctionInstanceCall(final Frame cf, final int[] functions, final int[] constructors, final int scopeIn, final Type types, final int arity) {
		assert scopeIn != -1 : "OverloadedFunctionInstanceCall, scopeIn should not be -1";
		for(Frame previousScope = cf; previousScope != null; previousScope = previousScope.previousScope) {
			if (previousScope.scopeId == scopeIn) {
				return new OverloadedFunctionInstanceCall(cf, functions, constructors, previousScope, types, arity);
			}
		}
		throw new CompilerError("Could not find a matching scope when computing a nested overloaded function instance: " + scopeIn, cf);
	}
	
	public Frame nextFrame(final List<Function> functionStore) {
		Function f = this.nextFunction(functionStore);
		if(f == null) {
			return null;
		}
		return cf.getFrame(f, previousScope, arity, sp);
	}
	
	public Function nextFunction(final List<Function> functionStore) {
		if(types == null) {
			return alternative < functions.length ? functionStore.get(functions[alternative++]) : null;
		} else {
			while(alternative < functions.length) {
				Function fun = functionStore.get(functions[alternative++]);
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
			if(constructors.length == 0){
				System.err.println("empty constructor list!");
			}
			assert constructors.length >= 1;
			return constructorStore.get(constructors[0]);
		} else {
			for(int index : constructors) {
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
