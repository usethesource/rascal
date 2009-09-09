package org.meta_environment.rascal.interpreter.strategy;

import org.meta_environment.rascal.interpreter.result.AbstractFunction;

public class Strategy extends AbstractFunction {

    protected AbstractFunction function;
	
	public Strategy(AbstractFunction function) {
		super(function.getAst(), function.getEval(), function.getFunctionType(), function.hasVarArgs(), function.getEnv());
		this.function = function;
	}

}
