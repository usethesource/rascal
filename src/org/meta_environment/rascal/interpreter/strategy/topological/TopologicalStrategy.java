package org.meta_environment.rascal.interpreter.strategy.topological;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.strategy.ContextualStrategy;

public class TopologicalStrategy extends ContextualStrategy {

	public TopologicalStrategy(AbstractFunction function) {
		super(new TopologicalVisitable(function), function);
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