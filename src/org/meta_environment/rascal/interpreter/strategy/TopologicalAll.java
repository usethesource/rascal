package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.ElementResult;
import org.meta_environment.rascal.interpreter.result.Result;

public class TopologicalAll extends All {

	public TopologicalAll(AbstractFunction function) {
		super(function);
	}
	
	private static VisitableRelationNode getRoot(IRelation relation) {
		//TODO: not implemented yet
		return null;
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues,
			IEvaluatorContext ctx) {
		if (argValues[0] instanceof IRelation) {
			IRelation relation = ((IRelation) argValues[0]);
			VisitableRelationNode root = getRoot(relation);
			function.call(new Type[]{root.getValue().getType()}, new IValue[]{root.getValue()}, ctx);
			return new ElementResult<IValue>(root.getRelation().getType(), root.getRelation(), ctx);
		} else {
			return super.call(argTypes, argValues, ctx);
		}
	}

	public static IValue makeTopologicalAll(IValue arg) {
		if (! Strategy.checkType(arg)) throw new RuntimeException("Unexpected strategy argument "+arg);
		return new TopologicalAll((AbstractFunction) arg);
	}

}
