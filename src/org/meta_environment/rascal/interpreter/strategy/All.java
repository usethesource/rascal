package org.meta_environment.rascal.interpreter.strategy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ElementResult;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;

public class All extends Strategy {

	public All(AbstractFunction function) {
		super(function);
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues,
			IEvaluatorContext ctx) {
		IVisitable result = VisitableFactory.makeVisitable(argValues[0]);
		List<IVisitable> newchildren = new ArrayList<IVisitable>();
		for (int i = 0; i < result.arity(); i++) {
			IValue child = result.getChildAt(i);
			newchildren.add(VisitableFactory.makeVisitable(function.call(new Type[]{child.getType()}, new IValue[]{child}, ctx).getValue()));
		}
		result = result.setChildren(newchildren);
		return new ElementResult<IValue>(result.getValue().getType(), result.getValue(), ctx);
	}

	public static IValue makeAll(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new All((AbstractFunction) arg);			}
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isStrategy()) {
					return new All((AbstractFunction) function);
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}

}
