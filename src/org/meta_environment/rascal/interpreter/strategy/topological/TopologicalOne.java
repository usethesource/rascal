package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ElementResult;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.strategy.IVisitable;
import org.meta_environment.rascal.interpreter.strategy.Strategy;
import org.meta_environment.rascal.interpreter.strategy.VisitableFactory;

public class TopologicalOne extends Strategy {

	public TopologicalOne(AbstractFunction function) {
		super(function);
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		if (argValues[0] instanceof TopologicalVisitable<?>) {
			TopologicalVisitable<?> r = (TopologicalVisitable<?>) argValues[0];
			RelationContext context = r.getContext();
			List<IVisitable> newchildren = new ArrayList<IVisitable>();
			for (int i = 0; i < r.getChildrenNumber(); i++) {
				IVisitable child = r.getChildAt(i);
				context.setCurrentNode(child);
				IValue oldvalue = child.getValue();
				IVisitable newchild = (TopologicalVisitable<?>) function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue();
				IValue newvalue = newchild.getValue();
				if (!newvalue.isEqual(oldvalue)) {
					r.update(oldvalue, newvalue);
					context.setCurrentNode(r);
					r.setChildAt(i, newchild);
					break;
				}
			}
			r.setChildren(newchildren);
			return new ElementResult<IValue>(r.getType(), r, ctx);
		} else if (argValues[0] instanceof IRelation) {
			IRelation relation = ((IRelation) argValues[0]);
			//only for binary relations
			if (relation.getType().getArity() == 2) {
				RelationContext context = new RelationContext(relation);
				TopologicalVisitable<?> r = TopologicalVisitableFactory.makeTopologicalVisitable(context,relation);
				for (int i = 0; i < r.getChildrenNumber(); i++) {
					IVisitable child = r.getChildAt(i);
					IValue oldvalue = child.getValue();
					IVisitable newchild = (TopologicalVisitable<?>) function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue();
					IValue newvalue = newchild.getValue();
					if (!newvalue.isEqual(oldvalue)) {
						r.update(oldvalue, newvalue);
						r.setChildAt(i, newchild);
						break;
					}
				}
				return new ElementResult<IValue>(context.getRelation().getType(), context.getRelation(), ctx);
			}
		}
		IVisitable r = VisitableFactory.makeVisitable(argValues[0]);
		for (int i = 0; i < r.getChildrenNumber(); i++) {
			IVisitable child = r.getChildAt(i);
			IValue oldvalue = child.getValue();
			IVisitable newchild = (TopologicalVisitable<?>) function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue();
			IValue newvalue = newchild.getValue();
			if (!newvalue.isEqual(oldvalue)) {
				r.update(oldvalue, newvalue);
				r.setChildAt(i, newchild);
				break;
			}
		}
		return new ElementResult<IValue>(r.getType(), r, ctx);
	}

	public static IValue makeTopologicalOne(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new TopologicalOne(function);	
			} 
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isStrategy()) {
					return new TopologicalOne(function);
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}

}
