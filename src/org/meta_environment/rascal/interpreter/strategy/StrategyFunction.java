package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.Result;
import org.eclipse.imp.pdb.facts.type.Type;


public class StrategyFunction implements Strategy {
	
	private AbstractFunction function;

	public StrategyFunction(AbstractFunction function) {
		this.function = function;
	}

	public Visitable apply(Visitable v) {
		IValue arg = v.getValue();
	    Result<IValue> res = (Result<IValue>) function.call(new Type[]{arg.getType()}, new IValue[]{arg}, function.getEval());
	    return VisitableFactory.make(res.getValue());
	}

}
