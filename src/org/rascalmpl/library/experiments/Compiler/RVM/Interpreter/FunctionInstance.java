package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IWithKeywordParameters;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.rascalmpl.interpreter.IEvaluator;				// TODO: remove import: NOT YET
import org.rascalmpl.interpreter.IRascalMonitor;			// TODO: remove import: NOT YET
import org.rascalmpl.interpreter.env.Environment;			// TODO: remove import: NOT YET
import org.rascalmpl.interpreter.result.ICallableValue;		// TODO: remove import: NOT YET
import org.rascalmpl.interpreter.result.Result;				// TODO: remove import: NOT YET
import org.rascalmpl.interpreter.result.ResultFactory;		// TODO: remove import: NOT YET

public class FunctionInstance implements ICallableValue, IExternalValue {
	
	final Function function;
	final Frame env;
	final RVM rvm;
	
	/*
	 * Records arguments in case of partial parameter binding
	 */
	Object[] args;
	int next = 0;
	
	public FunctionInstance(Function function, Frame env, RVM rvm) {
		this.function = function;
		this.env = env;
		this.rvm = rvm;
	}
	
	/**
	 * Assumption: scopeIn != -1; 
	 */
	public static FunctionInstance computeFunctionInstance(Function function, Frame cf, int scopeIn, RVM rvm) {
		for(Frame env = cf; env != null; env = env.previousScope) {
			if (env.scopeId == scopeIn) {
				return new FunctionInstance(function, env, rvm);
			}
		}
		throw new CompilerError("Inside " + cf.function.name + " (" + cf.src + "): cannot find matching scope when looking for nested function " + function.name);
	}
	
	/**
	 * Assumption: arity < function.nformals 
	 */
	public static FunctionInstance applyPartial(Function function, Frame env, RVM rvm, int arity, Object[] stack, int sp) {
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
	 * Assumption: next + arity < function.nformals 
	 */
	public FunctionInstance applyPartial(int arity, Object[] stack, int sp) {
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
	public int getArity() {
		return function.nformals;
	}

	@Override
	public boolean hasVarArgs() {
		return function.isVarArgs;
	}

	@Override
	public boolean hasKeywordArgs() {
		return true;
	}

	@Override
	public Result<IValue> call(IRascalMonitor monitor, Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) {
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
		IValue rval = rvm.executeFunction(this, args);
		return ResultFactory.makeResult(rval.getType(), rval, rvm.getEvaluatorContext());	// TODO: remove CTX
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) {
		return this.call(null, argTypes, argValues, keyArgValues);
	}

	@Override
	public ICallableValue cloneInto(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isStatic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IEvaluator<Result<IValue>> getEval() {
		return rvm.getEvaluatorContext().getEvaluator();	// TODO: remove CTX
	}
	
	@Override
  public boolean mayHaveKeywordParameters() {
    return false;
  }
  
  @Override
  public IWithKeywordParameters<? extends IValue> asWithKeywordParameters() {
    throw new IllegalOperationException(
        "Cannot be viewed as with keyword parameters", getType());
  }

}
