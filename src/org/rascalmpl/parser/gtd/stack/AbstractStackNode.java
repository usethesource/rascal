/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.stack;

import java.util.Arrays;

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.edge.EdgesSet;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.BitSet;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;
import org.rascalmpl.parser.util.DebugUtil;

import io.usethesource.vallang.IConstructor;

@SuppressWarnings({"unchecked", "cast"})
public abstract class AbstractStackNode<P>{
	public static final int START_SYMBOL_ID = -1;
	public static final int DEFAULT_START_LOCATION = -1;

	protected AbstractStackNode<P>[] production;
	protected AbstractStackNode<P>[][] alternateProductions;

	// Our edges
	protected IntegerObjectList<EdgesSet<P>> edgesMap; // <PO>: key=startLocation, value=EdgesSet at that location
	// Edges of our children
	protected ArrayList<Link>[] prefixesMap;

	protected EdgesSet<P> incomingEdges;

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
	private P alternativeProduction;

	// Hidden-right-recursion related
	private BitSet propagatedPrefixes;
	private IntegerList propagatedReductions;

	protected AbstractStackNode(int id, int dot){
		this(id, dot, DEFAULT_START_LOCATION);
	}

	protected AbstractStackNode(int id, int dot, int startLocation) {
		super();

		this.id = id;
		this.dot = dot;

		this.startLocation = startLocation;

		this.enterFilters = null;
		this.completionFilters = null;
	}

	protected AbstractStackNode(int id, int dot, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super();

		this.id = id;
		this.dot = dot;

		this.startLocation = DEFAULT_START_LOCATION;

		this.enterFilters = enterFilters;
		this.completionFilters = completionFilters;
	}

	protected AbstractStackNode(AbstractStackNode<P> original, int startLocation){
		this(original.id, original, startLocation);
	}

