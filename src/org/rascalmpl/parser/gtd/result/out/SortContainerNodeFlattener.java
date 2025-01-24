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
import java.util.IdentityHashMap;
import java.util.Map;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.SortContainerNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.ForwardLink;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashSet;

/**
 * A converter for sort container result nodes.
 */
public class SortContainerNodeFlattener<P, T, S>{
	@SuppressWarnings("unchecked")
	private final static ForwardLink<AbstractNode> NO_NODES = ForwardLink.TERMINATOR;

	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashSet<T>> cache;

	private final Map<AbstractNode, T> nodeCache;

	public SortContainerNodeFlattener(){
		super();
		
		cache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashSet<T>>();

		nodeCache = new IdentityHashMap<>();
	}

	/**
	 * Gather all the alternatives ending with the given child.
	 */
	private void gatherAlternatives(INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, Link child, ArrayList<T> gatheredAlternatives, Object production, IndexedStack<AbstractNode> stack, int depth, PositionStore positionStore, S sourceLocation, int offset, int endOffset, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object environment, boolean hasSideEffects){
		AbstractNode resultNode = child.getNode();

		if(!(resultNode.isEpsilon() && child.getPrefixes() == null)){ // Has non-epsilon results.
			boolean cacheable = child.isCacheable();
			INodeFlattener.CacheMode cacheMode = INodeFlattener.getCacheMode(cacheable, hasSideEffects);
			gatherProduction(converter, nodeConstructorFactory, child, new ForwardLink<>(NO_NODES, resultNode, cacheMode), gatheredAlternatives, production, stack, depth, positionStore, sourceLocation, offset, endOffset, filteringTracker, actionExecutor, environment, cacheable, hasSideEffects);
		}else{ // Has a single epsilon result.
			buildAlternative(converter, nodeConstructorFactory, NO_NODES, gatheredAlternatives, production, stack, depth, positionStore, sourceLocation, offset, endOffset, filteringTracker, actionExecutor, environment);
		}
	}

	/**
	 * Gathers all alternatives for the given production related to the given child and postfix.
	 */
	private void gatherProduction(INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, Link child, ForwardLink<AbstractNode> postFix, ArrayList<T> gatheredAlternatives, Object production, IndexedStack<AbstractNode> stack, int depth, PositionStore positionStore, S sourceLocation, int offset, int endOffset, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object environment, boolean parentCacheable, boolean hasSideEffects){
		ArrayList<Link> prefixes = child.getPrefixes();
		if(prefixes == null){ // Reached the start of the production.
			buildAlternative(converter, nodeConstructorFactory, postFix, gatheredAlternatives, production, stack, depth, positionStore, sourceLocation, offset, endOffset, filteringTracker, actionExecutor, environment);
			return;
		}
		
		for(int i = prefixes.size() - 1; i >= 0; --i){ // Traverse all the prefixes (can be more then one in case of ambiguity).
			Link prefix = prefixes.get(i);
			boolean cacheable = parentCacheable || prefix.isCacheable();
			INodeFlattener.CacheMode cacheMode = INodeFlattener.getCacheMode(cacheable, hasSideEffects);
			gatherProduction(converter, nodeConstructorFactory, prefix, new ForwardLink<>(postFix, prefix.getNode(), cacheMode), gatheredAlternatives, production, stack, depth, positionStore, sourceLocation, offset, endOffset, filteringTracker, actionExecutor, environment, cacheable, hasSideEffects);
		}
	}
	
	/**
	 * Construct the UPTR representation for the given production.
	 * Additionally, it handles all semantic actions related 'events' associated with it.
	 */
	private void buildAlternative(INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, ForwardLink<AbstractNode> postFix, ArrayList<T> gatheredAlternatives, Object production, IndexedStack<AbstractNode> stack, int depth, PositionStore positionStore, S sourceLocation, int offset, int endOffset, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object environment){
		Object newEnvironment = actionExecutor.enteringProduction(production, environment); // Fire a 'entering production' event to enable environment handling.
		
		int postFixLength = postFix.length;
		ArrayList<T> children = new ArrayList<>();
		for(int i = 0; i < postFixLength; ++i){
			AbstractNode node = postFix.element;
			postFix = postFix.next;

			newEnvironment = actionExecutor.enteringNode(production, i, newEnvironment); // Fire a 'entering node' event when converting a child to enable environment handling.

			T constructedNode = converter.convert(nodeConstructorFactory, node, stack, depth, positionStore, filteringTracker, actionExecutor, environment, postFix.cacheMode);

			if(constructedNode == null){
				actionExecutor.exitedProduction(production, true, newEnvironment); // Filtered.
				return;
			}

			children.add(constructedNode);
		}
		
		T result = nodeConstructorFactory.createSortNode(children, production);
		
		if(sourceLocation != null) result = nodeConstructorFactory.addPositionInformation(result, sourceLocation); // Add location information (if available).
		
		result = actionExecutor.filterProduction(result, environment); // Execute the semantic actions associated with this node.
		if(result == null){
			filteringTracker.setLastFiltered(offset, endOffset);
			actionExecutor.exitedProduction(production, true, environment); // Filtered.
			return;
		}

		// TODO: what about if somebody build a tree without a new location?
		
		gatheredAlternatives.add(result);
		actionExecutor.exitedProduction(production, false, environment); // Successful construction.
	}
	
