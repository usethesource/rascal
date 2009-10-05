package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.strategy.IStrategyContext;

public class TopologicalContext implements IStrategyContext {

	private HashMap<IValue, List<IValue>> adjacencies;
	private IRelation relation;

	public TopologicalContext(IRelation relation) {
		setValue(relation);
	}

	private static HashMap<IValue, List<IValue>> computeAdjacencies(IRelation relation) {
		HashMap<IValue, List<IValue>> adjacencies = new HashMap<IValue, List<IValue>> ();
		for(IValue v : relation.domain().union(relation.range())){
			adjacencies.put(v, new ArrayList<IValue>());
		}
		for(IValue v : relation){
			ITuple tup = (ITuple) v;
			adjacencies.get(tup.get(0)).add(tup.get(1));
		}  
		return adjacencies;
	}

	public IValue getValue() {
		return relation;
	}

	public void setValue(IValue value) {
		if (value instanceof IRelation) {
			relation = (IRelation) value;
			adjacencies = computeAdjacencies(relation);
		} else {
			throw new RuntimeException("Unexpected context type "+value.getType());
		}

	}

	public void update(IValue oldvalue, IValue newvalue) {
		if (relation != null) {
			if (! oldvalue.isEqual(newvalue)) {
				//update the relation itself
				IRelationWriter writer = ValueFactoryFactory.getValueFactory().relationWriter(relation.getElementType());
				for (IValue v : relation) {
					if (v.getType().isTupleType()) {
						ITuple t = (ITuple) v;
						ITuple newt = t;
						if (t.get(0).isEqual(oldvalue)) newt = newt.set(0, newvalue);
						if (t.get(1).isEqual(oldvalue)) newt = newt.set(1, newvalue);
						writer.insert(newt);
					}
				}
				relation = writer.done();
				//update the adjacencies
				if (adjacencies.containsKey(oldvalue)) {
					if (adjacencies.containsKey(newvalue)) {
						adjacencies.get(newvalue).addAll(adjacencies.get(oldvalue));
					} else {
						adjacencies.put(newvalue, adjacencies.get(oldvalue));
					}
					adjacencies.remove(oldvalue);
				}
				for(IValue key : adjacencies.keySet()) {
					List<IValue> children = adjacencies.get(key);
					if (children.contains(oldvalue)) {
						children.set(children.indexOf(oldvalue), newvalue);
					}
				}
			}
		}
	}

	public List<IValue> getChildren(IValue v) {
		List<IValue> res =  adjacencies.get(v);
		if (res != null) return res;
		return new ArrayList<IValue>();
	}

}
