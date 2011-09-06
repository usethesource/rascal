/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

// TODO: Why is this code in the library? This should be done in pure Rascal.

class Distance{
	public int intval;
	
	Distance(int n){
		intval = n;
	}
}

class NodeComparator implements Comparator<IValue> {
	private final HashMap<IValue,Distance> distance;
	
	NodeComparator(HashMap<IValue,Distance> distance){
		this.distance = distance;
	}

	public int compare(IValue arg0, IValue arg1) {
		int d0 = distance.get(arg0).intval;
		int d1 = distance.get(arg1).intval;
		
		return d0 < d1 ? -1 : ((d0 == d1) ? 0 : 1);
	}
}

public class Graph{
	private static final TypeFactory types = TypeFactory.getInstance();

	private final IValueFactory values;
	private HashMap<IValue,Distance> distance;
	private HashMap<IValue, IValue> pred;
	private HashSet<IValue> settled;
	private PriorityQueue<IValue> Q;
	private int MAXDISTANCE = 10000;
	
	private HashMap<IValue, LinkedList<IValue>> adjacencyList;
	
	public Graph(IValueFactory values){
		super();
		
		this.values = values;
	}
	
	private void buildAdjacencyListAndDistance(IRelation G){
		adjacencyList = new HashMap<IValue, LinkedList<IValue>> ();
		distance = new HashMap<IValue, Distance>();
		
		for(IValue v : G){
			ITuple tup = (ITuple) v;
			IValue from = tup.get(0);
			IValue to = tup.get(1);
			
			if(distance.get(from) == null)
				distance.put(from, new Distance(MAXDISTANCE));
			if(distance.get(to) == null)
				distance.put(to, new Distance(MAXDISTANCE));
			
			LinkedList<IValue> adjacencies = adjacencyList.get(from);
			if(adjacencies == null)
				adjacencies = new LinkedList<IValue>();
			adjacencies.add(to);
			adjacencyList.put(from, adjacencies);
		}
	}
	
	public IValue shortestPathPair(IRelation G, IValue From, IValue To){
		buildAdjacencyListAndDistance(G);
		distance.put(From, new Distance(0));
		
		pred = new HashMap<IValue, IValue>();
		settled = new HashSet<IValue>();
		Q = new PriorityQueue<IValue>(G.size(), new NodeComparator(distance));
		Q.add(From);
		
		while(!Q.isEmpty()){
			IValue u = Q.remove();
			if(u.isEqual(To))	
				return extractPath(From, u);
			settled.add(u);
			relaxNeighbours(u);
		}
		return values.list();
	}
	
	private void relaxNeighbours(IValue u){
		LinkedList<IValue> adjacencies = adjacencyList.get(u);
		if(adjacencies != null) {
			for(IValue v : adjacencyList.get(u)){
				if(!settled.contains(v)){
					Distance dv = distance.get(v);
					Distance du = distance.get(u);
					if(dv.intval > du.intval + 1){  // 1 is default weight of each edge
						dv.intval = du.intval + 1;
						pred.put(v,u);
						Q.add(v);
					}
				}
			}
		}
	}
	
	private IList extractPath(IValue start, IValue u){
		Type listType = types.listType(start.getType());
		IListWriter w = listType.writer(values);
		
		if(!start.isEqual(u)){
			w.insert(u);
			while(!pred.get(u).isEqual(start)){
				u = pred.get(u);
				w.insert(u);
			}
			// TODO Check if a path was found at all; it could be that we just hit the root of the graph.
		}
		w.insert(start);
		return w.done();
	}
}