	/**
	 * Converts the given sort container result node to the UPTR format.
	 */
	public T convertToUPTR(INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, SortContainerNode<P> node, IndexedStack<AbstractNode> stack, int depth, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object environment){
		int offset = node.getOffset();
		int endOffset = node.getEndOffset();

		Object firstProduction = node.getFirstProduction();
		Object rhs = nodeConstructorFactory.getRhs(node.getFirstProduction());
		boolean hasSideEffects = actionExecutor.isImpure(rhs);

		S sourceLocation = null;
		URI input = node.getInput();
		if(!(node.isLayout() || input == null)){ // Construct a source location annotation if this sort container does not represent a layout non-terminal and if it's available.
			sourceLocation = nodeConstructorFactory.createPositionInformation(input, offset, endOffset, positionStore);
		}
		
		int index = stack.contains(node);
		if(index != -1){ // Cycle detected.
			T cycle = nodeConstructorFactory.createCycleNode(depth - index, firstProduction);
			cycle = actionExecutor.filterCycle(cycle, environment);
			if (cycle != null) {
				if(sourceLocation != null) cycle = nodeConstructorFactory.addPositionInformation(cycle, sourceLocation);
			} else {
				filteringTracker.setLastFiltered(offset, endOffset);
			}
			
			return cycle;
		}
		
		int childDepth = depth + 1;
		
		stack.push(node, depth); // Push this node on the stack.
		
		// Gather the alternatives.
		ArrayList<T> gatheredAlternatives = new ArrayList<T>();
		gatherAlternatives(converter, nodeConstructorFactory, node.getFirstAlternative(), gatheredAlternatives, firstProduction, stack, childDepth, positionStore, sourceLocation, offset, endOffset, filteringTracker, actionExecutor, environment, hasSideEffects);
		ArrayList<Link> alternatives = node.getAdditionalAlternatives();
		ArrayList<P> productions = node.getAdditionalProductions();
		if(alternatives != null){
			for(int i = alternatives.size() - 1; i >= 0; --i){
				gatherAlternatives(converter, nodeConstructorFactory, alternatives.get(i), gatheredAlternatives, productions.get(i), stack, childDepth, positionStore, sourceLocation, offset, endOffset, filteringTracker, actionExecutor, environment, hasSideEffects);
			}
		}
		
		// Construct the resulting tree containing all gathered alternatives.
		T result = null;
		
		int nrOfAlternatives = gatheredAlternatives.size();
		if (nrOfAlternatives == 1) { // Not ambiguous.
			result = gatheredAlternatives.get(0);
		}
		else if (nrOfAlternatives > 0) { // Ambiguous.
			result = nodeConstructorFactory.createAmbiguityNode(gatheredAlternatives);
			result = actionExecutor.filterAmbiguity(result, environment);
			if(result != null){
				if(sourceLocation != null){
					result = nodeConstructorFactory.addPositionInformation(result, sourceLocation);
				}
			}else{
				filteringTracker.setLastFiltered(offset, endOffset);
			}
		}
		
		stack.dirtyPurge(); // Pop this node off the stack.
		
		/*
		if (hasSideEffects) {
			ObjectIntegerKeyedHashSet<T> levelCache = cache.get(offset);
			if (levelCache != null) {
				T cachedResult = levelCache.getEquivalent(result, endOffset);
				if (cachedResult != null) {
					return cachedResult;
				}

				levelCache.putUnsafe(result, endOffset);
				return result;
			}

			levelCache = new ObjectIntegerKeyedHashSet<T>();
			levelCache.putUnsafe(result, endOffset);
			cache.putUnsafe(offset, levelCache);
		}
		*/
		return result;
	}

}
