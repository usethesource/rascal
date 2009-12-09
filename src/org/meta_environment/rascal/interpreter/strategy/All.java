package org.meta_environment.rascal.interpreter.strategy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.Result;

public class All extends AbstractStrategy {
	private final IVisitable v;

	public All(AbstractFunction function, IVisitable v) {
		super(function);
		this.v = v;
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		IValue res = argValues[0];
		boolean entered = v.init(res);
		
		List<IValue> newchildren = new ArrayList<IValue>();
		for (int i = 0; i < v.getChildrenNumber(res); i++) {
			IValue child = v.getChildAt(res, i);
			v.mark(child);
			newchildren.add(function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue());
		}
		res = v.setChildren(res, newchildren);
		
		if(entered){
			getEvaluatorContext().popStrategyContext(); // Exit the context.
		}
		
		return makeResult(res, ctx);
	}
}
