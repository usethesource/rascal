package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.INode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.LinearIntegerKeyedMap;

public abstract class AbstractStackNode{
	protected final static int DEFAULT_LIST_EPSILON_ID = -1;
	
	protected AbstractStackNode next;
	protected ArrayList<AbstractStackNode> edges;
	
	protected final int id;
	
	protected int startLocation;

	protected LinearIntegerKeyedMap<ArrayList<Link>> prefixesMap;
	
	// Last node specific filter stuff
	private IConstructor parentProduction;
	private IReducableStackNode[] followRestrictions;
	private boolean isReject;
	
	public AbstractStackNode(int id){
		super();
		
		this.id = id;
		
		startLocation = -1;
	}
	
	protected AbstractStackNode(AbstractStackNode original){
		super();
		
		id = original.id;
		
		next = original.next;
		edges = original.edges;
		
		parentProduction = original.parentProduction;
	}
	
	protected AbstractStackNode(AbstractStackNode original, LinearIntegerKeyedMap<ArrayList<Link>> prefixes){
		super();
		
		id = original.id;
		
		next = original.next;
		edges = original.edges;
		
		parentProduction = original.parentProduction;
		
		this.prefixesMap = prefixes;
	}
	
	// General.
	public int getId(){
		return id;
	}
	
	public final boolean isReducable(){
		return (this instanceof IReducableStackNode);
	}
	
	public final boolean isEpsilon(){
		return (this instanceof EpsilonStackNode);
	}
	
	public final boolean isList(){
		return (this instanceof IListStackNode);
	}
	
	public abstract String getMethodName();
	
	public abstract boolean reduce(char[] input);
	
	// Last node specific stuff.
	public void setParentProduction(IConstructor parentProduction){
		this.parentProduction = parentProduction;
	}
	
	public IConstructor getParentProduction(){
		return parentProduction;
	}
	
	public void setFollowRestriction(IReducableStackNode[] followRestrictions){
		this.followRestrictions = followRestrictions;
	}
	
	public boolean isReductionFiltered(char[] input, int location){
		// Check if follow restrictions apply.
		if(followRestrictions != null){
			for(int i = followRestrictions.length - 1; i >= 0; i--){
				if(followRestrictions[i].reduce(input, location)) return true;
			}
		}
		return false;
	}
	
	public void setReject(boolean isReject){
		this.isReject = isReject;
	}
	
	public boolean isReject(){
		return isReject;
	}
	
	// Sharing.
	public abstract boolean isClean();
	
	public abstract AbstractStackNode getCleanCopy();
	
	public abstract AbstractStackNode getCleanCopyWithPrefix();
	
	public boolean isSimilar(AbstractStackNode node){
		return (node.getId() == getId());
	}
	
	// Linking.
	public void addNext(AbstractStackNode next){
		this.next = next;
	}
	
	public boolean hasNext(){
		return (next != null);
	}
	
	public AbstractStackNode getNext(){
		return next;
	}
	
	public void addEdge(AbstractStackNode edge){
		if(edges == null) edges = new ArrayList<AbstractStackNode>(1);
		edges.add(edge);
	}
	
	public void addEdges(ArrayList<AbstractStackNode> edgesToAdd){
		if(edges != edgesToAdd){
			OUTER : for(int i = edgesToAdd.size() - 1; i >= 0; i--){
				AbstractStackNode node = edgesToAdd.get(i);
				for(int j = edges.size() - 1; j >= 0; j--){
					AbstractStackNode edge = edges.get(j);
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
	
	public ArrayList<AbstractStackNode> getEdges(){
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
	
	public abstract int getLength();
	
	// Lists.
	public abstract AbstractStackNode[] getChildren();
	
	// Results.
	public void addPrefix(Link prefix, int prefixStartLocation){
		ArrayList<Link> prefixes;
		if(prefixesMap == null){
			prefixesMap = new LinearIntegerKeyedMap<ArrayList<Link>>();
			prefixes = new ArrayList<Link>(1);
			prefixesMap.add(prefixStartLocation, prefixes);
		}else{
			prefixes = prefixesMap.findValue(prefixStartLocation);
			if(prefixes == null){
				prefixes = new ArrayList<Link>(1);
				prefixesMap.add(prefixStartLocation, prefixes);
			}
		}
		
		prefixes.add(prefix);
	}
	
	public LinearIntegerKeyedMap<ArrayList<Link>> getPrefixesMap(){
		return prefixesMap;
	}
	
	public abstract void setResultStore(ContainerNode resultStore);
	
	public abstract ContainerNode getResultStore();
	
	public abstract INode getResult();
}
