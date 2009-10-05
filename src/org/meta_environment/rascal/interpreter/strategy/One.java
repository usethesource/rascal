package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ElementResult;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.strategy.topological.TopologicalVisitable;

public class One extends Strategy {

	private IVisitable v;

	public One(AbstractFunction function, IVisitable v) {
		super(function);
		this.v = v;
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		IValue res = argValues[0];
		v.init(res);
		for (int i = 0; i < v.getChildrenNumber(res); i++) {
			IValue child = v.getChildAt(res, i);
			if(v instanceof IContextualVisitable) {
				IContextualVisitable cv = (IContextualVisitable) v;
				IValue oldctx = cv.getContext().getValue();
				IValue newchild = function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue();
				IValue newctx = cv.getContext().getValue();
				if (! oldctx.isEqual(newctx)) {
					res = v.setChildAt(res, i, newchild);
					break;
				}
			} else {
				IValue newchild = function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue();
				if (! newchild.isEqual(child)) {
					res = v.setChildAt(res, i, newchild);
					break;
				}
			}
		}
		return new ElementResult<IValue>(res.getType(), res, ctx);
	}

	public static IValue makeOne(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new One(function, Visitable.getInstance());			}
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isStrategy()) {
					return new One(function, Visitable.getInstance());
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}

	public static IValue makeTopologicalOne(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new One(function, new TopologicalVisitable(function));	
			} 
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isStrategy()) {
					return new One(function, new TopologicalVisitable(function));	
				} 
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}


}
