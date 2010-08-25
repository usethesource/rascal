package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.LinearIntegerKeyedMap;

public abstract class AbstractStackNode{
	public final static int START_SYMBOL_ID = -1;
	public final static int DEFAULT_LEVEL_ID = 0;
	protected final static int DEFAULT_LIST_EPSILON_ID = -2; // (0xeffffffe | 0x80000000)
	
	protected AbstractStackNode next;
	protected LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap;
	protected ArrayList<Link>[] prefixesMap;
	
	protected final int id;
	
	protected int startLocation;

	protected boolean isEndNode;
	
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
		edgesMap = new LinearIntegerKeyedMap<ArrayList<AbstractStackNode>>();
		
		startLocation = original.startLocation;
		
		isEndNode = original.isEndNode;
		
		parentProduction = original.parentProduction;
		followRestrictions = original.followRestrictions;
		isReject = original.isReject;
	}
	
	protected AbstractStackNode(AbstractStackNode original, ArrayList<Link>[] prefixes){
		super();
		
		id = original.id;

		next = original.next;
		edgesMap = new LinearIntegerKeyedMap<ArrayList<AbstractStackNode>>(original.edgesMap);
		
		prefixesMap = prefixes;
		
		startLocation = original.startLocation;
		
		isEndNode = original.isEndNode;
		
		parentProduction = original.parentProduction;
		followRestrictions = original.followRestrictions;
		isReject = original.isReject;
	}
	
	// General.
	public int getId(){
		return id;
	}
	
	public void markAsEndNode(){
		isEndNode = true;
	}
	
	public boolean isEndNode(){
		return isEndNode;
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
	
	public void setFollowRestriction(IReducableStackNode[] followRestrictions) {
		this.followRestrictions = followRestrictions;
	}
	
	public boolean isReductionFiltered(char[] input, int location){
		// Check if follow restrictions apply.
		if(followRestrictions != null){
			for(int i = followRestrictions.length - 1; i >= 0; --i){
				IReducableStackNode followRestriction = followRestrictions[i];
				if((location + followRestriction.getLength()) <= input.length &&
					followRestriction.reduceWithoutResult(input, location)) return true;
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
	
	// Linking & prefixes.
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
		
		ArrayList<AbstractStackNode> edges = edgesMap.findValue(startLocation);
		if(edges == null){
			edges = new ArrayList<AbstractStackNode>(1);
			edgesMap.add(startLocation, edges);
		}
		
		edges.add(edge);
	}
	
	public void addEdgeWithPrefix(AbstractStackNode edge, Link prefix, int startLocation){
		int edgesMapSize = edgesMap.size();
		if(prefixesMap == null){
			prefixesMap = (ArrayList<Link>[]) new ArrayList[(edgesMapSize + 1) << 1];
		}else{
			int prefixesMapSize = prefixesMap.length;
			int possibleMaxSize = edgesMapSize + 1;
			if(prefixesMapSize < possibleMaxSize){
				ArrayList<Link>[] oldPrefixesMap = prefixesMap;
				prefixesMap = (ArrayList<Link>[]) new ArrayList[possibleMaxSize << 1];
				System.arraycopy(oldPrefixesMap, 0, prefixesMap, 0, edgesMapSize);
			}
		}
		
		ArrayList<AbstractStackNode> edges;
		int index = edgesMap.findKey(startLocation);
		if(index == -1){
			index = edgesMap.size();
			
			edges = new ArrayList<AbstractStackNode>(1);
			edgesMap.add(startLocation, edges);
		}else{
			edges = edgesMap.getValue(index);
		}
		edges.add(edge);
		
		ArrayList<Link> prefixes = prefixesMap[index];
		if(prefixes == null){
			prefixes = new ArrayList<Link>(1);
			prefixesMap[index] = prefixes;
		}
		prefixes.add(prefix);
	}
	
	private void addPrefix(Link prefix, int prefixStartLocation){
		int index = edgesMap.findKey(prefixStartLocation);
		
		ArrayList<Link> prefixes = prefixesMap[index];
		if(prefixes == null){
			prefixes = new ArrayList<Link>(1);
			prefixesMap[index] = prefixes;
		}
		
		prefixes.add(prefix);
	}
	
	public void updateNode(AbstractStackNode predecessor){
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMapToAdd = predecessor.edgesMap;
		ArrayList<Link>[] prefixesMapToAdd = predecessor.prefixesMap;
		AbstractNode result = predecessor.getResult();
		
		int edgesMapSize = edgesMap.size();
		if(edgesMapSize == 0){
			edgesMap = new LinearIntegerKeyedMap<ArrayList<AbstractStackNode>>(edgesMapToAdd);

			if(prefixesMap == null){
				prefixesMap = (ArrayList<Link>[]) new ArrayList[edgesMap.size()];
			}
			
			if(prefixesMapToAdd == null){
				addPrefix(new Link(null, result), predecessor.getStartLocation());
			}else{
				int nrOfPrefixes = edgesMapToAdd.size();
				for(int i = nrOfPrefixes - 1; i >= 0; --i){
					ArrayList<Link> prefixes = prefixesMap[i];
					if(prefixes == null){
						prefixes = new ArrayList<Link>(1);
						prefixesMap[i] = prefixes;
					}
					
					prefixes.add(new Link(prefixesMapToAdd[i], result));
				}
			}
		}else if(edgesMap != edgesMapToAdd){
			int possibleMaxSize = edgesMapSize + edgesMapToAdd.size();
			if(prefixesMap == null){
				prefixesMap = (ArrayList<Link>[]) new ArrayList[possibleMaxSize];
			}else{
				if(prefixesMap.length < possibleMaxSize){
					ArrayList<Link>[] oldPrefixesMap = prefixesMap;
					prefixesMap = (ArrayList<Link>[]) new ArrayList[possibleMaxSize];
					System.arraycopy(oldPrefixesMap, 0, prefixesMap, 0, edgesMapSize);
				}
			}
			
			if(prefixesMapToAdd == null){
				int startLocation = edgesMapToAdd.getKey(0);
				int index = edgesMap.findKey(startLocation);
				if(index == -1){
					edgesMap.add(startLocation, edgesMapToAdd.getValue(0));
				}
				
				addPrefix(new Link(null, result), predecessor.getStartLocation());
			}else{
				for(int i = edgesMapToAdd.size() - 1; i >= 0; --i){
					int startLocation = edgesMapToAdd.getKey(i);
					int index = edgesMap.findKey(startLocation);
					if(index == -1){
						index = edgesMap.size();
						edgesMap.add(startLocation, edgesMapToAdd.getValue(i));
					}
					
					ArrayList<Link> prefixes = prefixesMap[index];
					if(prefixes == null){
						prefixes = new ArrayList<Link>(1);
						prefixesMap[index] = prefixes;
					}
					prefixes.add(new Link(prefixesMapToAdd[i], result));
				}
			}
		}else{
			if(prefixesMap == null){
				prefixesMap = (ArrayList<Link>[]) new ArrayList[edgesMap.size()];
			}
			
			if(prefixesMapToAdd == null){
				addPrefix(new Link(null, result), predecessor.getStartLocation());
			}else{
				int nrOfPrefixes = edgesMapToAdd.size();
				for(int i = nrOfPrefixes - 1; i >= 0; --i){
					ArrayList<Link> prefixes = prefixesMap[i];
					if(prefixes == null){
						prefixes = new ArrayList<Link>(1);
						prefixesMap[i] = prefixes;
					}
					
					prefixes.add(new Link(prefixesMapToAdd[i], result));
				}
			}
		}
	}
	
	public boolean hasEdges(){
		return (edgesMap.size() != 0);
	}
	
	public LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> getEdges(){
		return edgesMap;
	}
	
	public ArrayList<Link>[] getPrefixesMap(){
		return prefixesMap;
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
	public abstract void setResultStore(ContainerNode resultStore);
	
	public abstract ContainerNode getResultStore();
	
	public abstract AbstractNode getResult();
}
