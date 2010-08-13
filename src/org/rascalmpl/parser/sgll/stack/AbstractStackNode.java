package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.LinearIntegerKeyedMap;

public abstract class AbstractStackNode{
	protected AbstractStackNode next;
	protected LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap;
	
	protected final int id;
	
	protected int startLocation;

	protected LinearIntegerKeyedMap<ArrayList<Link>> prefixesMap;
	
	private boolean markedAsWithResults;
	
	// Last node specific filter stuff
	private IConstructor parentProduction;
	private IReducableStackNode[] followRestrictions;
	private boolean isReject;
	
	public AbstractStackNode(int id){
		super();
		
		this.id = id;
		
		startLocation = -1;
	}
	
	public AbstractStackNode(int id, IReducableStackNode[] followRestrictions){
		super();
		
		this.id = id;
		
		startLocation = -1;
		
		this.followRestrictions = followRestrictions;
	}
	
	protected AbstractStackNode(AbstractStackNode original){
		super();
		
		id = original.id;
		
		next = original.next;
		edgesMap = original.edgesMap;
		
		parentProduction = original.parentProduction;
		followRestrictions = original.followRestrictions;
		isReject = original.isReject;
	}
	
	protected AbstractStackNode(AbstractStackNode original, LinearIntegerKeyedMap<ArrayList<Link>> prefixes){
		super();
		
		id = original.id;
		
		next = original.next;
		edgesMap = original.edgesMap;
		
		parentProduction = original.parentProduction;
		followRestrictions = original.followRestrictions;
		isReject = original.isReject;
		
		prefixesMap = prefixes;
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
	
	public abstract String getName();
	
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
			for(int i = followRestrictions.length - 1; i >= 0; --i){
				IReducableStackNode followRestriction = followRestrictions[i];
				if((location + followRestriction.getLength()) <= input.length &&
					followRestriction.reduce(input, location)) return true;
			}
		}
		return false;
	}
	
	public void markAsReject(){
		isReject = true;
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
		int startLocation = edge.getStartLocation();
		
		ArrayList<AbstractStackNode> edges;
		if(edgesMap == null){
			edgesMap = new LinearIntegerKeyedMap<ArrayList<AbstractStackNode>>();
			edges = new ArrayList<AbstractStackNode>(1);
			edgesMap.add(startLocation, edges);
		}else{
			edges = edgesMap.findValue(startLocation);
			if(edges == null){
				edges = new ArrayList<AbstractStackNode>(1);
				edgesMap.add(startLocation, edges);
			}
		}
		
		edges.add(edge);
	}
	
	public void addEdges(LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMapToAdd){
		if(edgesMap != edgesMapToAdd){
			for(int i = edgesMapToAdd.size() - 1; i >= 0; --i){
				int startLocation = edgesMapToAdd.getKey(i);
				ArrayList<AbstractStackNode> edgesToAdd = edgesMapToAdd.getValue(i);
				
				ArrayList<AbstractStackNode> edges = edgesMap.findValue(startLocation);
				
				if(edges == null){
					edgesMap.add(startLocation, edgesToAdd);
				}else if(edges != edgesToAdd){
					OUTER : for(int j = edgesToAdd.size() - 1; j >= 0; --j){
						AbstractStackNode edgeToAdd = edgesToAdd.get(j);
						for(int k = edges.size() - 1; k >= 0; --k){
							AbstractStackNode edge = edges.get(k);
							if(edgeToAdd == edge){
								continue OUTER;
							}
						}
						
						edges.add(edgeToAdd);
					}
				}
			}
		}
	}
	
	public boolean hasEdges(){
		return (edgesMap != null);
	}
	
	public LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> getEdges(){
		return edgesMap;
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
	
	public void markAsWithResults(){
		markedAsWithResults = true;
	}
	
	public boolean isMarkedAsWithResults(){
		return markedAsWithResults;
	}
	
	public abstract void setResultStore(ContainerNode resultStore);
	
	public abstract ContainerNode getResultStore();
	
	public abstract AbstractNode getResult();
}
