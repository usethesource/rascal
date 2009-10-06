package org.meta_environment.rascal.interpreter.strategy.topological;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.strategy.IStrategyContext;

public class TopologicalContext implements IStrategyContext {

	private HashMap<IValue, List<IValue>> adjacencies;
	private HashMap<IValue, Integer> visits;
	private Type type;

	public TopologicalContext() {
		adjacencies = new HashMap<IValue, List<IValue>>();
		visits = new HashMap<IValue, Integer>();
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
		IRelationWriter writer = ValueFactoryFactory.getValueFactory().relationWriter(type);
		for (IValue v1: adjacencies.keySet()) {
			for (IValue v2: adjacencies.get(v1) ) {
				writer.insert(ValueFactoryFactory.getValueFactory().tuple(v1,v2));
			}

		}
		return writer.done();
	}

	public void setValue(IValue value) {
		if (value instanceof IRelation) {
			IRelation relation = (IRelation) value;
			adjacencies = computeAdjacencies(relation);
			type =relation.getElementType();
		} else {
			throw new RuntimeException("Unexpected context type "+value.getType());
		}

	}

	public void update(IValue oldvalue, IValue newvalue) {
		if (! oldvalue.isEqual(newvalue)) {
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

	public List<IValue> getChildren(IValue v) {
		if (v instanceof IRelation) {
			IRelation relation = (IRelation) v;
			ISet roots = relation.domain().subtract(relation.range());
			ArrayList<IValue> res = new ArrayList<IValue>();
			for (IValue value: roots) {
				res.add(value);
			}
			return res;
		}
		if (visits.get(v) <= 1)  {
			List<IValue> res =  adjacencies.get(v);
			if (res != null) return res;
		}
		return new ArrayList<IValue>();
	}


	public void mark(IValue v) {
		if (visits.containsKey(v)) {
			visits.put(v, visits.get(v)+1);	
		} else {
			visits.put(v, 1);
		}
	}

}
