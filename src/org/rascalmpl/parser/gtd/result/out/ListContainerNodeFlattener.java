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
package org.rascalmpl.parser.gtd.result.out;

import java.net.URI;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.ExpandableContainerNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.INodeFlattener.CycleMark;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.ForwardLink;
import org.rascalmpl.parser.gtd.util.HashMap;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashSet;

/**
 * A converter for 'expandable' container result nodes.
 * In the case of the UPTR format, this implies variations of lists.
 */
@SuppressWarnings("unchecked")
public class ListContainerNodeFlattener<P, T, S>{
	private final static ForwardLink<AbstractNode> NO_NODES = ForwardLink.TERMINATOR;
	private final static Object[] NO_CHILDREN = new Object[]{};
	
	private final T[] noChildren = (T[]) NO_CHILDREN;
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<Object, T>> preCache;
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashSet<T>> cache;
	
	public ListContainerNodeFlattener(){
		super();

		preCache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<Object, T>>();
		cache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashSet<T>>();
	}
	
	/**
	 * A helper structure for keeping track of cycles inside lists.
	 * These cycles can occur due to nullable elements and separators.
	 */
	protected static class CycleNode extends AbstractNode{
		public final AbstractNode[] cycle;
		
		public CycleNode(AbstractNode[] cycle){
			super();
			
			this.cycle = cycle;
		}
		
		public int getTypeIdentifier(){
			throw new UnsupportedOperationException("CycleNode does not have an ID, it's for internal use only.");
		}
		
		public boolean isEmpty(){
			throw new UnsupportedOperationException();
		}
		
		public boolean isRejected(){
			throw new UnsupportedOperationException();
		}
		
		public boolean isNonterminalSeparator(){
			throw new UnsupportedOperationException();
		}
		
