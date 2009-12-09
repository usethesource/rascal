package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;

public abstract class AbstractStrategy extends AbstractFunction {

	protected final AbstractFunction function;

	public AbstractStrategy(AbstractFunction function) {
		super(function.getAst(), function.getEval(), function.getFunctionType(), function.hasVarArgs(), function.getEnv());
		this.function = function;
	}

	public AbstractFunction getFunction() {
		return function;
	}
	
	public static Result<IValue> makeResult(IValue result, IEvaluatorContext ctx){
		return ResultFactory.makeResult(result.getType(), result, ctx);
	}
}