	protected AbstractStackNode(int id, AbstractStackNode<P> original, int startLocation){
		super();

		this.id = id;
		dot = original.dot;

		production = original.production;
		alternateProductions = original.alternateProductions;

		this.startLocation = startLocation;

		isEndNode = original.isEndNode;
		isSeparator = original.isSeparator;
		isLayout = original.isLayout;

		alternativeProduction = original.alternativeProduction;
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
	 * Returns the start location of this node.
	 */
	public int getStartLocation(){
		return startLocation;
	}

	/**
	 * Checks whether or not this node is the last node in the production.
	 * Note that while this may be an end node alternative continuations of this production may be possible.
	 */
	public boolean isEndNode(){
		return isEndNode;
	}

	public boolean isRecovered() {
		return false;
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
	public boolean isMatchable(){
		return false;
	}

	/**
	 * Check wheterh this node represents a node which needs to be expanded in-place.
	 */
	public boolean isExpandable(){
		return false;
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
	 * Check whether of this this node is equal to the given node.
	 */
	public abstract boolean isEqual(AbstractStackNode<P> stackNode);

	// Last node specific stuff.
	/**
	 * Associates a production with this node, indicating that this is the last node in the production.
	 * This production will be used in result construction during reduction.
	 */
	public void setAlternativeProduction(P parentProduction){
		this.alternativeProduction = parentProduction;
		isEndNode = true;
	}

	/**
	 * Retrieves the production associated with the alternative this node is a part of.
	 * Only the last node in the alternative will have this production on it.
	 */
	public P getParentProduction(){
		return alternativeProduction;
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
	public boolean hasEqualFilters(AbstractStackNode<P> otherNode){
		IEnterFilter[] otherEnterFilters = otherNode.enterFilters;
		if(otherEnterFilters != null){
			if(enterFilters == null || enterFilters.length != otherEnterFilters.length) {
				return false;
			}

			OUTER: for(int i = enterFilters.length - 1; i >= 0; --i){
				IEnterFilter enterFilter = enterFilters[i];
				for(int j = otherEnterFilters.length - 1; j >= 0; --j){
					if(enterFilter.isEqual(otherEnterFilters[j])) continue OUTER;
				}
				return false;
			}
		}else if(enterFilters != null){
			return false;
		}

		ICompletionFilter[] otherCompletionFilters = otherNode.completionFilters;
		if(otherCompletionFilters != null){
			if(completionFilters == null || completionFilters.length != otherCompletionFilters.length) {
				return false;
			}

			OUTER: for(int i = completionFilters.length - 1; i >= 0; --i){
				ICompletionFilter completionFilter = completionFilters[i];
				for(int j = otherCompletionFilters.length - 1; j >= 0; --j){
					if(completionFilter.isEqual(otherCompletionFilters[j])) continue OUTER;
				}
				return false;
			}
		}else if(completionFilters != null){
			return false;
		}

		return true;
	}

	/**
	 * Children must override the hash code method (it can't be enforce, but I'm putting it in here anyway).
	 */
	public abstract int hashCode();

	/**
	 * Checks equality.
	 */
	public boolean equals(Object o){
		if(o instanceof AbstractStackNode){
			return isEqual((AbstractStackNode<P>) o);
		}
		return false;
	}

	// Creation and sharing.
	/**
	 * Creates a new copy of this node for the indicated position.
	 */
	public abstract AbstractStackNode<P> getCleanCopy(int startLocation);

	/**
	 * Creates a new copy of this node for the indicated position and associated the given result with it.
	 * This method has the same effect as the one above, but is exclusively used for matchable nodes;
	 * as their results are constructed before the node is created.
	 */
	public abstract AbstractStackNode<P> getCleanCopyWithResult(int startLocation, AbstractNode result);

	/**
	 * Checks whether or not this node has the same id as the given one.
	 * This method is used when determining whether or not stacks should to be merged.
	 */
	public boolean isSimilar(AbstractStackNode<P> node){
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
	public void setProduction(AbstractStackNode<P>[] production){
		this.production = production;
	}

	/**
	 * Adds an additional alternative this node is a part of.
	 * This can be the case if the symbol this node is associated with is located in a shared prefix of more then one alternative.
	 */
	public void addProduction(AbstractStackNode<P>[] production){
		if(this.production == null){
			this.production = production;
		}else{
			if(alternateProductions == null){
				alternateProductions = (AbstractStackNode<P>[][]) new AbstractStackNode[][]{production};
			}else{
				int nrOfAlternateProductions = alternateProductions.length;
				AbstractStackNode<P>[][] newAlternateProductions = (AbstractStackNode<P>[][]) new AbstractStackNode[nrOfAlternateProductions + 1][];
				System.arraycopy(alternateProductions, 0, newAlternateProductions, 0, nrOfAlternateProductions);
				newAlternateProductions[nrOfAlternateProductions] = production;
				alternateProductions = newAlternateProductions;
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
	public AbstractStackNode<P>[] getProduction(){
		return production;
	}

	/**
	 * Retrieves the alternative continuations the symbols this node is associated with is a part of.
	 */
	public AbstractStackNode<P>[][] getAlternateProductions(){
		return alternateProductions;
	}

	// Edges & prefixes.
	/**
	 * Initializes the set of edges of this node.
	 */
	public void initEdges(){
		edgesMap = new IntegerObjectList<>();
	}

	/**
	 * Assigns the set of incoming edges to this stack node. Because of sharing there will always be only one of these sets.
	 */
	public void setIncomingEdges(EdgesSet<P> incomingEdges){
		this.incomingEdges = incomingEdges;
	}

	/**
	 * Adds the given edge to the set of edges for the indicated location.
	 */
	public EdgesSet<P> addEdge(AbstractStackNode<P> edge, int startLocation){
		EdgesSet<P> edges = edgesMap.findValue(startLocation);
		if(edges == null){
			edges = new EdgesSet<>(1);
			edgesMap.add(startLocation, edges);
		}

		edges.add(edge);

		return edges;
	}

	/**
	 * Adds the given set of edges to the edge map for the indicated location.
	 */
	public void addEdges(EdgesSet<P> edges, int startLocation){
		edgesMap.add(startLocation, edges);
	}

	/**
	 * Sets the given edges set for the indicated location and associated the given prefix with this node.
	 *
	 * This method is used by children of expandable nodes;
	 * expandable nodes contain dynamically unfolding alternatives, which can be cyclic.
	 * A 'null' prefix, for example, indicates that the current node starts the alternative.
	 * This may be required in case a stack merges occur at the point where one of these alternatives starts.
	 */
	public void setEdgesSetWithPrefix(EdgesSet<P> edges, Link prefix, int startLocation){
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

		edgesMap.add(startLocation, edges);

		ArrayList<Link> prefixes = prefixesMap[edgesMapSize];
		if(prefixes == null){
			prefixes = new ArrayList<>(1);
			prefixesMap[edgesMapSize] = prefixes;
		}
		prefixes.add(prefix);
	}

	/**
	 * Add the given prefix at the indicated index.
	 * This index is directly linked to a certain set of edges in the edge map.
	 */
	private void addPrefix(Link prefix, int index){
		ArrayList<Link> prefixes = prefixesMap[index];
		if(prefixes == null){
			prefixes = new ArrayList<>(1);
			prefixesMap[index] = prefixes;
		}

		prefixes.add(prefix);
	}

	/**
	 * Updates this node with information from its predecessor in the alternative.
	 * The sets of edges on this predecessor will be transferred to this node and
	 * the required prefixes for this node will be constructed, using the predecessor's
	 * prefixes in combination with its given result.
	 *
	 * This method also takes care of stack merges in the process of doing this.
	 */
	public void updateNode(AbstractStackNode<P> predecessor, AbstractNode predecessorResult){
		IntegerObjectList<EdgesSet<P>> edgesMapToAdd = predecessor.edgesMap;
		ArrayList<Link>[] prefixesMapToAdd = predecessor.prefixesMap;

		if(edgesMap == null){ // Clean node, no stack merge occurred.
			// Initialize the edges map by cloning the one of the predecessor, as we need to transfer all these edges to this node.
			edgesMap = new IntegerObjectList<>(edgesMapToAdd);

			// Initialize the prefixes map.
			prefixesMap = new ArrayList[edgesMap.size()];

			if(prefixesMapToAdd == null){ // The predecessor was the first node in the alternative, so the prefix of this node is just the predecessor's result.
				int index = edgesMap.findKey(predecessor.getStartLocation());
				addPrefix(new Link(null, predecessorResult), index);
			} else { // The predecessor has prefixes.
				int nrOfPrefixes = edgesMapToAdd.size();
				for(int i = nrOfPrefixes - 1; i >= 0; --i){
					ArrayList<Link> prefixes = prefixesMap[i];
					if(prefixes == null){
						prefixes = new ArrayList<>(1);
						prefixesMap[i] = prefixes;
					}

					prefixes.add(new Link(prefixesMapToAdd[i], predecessorResult));
				}
			}
		}else if(edgesMap != edgesMapToAdd){ // A stack merge occurred (and the production is non-cyclic (expandable nodes, such as lists, can have cyclic child alternatives)).
			handleStackMergeForNonCyclicProduction(predecessorResult, edgesMapToAdd, prefixesMapToAdd);
		}else{ // A stack merge occurred and the production is self cyclic (expandable nodes, such as lists, can have cyclic child alternatives).
			handleStackMergeForSelfCyclicProduction(predecessor, predecessorResult, edgesMapToAdd, prefixesMapToAdd);
		}
	}

    private void handleStackMergeForSelfCyclicProduction(AbstractStackNode<P> predecessor,
        AbstractNode predecessorResult, IntegerObjectList<EdgesSet<P>> edgesMapToAdd,
        ArrayList<Link>[] prefixesMapToAdd) {
        if(prefixesMapToAdd == null){
        	int index = edgesMap.findKey(predecessor.getStartLocation());
        	addPrefix(new Link(null, predecessorResult), index);
        }else{
        	int nrOfPrefixes = edgesMapToAdd.size();
        	for(int i = nrOfPrefixes - 1; i >= 0; --i){
        		// Add the prefix to the appropriate prefixes set.
        		prefixesMap[i].add(new Link(prefixesMapToAdd[i], predecessorResult));
        	}
        }
    }

    private void handleStackMergeForNonCyclicProduction(AbstractNode predecessorResult,
        IntegerObjectList<EdgesSet<P>> edgesMapToAdd, ArrayList<Link>[] prefixesMapToAdd) {
        // Initialize the prefixes map (if necessary).
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

        if(prefixesMapToAdd == null){ // The predecessor was the first node in the alternative, so the prefix of this node is just the predecessor's result.
        	addPrefix(new Link(null, predecessorResult), edgesMapSize);
        	edgesMap.add(edgesMapToAdd.getKey(0), edgesMapToAdd.getValue(0));
        }else{ // The predecessor has prefixes.
        	for(int i = edgesMapToAdd.size() - 1; i >= 0; --i){
        		int locationStart = edgesMapToAdd.getKey(i);
        		int index = edgesMap.findKeyBefore(locationStart, edgesMapSize); // Only look where needed.
        		ArrayList<Link> prefixes;
        		if(index == -1){ // No prefix set for the given start location is present yet.
        			index = edgesMap.size();
        			edgesMap.add(locationStart, edgesMapToAdd.getValue(i));

        			prefixes = new ArrayList<>(1);
        			prefixesMap[index] = prefixes;
        		}else{ // A prefix set for the given start location is present.
        			prefixes = prefixesMap[index];
        		}

        		// Add the prefix to the appropriate prefix set.
        		prefixes.add(new Link(prefixesMapToAdd[i], predecessorResult));
        	}
        }
    }

	/**
	 * This method is a specialized version of 'updateNode'.
	 *
	 * In essence its function is the same, with the difference that stack merges are not handled
	 * and the edges map is simply reused.
	 *
	 * The is part of the 'selective edge sharing' optimization.
	 * Since it is guaranteed that stack merges can never occur after non-nullable terminals
	 * in a production it is save to use this assumption to improve efficiency.
	 */
	public void updateNodeAfterNonEmptyMatchable(AbstractStackNode<P> predecessor, AbstractNode result){
		ArrayList<Link>[] prefixesMapToAdd = predecessor.prefixesMap;

		// Set the edges map.
		edgesMap = predecessor.edgesMap;

		// Initialize the prefixes map.
		prefixesMap = new ArrayList[edgesMap.size()];

		if(prefixesMapToAdd == null){ // The predecessor was the first node in the alternative, so the prefix of this node is just the predecessor's result.
			addPrefix(new Link(null, result), edgesMap.findKey(predecessor.getStartLocation()));
		}else{ // The predecessor has prefixes.
			int nrOfPrefixes = edgesMap.size();
			for(int i = nrOfPrefixes - 1; i >= 0; --i){ // Since we reuse the edges map the indexes of the prefixes map will correspond to the same start locations.
				ArrayList<Link> prefixes = new ArrayList<>(1);
				prefixesMap[i] = prefixes;
				// Add the prefix to the appropriate prefixes set.
				prefixes.add(new Link(prefixesMapToAdd[i], result));
			}
		}
	}

	/**
	 * This method is a specialized version of 'updateNode'.
	 *
	 * Specifically, this is a part of the hidden-right-recursion fix
	 * (for non-nullable predecessor nodes).
	 *
	 * It merges stacks and keeps track of which ones get merged
	 * (the returned number indicated how many edge sets were added and
	 * may need to be propagated forward).
	 */
	public int updateOvertakenNode(AbstractStackNode<P> predecessor, AbstractNode result){
		IntegerObjectList<EdgesSet<P>> edgesMapToAdd = predecessor.edgesMap;
		ArrayList<Link>[] prefixesMapToAdd = predecessor.prefixesMap;

		// Initialize the prefixes map.
		int edgesMapSize = edgesMap.size();
		// Before error recovery: int possibleMaxSize = edgesMapSize + edgesMapSize;
		// It is unclear why error recovery can cause more edges to be added than previously accounted for,
		// although this might just have been a bug.
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

		int nrOfAddedEdges = 0;
		if(prefixesMapToAdd == null){ // The predecessor was the first node in the alternative, so the prefix of this node is just the predecessor's result.
			addPrefix(new Link(null, result), edgesMapSize);
			edgesMap.add(predecessor.getStartLocation(), edgesMapToAdd.getValue(0));
			nrOfAddedEdges = 1;
		}else{ // The predecessor has prefixes.
			for(int i = edgesMapToAdd.size() - 1; i >= 0; --i){
				int locationStart = edgesMapToAdd.getKey(i);

				// Prefix not present, add it.
				int index = edgesMap.findKey(locationStart);
				ArrayList<Link> prefixes;
				if(index == -1){ // No prefix set for the given start location is present yet.
					index = edgesMap.size();
					edgesMap.add(locationStart, edgesMapToAdd.getValue(i));

					prefixes = new ArrayList<>(1);
					prefixesMap[index] = prefixes;

					++nrOfAddedEdges;
				}else{ // A prefix set for the given start location is present.
					prefixes = prefixesMap[index];
				}

				// Add the prefix to the prefix set.
				prefixes.add(new Link(prefixesMapToAdd[i], result));
			}
		}

		return nrOfAddedEdges;
	}

	/**
	 * This method is a specialized version of 'updateNode'.
	 *
	 * Specifically, this is a part of the hidden-right-recursion fix
	 * (for nullable predecessor nodes).
	 *
	 * It merges stacks and keeps track of which ones get merged
	 * (the returned number indicated how many edge sets were added and
	 * may need to be propagated forward).
	 * It also prevents possible prefix duplication, which is an artifact
	 * of the parser's implementation.
	 */
	public int updateOvertakenNullableNode(AbstractStackNode<P> predecessor, AbstractNode result, int potentialNewEdges){
		IntegerObjectList<EdgesSet<P>> edgesMapToAdd = predecessor.edgesMap;
		ArrayList<Link>[] prefixesMapToAdd = predecessor.prefixesMap;

		// Initialize the prefixes map.
		int edgesMapSize = edgesMap.size();
		// Before error recovery: int possibleMaxSize = edgesMapSize + potentialNewEdges;
		// It is unclear why error recovery can cause more edges to be added than previously accounted for.
		int possibleMaxSize = edgesMapSize + edgesMapToAdd.size();
		if(prefixesMap == null){
			prefixesMap = new ArrayList[possibleMaxSize];

			propagatedPrefixes = new BitSet(edgesMapSize);
		}else{
			if(prefixesMap.length < possibleMaxSize){
				ArrayList<Link>[] oldPrefixesMap = prefixesMap;
				prefixesMap = new ArrayList[possibleMaxSize];
				System.arraycopy(oldPrefixesMap, 0, prefixesMap, 0, edgesMapSize);
			}

			if(propagatedPrefixes == null){
				propagatedPrefixes = new BitSet(edgesMapSize);
			}else{
				propagatedPrefixes.enlargeTo(possibleMaxSize);
			}
		}

		int nrOfAddedEdges = 0;
		if(prefixesMapToAdd == null){ // The predecessor was the first node in the alternative, so the prefix of this node is just the predecessor's result.
			// A prefix is not yet present, so add it. As it's the first and only possible nullable prefix, it's guaranteed that there aren't any prefixes for the current start location present yet.
			addPrefix(new Link(null, result), edgesMapSize);
			edgesMap.add(predecessor.getStartLocation(), edgesMapToAdd.getValue(0));
			nrOfAddedEdges = 1;
		}else{ // The predecessor has prefixes.
			for(int i = edgesMapToAdd.size() - 1; i >= 0; --i){
				int locationStart = edgesMapToAdd.getKey(i);

				// Prefix not present, add it.
				int index = edgesMap.findKey(locationStart);

				ArrayList<Link> prefixes;
				if(index == -1){ // No prefix set for the given start location is present yet.
					index = edgesMap.size();
					edgesMap.add(locationStart, edgesMapToAdd.getValue(i));
					propagatedPrefixes.set(index);

					prefixes = new ArrayList<>(1);
					prefixesMap[index] = prefixes;

					++nrOfAddedEdges;
				}else{ // A prefix set for the given start location is present.
					if(propagatedPrefixes.isSet(index)) continue; // Prefix present, abort.

					prefixes = prefixesMap[index];
				}

				// Add the prefix to the prefix set.
				prefixes.add(new Link(prefixesMapToAdd[i], result));
			}
		}

		return nrOfAddedEdges;
	}

	/**
	 * This method is a specialized version of 'updateNode'.
	 *
	 * When alternatives have a shared prefix, their successors can shared the same edges and prefixes maps.
	 */
	public void updatePrefixSharedNode(IntegerObjectList<EdgesSet<P>> edgesMap, ArrayList<Link>[] prefixesMap){
		this.edgesMap = edgesMap;
		this.prefixesMap = prefixesMap;
	}

	/**
	 * Returns the edges map.
	 */
	public IntegerObjectList<EdgesSet<P>> getEdges(){
		return edgesMap;
	}

	/**
	 * Returns the incoming edges set.
	 */
	public EdgesSet<P> getIncomingEdges(){
		return incomingEdges;
	}

	/**
	 * Returns the prefixes map.
	 */
	public ArrayList<Link>[] getPrefixesMap(){
		return prefixesMap;
	}

	/**
	 * Returns the list of start location for which the results have already
	 * been propagated (hidden-right-recursion specific).
	 */
	public IntegerList getPropagatedReductions(){
		if(propagatedReductions == null) propagatedReductions = new IntegerList();

		return propagatedReductions;
	}

	public abstract String toShortString();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(id + "." + dot + "@" + startLocation);
		if (production != null) {
			builder.append(",prod=[");
			boolean first = true;
			for (AbstractStackNode<P> prodElem : production) {
				if (first) {
					first = false;
				} else {
					builder.append(",");
				}
				builder.append(prodElem.toShortString());
			}
			builder.append("]");
		}
		if (isEndNode) {
			builder.append(",endNode");
		}
		if (isSeparator) {
			builder.append(",separator");
		}
		if (isLayout) {
			builder.append(",layout");
		}

		if (alternateProductions != null && alternateProductions.length != 0) {
			builder.append(",alternateProductions=" + Arrays.toString(alternateProductions));
		}
		if (prefixesMap != null && prefixesMap.length != 0) {
			builder.append(",prefixes=" + Arrays.toString(prefixesMap));
		}
		if (incomingEdges != null && incomingEdges.size() != 0) {
			builder.append(",incomingEdges=" + incomingEdges);
		}
		if (alternativeProduction != null) {
			builder.append(",alternativeProduction=" + DebugUtil.prodToString((IConstructor)alternativeProduction));
		}
		if (propagatedPrefixes != null) {
			builder.append(",propagatedPrefixes=" + propagatedPrefixes);
		}
		if (propagatedReductions != null) {
			builder.append(",propagatedReductions=" + propagatedReductions);
		}

		// Do not print filters for now.

		return builder.toString();
	}

	public abstract <R> R accept(StackNodeVisitor<P, R> visitor);

	// Matchables.
	/**
	 * Matches the symbol associated with this node to the input at the specified location.
	 * This operation is only available on matchable nodes.
	 */
	public abstract AbstractNode match(int[] input, int location);

	/**
	 * Returns the length of this node (only applies to matchables).
	 */
	public abstract int getLength();

	/**
	 * Returns the result associated with this node (only applies to matchables, after matching).
	 */
	public abstract AbstractNode getResult();

	// Expandables.
	/**
	 * Returns the set of children of this node (only applies to expandables).
	 */
	public abstract AbstractStackNode<P>[] getChildren();

	/**
	 * Returns whether or not this node can be nullable (only applies to expandables).
	 */
	public abstract boolean canBeEmpty();

	/**
	 * Returns the empty child of this node (only applies to expandables that are nullable).
	 */
	public abstract AbstractStackNode<P> getEmptyChild();
}
