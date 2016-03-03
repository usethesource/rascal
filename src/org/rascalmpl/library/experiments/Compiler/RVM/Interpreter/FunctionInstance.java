package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.value.IAnnotatable;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IExternalValue;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.value.exceptions.IllegalOperationException;
import org.rascalmpl.value.impl.AbstractExternalValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.visitors.IValueVisitor;


public class FunctionInstance implements ICallableCompiledValue, IExternalValue {
	
	public final Function function;
	final Frame env;
	final RVMInterpreter rvm;
	
	/*
	 * Records arguments in case of partial parameter binding
	 */
	Object[] args;
	public int next = 0;
	
	public FunctionInstance(final Function function, final Frame env, final RVMInterpreter rvm) {
		this.function = function;
		this.env = env;
		this.rvm = rvm;
	}
	
	/**
	 * Assumption: scopeIn != -1; 
	 */
	public static FunctionInstance computeFunctionInstance(final Function function, final Frame cf, final int scopeIn, final RVMInterpreter rvm) {
		assert scopeIn != -1;
		for(Frame env = cf; env != null; env = env.previousScope) {
			if (env.scopeId == scopeIn) {
				return new FunctionInstance(function, env, rvm);
			}
		}
		System.err.println("computeFunctionInstance " + function.name + ", scopeIn=" + scopeIn);
		System.err.println("Searched scopes:");
		for(Frame env = cf; env != null; env = env.previousScope) {
			System.err.println(env.scopeId);
		}
		
		throw new CompilerError("Inside " + cf.function.name + " (" + cf.src + ") and scope " + scopeIn + ": cannot find matching scope when looking for nested function " + function.name, rvm.getStdErr(), cf);
	}
	
	/**
	 * Assumption: arity <= function.nformals 
	 */
	public static FunctionInstance applyPartial(final Function function, final Frame env, final RVMInterpreter rvm, final int arity, final Object[] stack, final int sp) {
		assert arity <= function.nformals;
		FunctionInstance fun_instance = new FunctionInstance(function, env, rvm);
		if(arity == 0) {
			return fun_instance;
		}
		fun_instance.args = new Object[function.nformals];
		int start = sp - arity;
		for(int i = 0; i < arity; i++) {
			fun_instance.args[fun_instance.next++] = stack[start + i];
		}
		return fun_instance;
	}
	
	/**
	 * Assumption: next + arity <= function.nformals 
	 */
	public FunctionInstance applyPartial(final int arity, final Object[] stack, final int sp) {
		assert next + arity <= function.nformals;
		if(arity == 0) {
			return this;
		}
		FunctionInstance fun_instance = this.copy();
		int start = sp - arity;
		for(int i = 0; i < arity; i++) {
			fun_instance.args[fun_instance.next++] = stack[start + i];
		}
		return fun_instance;
	}
	
	private FunctionInstance copy() {
		FunctionInstance fun_instance = new FunctionInstance(function, env, rvm);
		if(args != null) {
			fun_instance.args = args.clone();
			fun_instance.next = next;
		} else {
			fun_instance.args = new Object[function.nformals];
		}
		return fun_instance;
	}
	
	@Override
	public Type getType() {
		return function.ftype;
	}

	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return v.visitExternal((IExternalValue) this);
	}

	@Override
	public boolean isEqual(IValue other) {
		return this == other;
	}

	@Override
	public boolean isAnnotatable() {
		return false;
	}

	@Override
	public IAnnotatable<? extends IValue> asAnnotatable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue call(IRascalMonitor monitor, Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) {
		IValue[] args = new IValue[argValues.length + 1];
		int i = 0;
		for(IValue argValue : argValues) {
			args[i++] = argValue;
		}
		IMapWriter kwargs = rvm.vf.mapWriter();
		if(keyArgValues != null) {
			for(Entry<String, IValue> entry : keyArgValues.entrySet()) {
				kwargs.put(rvm.vf.string(entry.getKey()), keyArgValues.get(entry.getValue()));
			}
		}
		args[i] = kwargs.done();
		IValue rval = rvm.executeRVMFunction(this, args);
		return rval;
	}

	@Override
	public IValue call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) {
		return this.call(rvm.getMonitor(), argTypes, argValues, keyArgValues);
	}

	@Override
	public boolean mayHaveKeywordParameters() {
		return false;
	}

	@Override
	public IWithKeywordParameters<? extends IValue> asWithKeywordParameters() {
		throw new IllegalOperationException("Cannot be viewed as with keyword parameters", getType());
	}

	@Override
	public IConstructor encodeAsConstructor() {
		return AbstractExternalValue.encodeAsConstructor(this);
	}

	public String toString() {
		return "FunctionInstance[" + function.name + "]";
	}

}
