package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ElementResult;
import org.meta_environment.rascal.interpreter.result.Result;


public class StrategyFunction extends Strategy {
	
	protected AbstractFunction function;
	
	public StrategyFunction(AbstractFunction function) {
		super(function);
	}
	
	@Override
	public Result<?> call(Type[] argTypes, IValue[] argValues,
			IEvaluatorContext ctx) {
		if (argTypes[0].comparable(function.getFormals().getFieldType(0))) {
			return function.call(argTypes, argValues, ctx);
		} else {
			// identity
			return new ElementResult<IValue>(argValues[0].getType(), argValues[0], ctx);
		}
	}

}
