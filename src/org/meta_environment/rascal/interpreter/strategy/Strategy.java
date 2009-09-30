package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.Result;

public class Strategy extends AbstractFunction {

    protected final AbstractFunction function;
	
	public Strategy(AbstractFunction function) {
		super(function.getAst(), function.getEval(), function.getFunctionType(), function.hasVarArgs(), function.getEnv());
		this.function = function;
	}
	
	public AbstractFunction getFunction() {
		return function;
	}
	
	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		return function.call(argTypes, argValues);
	}

}
