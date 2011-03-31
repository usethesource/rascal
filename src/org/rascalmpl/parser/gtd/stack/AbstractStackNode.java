package org.rascalmpl.parser.gtd.stack;


import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.filter.IReductionFilter;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.LinearIntegerKeyedMap;
import org.rascalmpl.parser.gtd.util.specific.PositionStore;

public abstract class AbstractStackNode{
	public final static int START_SYMBOL_ID = -1;
	
	protected AbstractStackNode[] production;
	protected ArrayList<AbstractStackNode[]> alternateProductions;
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
	private IReductionFilter[] reductionFilters;
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
		
		production = original.production;
		alternateProductions = original.alternateProductions;
		
		startLocation = original.startLocation;
		
		isEndNode = original.isEndNode;
		isSeparator = original.isSeparator;
		isLayout = original.isLayout;
		
		parentProduction = original.parentProduction;
		followRestrictions = original.followRestrictions;
		reductionFilters = original.reductionFilters;
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
	
	public abstract boolean isEmptyLeafNode();
	
	public abstract String getName();
	
	public abstract void setPositionStore(PositionStore positionStore);
	
	public abstract boolean match(char[] input);
	
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
	
	public void setReductionFilters(IReductionFilter[] reductionFilters){
		this.reductionFilters = reductionFilters;
	}
	
	public IReductionFilter[] getReductionFilters() {
		return reductionFilters;
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
		
		if(reductionFilters != null){
			for(int i = reductionFilters.length - 1; i >= 0; --i){
				if(reductionFilters[i].isFiltered(input, startLocation, location)) return true;
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
		return (node.id == id);
	}
	
	// Linking & prefixes.
	public int getDot(){
		return dot;
	}
	
	public void setProduction(AbstractStackNode[] production){
		this.production = production;
	}
	
	public void addProduction(AbstractStackNode[] production){
		if(this.production == null){
			this.production = production;
		}else{
			if(alternateProductions == null){
				alternateProductions = new ArrayList<AbstractStackNode[]>();
			}
			if(production.length > this.production.length){
				alternateProductions.add(this.production);
				this.production = production;
			}else{
				alternateProductions.add(production);
			}
		}
	}
	
	public boolean hasNext(){
		return !((production == null) || ((dot + 1) == production.length));
	}
	
	public AbstractStackNode[] getProduction(){
		return production;
	}
	
	public ArrayList<AbstractStackNode[]> getAlternateProductions(){
		return alternateProductions;
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
	
	private void addPrefix(Link prefix, int index){
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
				int index = edgesMap.findKey(predecessor.getStartLocation());
				addPrefix(new Link(null, result), index);
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
					addPrefix(new Link(null, result), edgesMap.size());
					edgesMap.add(startLocation, edgesMapToAdd.getValue(0));
				}else{
					addPrefix(new Link(null, result), index);
				}
			}else{
				for(int i = edgesMapToAdd.size() - 1; i >= 0; --i){
					int startLocation = edgesMapToAdd.getKey(i);
					int index = edgesMap.findKeyBefore(startLocation, edgesMapSize);
					ArrayList<Link> prefixes;
					if(index == -1){
						index = edgesMap.size();
						edgesMap.add(startLocation, edgesMapToAdd.getValue(i));
						
						prefixes = new ArrayList<Link>(1);
						prefixesMap[index] = prefixes;
					}else{
						prefixes = prefixesMap[index];
					}
					
					prefixes.add(new Link(prefixesMapToAdd[i], result));
				}
			}
		}else{
			if(prefixesMap == null){
				prefixesMap = (ArrayList<Link>[]) new ArrayList[edgesMap.size()];
			}
			
			if(prefixesMapToAdd == null){
				int index = edgesMap.findKey(predecessor.getStartLocation());
				addPrefix(new Link(null, result), index);
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
	
	public int updateOvertakenNode(AbstractStackNode predecessor, AbstractNode result, int potentialNewEdges, IntegerList touched){
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMapToAdd = predecessor.edgesMap;
		ArrayList<Link>[] prefixesMapToAdd = predecessor.prefixesMap;
		
		int edgesMapSize = edgesMap.size();
		int possibleMaxSize = edgesMapSize + potentialNewEdges;
		if(prefixesMap == null){
			prefixesMap = (ArrayList<Link>[]) new ArrayList[possibleMaxSize];
		}else{
			if(prefixesMap.length < possibleMaxSize){
				ArrayList<Link>[] oldPrefixesMap = prefixesMap;
				prefixesMap = (ArrayList<Link>[]) new ArrayList[possibleMaxSize];
				System.arraycopy(oldPrefixesMap, 0, prefixesMap, 0, edgesMapSize);
			}
		}
		
		int nrOfAddedEdges = 0;
		if(prefixesMapToAdd == null){
			int startLocation = predecessor.getStartLocation();
			if(touched.contains(startLocation)) return 0;
			
			int index = edgesMap.findKey(startLocation);
			if(index == -1){
				addPrefix(new Link(null, result), edgesMap.size());
				edgesMap.add(startLocation, edgesMapToAdd.getValue(0));
				touched.add(startLocation);
				nrOfAddedEdges = 1;
			}else{
				addPrefix(new Link(null, result), index);
			}
		}else{
			int fromIndex = edgesMapToAdd.size() - potentialNewEdges;
			for(int i = edgesMapToAdd.size() - 1; i >= fromIndex; --i){
				int startLocation = edgesMapToAdd.getKey(i);
				if(touched.contains(startLocation)) continue;
				
				int index = edgesMap.findKey(startLocation);
				ArrayList<Link> prefixes;
				if(index == -1){
					index = edgesMap.size();
					edgesMap.add(startLocation, edgesMapToAdd.getValue(i));
					touched.add(startLocation);
					
					prefixes = new ArrayList<Link>(1);
					prefixesMap[index] = prefixes;
					
					++nrOfAddedEdges;
				}else{
					prefixes = prefixesMap[index];
				}
				
				prefixes.add(new Link(prefixesMapToAdd[i], result));
			}
		}
		
		return nrOfAddedEdges;
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
