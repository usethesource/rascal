package org.meta_environment.rascal.std;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;

class Distance {
	int intval;
	
	Distance(int n){
		intval = n;
	}
}

class NodeComparator implements Comparator<IValue> {
	private final HashMap<IValue,Distance> distance;
	
	NodeComparator(HashMap<IValue,Distance> distance){
		this.distance = distance;
	}

	@Override
	public int compare(IValue arg0, IValue arg1) {
		int d0 = distance.get(arg0).intval;
		int d1 = distance.get(arg1).intval;
		
		return d0 < d1 ? -1 : ((d0 == d1) ? 0 : 1);
	}
}

public class Graph {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final TypeFactory types = TypeFactory.getInstance();
	
	private static HashMap<IValue,Distance> distance;
	private static HashMap<IValue, IValue> pred;
	private static HashSet<IValue> settled;
	private static PriorityQueue<IValue> Q;
	private static int MAXDISTANCE = 10000;
	
	private static HashMap<IValue, LinkedList<IValue>> AdjacencyList;
	
	private static void buildAdjacencyListAndDistance(IRelation G){
		
		AdjacencyList = new HashMap<IValue, LinkedList<IValue>> ();
		distance = new HashMap<IValue, Distance>();
		
		for(IValue v : G){
			ITuple tup = (ITuple) v;
			IValue from = tup.get(0);
			IValue to = tup.get(1);
			
			if(distance.get(from) == null)
				distance.put(from, new Distance(MAXDISTANCE));
			if(distance.get(to) == null)
				distance.put(to, new Distance(MAXDISTANCE));
			
			LinkedList<IValue> adjacencies = AdjacencyList.get(from);
			if(adjacencies == null)
				adjacencies = new LinkedList<IValue>();
			adjacencies.add(to);
			AdjacencyList.put(from, adjacencies);
		}
	/*	
		for(Entry<IValue, LinkedList<IValue>>  entry: AdjacencyList.entrySet()){
			System.err.printf("%s -> ", entry.getKey());
			for(IValue v : entry.getValue()){
				System.err.printf("%s ", v);
			}
			System.err.printf("\n");
		}
		*/
	}
	
	public static IValue shortestPathPair(IRelation G, IValue From, IValue To){
		buildAdjacencyListAndDistance(G);
		distance.put(From, new Distance(0));
		
		pred = new HashMap<IValue, IValue>();
		settled = new HashSet<IValue>();
		Q = new PriorityQueue<IValue>(G.size(), new NodeComparator(distance));
		Q.add(From);
		
		while(!Q.isEmpty()){
			IValue u = Q.remove();
			//System.err.println("u = " + u);
			if(u.isEqual(To))	
				return extractPath(From, u);
			settled.add(u);
			relaxNeighbours(u);
		}
		return values.list();
	}
	
	private static void relaxNeighbours(IValue u){
		for(IValue v : AdjacencyList.get(u)){
			if(!settled.contains(v)){
				Distance dv = distance.get(v);
				Distance du = distance.get(u);
				//System.err.println("u = " + u + " v = " + v + " du = " + du.intval + " dv = " + dv.intval);
				if(dv.intval > du.intval + 1){  // 1 is default weight of each edge
			           dv.intval = du.intval + 1;
			           //System.err.println("distance[" + v + "] = " + (du.intval + 1));
			           pred.put(v,u);
			           Q.add(v);
			           //System.err.println("add pred(" + v + "," + u + ")");
			           //System.err.println("add " + v + " to Q");
			        }
			}
		}
	}
	
	private static IList extractPath(IValue start, IValue u){
		Type listType = types.listType(start.getType());
		IListWriter w = listType.writer(values);
		
		w.insert(u);
		while(!pred.get(u).isEqual(start)){
			u = pred.get(u);
			w.insert(u);
		}
		w.insert(start);
		return w.done();
	}
}
