package org.meta_environment.rascal.interpreter.strategy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ITuple;
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


	private HashMap<IValue, LinkedList<IValue>> computeAdjacencies(IRelation relation) {
		HashMap<IValue, LinkedList<IValue>> adjacencies = new HashMap<IValue, LinkedList<IValue>> ();
		for(IValue v : relation){
			ITuple tup = (ITuple) v;
			IValue from = tup.get(0);
			IValue to = tup.get(1);
			LinkedList<IValue> children = adjacencies.get(from);
			if(children == null)
				children = new LinkedList<IValue>();
			children.add(to);
			adjacencies.put(from, children);
		}  
		return adjacencies;
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues,
			IEvaluatorContext ctx) {
		if (argValues[0] instanceof IRelation) {
			IRelation relation = ((IRelation) argValues[0]);
			HashMap<IValue, LinkedList<IValue>> adjacencies = computeAdjacencies(relation);
			Visitable result = VisitableFactory.make(argValues[0]);
			for (int i = 0; i < result.arity(); i++) {
				IValue child = result.get(i).getValue();
				result = result.set(i, VisitableFactory.make(function.call(new Type[]{child.getType()}, new IValue[]{child}, ctx).getValue()));
			}
			return new ElementResult<IValue>(result.getValue().getType(), result.getValue(), ctx);
		} else {
			return super.call(argTypes, argValues, ctx);
		}
	}

	public static IValue makeTopologicalAll(IValue arg) {
		if (! Strategy.checkType(arg)) throw new RuntimeException("Unexpected strategy argument "+arg);
		return new TopologicalAll((AbstractFunction) arg);
	}

}
