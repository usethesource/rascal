package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.values.ValueFactoryFactory;

public class Strategy extends AbstractFunction {

	protected final AbstractFunction function;

	public Strategy(AbstractFunction function) {
		super(function.getAst(), function.getEval(), function.getFunctionType(), function.hasVarArgs(), function.getEnv());
		this.function = function;
	}

	public AbstractFunction getFunction() {
		return function;
	}
	
	public static Result<IValue> makeResult(IValue result, IEvaluatorContext ctx){
		return ResultFactory.makeResult(result.getType(), result, ctx);
	}
	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		return function.call(argTypes, argValues);
	}

	public static IValue getCurrentStratCtx(IEvaluatorContext ctx) {
		if (ctx.getStrategyContext() != null) {
			return ctx.getStrategyContext().getValue();
		}
		//TODO: need to be fix
		return ValueFactoryFactory.getValueFactory().string("strategycontext_null");
	}

}
