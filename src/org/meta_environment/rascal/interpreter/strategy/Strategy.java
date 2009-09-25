package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;

public class Strategy extends AbstractFunction {

    protected final AbstractFunction function;
	
	public Strategy(AbstractFunction function) {
		super(function.getAst(), function.getEval(), function.getFunctionType(), function.hasVarArgs(), function.getEnv());
		this.function = function;
	}
	
	public AbstractFunction getFunction() {
		return function;
	}
	
	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		if (argValues[0] instanceof IVisitable) {
			IVisitable visitable = (IVisitable) argValues[0];
			IValue v = visitable.getValue();	
			Result<IValue> res = function.call(argTypes, new IValue[]{v});
			return ResultFactory.makeResult(res.getType(), VisitableFactory.makeVisitable(res.getValue()), ctx);
		}
		
		return function.call(argTypes, argValues);
	}

	public static IValue makeStrategy(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new Strategy(function);	
			} 
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isStrategy()) {
					return new Strategy(function);
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}
	
	

}
