package org.rascalmpl.library;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.strategy.All;
import org.rascalmpl.interpreter.strategy.One;
import org.rascalmpl.interpreter.strategy.StrategyFunction;
import org.rascalmpl.interpreter.strategy.Visitable;
import org.rascalmpl.interpreter.strategy.topological.TopologicalVisitable;
import org.rascalmpl.values.ValueFactoryFactory;

public class Strategy{
	
	public Strategy(IValueFactory values){
		super();
	}
	
	public IValue makeOne(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isTypePreserving()) {
				return new One(function, Visitable.getInstance());
			}
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isTypePreserving()) {
					return new One(function, Visitable.getInstance());
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}

	public IValue makeTopologicalOne(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isTypePreserving()) {
				return new One(function, new TopologicalVisitable(function));	
			} 
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isTypePreserving()) {
					return new One(function, new TopologicalVisitable(function));	
				} 
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}

	public IValue makeAll(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isTypePreserving()) {
				return new All(function, Visitable.getInstance());
			}
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isTypePreserving()) {
					return new All(function, Visitable.getInstance());	
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}
	
	public IValue makeTopologicalAll(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isTypePreserving()) {
				return new All(function, new TopologicalVisitable(function));	
			} 
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isTypePreserving()) {
					return new All(function, new TopologicalVisitable(function));	
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}
	
	public IValue functionToStrategy(IValue arg) {
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

	public static IValue getCurrentStratCtx(IEvaluatorContext ctx) {
		if (ctx.getStrategyContext() != null) {
			return ctx.getStrategyContext().getValue();
		}
		//TODO: need to be fix
		return ValueFactoryFactory.getValueFactory().string("strategycontext_null");
	}
}
