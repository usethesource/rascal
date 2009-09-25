package org.meta_environment.rascal.interpreter.strategy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ElementResult;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;

public class All extends Strategy {

	protected boolean isStrategy;

	public All(AbstractFunction function, boolean isStrategy) {
		super(function);
		this.isStrategy = isStrategy;
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues,
			IEvaluatorContext ctx) {
		IValueFactory oldFactory = ctx.getEvaluator().getIValueFactory();
		ctx.getEvaluator().setIValueFactory( new VisitableFactory(ctx.getEvaluator().getIValueFactory()));
		IVisitable result = VisitableFactory.makeVisitable(argValues[0]);
		List<IVisitable> newchildren = new ArrayList<IVisitable>();
		for (int i = 0; i < result.getChildrenNumber(); i++) {
			IVisitable child = result.getChildAt(i);
			IVisitable newchild = VisitableFactory.makeVisitable(function.call(new Type[]{child.getType()}, new IValue[]{child}, ctx).getValue());
			result.update(child.getValue(), newchild.getValue());
			newchildren.add(newchild);
		}
		result.setChildren(newchildren);
		ctx.getEvaluator().setIValueFactory(oldFactory);
		if (isStrategy) {
			return new ElementResult<IValue>(result.getType(), result, ctx);
		} 
		return new ElementResult<IValue>(result.getValue().getType(), result.getValue(), ctx);

	}

	public static IValue makeAll(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function instanceof Strategy) {
				return new All((AbstractFunction) arg, true);	
			} else if (function.isStrategy()) {
				return new All((AbstractFunction) arg, false);
			}
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function instanceof Strategy) {
					return new All((AbstractFunction) arg, true);	
				} else if (function.isStrategy()) {
					return new All((AbstractFunction) arg, false);
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}

}
