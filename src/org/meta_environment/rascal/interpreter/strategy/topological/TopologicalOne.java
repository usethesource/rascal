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
import org.meta_environment.rascal.interpreter.strategy.IContextualVisitable;

public class TopologicalOne extends ContextualStrategy {

	public TopologicalOne(IContextualVisitable v, AbstractFunction function) {
		super(v, function);
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		if (argValues[0] instanceof IRelation) {
			IRelation relation = ((IRelation) argValues[0]);
			IValue lastContext = v.getContext();
			v.initContext(relation);
			//only for binary relations
			if (relation.getType().getArity() == 2) {
				Iterator<IValue> roots = relation.domain().subtract(relation.range()).iterator();
				while (roots.hasNext()) {
					IValue child = roots.next();
					IValue newchild = function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue();
					if (! child.isEqual(newchild)) {
						v.updateContext(child, newchild);
						break;
					}
				}
				v.initContext(lastContext);
				return new ElementResult<IValue>(v.getContext().getType(), v.getContext(), ctx);
			}
		}
		IValue res = argValues[0];
		for (int i = 0; i < v.getChildrenNumber(argValues[0]); i++) {
			IValue child = v.getChildAt(argValues[0], i);
			IValue newchild = function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue();
			if (! child.isEqual(newchild)) {
				//res = v.setChildAt(res, i, newchild);
				v.updateContext(child, newchild);
				break;
			}
		}
		return new ElementResult<IValue>(res.getType(), res, ctx);
	}

	public static IValue makeTopologicalOne(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function instanceof ContextualStrategy) {
				return new TopologicalOne(((ContextualStrategy)function).getVisitable(), function);	
			} else if (function.isStrategy()) {
				return new TopologicalOne(new TopologicalVisitable(), function);	
			} 
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function instanceof ContextualStrategy) {
					return new TopologicalOne(((ContextualStrategy)function).getVisitable(), function);	
				} else if (function.isStrategy()) {
					return new TopologicalOne(new TopologicalVisitable(), function);	
				} 
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}


}
