package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.strategy.IContextualVisitable;

public class TopologicalVisitable implements IContextualVisitable {

	protected IRelation context;

	public TopologicalVisitable() {}

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

	public void initContext(IValue v) {
		if (v instanceof IRelation) {
			IRelation relation = ((IRelation) v);
			//only for binary relations
			if (relation.getType().getArity() == 2) {
				context = relation;
			}
		}
	}

	public void updateContext(IValue oldvalue, IValue newvalue) {
		if (context != null) {
			IRelationWriter writer = ValueFactoryFactory.getValueFactory().relationWriter(context.getElementType());
			for (IValue v : context) {
				if (v.getType().isTupleType()) {
					ITuple t = (ITuple) v;
					ITuple newt = t;
					if (t.get(0).isEqual(oldvalue)) newt = newt.set(0, newvalue);
					if (t.get(1).isEqual(oldvalue)) newt = newt.set(1, newvalue);
					writer.insert(newt);
				}
			}
			context = writer.done();
		}
	}

	public IValue getChildAt(IValue v, int i) throws IndexOutOfBoundsException {
		List<ITuple> children = computeAdjacencies(context).get(v);
		return children.get(i).get(1);
	}

	public <T extends IValue> T setChildAt(T v, int i, IValue newchild)
	throws IndexOutOfBoundsException {
		List<ITuple> adjacencies = computeAdjacencies(context).get(v);
		List<IValue> newchildren = new ArrayList<IValue>();
		for (ITuple t : adjacencies) newchildren.add(t.get(1));
		newchildren.set(i, newchild);
		return setChildren(v, newchildren);
	}


	public int getChildrenNumber(IValue v) {
		List<ITuple> adjacencies = computeAdjacencies(context).get(v);
		if (adjacencies == null) {
			throw new RuntimeException("Unexpected value "+v+" in the context "+context);
		}
		return adjacencies.size();
	}


	public <T extends IValue> T setChildren(T v, List<IValue> children)
	throws IndexOutOfBoundsException {
		List<ITuple> oldchildren = computeAdjacencies(context).get(v);
		IRelation oldvalues = ValueFactoryFactory.getValueFactory().relation(oldchildren.toArray(new ITuple[]{}));
		List<ITuple> newchildren = new ArrayList<ITuple>();
		for (IValue child: children) {
			newchildren.add(ValueFactoryFactory.getValueFactory().tuple(v,child));
		}
		IRelation newvalues = ValueFactoryFactory.getValueFactory().relation(newchildren.toArray(new ITuple[]{}));
		context =  context.subtract(oldvalues).union(newvalues);
		return v;
	}

	public IValue getContext() {
		return context;
	}

}