		public void setRejected(){
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * A helper structure for storing shared prefixes of lists.
	 */
	protected static class SharedPrefix<T>{
		public final T[] prefix;
		public final Object environment;
		
		public SharedPrefix(T[] prefix, Object environment){
			super();
			
			this.prefix = prefix;
			this.environment = environment;
		}
	}
	
	/**
	 * Construct the UPTR representation for the given production.
	 * Additionally, it handles all semantic actions related 'events' associated with it.
	 */
	private Object buildAlternative(INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, T[] prefix, ForwardLink<AbstractNode> postFix, Object production, ArrayList<T> gatheredAlternatives, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, int offset, int endOffset, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object environment){
		Object newEnvironment = actionExecutor.enteringListProduction(production, environment); // Fire a 'entering production' event to enable environment handling.
		
		ArrayList<T> children = new ArrayList<T>();
		for(int i = 0; i < prefix.length; ++i){
			children.add(prefix[i]);
		}

		int index = prefix.length - 1;
		
		int postFixLength = postFix.length;
		for(int i = 0; i < postFixLength; ++i){
			AbstractNode node = postFix.element;
			postFix = postFix.next;
			
			newEnvironment = actionExecutor.enteringListNode(production, index++, newEnvironment); // Fire a 'entering node' event when converting a child to enable environment handling.
			
			if(!(node instanceof CycleNode)){ // Not a cycle.
				T constructedNode = converter.convert(nodeConstructorFactory, node, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, newEnvironment);
				if(constructedNode == null){
					actionExecutor.exitedListProduction(production, true, newEnvironment); // Filtered.
					return null;
				}
				
				children.add(constructedNode);
			}else{ // Cycle.
				CycleNode cycleNode = (CycleNode) node;
				T[] constructedCycle = constructCycle(converter, nodeConstructorFactory, production, cycleNode, stack, depth, cycleMark, positionStore, offset, endOffset, filteringTracker, actionExecutor, newEnvironment);
				if(constructedCycle == null){
					actionExecutor.exitedListProduction(production, true, newEnvironment); // Filtered.
					return null;
				}
				
				int constructedCycleLength = constructedCycle.length;
				if(constructedCycleLength == 1){
					children.add(constructedCycle[0]);
				}else{
					for(int j = 0; j < constructedCycleLength; ++j){
						children.add(constructedCycle[j]);
					}
				}
			}
		}
		
		T result = nodeConstructorFactory.createListNode(children, production);
		result = actionExecutor.filterListProduction(result, newEnvironment); // Execute the semantic actions associated with this list.
		if(result == null){
			filteringTracker.setLastFiltered(offset, endOffset);
			actionExecutor.exitedListProduction(production, true, newEnvironment); // Filtered.
			return null;
		}

		actionExecutor.exitedListProduction(production, false, newEnvironment); // Successful construction.
		
		gatheredAlternatives.add(result);
		
		return newEnvironment;
	}
	
	/**
	 * Construct the UPTR representation for the given cycle.
	 */
	private T[] constructCycle(INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, Object production, CycleNode cycleNode, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, int offset, int endOffset, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object environment){
		Object newEnvironment = actionExecutor.enteringListProduction(production, environment); // Fire a 'entering production' event to enable environment handling.
		
		AbstractNode[] cycleElements = cycleNode.cycle;
		
		int nrOfCycleElements = cycleElements.length;
		T[] convertedCycle;
		if(nrOfCycleElements == 1){ // A single element cycle (non-separated list).
			convertedCycle = (T[]) new Object[1];
			
			newEnvironment = actionExecutor.enteringListNode(production, 0, newEnvironment); // Fire a 'entering node' event when converting a child to enable environment handling.
			T element = converter.convert(nodeConstructorFactory, cycleElements[0], stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, newEnvironment);
			if(element == null){
				actionExecutor.exitedListProduction(production, true, newEnvironment); // Filtered.
				return null;
			}
			convertedCycle[0] = element;
		}else{ // A multi element cycle (separated list).
			// The last node in the array is the actual list element.
			// Since cycles aren't allowed to start or end at separators, construct the cycle with the element both at the begin and end.
			convertedCycle = (T[]) new Object[nrOfCycleElements + 1];
			
			newEnvironment = actionExecutor.enteringListNode(production, 0, newEnvironment); // Fire a 'entering node' event when converting a child to enable environment handling.
			convertedCycle[0] = converter.convert(nodeConstructorFactory, cycleElements[nrOfCycleElements - 1], stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, newEnvironment);
			for(int i = 0; i < nrOfCycleElements; ++i){
				newEnvironment = actionExecutor.enteringListNode(production, i + 1, newEnvironment); // Fire a 'entering node' event when converting a child to enable environment handling.
				T element = converter.convert(nodeConstructorFactory, cycleElements[i], stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, newEnvironment);
				if(element == null) {
					actionExecutor.exitedListProduction(production, true, newEnvironment); // Filtered.
					return null;
				}
				convertedCycle[i + 1] = element;
			}
		}
		
		T cycle = nodeConstructorFactory.createSubListCycleNode(production);
		cycle = actionExecutor.filterListCycle(cycle, environment); // Execute the semantic actions associated with the list this cycle belongs to.
		if(cycle == null){
			return convertedCycle;
		}
		
		ArrayList<T> children = new ArrayList<T>();
		int convertedCycleLength = convertedCycle.length;
		for(int i = 0; i < convertedCycleLength; ++i){
			children.add(convertedCycle[i]);
		}
		T elements = nodeConstructorFactory.createListNode(children, production);
		elements = actionExecutor.filterListProduction(elements, newEnvironment); // Execute the semantic actions associated with this list.
		if(elements == null){
			filteringTracker.setLastFiltered(offset, endOffset);
			actionExecutor.exitedListProduction(production, true, newEnvironment); // Filtered.
			return null;
		}
		
		actionExecutor.exitedListProduction(production, false, newEnvironment); // Successful construction.
		
		ArrayList<T> alternatives = new ArrayList<T>();
		alternatives.add(elements);
		alternatives.add(cycle);
		T constructedCycle = nodeConstructorFactory.createSubListAmbiguityNode(alternatives);
		// Execute the semantic actions associated with this ambiguous list.
		constructedCycle = actionExecutor.filterListAmbiguity(constructedCycle, newEnvironment);
		if(constructedCycle == null){
			filteringTracker.setLastFiltered(offset, endOffset);
			return null;
		}
		
		return (T[]) new Object[]{constructedCycle};
	}
	
	/**
	 * Gather all the alternatives ending with the given child.
	 */
	protected void gatherAlternatives(INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, Link child, ArrayList<T> gatheredAlternatives, Object production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, SharedPrefix<T>> sharedPrefixCache, PositionStore positionStore, int offset, int endOffset, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object environment){
		AbstractNode childNode = child.getNode();
		
		if(!(childNode.isEpsilon() && child.getPrefixes() == null)){ // Has non-epsilon results.
			ArrayList<AbstractNode> blackList = new ArrayList<AbstractNode>();
			if(childNode.isEmpty()){ // Child starts a cycle.
				CycleNode cycle = gatherCycle(child, new AbstractNode[]{childNode}, blackList);
				if(cycle != null){ // Encountered a cycle.
					if(cycle.cycle.length == 1){
						gatherProduction(converter, nodeConstructorFactory, child, new ForwardLink<AbstractNode>(NO_NODES, cycle, false), gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, offset, endOffset, filteringTracker, actionExecutor, environment);
					}else{
						ForwardLink<AbstractNode> cycleLink = new ForwardLink<AbstractNode>(NO_NODES, cycle, false);
						gatherProduction(converter, nodeConstructorFactory, child, new ForwardLink<AbstractNode>(cycleLink, childNode, false), gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, offset, endOffset, filteringTracker, actionExecutor, environment);
					}
					return;
				}
			}
			// Encountered non-cyclic child.
			gatherProduction(converter, nodeConstructorFactory, child, new ForwardLink<AbstractNode>(NO_NODES, childNode, false), gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, offset, endOffset, filteringTracker, actionExecutor, environment);
		}else{ // Has a single epsilon result.
			buildAlternative(converter, nodeConstructorFactory, noChildren, NO_NODES, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, offset, endOffset, filteringTracker, actionExecutor, environment);
		}
	}
	
	/**
	 * Gathers all alternatives for the given production related to the given child and postfix.
	 */
	private void gatherProduction(INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, Link child, ForwardLink<AbstractNode> postFix, ArrayList<T> gatheredAlternatives, Object production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, SharedPrefix<T>> sharedPrefixCache, PositionStore positionStore, ArrayList<AbstractNode> blackList, int offset, int endOffset, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object environment){
		do{
			ArrayList<Link> prefixes = child.getPrefixes();
			if(prefixes == null){ // Start of the production encountered.
				buildAlternative(converter, nodeConstructorFactory, noChildren, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, offset, endOffset, filteringTracker, actionExecutor, environment);
				return;
			}
			
			// One prefix, so not ambiguous at this point.
			if(prefixes.size() == 1){
				Link prefix = prefixes.get(0);
				
				if(prefix == null){ // Start of the production encountered.
					buildAlternative(converter, nodeConstructorFactory, noChildren, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, offset, endOffset, filteringTracker, actionExecutor, environment);
					return;
				}
				
				AbstractNode prefixNode = prefix.getNode();
				if(blackList.contains(prefixNode)) return; // Prefix node is not allowed (due to being part of a cycle already gathered cycle).
				
				if(prefixNode.isEmpty() && !prefixNode.isNonterminalSeparator()){ // Possibly a cycle (separators can't start or end cycles, only elements can).
					CycleNode cycle = gatherCycle(prefix, new AbstractNode[]{prefixNode}, blackList);
					if(cycle != null){ // Encountered cycle, insert it.
						prefixNode = cycle;
					}
				}
				
				child = prefix;
				postFix = new ForwardLink<AbstractNode>(postFix, prefixNode, false);
				continue; // Reuse the stack frame for the next iteration (part of the conditional tail-recursion optimization; this is required to prevent stack-overflows when flattening long lists).
			}
			
			// Multiple prefixes, so the list is ambiguous at this point.
			gatherAmbiguousProduction(converter, nodeConstructorFactory, prefixes, postFix, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, offset, endOffset, filteringTracker, actionExecutor, environment);
			
			break;
		}while(true);
	}
	
	/**
	 * Gathers all alternatives for the given ambiguous production related to the given child and postfix.
	 */
	private void gatherAmbiguousProduction(INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, ArrayList<Link> prefixes, ForwardLink<AbstractNode> postFix, ArrayList<T> gatheredAlternatives, Object production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, SharedPrefix<T>> sharedPrefixCache, PositionStore positionStore, ArrayList<AbstractNode> blackList, int offset, int endOffset, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object environment){
		// Check if we've been at this node before. If so reuse the cached prefix.
		SharedPrefix<T> sharedPrefix = sharedPrefixCache.get(prefixes);
		if(sharedPrefix != null){
			T[] cachedPrefix = sharedPrefix.prefix;
			if(cachedPrefix != null){
				buildAlternative(converter, nodeConstructorFactory, cachedPrefix, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, offset, endOffset, filteringTracker, actionExecutor, sharedPrefix.environment);
			}
			
			// Check if there is a null prefix in this node's prefix list (this can happen if this node both start the list and has an empty prefix).
			for(int i = prefixes.size() - 1; i >= 0; --i){
				if(prefixes.get(i) == null){
					buildAlternative(converter, nodeConstructorFactory, noChildren, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, offset, endOffset, filteringTracker, actionExecutor, environment);
				}
			}
			
			return;
		}
		
		// Gather all alternative prefixes.
		ArrayList<T> gatheredPrefixes = new ArrayList<T>();
		
		for(int i = prefixes.size() - 1; i >= 0; --i){
			Link prefix = prefixes.get(i);
			
			if(prefix == null){ // List start node encountered.
				buildAlternative(converter, nodeConstructorFactory, noChildren, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, offset, endOffset, filteringTracker, actionExecutor, environment);
			}else{
				AbstractNode prefixNode = prefix.getNode();
				if(blackList.contains(prefixNode)) continue; // Prefix node is not allowed (due to being part of a cycle already gathered cycle).
				
				if(prefixNode.isEmpty() && !prefixNode.isNonterminalSeparator()){ // Possibly a cycle (separators can't start or end cycles, only elements can).
					CycleNode cycle = gatherCycle(prefix, new AbstractNode[]{prefixNode}, blackList);
					if(cycle != null){ // Encountered a cycle.
						gatherProduction(converter, nodeConstructorFactory, prefix, new ForwardLink<AbstractNode>(NO_NODES, cycle, false), gatheredPrefixes, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, offset, endOffset, filteringTracker, actionExecutor, environment);
						continue;
					}
				}
				
				gatherProduction(converter, nodeConstructorFactory, prefix, new ForwardLink<AbstractNode>(NO_NODES, prefixNode, false), gatheredPrefixes, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, offset, endOffset, filteringTracker, actionExecutor, environment);
			}
		}
		
		int nrOfGatheredPrefixes = gatheredPrefixes.size();
		
		// Non-ambiguous prefix.
		if(nrOfGatheredPrefixes == 1){
			T prefixAlternative = gatheredPrefixes.get(0);
			ArrayList<T> prefixAlternativeChildrenList = nodeConstructorFactory.getChildren(prefixAlternative);
			
			int prefixLength = prefixAlternativeChildrenList.size();
			T[] prefixAlternativeChildren = (T[]) new Object[prefixLength];
			for(int i = prefixLength - 1; i >= 0; --i){
				prefixAlternativeChildren[i] = prefixAlternativeChildrenList.get(i);
			}
			
			Object newEnvironment = buildAlternative(converter, nodeConstructorFactory, prefixAlternativeChildren, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, offset, endOffset, filteringTracker, actionExecutor, environment);
			
			sharedPrefixCache.put(prefixes, new SharedPrefix<T>(newEnvironment != null ? prefixAlternativeChildren : null, newEnvironment));
		}else if(nrOfGatheredPrefixes > 0){ // Ambiguous prefix.
			ArrayList<T> alternatives = new ArrayList<T>();
			for(int i = nrOfGatheredPrefixes - 1; i >= 0; --i){
				T prefixAlternative = gatheredPrefixes.get(i);
				ArrayList<T> prefixAlternativeChildrenList = nodeConstructorFactory.getChildren(prefixAlternative);
				T alternativeSubList = nodeConstructorFactory.createSubListNode(prefixAlternativeChildrenList, production);
				
				alternatives.add(alternativeSubList);
			}
			
			T prefixResult = nodeConstructorFactory.createSubListAmbiguityNode(alternatives);
			prefixResult = actionExecutor.filterListAmbiguity(prefixResult, environment);
			if(prefixResult == null){ // Ambiguous list prefix got filtered, remember this.
				filteringTracker.setLastFiltered(offset, endOffset);
				sharedPrefixCache.put(prefixes, new SharedPrefix<T>(null, null));
				return;
			}
			
			// Splice the elements into the list if a single alternative remained after filtering the ambiguity cluster.
			if(!nodeConstructorFactory.isAmbiguityNode(prefixResult)){
				if(nodeConstructorFactory.getRhs(nodeConstructorFactory.getProductionFromNode(prefixResult)).equals(nodeConstructorFactory.getRhs(production))){
					T filteredAlternative = gatheredPrefixes.get(0);
					ArrayList<T> filteredAlternativeChildrenList = nodeConstructorFactory.getChildren(filteredAlternative);
					
					int prefixLength = filteredAlternativeChildrenList.size();
					T[] filteredAlternativeChildren = (T[]) new Object[prefixLength];
					for(int i = prefixLength - 1; i >= 0; --i){
						filteredAlternativeChildren[i] = filteredAlternativeChildrenList.get(i);
					}
					
					Object newEnvironment = buildAlternative(converter, nodeConstructorFactory, filteredAlternativeChildren, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, offset, endOffset, filteringTracker, actionExecutor, environment);
					
					sharedPrefixCache.put(prefixes, new SharedPrefix<T>(newEnvironment != null ? filteredAlternativeChildren : null, newEnvironment));
					return;
				}
			}
			
			T[] prefixNodes = (T[]) new Object[]{prefixResult};
			
			Object newEnvironment = buildAlternative(converter, nodeConstructorFactory, prefixNodes, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, offset, endOffset, filteringTracker, actionExecutor, environment);
			
			sharedPrefixCache.put(prefixes, new SharedPrefix<T>(newEnvironment != null ? prefixNodes : null, newEnvironment));
		}
	}
	
	/**
	 * Gathers the cycle related to the given child.
	 */
	private CycleNode gatherCycle(Link child, AbstractNode[] postFix, ArrayList<AbstractNode> blackList){
		AbstractNode originNode = child.getNode();
		
		blackList.add(originNode); // Prevent the cycle node from being traversed again.
		
		OUTER : do{
			ArrayList<Link> prefixes = child.getPrefixes();
			if(prefixes == null){ // Encountered the start of the list (so no cycle detected).
				return null;
			}
			
			int nrOfPrefixes = prefixes.size();
			
			for(int i = nrOfPrefixes - 1; i >= 0; --i){
				Link prefix = prefixes.get(i);
				if(prefix == null) continue;
				AbstractNode prefixNode = prefix.getNode();
				
				if(prefixNode == originNode){ // Cycle detected.
					return new CycleNode(postFix);
				}
				
				if(prefixNode.isEmpty()){ // Only empty nodes can be part of a cycle.
					int length = postFix.length;
					AbstractNode[] newPostFix = new AbstractNode[length + 1];
					System.arraycopy(postFix, 0, newPostFix, 1, length);
					newPostFix[0] = prefixNode;
					
					child = prefix;
					postFix = newPostFix;
					continue OUTER;
				}
				// Fall through means no cycle was detected.
			}
			break;
		}while(true);
		
		return null;
	}
	
	/**
	 * Converts the given expandable container result node to the UPTR format.
	 */
	public T convertToUPTR(INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, ExpandableContainerNode<P> node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object environment){
		int offset = node.getOffset();
		int endOffset = node.getEndOffset();
		
		Object rhs = nodeConstructorFactory.getRhs(node.getFirstProduction());
		boolean hasSideEffects = actionExecutor.isImpure(rhs);
		
		if(DefaultNodeFlattener.nodeMemoization && depth <= cycleMark.depth){ // Only check for sharing if we are not currently inside a cycle.
			if(!hasSideEffects){ // If this sort node and its direct and indirect children do not rely on side-effects from semantic actions, check the cache for existing results.
				ObjectIntegerKeyedHashMap<Object, T> levelCache = preCache.get(offset);
				if(levelCache != null){
					T cachedResult = levelCache.get(rhs, endOffset);
					if(cachedResult != null){
						return cachedResult;
					}
				}
			}
		}

		if (depth <= cycleMark.depth) {
			cycleMark.reset();
		}
		
		S sourceLocation = null;
		URI input = node.getInput();
		if(!(node.isLayout() || input == null)){ // Construct a source location annotation if this sort container does not represent a layout non-terminal and if it's available.
			sourceLocation = nodeConstructorFactory.createPositionInformation(input, offset, endOffset, positionStore);
		}
		
		int index = stack.contains(node);
		if(index != -1){ // Cycle detected.
			T cycle = nodeConstructorFactory.createCycleNode(depth - index, node.getFirstProduction());
			cycle = actionExecutor.filterListCycle(cycle, environment);
			if(cycle != null){
				if(sourceLocation != null) cycle = nodeConstructorFactory.addPositionInformation(cycle, sourceLocation);
			}else{
				filteringTracker.setLastFiltered(offset, endOffset);
			}
			
			cycleMark.setMark(index);
			
			return cycle;
		}
		
		int childDepth = depth + 1;
		
		stack.push(node, depth); // Push this node on the stack.
		
		// Gather the alternatives.
		HashMap<ArrayList<Link>, SharedPrefix<T>> sharedPrefixCache = new HashMap<ArrayList<Link>, SharedPrefix<T>>();
		ArrayList<T> gatheredAlternatives = new ArrayList<T>();
		gatherAlternatives(converter, nodeConstructorFactory, node.getFirstAlternative(), gatheredAlternatives, node.getFirstProduction(), stack, childDepth, cycleMark, sharedPrefixCache, positionStore, offset, endOffset, filteringTracker, actionExecutor, environment);
		ArrayList<Link> alternatives = node.getAdditionalAlternatives();
		ArrayList<P> productions = node.getAdditionalProductions();
		if(alternatives != null){
			for(int i = alternatives.size() - 1; i >= 0; --i){
				gatherAlternatives(converter, nodeConstructorFactory, alternatives.get(i), gatheredAlternatives, productions.get(i), stack, childDepth, cycleMark, sharedPrefixCache, positionStore, offset, endOffset, filteringTracker, actionExecutor, environment);
			}
		}
		
		// Construct the resulting tree containing all gathered alternatives.
		T result = null;
		
		int nrOfAlternatives = gatheredAlternatives.size();
		if(nrOfAlternatives == 1){ // Not ambiguous.
			result = gatheredAlternatives.get(0);
			if(sourceLocation != null) result = nodeConstructorFactory.addPositionInformation(result, sourceLocation);
		}else if(nrOfAlternatives > 0){ // Ambiguous.
			for(int i = nrOfAlternatives - 1; i >= 0; --i){
				T alt = gatheredAlternatives.get(i);
				
				if(sourceLocation != null) alt = nodeConstructorFactory.addPositionInformation(alt, sourceLocation);
			}
			
			result = nodeConstructorFactory.createListAmbiguityNode(gatheredAlternatives);
			result = actionExecutor.filterListAmbiguity(result, environment);
			if(result != null){
				if(sourceLocation != null) result = nodeConstructorFactory.addPositionInformation(result, sourceLocation);
			}else{
				filteringTracker.setLastFiltered(offset, endOffset);
			}
		}
		
		stack.dirtyPurge(); // Pop this node off the stack.
		
		if(DefaultNodeFlattener.nodeMemoization && result != null && depth < cycleMark.depth){ // Only share the constructed tree if we are not in a cycle.
			if(!hasSideEffects){ // Cache side-effect free tree.
				ObjectIntegerKeyedHashMap<Object, T> levelCache = preCache.get(offset);
				if(levelCache != null){
					T cachedResult = levelCache.get(rhs, endOffset);
					if(cachedResult != null){
						return cachedResult;
					}
					
					levelCache.putUnsafe(rhs, endOffset, result);
					return result;
				}
				
				levelCache = new ObjectIntegerKeyedHashMap<Object, T>();
				levelCache.putUnsafe(rhs, endOffset, result);
				preCache.put(offset, levelCache);
			}else{ // Cache tree with side-effects.
				ObjectIntegerKeyedHashSet<T> levelCache = cache.get(offset);
				if(levelCache != null){
					T cachedResult = levelCache.getEquivalent(result, endOffset);
					if(cachedResult != null){
						return cachedResult;
					}
					
					levelCache.putUnsafe(result, endOffset);
					return result;
				}
				
				levelCache = new ObjectIntegerKeyedHashSet<T>();
				levelCache.putUnsafe(result, endOffset);
				cache.putUnsafe(offset, levelCache);
			}
		}
		
		return result;
	}
}
