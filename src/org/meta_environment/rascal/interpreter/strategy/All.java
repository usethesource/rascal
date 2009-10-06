package org.meta_environment.rascal.interpreter.strategy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.strategy.topological.TopologicalVisitable;

public class All extends Strategy {

	private IVisitable v;

	public All(AbstractFunction function, IVisitable v) {
		super(function);
		this.v = v;
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		IValue res = argValues[0];
		List<IValue> newchildren = new ArrayList<IValue>();
		v.init(res);
		for (int i = 0; i < v.getChildrenNumber(res); i++) {
			IValue child = v.getChildAt(res, i);
			newchildren.add(function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue());
		}
		res = v.setChildren(res, newchildren);
		return makeResult(res, ctx);
	}

	public static IValue makeAll(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new All(function, Visitable.getInstance());
			}
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isStrategy()) {
					return new All(function, Visitable.getInstance());	
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}
	
	public static IValue makeTopologicalAll(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new All(function, new TopologicalVisitable(function));	
			} 
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isStrategy()) {
					return new All(function, new TopologicalVisitable(function));	
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}

}
