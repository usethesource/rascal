package org.rascalmpl.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;

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
	
	@Override
	public boolean isDefault() {
		return false;
	}
}
