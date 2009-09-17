package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;

public class Strategy extends AbstractFunction {

    protected final AbstractFunction function;
	
	public Strategy(AbstractFunction function) {
		super(function.getAst(), function.getEval(), function.getFunctionType(), function.hasVarArgs(), function.getEnv());
		this.function = function;
	}
	
	public static boolean checkType(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return true;	
			}
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isStrategy()) {
					return true;	
				}
			}
		}
		return false;
	}

}
