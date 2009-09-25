package org.meta_environment.rascal.interpreter.strategy.topological;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.strategy.Strategy;

public class TopologicalStrategy extends Strategy {

	public TopologicalStrategy(AbstractFunction function) {
		super(function);
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		if (argValues[0] instanceof TopologicalVisitable<?>) {
			TopologicalVisitable<?> visitable = (TopologicalVisitable<?>)argValues[0];
			RelationContext context = visitable.getContext();
			IValue v = visitable.getValue();	
			Result<IValue> res = function.call(argTypes, new IValue[]{v});
			return ResultFactory.makeResult(res.getType(), TopologicalVisitableFactory.makeTopologicalVisitable(context, res.getValue()), ctx);
		}
		
		return function.call(argTypes, argValues);
	}

	public static IValue makeTopologicalStrategy(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new TopologicalStrategy(function);	
			} 
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isStrategy()) {
					return new TopologicalStrategy(function);
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}


}
