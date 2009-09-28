package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ElementResult;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.strategy.IVisitable;
import org.meta_environment.rascal.interpreter.strategy.Strategy;
import org.meta_environment.rascal.interpreter.strategy.VisitableFactory;

public class TopologicalAll extends Strategy {

	public TopologicalAll(AbstractFunction function) {
		super(function);
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		if (argValues[0] instanceof TopologicalVisitable<?>) {
			TopologicalVisitable<?> result = (TopologicalVisitable<?>) argValues[0];
			RelationContext context = result.getContext();
			List<IVisitable> newchildren = new ArrayList<IVisitable>();
			for (int i = 0; i < result.getChildrenNumber(); i++) {
				IVisitable child = result.getChildAt(i);
				context.setCurrentNode(child);
				IVisitable newchild = (TopologicalVisitable<?>) function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue();
				result.update(child.getValue(), newchild.getValue());
				newchildren.add(newchild);
				context.setCurrentNode(result);
			}
			result.setChildren(newchildren);
			return new ElementResult<IValue>(result.getType(), result, ctx);
		} else if (argValues[0] instanceof IRelation) {
			IRelation relation = ((IRelation) argValues[0]);
			//only for binary relations
			if (relation.getType().getArity() == 2) {
				RelationContext context = new RelationContext(relation);
				IValueFactory oldFactory = ctx.getEvaluator().getIValueFactory();
				ctx.getEvaluator().setIValueFactory( new TopologicalVisitableFactory(context, oldFactory));
				TopologicalVisitable<?> result = TopologicalVisitableFactory.makeTopologicalVisitable(context,relation);
				List<IVisitable> newchildren = new ArrayList<IVisitable>();
				for (int i = 0; i < result.getChildrenNumber(); i++) {
					IVisitable child = result.getChildAt(i);
					context.setCurrentNode(child);
					IVisitable newchild = (TopologicalVisitable<?>) function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue();
					result.update(child.getValue(), newchild.getValue());
					newchildren.add(newchild);
					context.setCurrentNode(result);
				}
				result.setChildren(newchildren);
				ctx.getEvaluator().setIValueFactory(oldFactory);
				return new ElementResult<IValue>(context.getRelation().getType(), context.getRelation(), ctx);
			}
		}
		IVisitable result = VisitableFactory.makeVisitable(argValues[0]);

		List<IVisitable> newchildren = new ArrayList<IVisitable>();
		for (int i = 0; i < result.getChildrenNumber(); i++) {
			IVisitable child = result.getChildAt(i);
			IVisitable newchild = VisitableFactory.makeVisitable(function.call(new Type[]{child.getType()}, new IValue[]{child}).getValue());
			result.update(child.getValue(), newchild.getValue());
			newchildren.add(newchild);
		}
		result.setChildren(newchildren);
		return new ElementResult<IValue>(result.getType(), result, ctx);
	}

	public static IValue makeTopologicalAll(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new TopologicalAll((AbstractFunction) arg);	
			} 
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function.isStrategy()) {
					return new TopologicalAll((AbstractFunction) arg);	
				} 
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}


}
