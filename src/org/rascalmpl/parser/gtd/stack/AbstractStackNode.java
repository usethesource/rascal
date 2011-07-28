/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;

public abstract class AbstractStackNode{
	public final static int START_SYMBOL_ID = -1;
	public final static int DEFAULT_START_LOCATION = -1;
	
	protected AbstractStackNode[] production;
	protected AbstractStackNode[][] alternateProductions;
	
	protected IntegerObjectList<ArrayList<AbstractStackNode>> edgesMap;
	protected ArrayList<Link>[] prefixesMap;
	
	protected final int id;
	protected final int dot;
	
	protected final int startLocation;
	
	// Flags
	private boolean isEndNode;
	private boolean isSeparator;
	private boolean isLayout;
	
	// Filters
	private final IEnterFilter[] enterFilters;
	private final ICompletionFilter[] completionFilters;
	
	// The production (specific to end nodes only)
	private IConstructor parentProduction;
	
	public AbstractStackNode(int id, int dot){
		super();
		
		this.id = id;
		this.dot = dot;
		
		this.startLocation = DEFAULT_START_LOCATION;
		
		this.enterFilters = null;
		this.completionFilters = null;
	}
	
	public AbstractStackNode(int id, int dot, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super();
		
		this.id = id;
		this.dot = dot;
		
		this.startLocation = DEFAULT_START_LOCATION;
		
		this.enterFilters = enterFilters;
		this.completionFilters = completionFilters;
	}
	
	protected AbstractStackNode(AbstractStackNode original, int startLocation){
		super();
		
		id = original.id;
		dot = original.dot;
		
		production = original.production;
		alternateProductions = original.alternateProductions;
		
		this.startLocation = startLocation;
		
		isEndNode = original.isEndNode;
		isSeparator = original.isSeparator;
		isLayout = original.isLayout;
		
		parentProduction = original.parentProduction;
		enterFilters = original.enterFilters;
		completionFilters = original.completionFilters;
	}
	
	// General.
	/**
	 * Returns the id of the node.
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * Checks whether or not this node is the last node in the production.
	 * Note that while this may be an end node alternative continuations of this production may be possible.
	 */
	public boolean isEndNode(){
		return isEndNode;
	}
	
	/**
	 * Mark this node as being a separator.
	 */
	public void markAsSeparator(){
		isSeparator = true;
	}
	
	/**
	 * Checks whether or not this nodes is a separator.
	 */
	public boolean isSeparator(){
		return isSeparator;
	}
	
	/**
	 * Marks this node as representing layout.
	 */
	public void markAsLayout(){
		isLayout = true;
	}
	
	/**
	 * Checks whether or not this node represents layout.
	 */
	public boolean isLayout(){
		return isLayout;
	}
	
	/**
	 * Checks whether this node represents a 'matchable' leaf node.
	 */
	public final boolean isMatchable(){
		return (this instanceof IMatchableStackNode);
	}
	
	/**
	 * Check wheterh this node represents a node which needs to be expanded in-place.
	 */
	public final boolean isExpandable(){
		return (this instanceof IExpandableStackNode);
	}
	
	/**
	 * Checks if this node represents a nullable leaf node.
	 */
	public abstract boolean isEmptyLeafNode();
	
	/**
	 * Returns the name associated with the symbol in this node (optional operation).
	 */
	public abstract String getName();
	
	/**
	 * Matches the symbol associated with this node to the input at the specified location.
	 * This operation is only available on matchable nodes.
	 */
	public abstract AbstractNode match(char[] input, int location);
	
	/**
	 * Check whether of this this node is equal to the given node.
	 */
	public abstract boolean isEqual(AbstractStackNode stackNode);
	
	// Last node specific stuff.
	/**
	 * Associates a production with this node, indicating that this is the last node in the production.
	 * This production will be used in result construction during reduction.
	 */
	public void setParentProduction(IConstructor parentProduction){
		this.parentProduction = parentProduction;
		isEndNode = true;
	}
	
	/**
	 * Retrieves the production associated with the alternative this node is a part of.
	 * Only the last node in the alternative will have this production on it.
	 */
	public IConstructor getParentProduction(){
		return parentProduction;
	}
	
	/**
	 * Retrieves the enter filters associated with the symbol in this node.
	 * The returned value may be null if no enter filters are present.
	 */
	public IEnterFilter[] getEnterFilters(){
		return enterFilters;
	}
	
	/**
	 * Retrieves the completion filters associated with the symbol in this node.
	 * The returned value may be null if no completion filters are present.
	 */
	public ICompletionFilter[] getCompletionFilters(){
		return completionFilters;
	}
	
	/**
	 * Checks whether or not the filters that are associated with this nodes symbol are equal to those of the given node.
	 */
	public boolean hasEqualFilters(AbstractStackNode otherNode){
		IEnterFilter[] otherEnterFilters = otherNode.enterFilters;
		OUTER: for(int i = enterFilters.length - 1; i >= 0; --i){
			IEnterFilter enterFilter = enterFilters[i];
			for(int j = otherEnterFilters.length - 1; j >= 0; --j){
				if(enterFilter.isEqual(otherEnterFilters[j])) continue OUTER;
			}
			return false;
		}
		
		ICompletionFilter[] otherCompletionFilters = otherNode.completionFilters;
		OUTER: for(int i = completionFilters.length - 1; i >= 0; --i){
			ICompletionFilter completionFilter = completionFilters[i];
			for(int j = otherCompletionFilters.length - 1; j >= 0; --j){
				if(completionFilter.isEqual(otherCompletionFilters[j])) continue OUTER;
			}
			return false;
		}
		
		return true;
	}
	
	// Creation and sharing.
	/**
	 * Creates a new copy of this node for the indicated position.
	 */
	public abstract AbstractStackNode getCleanCopy(int startLocation);
	
