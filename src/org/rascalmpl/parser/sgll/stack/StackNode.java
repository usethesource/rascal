package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.INode;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.IntegerList;

public abstract class StackNode{
	protected final static int DEFAULT_LIST_EPSILON_ID = -1;
	
	protected StackNode next;
	protected ArrayList<StackNode> edges;
	
	protected final int id;
	
	protected int startLocation;
	
	protected ArrayList<INode[]> prefixes;
	protected IntegerList prefixStartLocations;
	
	public StackNode(int id){
		super();
		
		this.id = id;
		
		startLocation = -1;
	}
	
	public StackNode(StackNode parseStackNode){
		super();
		
		id = parseStackNode.id;
		
		next = parseStackNode.next;
		edges = parseStackNode.edges;
	}
	
	// General.
	public int getId(){
		return id;
	}
	
	public abstract boolean isReducable();
	
	public abstract boolean isList();
	
	public abstract String getMethodName();
	
	public abstract boolean reduce(char[] input);
	
	// Sharing.
	public abstract StackNode getCleanCopy();
	
	public abstract StackNode getCleanCopyWithPrefix();
	
	public boolean isSimilar(StackNode node){
		return (node.getId() == getId());
	}
	
	// Linking.
	public void addNext(StackNode next){
		this.next = next;
	}
	
	public boolean hasNext(){
		return (next != null);
	}
	
	public StackNode getNext(){
		return next;
	}
	
	public void addEdge(StackNode edge){
		if(edges == null) edges = new ArrayList<StackNode>(1);
		edges.add(edge);
	}
	
	public void addEdges(ArrayList<StackNode> edgesToAdd){
		if(edges != edgesToAdd){
			OUTER : for(int i = edgesToAdd.size() - 1; i >= 0; i--){
				StackNode node = edgesToAdd.get(i);
				for(int j = edges.size() - 1; j >= 0; j--){
					StackNode edge = edges.get(j);
					if(edge == node || (edge.getId() == node.getId() && edge.getStartLocation() == node.getStartLocation())){
						break OUTER;
					}
				}
				edges.add(node);
			}
		}
	}
	
	public boolean hasEdges(){
		return (edges != null);
	}
	
	public ArrayList<StackNode> getEdges(){
		return edges;
	}
	
	// Location.
	public void setStartLocation(int startLocation){
		this.startLocation = startLocation;
	}
	
	public boolean startLocationIsSet(){
		return (startLocation != -1);
	}
	
	public int getStartLocation(){
		return startLocation;
	}
	
	public abstract void mark();
	
	public abstract boolean isMarked();
	
	public abstract int getLength();
	
	// Lists.
	public abstract StackNode[] getChildren();
	
	// Results.
	public void addPrefix(INode[] prefix, int length){
		if(prefixes == null){
			prefixes = new ArrayList<INode[]>(1);
			prefixStartLocations = new IntegerList(1);
		}
		
		prefixes.add(prefix);
		prefixStartLocations.add(length);
	}
	
	public abstract void addResult(INode[] children);
	
	public abstract INode getResult();
	
	public INode[][] getResults(){
		if(prefixes == null){
			INode[][] results = new INode[1][1];
			results[0][0] = getResult();
			return results;
		}
		
		int nrOfPrefixes = prefixes.size();
		INode[][] results = new INode[nrOfPrefixes][];
		INode thisResult = getResult();
		for(int i = 0; i < nrOfPrefixes; i++){
			INode[] prefix = prefixes.get(i);
			int prefixLength = prefix.length;
			INode[] result = new INode[prefixLength + 1];
			System.arraycopy(prefix, 0, result, 0, prefixLength);
			result[prefixLength] = thisResult;
			
			results[i] = result;
		}
		
		return results;
	}
	
	public int[] getResultStartLocations(){
		if(prefixStartLocations == null){
			return new int[]{startLocation};
		}
		
		return prefixStartLocations.getBackingArray();
	}
}
