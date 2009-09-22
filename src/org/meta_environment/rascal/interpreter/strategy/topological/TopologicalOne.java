package org.meta_environment.rascal.interpreter.strategy.topological;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ElementResult;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.strategy.One;
import org.meta_environment.rascal.interpreter.strategy.VisitableFactory;

public class TopologicalOne extends One {

	public TopologicalOne(AbstractFunction function) {
		super(function);
	}

	private static ISet getRoots(IRelation relation) {
		return relation.domain().subtract(relation.range());
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues,
			IEvaluatorContext ctx) {
		if (argValues[0] instanceof IRelation) {
			IRelation relation = ((IRelation) argValues[0]);
			//only for binary relations
			if (relation.getType().getArity() == 2) {
				ISet roots = getRoots(relation);
				RelationContext context = new RelationContext(relation);
				for (IValue root: roots) {
					TopologicalVisitable<?> visitableroot = VisitableFactory.makeTopologicalVisitable(context,root);
					IValue oldvalue = visitableroot.getValue();
					IValue newvalue = function.call(new Type[]{visitableroot.getType()}, new IValue[]{visitableroot}, ctx).getValue();
					if (!newvalue.equals(oldvalue)) {
						visitableroot.update(oldvalue, newvalue);
						break;
					}
				}
				return new ElementResult<IValue>(context.getRelation().getType(), context.getRelation(), ctx);
			}
		}
		return super.call(argTypes, argValues, ctx);
	}

	public static IValue makeTopologicalOne(IValue arg) {
		if (arg instanceof AbstractFunction) {
			AbstractFunction function = (AbstractFunction) arg;
			if (function.isStrategy()) {
				return new TopologicalOne((AbstractFunction) arg);			}
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
