package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ElementResult;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;

public class One extends Strategy {

	public One(AbstractFunction function) {
		super(function);
	}
	
	IVisitable v = Visitable.getInstance();

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		IValue res = argValues[0];
		for (int i = 0; i < v.getChildrenNumber(res); i++) {
			IValue child = v.getChildAt(res, i);
			IValue newchild = function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue();
			if (! newchild.isEqual(child)) {
				res = v.setChildAt(res, i, newchild);
				break;
			}
		}
		return new ElementResult<IValue>(res.getType(), res, ctx);
	}

	public static IValue makeOne(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new One(function);			}
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isStrategy()) {
					return new One(function);
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}

}
