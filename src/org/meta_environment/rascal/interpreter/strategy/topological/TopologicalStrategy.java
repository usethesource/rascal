package org.meta_environment.rascal.interpreter.strategy.topological;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.strategy.ContextualStrategy;
import org.meta_environment.rascal.interpreter.strategy.IContextualVisitable;

public class TopologicalStrategy extends ContextualStrategy {

	public TopologicalStrategy(IContextualVisitable v, AbstractFunction function) {
		super(v, function);
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		Result<IValue> res = function.call(argTypes, argValues);
		v.updateContext(argValues[0], res.getValue());
		return res;
	}

	public static IValue makeTopologicalStrategy(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function instanceof ContextualStrategy) {
				return new TopologicalStrategy(((ContextualStrategy)function).getVisitable(), function);	
			} else if (function.isStrategy()) {
				return new TopologicalStrategy(new TopologicalVisitable(), function);	
			} 
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function instanceof ContextualStrategy) {
					return new TopologicalStrategy(((ContextualStrategy)function).getVisitable(), function);	
				} else if (function.isStrategy()) {
					return new TopologicalStrategy(new TopologicalVisitable(), function);	
				}  
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}

	public static IValue propagateContext(IValue arg, IValue contextarg) {
		if (contextarg instanceof ContextualStrategy) {
			IContextualVisitable v = ((ContextualStrategy) contextarg).getVisitable();
			if (arg instanceof AbstractFunction) {
				AbstractFunction function = (AbstractFunction) arg;
				return new TopologicalStrategy(v, function);	
			} else if (arg instanceof OverloadedFunctionResult) {
				OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
				for (AbstractFunction function: res.iterable()) {
					return new TopologicalStrategy(v, function);	
				}
			}
		}
		throw new RuntimeException("Unexpected strategy arguments "+arg + ","+contextarg);
	}


}