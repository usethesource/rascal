package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.Result;
import org.eclipse.imp.pdb.facts.type.Type;


public class StrategyFunction extends AbstractFunction {
	
	protected AbstractFunction function;

	public StrategyFunction(AbstractFunction function) {
		super(function.getAst(), function.getEval(), function.getFunctionType(), function.hasVarArgs(), function.getEnv());
		this.function = function;
	}
	
	@Override
	public Result<?> call(Type[] argTypes, IValue[] argValues,
			IEvaluatorContext ctx) {
		return function.call(argTypes, argValues, ctx);
	}

}
