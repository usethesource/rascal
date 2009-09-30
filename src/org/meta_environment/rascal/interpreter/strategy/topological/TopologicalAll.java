package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ElementResult;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.strategy.ContextualStrategy;
import org.meta_environment.rascal.interpreter.strategy.IContextualVisitable;

public class TopologicalAll extends ContextualStrategy {

	public TopologicalAll(IContextualVisitable v, AbstractFunction function) {
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
					v.updateContext(child, newchild);
				}
				v.initContext(lastContext);
				return new ElementResult<IValue>(v.getContext().getType(), v.getContext(), ctx);
			}
		}
		List<IValue> newchildren = new ArrayList<IValue>();
		for (int i = 0; i < v.getChildrenNumber(argValues[0]); i++) {
			IValue child = v.getChildAt(argValues[0], i);
			IValue newchild = function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue();
			newchildren.add(newchild);
			v.updateContext(child, newchild);
		}
		IValue res = v.setChildren(argValues[0], newchildren);
		return new ElementResult<IValue>(res.getType(), res, ctx);
	}

	public static IValue makeTopologicalAll(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function instanceof ContextualStrategy) {
				return new TopologicalAll(((ContextualStrategy)function).getVisitable(), function);	
			} else if (function.isStrategy()) {
				return new TopologicalAll(new TopologicalVisitable(), function);	
			} 
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function instanceof ContextualStrategy) {
					return new TopologicalAll(((ContextualStrategy)function).getVisitable(), function);	
				} else if (function.isStrategy()) {
					return new TopologicalAll(new TopologicalVisitable(), function);	
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}


}
