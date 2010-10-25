package org.rascalmpl.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.Result;

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
}
