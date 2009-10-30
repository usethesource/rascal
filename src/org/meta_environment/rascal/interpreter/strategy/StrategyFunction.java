package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;

public class StrategyFunction extends AbstractStrategy {
	
	public StrategyFunction(AbstractFunction function) {
		super(function);
	}
	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		if (argTypes[0].comparable(function.getFormals().getFieldType(0))) {
			return function.call(argTypes, argValues);
		}
		
		// identity
		return makeResult(argValues[0], ctx);
	}
	
	public static IValue functionToStrategy(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isTypePreserving()) {
				return new StrategyFunction(function);	
			}
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isTypePreserving()) {
					return new StrategyFunction(function);	
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}


}
