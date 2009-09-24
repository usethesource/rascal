package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ElementResult;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.strategy.IVisitable;
import org.meta_environment.rascal.interpreter.strategy.Strategy;
import org.meta_environment.rascal.interpreter.strategy.VisitableFactory;

public class TopologicalAll extends Strategy {

	private boolean isStrategy;
	
	public TopologicalAll(AbstractFunction function, boolean isStrategy) {
		super(function);
		this.isStrategy = isStrategy;
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues,
			IEvaluatorContext ctx) {
		if (argValues[0] instanceof IRelation) {
			IRelation relation = ((IRelation) argValues[0]);
			//only for binary relations
			if (relation.getType().getArity() == 2) {
				RelationContext context = new RelationContext(relation);
				IValueFactory oldFactory = ctx.getEvaluator().getIValueFactory();
				ctx.getEvaluator().setIValueFactory( new TopologicalVisitableFactory(context, oldFactory));
				TopologicalVisitable<?> result = TopologicalVisitableFactory.makeTopologicalVisitable(context,relation);
				//super.call(new Type[]{r.getType()}, new IValue[]{r}, ctx).getValue();
				List<IVisitable> newchildren = new ArrayList<IVisitable>();
				for (int i = 0; i < result.getChildrenNumber(); i++) {
					IVisitable child = result.getChildAt(i);
					context.setCurrentNode(child);
					IVisitable newchild = (TopologicalVisitable<?>) function.call(new Type[]{child.getType()}, new IValue[]{child}, ctx).getValue();
					result.update(child.getValue(), newchild.getValue());
					newchildren.add(newchild);
					context.setCurrentNode(result);
				}
				result.setChildren(newchildren);
				ctx.getEvaluator().setIValueFactory(oldFactory);
				return new ElementResult<IValue>(context.getRelation().getType(), context.getRelation(), ctx);
			}
		} else if (argValues[0] instanceof TopologicalVisitable<?>) {
			TopologicalVisitable<?> result = (TopologicalVisitable<?>) argValues[0];
			RelationContext context = result.getContext();
			IValueFactory oldFactory = ctx.getEvaluator().getIValueFactory();
			//ctx.getEvaluator().setIValueFactory( new TopologicalVisitableFactory(context, ctx.getEvaluator().getIValueFactory()));
			List<IVisitable> newchildren = new ArrayList<IVisitable>();
			for (int i = 0; i < result.getChildrenNumber(); i++) {
				IVisitable child = result.getChildAt(i);
				context.setCurrentNode(child);
				IVisitable newchild = (TopologicalVisitable<?>) function.call(new Type[]{child.getType()}, new IValue[]{child}, ctx).getValue();
				result.update(child.getValue(), newchild.getValue());
				newchildren.add(newchild);
				context.setCurrentNode(result);
			}
			result.setChildren(newchildren);
			//ctx.getEvaluator().setIValueFactory(oldFactory);
			if (isStrategy) {
				return new ElementResult<IValue>(result.getType(), result, ctx);
			} 
			return new ElementResult<IValue>(result.getValue().getType(), result.getValue(), ctx);
		} 
		IVisitable result = VisitableFactory.makeVisitable(argValues[0]);
		
		List<IVisitable> newchildren = new ArrayList<IVisitable>();
		for (int i = 0; i < result.getChildrenNumber(); i++) {
			IVisitable child = result.getChildAt(i);
			IVisitable newchild = VisitableFactory.makeVisitable(function.call(new Type[]{child.getType()}, new IValue[]{child}, ctx).getValue());
			result.update(child.getValue(), newchild.getValue());
			newchildren.add(newchild);
		}
		result.setChildren(newchildren);
		if (isStrategy) {
			return new ElementResult<IValue>(result.getType(), result, ctx);
		} 
		return new ElementResult<IValue>(result.getValue().getType(), result.getValue(), ctx);
	}

	public static IValue makeTopologicalAll(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function instanceof Strategy) {
				return new TopologicalAll((AbstractFunction) arg, true);	
			} else if (function.isStrategy()) {
				return new TopologicalAll((AbstractFunction) arg, false);
			}
		} else if (arg instanceof OverloadedFunctionResult) {
			OverloadedFunctionResult res = (OverloadedFunctionResult) arg;
			for (AbstractFunction function: res.iterable()) {
				if (function instanceof Strategy) {
					return new TopologicalAll((AbstractFunction) arg, true);	
				} else if (function.isStrategy()) {
					return new TopologicalAll((AbstractFunction) arg, false);
				}
			}
		}
		throw new RuntimeException("Unexpected strategy argument "+arg);
	}


}