	/**
	 * Creates a new copy of this node for the indicated position and associated the given result with it.
	 * This method has the same effect as the one above, but is exclusively used for matchable nodes;
	 * as their results are constructed before the node is created.
	 */
	public abstract AbstractStackNode getCleanCopyWithResult(int startLocation, AbstractNode result);
	
	/**
	 * Checks whether or not this node has the same id as the given one.
	 * This method is used when determining whether or not stacks should to be merged.
	 */
	public boolean isSimilar(AbstractStackNode node){
		return (node.id == id);
	}
	
	// Node continuations.
	/**
	 * Indicates the current location in the alternative this node is a part of.
	 */
	public int getDot(){
		return dot;
	}
	
	/**
	 * Sets the main alternative this node is a part of.
	 */
	public void setProduction(AbstractStackNode[] production){
		this.production = production;
	}
	
	/**
	 * Adds an additional alternative this node is a part of.
	 * This can be the case if the symbol this node is associated with is located in a shared prefix of more then one alternative.
	 */
	public void addProduction(AbstractStackNode[] production){
		if(this.production == null){
			this.production = production;
		}else{
			if(alternateProductions == null){
				alternateProductions = new AbstractStackNode[][]{production};
			}else{
				int nrOfAlternateProductions = alternateProductions.length;
				AbstractStackNode[][] newAlternateProductions = new AbstractStackNode[nrOfAlternateProductions][];
				System.arraycopy(alternateProductions, 0, newAlternateProductions, 0, nrOfAlternateProductions);
			}
		}
	}
	
	/**
	 * Checks if this node has possible continuations.
	 */
	public boolean hasNext(){
		return !((production == null) || ((dot + 1) == production.length));
	}
	
	/**
	 * Retrieves the main alternative the symbol this node is associated with is a part of.
	 */
	public AbstractStackNode[] getProduction(){
		return production;
	}
	
	/**
	 * Retrieves the alternative continuations the symbols this node is associated with is a part of.
	 */
	public AbstractStackNode[][] getAlternateProductions(){
		return alternateProductions;
	}
	
	// Edges & prefixes.
	public void initEdges(){
		edgesMap = new IntegerObjectList<ArrayList<AbstractStackNode>>();
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
			prefixesMap = new ArrayList[(edgesMapSize + 1) << 1];
		}else{
			int prefixesMapSize = prefixesMap.length;
			int possibleMaxSize = edgesMapSize + 1;
			if(prefixesMapSize < possibleMaxSize){
				ArrayList<Link>[] oldPrefixesMap = prefixesMap;
				prefixesMap = new ArrayList[possibleMaxSize << 1];
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
	
	public void updateNodeAfterNonEmptyMatchable(AbstractStackNode predecessor, AbstractNode result){
		ArrayList<Link>[] prefixesMapToAdd = predecessor.prefixesMap;
		
		edgesMap = predecessor.edgesMap;

		if(prefixesMap == null){
			prefixesMap = new ArrayList[edgesMap.size()];
		}
		
		if(prefixesMapToAdd == null){
			int index = edgesMap.findKey(predecessor.getStartLocation());
			addPrefix(new Link(null, result), index);
		}else{
			int nrOfPrefixes = edgesMap.size();
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
	
	public void updateNode(AbstractStackNode predecessor, AbstractNode result){
		IntegerObjectList<ArrayList<AbstractStackNode>> edgesMapToAdd = predecessor.edgesMap;
		ArrayList<Link>[] prefixesMapToAdd = predecessor.prefixesMap;
		
		if(edgesMap == null){
			edgesMap = new IntegerObjectList<ArrayList<AbstractStackNode>>(edgesMapToAdd);

			if(prefixesMap == null){
				prefixesMap = new ArrayList[edgesMap.size()];
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
				prefixesMap = new ArrayList[possibleMaxSize];
			}else{
				if(prefixesMap.length < possibleMaxSize){
					ArrayList<Link>[] oldPrefixesMap = prefixesMap;
					prefixesMap = new ArrayList[possibleMaxSize];
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
				prefixesMap = new ArrayList[edgesMap.size()];
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
		IntegerObjectList<ArrayList<AbstractStackNode>> edgesMapToAdd = predecessor.edgesMap;
		ArrayList<Link>[] prefixesMapToAdd = predecessor.prefixesMap;
		
		int edgesMapSize = edgesMap.size();
		int possibleMaxSize = edgesMapSize + potentialNewEdges;
		if(prefixesMap == null){
			prefixesMap = new ArrayList[possibleMaxSize];
		}else{
			if(prefixesMap.length < possibleMaxSize){
				ArrayList<Link>[] oldPrefixesMap = prefixesMap;
				prefixesMap = new ArrayList[possibleMaxSize];
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
	
	public void updatePrefixSharedNode(IntegerObjectList<ArrayList<AbstractStackNode>> edgesMap, ArrayList<Link>[] prefixesMap){
		this.edgesMap = edgesMap;
		this.prefixesMap = prefixesMap;
	}
	
	public boolean hasEdges(){
		return (edgesMap.size() > 0);
	}
	
	public IntegerObjectList<ArrayList<AbstractStackNode>> getEdges(){
		return edgesMap;
	}
	
	public ArrayList<Link>[] getPrefixesMap(){
		return prefixesMap;
	}
	
	// Location.
	public int getStartLocation(){
		return startLocation;
	}
	
	public abstract int getLength();
	
	// Lists.
	public abstract AbstractStackNode[] getChildren();
	
	public abstract boolean canBeEmpty();
	
	public abstract AbstractStackNode getEmptyChild();
	
	// Results.
	public abstract AbstractNode getResult();
}
