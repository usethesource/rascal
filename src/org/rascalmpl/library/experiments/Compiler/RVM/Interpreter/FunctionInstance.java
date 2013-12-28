package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;

public class FunctionInstance implements ICallableValue {
	
	final Function function;
	final Frame env;
	final RVM rvm;
	
	public FunctionInstance(Function function, Frame env, RVM rvm) {
		this.function = function;
		this.env = env;
		this.rvm = rvm;
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
		return ResultFactory.makeResult(rval.getType(), rval, rvm.ctx);
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
		return rvm.ctx.getEvaluator();
	}

}
