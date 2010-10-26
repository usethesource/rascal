package org.rascalmpl.parser.sgll.stack;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.LinearIntegerKeyedMap;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;

public abstract class AbstractStackNode{
	public final static int START_SYMBOL_ID = -1;
	protected final static int DEFAULT_LIST_EPSILON_ID = -2; // (0xeffffffe | 0x80000000)
	
	protected AbstractStackNode[] next;
	protected ArrayList<AbstractStackNode[]> alternateNexts;
	protected LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap;
	protected ArrayList<Link>[] prefixesMap;
	
	protected final int id;
	protected final int dot;
	
	protected int startLocation;

	protected boolean isEndNode;
	private boolean isSeparator;
	private boolean isLayout;
	
	// Last node specific filter stuff
	private IConstructor parentProduction;
	private IMatchableStackNode[] followRestrictions;
	private boolean isReject;
	
	public AbstractStackNode(int id, int dot){
		super();
		
		this.id = id;
		this.dot = dot;
		
		startLocation = -1;
	}
	
	public AbstractStackNode(int id, int dot, IMatchableStackNode[] followRestrictions){
		super();
		
		this.id = id;
		this.dot = dot;
		
		startLocation = -1;
		
		this.followRestrictions = followRestrictions;
	}
	
	protected AbstractStackNode(AbstractStackNode original){
		super();
		
		id = original.id;
		dot = original.dot;
		
		next = original.next;
		alternateNexts = original.alternateNexts;
		
		startLocation = original.startLocation;
		
		isEndNode = original.isEndNode;
		isSeparator = original.isSeparator;
		isLayout = original.isLayout;
		
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
	
	public void markAsSeparator(){
		isSeparator = true;
	}
	
	public boolean isSeparator(){
		return isSeparator;
	}
	
	public void markAsLayout(){
		isLayout = true;
	}
	
	public boolean isLayout(){
		return isLayout;
	}
	
	public final boolean isMatchable(){
		return (this instanceof IMatchableStackNode);
	}
	
	public final boolean isLocatable(){
		return (this instanceof ILocatableStackNode);
	}
	
	public final boolean isList(){
		return (this instanceof IListStackNode);
	}
	
	public abstract String getName();
	
	public abstract void setPositionStore(PositionStore positionStore);
	
	public abstract boolean match(URI inputURI, char[] input);
	
	// Last node specific stuff.
	public void setParentProduction(IConstructor parentProduction){
		this.parentProduction = parentProduction;
	}
	
	public IConstructor getParentProduction(){
		return parentProduction;
	}
	
	public void setFollowRestriction(IMatchableStackNode[] followRestrictions) {
		this.followRestrictions = followRestrictions;
	}
	
	public IMatchableStackNode[] getFollowRestriction() {
		return followRestrictions;
	}
	
	public boolean isReductionFiltered(char[] input, int location){
		// Check if follow restrictions apply.
		if(followRestrictions != null){
			for(int i = followRestrictions.length - 1; i >= 0; --i){
				IMatchableStackNode followRestriction = followRestrictions[i];
				if((location + followRestriction.getLength()) <= input.length &&
					followRestriction.matchWithoutResult(input, location)) return true;
			}
		}
		return false;
	}
	
	public void markAsReject(){
		isReject = true;
	}
	
	public void setReject(boolean isReject){
		this.isReject = isReject;
	}
	
	public boolean isReject(){
		return isReject;
	}
	
	// Sharing.
	public abstract AbstractStackNode getCleanCopy();
	
	public boolean isSimilar(AbstractStackNode node){
		return (node.getId() == getId());
	}
	
	// Linking & prefixes.
	public int getDot(){
		return dot;
	}
	
	public void setNext(AbstractStackNode[] next){
		this.next = next;
	}
	
	public void addNext(AbstractStackNode[] next){
		if(this.next == null){
			this.next = next;
		}else{
			if(alternateNexts == null){
				alternateNexts = new ArrayList<AbstractStackNode[]>();
			}
			alternateNexts.add(next);
		}
	}
	
	public boolean hasNext(){
		return !((next == null) || (next.length == (dot + 1)));
	}
	
	public AbstractStackNode[] getNext(){
		return next;
	}
	
	public ArrayList<AbstractStackNode[]> getAlternateNexts(){
		return alternateNexts;
	}
	
	public void initEdges(){
		edgesMap = new LinearIntegerKeyedMap<ArrayList<AbstractStackNode>>();
	}
	
	public ArrayList<AbstractStackNode> addEdge(AbstractStackNode edge){
		int startLocation = edge.getStartLocation();
		
		ArrayList<AbstractStackNode> edges = edgesMap.findValue(startLocation);
		if(edges == null){
			edges = new ArrayList<AbstractStackNode>(1);
			edgesMap.add(startLocation, edges);
		}
		
		edges.add(edge);
		
		return edges;
	}
	
	public void addEdges(ArrayList<AbstractStackNode> edges, int startLocation){
		edgesMap.add(startLocation, edges);
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
	
	public void updateNode(AbstractStackNode predecessor, AbstractNode result){
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMapToAdd = predecessor.edgesMap;
		ArrayList<Link>[] prefixesMapToAdd = predecessor.prefixesMap;
		
		if(edgesMap == null){
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
			int edgesMapSize = edgesMap.size();
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
	
	public void updatePrefixSharedNode(LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap, ArrayList<Link>[] prefixesMap){
		this.edgesMap = edgesMap;
		this.prefixesMap = prefixesMap;
	}
	
	public boolean hasEdges(){
		return (edgesMap.size() > 0);
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
	
	public int getStartLocation(){
		return startLocation;
	}
	
	public abstract int getLength();
	
	// Lists.
	public abstract AbstractStackNode[] getChildren();
	
	// Results.
	public abstract AbstractNode getResult();
}
