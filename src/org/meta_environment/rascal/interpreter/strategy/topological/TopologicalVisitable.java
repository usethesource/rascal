package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.strategy.IContextualVisitable;

public class TopologicalVisitable implements IContextualVisitable {

	private AbstractFunction function;

	public TopologicalVisitable(AbstractFunction function) {
		this.function = function;
	}

	protected static HashMap<IValue, List<ITuple>> computeAdjacencies(IRelation relation) {
		HashMap<IValue, List<ITuple>> adjacencies = new HashMap<IValue, List<ITuple>> ();
		for(IValue v : relation.domain().union(relation.range())){
			adjacencies.put(v, new ArrayList<ITuple>());
		}
		for(IValue v : relation){
			ITuple tup = (ITuple) v;
			IValue from = tup.get(0);
			adjacencies.get(from).add(tup);
		}  
		return adjacencies;
	}

	public void updateContext(IValue oldvalue, IValue newvalue) {
		if (getContext() != null) {
			IRelationWriter writer = ValueFactoryFactory.getValueFactory().relationWriter(getRelationContext().getElementType());
			for (IValue v : getRelationContext()) {
				if (v.getType().isTupleType()) {
					ITuple t = (ITuple) v;
					ITuple newt = t;
					if (t.get(0).isEqual(oldvalue)) newt = newt.set(0, newvalue);
					if (t.get(1).isEqual(oldvalue)) newt = newt.set(1, newvalue);
					writer.insert(newt);
				}
			}
			setContext(writer.done());
		}
	}

	public IValue getContext() {
		Environment envt = function.getEvaluatorContext().getCurrentEnvt();
		while (envt != null) {
			Result<IValue> res = envt.getVariable("rascal_strategy_context");
			if (res != null) {
				return res.getValue();
			}
			envt = envt.getCallerScope();
		}
		return null;	
	}

	private IRelation getRelationContext() {
		return (IRelation) getContext();
	}

	public void setContext(IValue value) {
		if (value!= null && value instanceof IRelation) {
			function.getEvaluatorContext().getCurrentEnvt().storeVariable("rascal_strategy_context", ResultFactory.makeResult(value.getType(), value, function.getEvaluatorContext()));
		}
	}

	public IValue getChildAt(IValue v, int i) throws IndexOutOfBoundsException {
		List<ITuple> children = computeAdjacencies(getRelationContext()).get(v);
		return children.get(i).get(1);
	}

	public <T extends IValue> T setChildAt(T v, int i, IValue newchild)
	throws IndexOutOfBoundsException {
		List<ITuple> adjacencies = computeAdjacencies(getRelationContext()).get(v);
		List<IValue> newchildren = new ArrayList<IValue>();
		for (ITuple t : adjacencies) newchildren.add(t.get(1));
		newchildren.set(i, newchild);
		return setChildren(v, newchildren);
	}


	public int getChildrenNumber(IValue v) {
		List<ITuple> adjacencies = computeAdjacencies(getRelationContext()).get(v);
		if (adjacencies == null) {
			throw new RuntimeException("Unexpected value "+v+" in the context "+getContext());
		}
		return adjacencies.size();
	}


	public <T extends IValue> T setChildren(T v, List<IValue> children)
	throws IndexOutOfBoundsException {
		List<ITuple> oldchildren = computeAdjacencies(getRelationContext()).get(v);
		IRelation oldvalues = ValueFactoryFactory.getValueFactory().relation(oldchildren.toArray(new ITuple[]{}));
		List<ITuple> newchildren = new ArrayList<ITuple>();
		for (IValue child: children) {
			newchildren.add(ValueFactoryFactory.getValueFactory().tuple(v,child));
		}
		IRelation newvalues = ValueFactoryFactory.getValueFactory().relation(newchildren.toArray(new ITuple[]{}));
		setContext(getRelationContext().subtract(oldvalues).union(newvalues));
		return v;
	}

}
