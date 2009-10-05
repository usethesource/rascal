package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ElementResult;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.strategy.ContextualStrategy;
import org.meta_environment.rascal.interpreter.strategy.IStrategyContext;

public class TopologicalAll extends ContextualStrategy {

	public TopologicalAll(AbstractFunction function) {
		super(new TopologicalVisitable(function), function);
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		if (argValues[0] instanceof IRelation) {
			IRelation relation = ((IRelation) argValues[0]);
			IStrategyContext context = new TopologicalContext();
			context.setValue(relation);
			v.setContext(context);
			//only for binary relations
			if (relation.getType().getArity() == 2) {
				Iterator<IValue> roots = relation.domain().subtract(relation.range()).iterator();
				while (roots.hasNext()) {
					IValue child = roots.next();
					function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue();
				}
				return new ElementResult<IValue>(v.getContext().getValue().getType(), v.getContext().getValue(), ctx);
			}
		}
		for (int i = 0; i < v.getChildrenNumber(argValues[0]); i++) {
			IValue child = v.getChildAt(argValues[0], i);
			function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue();
		}
		return new ElementResult<IValue>(argValues[0].getType(), argValues[0], ctx);
	}

	public static IValue makeTopologicalAll(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new TopologicalAll(function);	
			} 
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isStrategy()) {
					return new TopologicalAll(function);	
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}


}
