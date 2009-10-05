package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.strategy.topological.TopologicalVisitable;

public class ContextualStrategy extends Strategy {

	protected IContextualVisitable v;

	public ContextualStrategy(IContextualVisitable v, AbstractFunction function) {
		super(function);
		this.v = v;
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		Result<IValue> res = function.call(argTypes, argValues);
		if (v.getContext() != null) v.getContext().update(argValues[0], res.getValue());
		return res;
	}

	public IContextualVisitable getVisitable() {
		return v;
	}

	public static IValue getCurrentStratCtx(IEvaluatorContext ctx) {
		if (ctx.getStrategyContext() != null) {
		return ctx.getStrategyContext().getValue();
		}
		//TODO: need to be fix
		return ValueFactoryFactory.getValueFactory().string("__null");
	}
	
	public static IValue makeTopologicalStrategy(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new ContextualStrategy(new TopologicalVisitable(function), function);	
			} 
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isStrategy()) {
					return new ContextualStrategy(new TopologicalVisitable(function), function);	
				}  
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}


}
