package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ElementResult;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;

public class One extends Strategy {

	public One(AbstractFunction function) {
		super(function);
	}

	@Override
	public Result<?> call(Type[] argTypes, IValue[] argValues,
			IEvaluatorContext ctx) {
		Visitable result = VisitableFactory.make(argValues[0]);
		for (int i = 0; i < result.arity(); i++) {
			IValue child = result.get(i).getValue();
			IValue newchild = function.call(new Type[]{child.getType()}, new IValue[]{child}, ctx).getValue();
			if (!newchild.equals(child)) {
				result =  result.set(i, VisitableFactory.make(newchild));
				break;
			}
		}
		return new ElementResult<IValue>(result.getValue().getType(), result.getValue(), ctx);
	}

	public static IValue makeOne(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new One(new StrategyFunction(function));	
			}
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isStrategy()) {
					return new One (new StrategyFunction(function));	
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}

}
