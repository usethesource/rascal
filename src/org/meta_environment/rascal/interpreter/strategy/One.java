package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ElementResult;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;

public class One extends Strategy {

	public One(AbstractFunction function) {
		super(function);
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		IValueFactory oldFactory = ctx.getEvaluator().getValueFactory();
		ctx.getEvaluator().setIValueFactory( new VisitableFactory(ctx.getEvaluator().getValueFactory()));
		IVisitable result = VisitableFactory.makeVisitable(argValues[0]);
		for (int i = 0; i < result.getChildrenNumber(); i++) {
			IVisitable child = result.getChildAt(i);
			IValue oldvalue = child.getValue();
			IVisitable newchild = VisitableFactory.makeVisitable(function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue());
			IValue newvalue = newchild.getValue();
			if (!newvalue.equals(oldvalue)) {
				result.update(oldvalue, newvalue);
				result.setChildAt(i, newchild);
				break;
			}
		}
		ctx.getEvaluator().setIValueFactory(oldFactory);
		return new ElementResult<IValue>(result.getValue().getType(), result.getValue(), ctx);
	}

	public static IValue makeOne(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new One((AbstractFunction) arg);			}
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
