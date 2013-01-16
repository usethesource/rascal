/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.strategy.topological;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.strategy.IStrategyContext;
import org.rascalmpl.values.ValueFactoryFactory;

public class TopologicalContext implements IStrategyContext {
	private final static Integer UNMARKED = new Integer(0);
	private final static Integer MARKED = new Integer(1);
	
	private final HashMap<IValue, List<IValue>> adjacencies;
	private final HashMap<IValue, Integer> visits;
	private final Type type;

	public TopologicalContext(IRelation relation) {
		super();
		
		adjacencies = computeAdjacencies(relation);
		visits = new HashMap<IValue, Integer>();
		type = relation.getElementType();
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
		if (!isMarked(v))  {
			List<IValue> res =  adjacencies.get(v);
			if (res != null) return res;
		}
		return new ArrayList<IValue>();
	}
	
	public boolean isMarked(IValue v){
		return !(visits.get(v) == null || visits.get(v) == UNMARKED);
	}
	
	public void mark(IValue v){
		if(visits.containsKey(v)){
			visits.put(v, MARKED);
		}else{
			visits.put(v, UNMARKED);
		}
	}
}
