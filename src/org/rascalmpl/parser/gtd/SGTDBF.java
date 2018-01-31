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
package org.rascalmpl.parser.gtd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;

import org.rascalmpl.parser.gtd.debug.IDebugListener;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.parser.gtd.exception.UndeclaredNonTerminalException;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.recovery.IRecoverer;
import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.ExpandableContainerNode;
import org.rascalmpl.parser.gtd.result.RecoveredNode;
import org.rascalmpl.parser.gtd.result.SortContainerNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.action.VoidActionExecutor;
import org.rascalmpl.parser.gtd.result.out.FilteringTracker;
import org.rascalmpl.parser.gtd.result.out.INodeConstructorFactory;
import org.rascalmpl.parser.gtd.result.out.INodeFlattener;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.AbstractExpandableStackNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.EpsilonStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.stack.edge.EdgesSet;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.HashMap;
import org.rascalmpl.parser.gtd.util.IntegerKeyedDoubleValueHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;
import org.rascalmpl.parser.gtd.util.Stack;

/**
 * This is the core of the parser; it drives the parse process.
 */
public abstract class SGTDBF<P, T, S> implements IGTD<P, T, S>{
	private final static int DEFAULT_TODOLIST_CAPACITY = 16;
	
	private URI inputURI;
	private int[] input;
	
	private final PositionStore positionStore;
	
	private DoubleStack<AbstractStackNode<P>, AbstractNode>[] todoLists;
	private int queueIndex;
	
	private final Stack<AbstractStackNode<P>> stacksToExpand;
	private final DoubleStack<AbstractStackNode<P>, AbstractContainerNode<P>> stacksWithNonTerminalsToReduce;
	private DoubleStack<AbstractStackNode<P>, AbstractNode> stacksWithTerminalsToReduce;
	
	private final HashMap<String, EdgesSet<P>> cachedEdgesForExpect;
	
	private final IntegerKeyedDoubleValueHashMap<AbstractStackNode<P>, DoubleArrayList<AbstractStackNode<P>, AbstractNode>> sharedNextNodes;
	
	private int location;
	
	protected int lookAheadChar;
	
	private final HashMap<String, AbstractStackNode<P>[]> expectCache;
	
	private final IntegerObjectList<AbstractStackNode<P>> sharedLastExpects;
	
	// Guard
	private boolean invoked;
	
	// Error reporting
	private final Stack<AbstractStackNode<P>> unexpandableNodes;
	private final Stack<AbstractStackNode<P>> unmatchableLeafNodes;
	private final DoubleStack<DoubleArrayList<AbstractStackNode<P>, AbstractNode>, AbstractStackNode<P>> unmatchableMidProductionNodes;
	private final DoubleStack<AbstractStackNode<P>, AbstractNode> filteredNodes;
	
	// Error reporting guards
	private boolean parseErrorOccured;
	
	// Error recovery
	private IRecoverer<P> recoverer;
	
	// Debugging
	private IDebugListener<P> debugListener;
	
	// Temporary instrumentation for accurate profiling
	private long timestamp;
	private boolean printTimes = false;
	
	public SGTDBF(){
		super();
		
		positionStore = new PositionStore();
		
		stacksToExpand = new Stack<AbstractStackNode<P>>();
		stacksWithNonTerminalsToReduce = new DoubleStack<AbstractStackNode<P>, AbstractContainerNode<P>>();
		
		cachedEdgesForExpect = new HashMap<String, EdgesSet<P>>();
		
		sharedNextNodes = new IntegerKeyedDoubleValueHashMap<AbstractStackNode<P>, DoubleArrayList<AbstractStackNode<P>, AbstractNode>>();
		
		location = 0;
		
		expectCache = new HashMap<String, AbstractStackNode<P>[]>();
		
		sharedLastExpects = new IntegerObjectList<AbstractStackNode<P>>();
		
		unexpandableNodes = new Stack<AbstractStackNode<P>>();
		unmatchableLeafNodes = new Stack<AbstractStackNode<P>>();
		unmatchableMidProductionNodes = new DoubleStack<DoubleArrayList<AbstractStackNode<P>, AbstractNode>, AbstractStackNode<P>>();
		filteredNodes = new DoubleStack<AbstractStackNode<P>, AbstractNode>();
	}
	
	/**
	 * Triggers the gathering of alternatives for the given non-terminal.
	 */
	@SuppressWarnings("unchecked")
	protected AbstractStackNode<P>[] invokeExpects(AbstractStackNode<P> nonTerminal){
		String name = nonTerminal.getName();
		AbstractStackNode<P>[] expects = expectCache.get(name);
		if(expects == null){
			try{
				Method method = getClass().getMethod(name);
				try{
					method.setAccessible(true); // Try to bypass the 'isAccessible' check to save time.
				}catch(SecurityException sex){
					// Ignore this if it happens.
				}
				
				expects = (AbstractStackNode<P>[]) method.invoke(this);
			}catch(NoSuchMethodException nsmex){
				throw new UndeclaredNonTerminalException(name, getClass());
			}catch(IllegalAccessException iaex){
				throw new RuntimeException(iaex);
			}catch(InvocationTargetException itex){
				throw new RuntimeException(itex.getTargetException());
			}
			
			expectCache.putUnsafe(name, expects);
		}
		
		 return expects;
	}
	
	/**
	 * Moves to the next symbol in the production.
	 */
	private AbstractStackNode<P> updateNextNode(AbstractStackNode<P> next, AbstractStackNode<P> node, AbstractNode result){
		IntegerKeyedDoubleValueHashMap.Entry<AbstractStackNode<P>, DoubleArrayList<AbstractStackNode<P>, AbstractNode>> alternativeEntry = sharedNextNodes.get(next.getId());
		if(alternativeEntry != null){ // Sharing check.
			DoubleArrayList<AbstractStackNode<P>, AbstractNode> predecessors = alternativeEntry.value2;
			if(predecessors != null){
				predecessors.add(node, result);
				return null;
			}
			
			AbstractStackNode<P> alternative = alternativeEntry.value1;
			if(alternative.getStartLocation() == location){
				if(alternative.isMatchable()){
					if(alternative.isEmptyLeafNode()){
						// Encountered a possible stack 'overtake'.
						if(node.getStartLocation() != location){
							propagateEdgesAndPrefixes(node, result, alternative, alternative.getResult());
						}else{
							propagateEdgesAndPrefixesForNullable(node, result, alternative, alternative.getResult(), node.getEdges().size());
						}
						return alternative;
					}
				}else{
					EdgesSet<P> alternativeEdgesSet = alternative.getIncomingEdges();
					int resultStoreId = getResultStoreId(alternative.getId());
					if(alternativeEdgesSet != null && alternativeEdgesSet.getLastVisitedLevel(resultStoreId) == location){
						// Encountered a possible stack 'overtake'.
						if(node.getStartLocation() != location){
							propagateEdgesAndPrefixes(node, result, alternative, alternativeEdgesSet.getLastResult(resultStoreId));
						}else{
							propagateEdgesAndPrefixesForNullable(node, result, alternative, alternativeEdgesSet.getLastResult(resultStoreId), node.getEdges().size());
						}
						return alternative;
					}
				}
			}
			
			alternative.updateNode(node, result);
			
			if(debugListener != null) debugListener.progressed(node, result, alternative);
			
			return alternative;
		}
		
		if(next.isMatchable()){ // Eager matching optimization.
			if((location + next.getLength()) > input.length) return null;
			
			AbstractNode nextResult = next.match(input, location);
			if(nextResult == null){
				// Push the node including it's predecessor to the appropriate error tracking collection (and take care of merging when necessary).
				DoubleArrayList<AbstractStackNode<P>, AbstractNode> predecessors = new DoubleArrayList<AbstractStackNode<P>, AbstractNode>();
				predecessors.add(node, result);
				unmatchableMidProductionNodes.push(predecessors, next);
				
				sharedNextNodes.putUnsafe(next.getId(), next, predecessors);
				
				if(debugListener != null) debugListener.failedToMatch(next);
				
				return null;
			}
			
			if(debugListener != null) debugListener.matched(next, nextResult);
			
			next = next.getCleanCopyWithResult(location, nextResult);
		}else{
			next = next.getCleanCopy(location);
		}
		
		if(!node.isMatchable() || result.isEmpty()){
			next.updateNode(node, result);
		}else{ // Non-nullable terminal specific edge set sharing optimization.
			next.updateNodeAfterNonEmptyMatchable(node, result);
		}
		
		if(debugListener != null) debugListener.progressed(node, result, next);
		
		sharedNextNodes.putUnsafe(next.getId(), next, null);
		stacksToExpand.push(next);
		
		return next;
	}
	
	/**
	 * Moves to the next symbol in an alternative continuation of a prefix-shared production.
	 */
	private boolean updateAlternativeNextNode(AbstractStackNode<P> next, AbstractStackNode<P> node, AbstractNode result, IntegerObjectList<EdgesSet<P>> edgesMap, ArrayList<Link>[] prefixesMap){
		int id = next.getId();
		IntegerKeyedDoubleValueHashMap.Entry<AbstractStackNode<P>, DoubleArrayList<AbstractStackNode<P>, AbstractNode>> alternativeEntry = sharedNextNodes.get(id);
		if(alternativeEntry != null){ // Sharing check.
			DoubleArrayList<AbstractStackNode<P>, AbstractNode> predecessors = alternativeEntry.value2;
			if(predecessors != null){
				predecessors.add(node, result);
				return false;
			}
			
			AbstractStackNode<P> alternative = alternativeEntry.value1;
			if(result.isEmpty()){
				if(alternative.isMatchable()){
					if(alternative.isEmptyLeafNode()){
						// Encountered a possible stack 'overtake'.
						propagateAlternativeEdgesAndPrefixes(node, result, alternative, alternative.getResult(), node.getEdges().size(), edgesMap, prefixesMap);
						return true;
					}
				}else{
					EdgesSet<P> alternativeEdgesSet = alternative.getIncomingEdges();
					int resultStoreId = getResultStoreId(alternative.getId());
					if(alternativeEdgesSet != null && alternativeEdgesSet.getLastVisitedLevel(resultStoreId) == location){
						AbstractContainerNode<P> nextResult = alternativeEdgesSet.getLastResult(resultStoreId);
						// Encountered a possible stack 'overtake'.
						propagateAlternativeEdgesAndPrefixes(node, result, alternative, nextResult, node.getEdges().size(), edgesMap, prefixesMap);
						return true;
					}
				}
			}
			
			alternative.updatePrefixSharedNode(edgesMap, prefixesMap); // Prevent unnecessary overhead; share whenever possible.
			
			if(debugListener != null) debugListener.progressed(node, result, alternative);
			
			return true;
		}
		
		if(next.isMatchable()){ // Eager matching optimization.
			if((location + next.getLength()) > input.length) return false;
			
			AbstractNode nextResult = next.match(input, location);
			if(nextResult == null){
				// Push the node including it's predecessor to the appropriate error tracking collection (and take care of merging when necessary).
				DoubleArrayList<AbstractStackNode<P>, AbstractNode> predecessors = new DoubleArrayList<AbstractStackNode<P>, AbstractNode>();
				predecessors.add(node, result);
				unmatchableMidProductionNodes.push(predecessors, next);
				
				sharedNextNodes.putUnsafe(id, next, predecessors);
				
				if(debugListener != null) debugListener.failedToMatch(next);
				
				return false;
			}
			
			if(debugListener != null) debugListener.matched(next, nextResult);
			
			next = next.getCleanCopyWithResult(location, nextResult);
		}else{
			next = next.getCleanCopy(location);
		}
		
		next.updatePrefixSharedNode(edgesMap, prefixesMap); // Prevent unnecessary overhead; share whenever possible.

		if(debugListener != null) debugListener.progressed(node, result, next);
		
		sharedNextNodes.putUnsafe(id, next, null);
		stacksToExpand.push(next);
		
		return true;
	}
	
	/**
	 * Part of the hidden-right-recursion fix.
	 * Executes absent reductions.
	 */
	private void propagateReductions(AbstractStackNode<P> node, AbstractNode nodeResultStore, AbstractStackNode<P> next, AbstractNode nextResultStore, int potentialNewEdges){
		IntegerList propagatedReductions = next.getPropagatedReductions();
		
		IntegerObjectList<EdgesSet<P>> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixes = node.getPrefixesMap();
		
		P production = next.getParentProduction();
		String name = edgesMap.getValue(0).get(0).getName();
		
		boolean hasNestingRestrictions = hasNestingRestrictions(name);
		IntegerList filteredParents = null;
		if(hasNestingRestrictions){
			filteredParents = getFilteredParents(next.getId());
		}
		
		int fromIndex = edgesMap.size() - potentialNewEdges;
		for(int i = edgesMap.size() - 1; i >= fromIndex; --i){
			int startLocation = edgesMap.getKey(i);
			
			// We know we haven't been here before.
			propagatedReductions.add(startLocation);
			
			ArrayList<Link> edgePrefixes = new ArrayList<Link>();
			Link prefix = (prefixes != null) ? new Link(prefixes[i], nodeResultStore) : new Link(null, nodeResultStore);
			edgePrefixes.add(prefix);
			
			Link resultLink = new Link(edgePrefixes, nextResultStore);
			
			EdgesSet<P> edgeSet = edgesMap.getValue(i);
			
			if(debugListener != null) debugListener.reducing(node, resultLink, edgeSet);
			
			if(!hasNestingRestrictions){
				handleEdgeList(edgeSet, name, production, resultLink, startLocation);
			}else{
				handleEdgeListWithRestrictions(edgeSet, name, production, resultLink, startLocation, filteredParents);
			}
		}
	}
	
	/**
	 * Part of the hidden-right-recursion fix.
	 * Propagates absent prefixes.
	 */
	private void propagatePrefixes(AbstractStackNode<P> next, AbstractNode nextResult, int nrOfAddedEdges){
		// Proceed with the tail of the production.
		int nextDot = next.getDot() + 1;
		AbstractStackNode<P>[] prod = next.getProduction();
		AbstractStackNode<P> nextNext = prod[nextDot];
		IntegerKeyedDoubleValueHashMap.Entry<AbstractStackNode<P>, DoubleArrayList<AbstractStackNode<P>, AbstractNode>> nextNextAlternativeEntry = sharedNextNodes.get(nextNext.getId());
		AbstractStackNode<P> nextNextAlternative = null;
		if(nextNextAlternativeEntry != null){ // Valid continuation.
			DoubleArrayList<AbstractStackNode<P>, AbstractNode> predecessors = nextNextAlternativeEntry.value2;
			if(predecessors == null){
				nextNextAlternative = nextNextAlternativeEntry.value1;
				if(nextNextAlternative.isMatchable()){
					if(nextNextAlternative.isEmptyLeafNode()){
						propagateEdgesAndPrefixesForNullable(next, nextResult, nextNextAlternative, nextNextAlternative.getResult(), nrOfAddedEdges);
					}else{
						nextNextAlternative.updateNode(next, nextResult);
						
						if(debugListener != null) debugListener.propagated(next, nextResult, nextNextAlternative);
					}
				}else{
					EdgesSet<P> nextNextAlternativeEdgesSet = nextNextAlternative.getIncomingEdges();
					int resultStoreId = getResultStoreId(nextNextAlternative.getId());
					if(nextNextAlternativeEdgesSet != null && nextNextAlternativeEdgesSet.getLastVisitedLevel(resultStoreId) == location){
						propagateEdgesAndPrefixesForNullable(next, nextResult, nextNextAlternative, nextNextAlternativeEdgesSet.getLastResult(resultStoreId), nrOfAddedEdges);
					}else{
						nextNextAlternative.updateNode(next, nextResult);
						
						if(debugListener != null) debugListener.propagated(next, nextResult, nextNextAlternative);
					}
				}
			}else{
				predecessors.add(next, nextResult);
			}
		}
		
		// Handle alternative continuations (related to prefix sharing).
		AbstractStackNode<P>[][] alternateProds = next.getAlternateProductions();
		if(alternateProds != null){
			if(nextNextAlternative == null){ // If the first continuation has not been initialized yet (it may be a matchable that didn't match), create a dummy version to construct the necessary edges and prefixes.
				if(!nextNext.isMatchable()) return; // Matchable, abort.
				nextNextAlternative = nextNext.getCleanCopy(location);
				nextNextAlternative.updateNode(next, nextResult);
				
				if(debugListener != null) debugListener.propagated(next, nextResult, nextNextAlternative);
			}
			
			IntegerObjectList<EdgesSet<P>> nextNextEdgesMap = nextNextAlternative.getEdges();
			ArrayList<Link>[] nextNextPrefixesMap = nextNextAlternative.getPrefixesMap();
			
			for(int i = alternateProds.length - 1; i >= 0; --i){
				prod = alternateProds[i];
				if(nextDot == prod.length) continue;
				AbstractStackNode<P> alternativeNextNext = prod[nextDot];
				
				IntegerKeyedDoubleValueHashMap.Entry<AbstractStackNode<P>, DoubleArrayList<AbstractStackNode<P>, AbstractNode>> nextNextAltAlternativeEntry = sharedNextNodes.get(alternativeNextNext.getId());
				AbstractStackNode<P> nextNextAltAlternative = null;
				if(nextNextAltAlternativeEntry != null){
					DoubleArrayList<AbstractStackNode<P>, AbstractNode> predecessors = nextNextAltAlternativeEntry.value2;
					if(predecessors == null){
						nextNextAltAlternative = nextNextAltAlternativeEntry.value1;
						if(nextNextAltAlternative.isMatchable()){
							if(nextNextAltAlternative.isEmptyLeafNode()){
								propagateAlternativeEdgesAndPrefixes(next, nextResult, nextNextAltAlternative, nextNextAltAlternative.getResult(), nrOfAddedEdges, nextNextEdgesMap, nextNextPrefixesMap);
							}else{
								nextNextAltAlternative.updatePrefixSharedNode(nextNextEdgesMap, nextNextPrefixesMap);
								
								if(debugListener != null) debugListener.propagated(next, nextResult, nextNextAltAlternative);
							}
						}else{
							EdgesSet<P> nextAlternativeEdgesSet = nextNextAlternative.getIncomingEdges();
							int resultStoreId = getResultStoreId(nextNextAltAlternative.getId());
							if(nextAlternativeEdgesSet != null && nextAlternativeEdgesSet.getLastVisitedLevel(resultStoreId) == location){
								propagateAlternativeEdgesAndPrefixes(next, nextResult, nextNextAltAlternative, nextAlternativeEdgesSet.getLastResult(resultStoreId), nrOfAddedEdges, nextNextEdgesMap, nextNextPrefixesMap);
							}else{
								nextNextAltAlternative.updatePrefixSharedNode(nextNextEdgesMap, nextNextPrefixesMap);
								
								if(debugListener != null) debugListener.propagated(next, nextResult, nextNextAltAlternative);
							}
						}
					}else{
						predecessors.add(next, nextResult);
					}
				}
			}
		}
	}
	
	/**
	 * Part of the hidden-right-recursion fix.
	 * Inserts missing prefixes and triggers reductions where necessary.
	 */
	private void propagateEdgesAndPrefixes(AbstractStackNode<P> node, AbstractNode nodeResult, AbstractStackNode<P> next, AbstractNode nextResult){
		int nrOfAddedEdges = next.updateOvertakenNode(node, nodeResult);

		if(debugListener != null) debugListener.propagated(node, nodeResult, next);
		
		if(nrOfAddedEdges == 0) return;
		
		if(next.isEndNode()){
			propagateReductions(node, nodeResult, next, nextResult, nrOfAddedEdges);
		}
		
		if(next.hasNext()){
			propagatePrefixes(next, nextResult, nrOfAddedEdges);
		}
	}
	
	/**
	 * Part of the hidden-right-recursion fix.
	 * Inserts missing prefixes and triggers reductions where necessary (specific for nullable nodes).
	 */
	private void propagateEdgesAndPrefixesForNullable(AbstractStackNode<P> node, AbstractNode nodeResult, AbstractStackNode<P> next, AbstractNode nextResult, int potentialNewEdges){
		int nrOfAddedEdges = next.updateOvertakenNullableNode(node, nodeResult, potentialNewEdges);

		if(debugListener != null) debugListener.propagated(node, nodeResult, next);
		
		if(nrOfAddedEdges == 0) return;
		
		if(next.isEndNode()){
			propagateReductions(node, nodeResult, next, nextResult, nrOfAddedEdges);
		}
		
		if(next.hasNext()){
			propagatePrefixes(next, nextResult, nrOfAddedEdges);
		}
	}
	
	/**
	 * Part of the hidden-right-recursion fix.
	 * Inserts missing prefixes and triggers reductions where necessary (specifically for alternative continuations of prefix-shared productions).
	 */
	private void propagateAlternativeEdgesAndPrefixes(AbstractStackNode<P> node, AbstractNode nodeResult, AbstractStackNode<P> next, AbstractNode nextResult, int potentialNewEdges, IntegerObjectList<EdgesSet<P>> edgesMap, ArrayList<Link>[] prefixesMap){
		next.updatePrefixSharedNode(edgesMap, prefixesMap);
		
		if(debugListener != null) debugListener.propagated(node, nodeResult, next);
		
		if(potentialNewEdges == 0) return;
		
		if(next.isEndNode()){
			propagateReductions(node, nodeResult, next, nextResult, potentialNewEdges);
		}
		
		if(next.hasNext()){
			propagatePrefixes(next, nextResult, potentialNewEdges);
		}
	}
	
	/**
	 * Initiates the handling of reductions.
	 */
	private void updateEdges(AbstractStackNode<P> node, AbstractNode result){
		IntegerObjectList<EdgesSet<P>> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		P production = node.getParentProduction();
		String name = edgesMap.getValue(0).get(0).getName();
		
		// Check for nesting restrictions.
		boolean hasNestingRestrictions = hasNestingRestrictions(name);
		IntegerList filteredParents = null;
		if(hasNestingRestrictions){
			filteredParents = getFilteredParents(node.getId());
		}
		
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			Link resultLink = new Link((prefixesMap != null) ? prefixesMap[i] : null, result);
			
			EdgesSet<P> edgeSet = edgesMap.getValue(i);
			
			if(debugListener != null) debugListener.reducing(node, resultLink, edgeSet);
			
			if(!hasNestingRestrictions){ // Select the optimized path for handling edge sets that don't have nesting restrictions associated with them.
				handleEdgeList(edgeSet, name, production, resultLink, edgesMap.getKey(i));
			}else{
				handleEdgeListWithRestrictions(edgeSet, name, production, resultLink, edgesMap.getKey(i), filteredParents);
			}
		}
	}
	
	/**
	 * Initiates the handling of reductions for nullable symbols.
	 */
	private void updateNullableEdges(AbstractStackNode<P> node, AbstractNode result){
		IntegerList propagatedReductions = node.getPropagatedReductions();
		
		int initialSize = propagatedReductions.size();
		
		IntegerObjectList<EdgesSet<P>> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		P production = node.getParentProduction();
		String name = edgesMap.getValue(0).get(0).getName();
		
		// Check for nesting restrictions.
		boolean hasNestingRestrictions = hasNestingRestrictions(name);
		IntegerList filteredParents = null;
		if(hasNestingRestrictions){
			filteredParents = getFilteredParents(node.getId());
		}
		
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			int startLocation = edgesMap.getKey(i);
			
			if(propagatedReductions.containsBefore(startLocation, initialSize)) continue; // Prevent duplicate reductions (artifact of the hidden-right-recursion fix).
			propagatedReductions.add(startLocation);
			
			Link resultLink = new Link((prefixesMap != null) ? prefixesMap[i] : null, result);
			
			EdgesSet<P> edgeSet = edgesMap.getValue(i);
			
			if(debugListener != null) debugListener.reducing(node, resultLink, edgeSet);
			
			if(!hasNestingRestrictions){ // Select the optimized path for handling edge sets that don't have nesting restrictions associated with them.
				handleEdgeList(edgeSet, name, production, resultLink, startLocation);
			}else{
				handleEdgeListWithRestrictions(edgeSet, name, production, resultLink, startLocation, filteredParents);
			}
		}
	}
	
	/**
	 * Handles reductions.
	 */
	private void handleEdgeList(EdgesSet<P> edgeSet, String name, P production, Link resultLink, int startLocation){
		AbstractContainerNode<P> resultStore = null;
		int resultStoreId = EdgesSet.DEFAULT_RESULT_STORE_ID;
		if(edgeSet.getLastVisitedLevel(resultStoreId) != location){
			AbstractStackNode<P> edge = edgeSet.get(0);
			
			if(edge.isRecovered()){
				resultStore = new RecoveredNode<P>(inputURI, startLocation, location);
			}else if(edge.isExpandable()){
				resultStore = new ExpandableContainerNode<P>(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout());
			}else{
				resultStore = new SortContainerNode<P>(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout());
			}
			
			stacksWithNonTerminalsToReduce.push(edge, resultStore);
			
			if(debugListener != null) debugListener.reduced(edge);
			
			for(int j = edgeSet.size() - 1; j >= 1; --j){
				edge = edgeSet.get(j);
				stacksWithNonTerminalsToReduce.push(edge, resultStore);
				
				if(debugListener != null) debugListener.reduced(edge);
			}
		
			edgeSet.setLastVisitedLevel(location, resultStoreId);
			edgeSet.setLastResult(resultStore, resultStoreId);
		}else{
			resultStore = edgeSet.getLastResult(resultStoreId);
		}
		
		resultStore.addAlternative(production, resultLink);
	}
	
	// Reuse these structures.
	private final IntegerList firstTimeRegistration = new IntegerList();
	private final IntegerList firstTimeReductions = new IntegerList();

	/**
	 * Handles reductions which may be associated with nesting restrictions.
	 */
	private void handleEdgeListWithRestrictions(EdgesSet<P> edgeSet, String name, P production, Link resultLink, int startLocation, IntegerList filteredParents){
		// Only add the result to each resultstore once.
		// Make sure each edge only gets added to the non-terminal reduction list once per level, by keeping track of them.
		firstTimeRegistration.clear();
		firstTimeReductions.clear();
		for(int j = edgeSet.size() - 1; j >= 0; --j){
			AbstractStackNode<P> edge = edgeSet.get(j);
			int resultStoreId = getResultStoreId(edge.getId());
			
			if(!firstTimeReductions.contains(resultStoreId)){
				if(firstTimeRegistration.contains(resultStoreId)){
					if(debugListener != null) debugListener.filteredByNestingRestriction(edge);
					
					continue;
				}
				firstTimeRegistration.add(resultStoreId);
				
				// Check whether or not the nesting is allowed.
				if(filteredParents == null || !filteredParents.contains(edge.getId())){
					AbstractContainerNode<P> resultStore = null;
					if(edgeSet.getLastVisitedLevel(resultStoreId) == location){
						resultStore = edgeSet.getLastResult(resultStoreId);
					}
					if(resultStore == null){
						if (edge.isRecovered()) {
							resultStore = new RecoveredNode<P>(inputURI, startLocation, location);
						}else if (edge.isExpandable()) {
							resultStore = new ExpandableContainerNode<P>(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout());
						}else {
							resultStore = new SortContainerNode<P>(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout());
						}
						
						edgeSet.setLastVisitedLevel(location, resultStoreId);
						edgeSet.setLastResult(resultStore, resultStoreId);
						
						stacksWithNonTerminalsToReduce.push(edge, resultStore);
						firstTimeReductions.add(resultStoreId);
					}
					
					resultStore.addAlternative(production, resultLink);
					
					if(debugListener != null) debugListener.reduced(edge);
				}else{
					if(debugListener != null) debugListener.filteredByNestingRestriction(edge);
				}
			}else{
				AbstractContainerNode<P> resultStore = edgeSet.getLastResult(resultStoreId);
				stacksWithNonTerminalsToReduce.push(edge, resultStore);
			}
		}
	}
	
	/**
	 * Move to the next symbol(s) in the production.
	 */
	private void moveToNext(AbstractStackNode<P> node, AbstractNode result){
		int nextDot = node.getDot() + 1;

		AbstractStackNode<P>[] prod = node.getProduction();
		AbstractStackNode<P> newNext = prod[nextDot];
		AbstractStackNode<P> next = updateNextNode(newNext, node, result);
		
		// Handle alternative continuations of this production (related to prefix-sharing).
		AbstractStackNode<P>[][] alternateProds = node.getAlternateProductions();
		if(alternateProds != null){
			IntegerObjectList<EdgesSet<P>> edgesMap = null;
			ArrayList<Link>[] prefixesMap = null;
			if(next != null){
				edgesMap = next.getEdges();
				prefixesMap = next.getPrefixesMap();
			}
			
			for(int i = alternateProds.length - 1; i >= 0; --i){
				prod = alternateProds[i];
				if(nextDot == prod.length) continue;
				AbstractStackNode<P> newAlternativeNext = prod[nextDot];
				
				if(edgesMap != null){
					updateAlternativeNextNode(newAlternativeNext, node, result, edgesMap, prefixesMap);
				}else{
					AbstractStackNode<P> alternativeNext = updateNextNode(newAlternativeNext, node, result);
					
					if(alternativeNext != null){
						edgesMap = alternativeNext.getEdges();
						prefixesMap = alternativeNext.getPrefixesMap();
					}
				}
			}
		}
	}
	
	/**
	 * Progress to the next 'states' associated with the given node.
	 * I.e. move to the next symbol(s) in the production (if available) and executed reductions if necessary.
	 */
	private void move(AbstractStackNode<P> node, AbstractNode result){
		if(debugListener != null) debugListener.moving(node, result);
		
		// Handle filtering.
		ICompletionFilter[] completionFilters = node.getCompletionFilters();
		if(completionFilters != null){
			int startLocation = node.getStartLocation();
			for(int i = completionFilters.length - 1; i >= 0; --i){
				if(completionFilters[i].isFiltered(input, startLocation, location, positionStore)){
					filteredNodes.push(node, result);
					
					if(debugListener != null) debugListener.filteredByCompletionFilter(node, result);
					
					return;
				}
			}
		}
		
		if(node.isEndNode()){
			if(!result.isEmpty() || node.getId() == AbstractExpandableStackNode.DEFAULT_LIST_EPSILON_ID){ // Only go into the nullable fix path for nullables (special list epsilons can be ignored as well).
				updateEdges(node, result);
			}else{
				updateNullableEdges(node, result);
			}
		}
		
		if(node.hasNext()){
			moveToNext(node, result);
		}
	}
	
	/**
	 * Initiate the handling of stacks.
	 */
	private void reduce(){
		// Reduce terminals
		while(!stacksWithTerminalsToReduce.isEmpty()){
			move(stacksWithTerminalsToReduce.peekFirst(), stacksWithTerminalsToReduce.popSecond());
		}
		
		// Reduce non-terminals
		while(!stacksWithNonTerminalsToReduce.isEmpty()){
			move(stacksWithNonTerminalsToReduce.peekFirst(), stacksWithNonTerminalsToReduce.popSecond());
		}
	}
	
	/**
	 * Locates the initial set of stacks that is queued for handling, for which the least amount of characters needs to be shifted.
	 */
	private boolean findFirstStacksToReduce(){
		for(int i = 0; i < todoLists.length; ++i){
			DoubleStack<AbstractStackNode<P>, AbstractNode> terminalsTodo = todoLists[i];
			if(!(terminalsTodo == null || terminalsTodo.isEmpty())){
				stacksWithTerminalsToReduce = terminalsTodo;
				
				location += i;
				
				queueIndex = i;
				
				return true;
			}
		}
		
		if (recoverer != null) {
			DoubleArrayList<AbstractStackNode<P>, AbstractNode> recoveredNodes = recoverer.reviveStacks(input, location, unexpandableNodes, unmatchableLeafNodes, unmatchableMidProductionNodes, filteredNodes);
			if (recoveredNodes.size() > 0) { // TODO Do something with the revived node. Is this the right location to do this?
				for (int i = 0; i < recoveredNodes.size(); i++) {
					AbstractStackNode<P> recovered = recoveredNodes.getFirst(i);
					addTodo(recovered, recovered.getLength(), recoveredNodes.getSecond(i));
				}
				return findStacksToReduce();
			}
			
			parseErrorOccured = true;
		}
		
		return false;
	}
	
	/**
	 * Locates the set of stacks that is queued for handling, for which the least amount of characters needs to be shifted.
	 */
	private boolean findStacksToReduce(){
		int queueDepth = todoLists.length;
		for(int i = 1; i < queueDepth; ++i){
			queueIndex = (queueIndex + 1) % queueDepth;
			
			DoubleStack<AbstractStackNode<P>, AbstractNode> terminalsTodo = todoLists[queueIndex];
			if(!(terminalsTodo == null || terminalsTodo.isEmpty())){
				stacksWithTerminalsToReduce = terminalsTodo;
				
				location += i;
				
				return true;
			}
		}
		
		if (recoverer != null) {
			DoubleArrayList<AbstractStackNode<P>, AbstractNode> recoveredNodes = recoverer.reviveStacks(input, location, unexpandableNodes, unmatchableLeafNodes, unmatchableMidProductionNodes, filteredNodes);
			if (recoveredNodes.size() > 0) {
				for (int i = 0; i < recoveredNodes.size(); i++) {
					AbstractStackNode<P> recovered = recoveredNodes.getFirst(i);
					
					int levelsFromHere = recovered.getLength() - (location - recovered.getStartLocation());
					
					addTodo(recovered, levelsFromHere, recoveredNodes.getSecond(i));
				}
				return findStacksToReduce();
			}
			
			parseErrorOccured = true;
		}
		
		return false;
	}
	
	public boolean parseErrorHasOccurred(){
		return parseErrorOccured;
	}
	
	/**
	 * Inserts a stack bottom into the todo-list.
	 */
	@SuppressWarnings("unchecked")
	private void addTodo(AbstractStackNode<P> node, int length, AbstractNode result){
		if(result == null) throw new RuntimeException();
		int queueDepth = todoLists.length;
		if(length >= queueDepth){
			DoubleStack<AbstractStackNode<P>, AbstractNode>[] oldTodoLists = todoLists;
			todoLists = new DoubleStack[length + 1];
			System.arraycopy(oldTodoLists, queueIndex, todoLists, 0, queueDepth - queueIndex);
			System.arraycopy(oldTodoLists, 0, todoLists, queueDepth - queueIndex, queueIndex);
			queueDepth = length + 1;
			queueIndex = 0;
		}
		
		int insertLocation = (queueIndex + length) % queueDepth;
		DoubleStack<AbstractStackNode<P>, AbstractNode> terminalsTodo = todoLists[insertLocation];
		if(terminalsTodo == null){
			terminalsTodo = new DoubleStack<AbstractStackNode<P>, AbstractNode>();
			todoLists[insertLocation] = terminalsTodo;
		}
		terminalsTodo.push(node, result);
	}
	
	/**
	 * Handles the retrieved alternatives for the given stack.
	 */
	private boolean handleExpects(AbstractStackNode<P>[] expects, EdgesSet<P> cachedEdges, AbstractStackNode<P> stackBeingWorkedOn){
		boolean hasValidAlternatives = false;
		
		sharedLastExpects.dirtyClear();
		
		EXPECTS: for(int i = expects.length - 1; i >= 0; --i){
			AbstractStackNode<P> first = expects[i];
			
			if(first.isMatchable()){ // Eager matching optimization.
				int length = first.getLength();
				int endLocation = location + length;
				if(endLocation > input.length) continue;
				
				AbstractNode result = first.match(input, location);
				if(result == null){
					unmatchableLeafNodes.push(first);
					
					if(debugListener != null) debugListener.failedToMatch(first);
					
					continue;
				}
				
				if(debugListener != null) debugListener.matched(first, result);
				
				// Handle filtering.
				IEnterFilter[] enterFilters = first.getEnterFilters();
				if(enterFilters != null){
					for(int j = enterFilters.length - 1; j >= 0; --j){
						if(enterFilters[j].isFiltered(input, location, positionStore)){
							if(debugListener != null) debugListener.filteredByEnterFilter(first);
							
							continue EXPECTS;
						}
					}
				}
				
				first = first.getCleanCopyWithResult(location, result);
				
				addTodo(first, length, result);
			}else{
				first = first.getCleanCopy(location);
				stacksToExpand.push(first);
			}
			
			first.initEdges();
			first.addEdges(cachedEdges, location);
			
			sharedLastExpects.add(first.getId(), first);
			
			hasValidAlternatives = true;
			
			if(debugListener != null) debugListener.expanded(stackBeingWorkedOn, first);
		}
		
		return hasValidAlternatives;
	}
	
	/**
	 * Check whether or not the given sort name has nesting restrictions associated with it.
	 */
	protected boolean hasNestingRestrictions(String name){
		return false; // Priority and associativity filtering is off by default.
	}
	
	/**
	 * Retrieves the set of disallowed parents for the given child.
	 */
	protected IntegerList getFilteredParents(int childId){
		return null; // Default implementation; intended to be overwritten in sub-classes.
	}
	
	/**
	 * Retrieves the resultstore id associated with the given id.
	 */
	protected int getResultStoreId(int id){
		return EdgesSet.DEFAULT_RESULT_STORE_ID; // Default implementation; intended to be overwritten in sub-classes.
	}
	
	/**
	 * Expands the given stack node.
	 */
	private void expandStack(AbstractStackNode<P> stack){		
		if(debugListener != null) debugListener.expanding(stack);
		
		// Handle filtering.
		IEnterFilter[] enterFilters = stack.getEnterFilters();
		if(enterFilters != null){
			for(int i = enterFilters.length - 1; i >= 0; --i){
				if(enterFilters[i].isFiltered(input, location, positionStore)){
					unexpandableNodes.push(stack);
					
					if(debugListener != null) debugListener.filteredByEnterFilter(stack);
					
					return;
				}
			}
		}
		
		if(stack.isMatchable()){ // Eager matching optimization related.
			addTodo(stack, stack.getLength(), stack.getResult());
		}else if(!stack.isExpandable()){ // A 'normal' non-terminal.
			EdgesSet<P> cachedEdges = cachedEdgesForExpect.get(stack.getName());
			if(cachedEdges == null){
				cachedEdges = new EdgesSet<P>(1);
				cachedEdgesForExpect.put(stack.getName(), cachedEdges);
				
				AbstractStackNode<P>[] expects = invokeExpects(stack);
				if(expects == null){
					unexpandableNodes.push(stack);
					return;
				}
				
				if(!handleExpects(expects, cachedEdges, stack)){
					unexpandableNodes.push(stack);
					return;
				}
			}else{
				int resultStoreId = getResultStoreId(stack.getId());
				if(cachedEdges.getLastVisitedLevel(resultStoreId) == location){ // Is nullable, add the known results.
					stacksWithNonTerminalsToReduce.push(stack, cachedEdges.getLastResult(resultStoreId));
					
					if(debugListener != null) debugListener.foundIterationCachedNullableResult(stack);
				}
			}
			
			cachedEdges.add(stack);
			
			stack.setIncomingEdges(cachedEdges);
		}else{ // Expandable
			EdgesSet<P> cachedEdges = cachedEdgesForExpect.get(stack.getName());
			if(cachedEdges == null){
				boolean expanded = false;
				
				cachedEdges = new EdgesSet<P>();
				cachedEdgesForExpect.put(stack.getName(), cachedEdges);
				
				AbstractStackNode<P>[] listChildren = stack.getChildren();
				
				CHILDREN : for(int i = listChildren.length - 1; i >= 0; --i){
					AbstractStackNode<P> child = listChildren[i];
					int childId = child.getId();
					
					IntegerKeyedDoubleValueHashMap.Entry<AbstractStackNode<P>, DoubleArrayList<AbstractStackNode<P>, AbstractNode>> sharedChildEntry = sharedNextNodes.get(childId);
					if(sharedChildEntry != null && sharedChildEntry.value2 == null){
						AbstractStackNode<P> sharedChild = sharedChildEntry.value1;
						sharedChild.setEdgesSetWithPrefix(cachedEdges, null, location);
					}else{
						if(child.isMatchable()){
							int length = child.getLength();
							int endLocation = location + length;
							if(endLocation > input.length) continue;
							
							AbstractNode result = child.match(input, location);
							if(result == null){
								unmatchableLeafNodes.push(child);
								
								if(debugListener != null) debugListener.failedToMatch(child);
								
								continue;
							}
							
							if(debugListener != null) debugListener.matched(child, result);
							
							// Handle filtering
							IEnterFilter[] childEnterFilters = child.getEnterFilters();
							if(childEnterFilters != null){
								for(int j = childEnterFilters.length - 1; j >= 0; --j){
									if(childEnterFilters[j].isFiltered(input, location, positionStore)) { 
										if(debugListener != null) debugListener.filteredByEnterFilter(child);
										
										continue CHILDREN;
									}
								}
							}
							
							child = child.getCleanCopyWithResult(location, result);
							addTodo(child, length, result);
						}else{
							child = child.getCleanCopy(location);
							stacksToExpand.push(child);
						}
						
						child.initEdges();
						child.setEdgesSetWithPrefix(cachedEdges, null, location);
						
						sharedNextNodes.putUnsafe(childId, child, null);
						
						if(debugListener != null) debugListener.expanded(stack, child);
					}
					
					expanded = true;
				}
				
				if(stack.canBeEmpty()){ // Star list or optional.
					AbstractStackNode<P> empty = stack.getEmptyChild().getCleanCopyWithResult(location, EpsilonStackNode.EPSILON_RESULT);
					empty.initEdges();
					empty.addEdges(cachedEdges, location);
					
					stacksToExpand.push(empty);
					
					if(debugListener != null) debugListener.expanded(stack, empty);
					
					expanded = true;
				}
				
				if(!expanded){
					unexpandableNodes.push(stack);
				}
			}

			int resultStoreId = getResultStoreId(stack.getId());
			if(cachedEdges.getLastVisitedLevel(resultStoreId) == location){ // Is nullable, add the known results.
				stacksWithNonTerminalsToReduce.push(stack, cachedEdges.getLastResult(resultStoreId));

				if(debugListener != null) debugListener.foundIterationCachedNullableResult(stack);
			}
			
			cachedEdges.add(stack);
			
			stack.setIncomingEdges(cachedEdges);
		}
	}
	
	/**
	 * Initiate stack expansion for all queued stacks.
	 */
	private void expand(){
		while(!stacksToExpand.isEmpty()){
			expandStack(stacksToExpand.pop());
		}
	}
	
	protected AbstractNode parse(AbstractStackNode<P> startNode, URI inputURI, int[] input){
		return parse(startNode, inputURI, input, (IRecoverer<P>) null, (IDebugListener<P>) null);
	}
	
	/**
	 * Initiates parsing.
	 */
	@SuppressWarnings("unchecked")
	protected AbstractNode parse(AbstractStackNode<P> startNode, URI inputURI, int[] input, IRecoverer<P> recoverer, IDebugListener<P> debugListener){
	    initTime();

	  try {

	    if(invoked){
	      throw new RuntimeException("Can only invoke 'parse' once.");
	    }


	    invoked = true;

	    // Initialize.
	    this.inputURI = inputURI;
	    this.input = input;

	    this.recoverer = recoverer;
	    this.debugListener = debugListener;

	    // Initialzed the position store.
	    positionStore.index(input);

	    todoLists = new DoubleStack[DEFAULT_TODOLIST_CAPACITY];

	    // Handle the initial expansion of the root node.
	    AbstractStackNode<P> rootNode = startNode;
	    rootNode.initEdges();
	    stacksToExpand.push(rootNode);
	    lookAheadChar = (input.length > 0) ? input[0] : 0;

	    if(debugListener != null) debugListener.shifting(location, input, positionStore);

	    expand();

	    if(findFirstStacksToReduce()){
	      boolean shiftedLevel = (location != 0);

	      do{
	        lookAheadChar = (location < input.length) ? input[location] : 0;
	        if(shiftedLevel){ // Nullable fix for the first level.
	          sharedNextNodes.clear();
	          cachedEdgesForExpect.clear();

	          unexpandableNodes.dirtyClear();
	          unmatchableLeafNodes.dirtyClear();
	          unmatchableMidProductionNodes.dirtyClear();
	          filteredNodes.dirtyClear();

	          if(debugListener != null) debugListener.shifting(location, input, positionStore);
	        }

	        // Reduce-expand loop.
	        do{
	          if(debugListener != null) debugListener.iterating();

	          reduce();

	          expand();
	        }while(!stacksWithNonTerminalsToReduce.isEmpty() || !stacksWithTerminalsToReduce.isEmpty());

	        shiftedLevel = true;
	      }while(findStacksToReduce());
	    }

	    // Check if we were successful.
	    if(location == input.length){
	      EdgesSet<P> startNodeEdgesSet = startNode.getIncomingEdges();
	      int resultStoreId = getResultStoreId(startNode.getId());
	      if(startNodeEdgesSet != null && startNodeEdgesSet.getLastVisitedLevel(resultStoreId) == input.length){
	        // Parsing succeeded.
	        return startNodeEdgesSet.getLastResult(resultStoreId); // Success.
	      }
	    }
	  }
	  finally {
	    checkTime("Parsing");
	  }

	  try {
	    // A parse error occured, and recovery failed as well
	    parseErrorOccured = true;
	    
	    int errorLocation = (location == Integer.MAX_VALUE ? 0 : location);
	    int line = positionStore.findLine(errorLocation);
	    int column = positionStore.getColumn(errorLocation, line);
	    if (location == input.length) {
	      throw new ParseError("Parse error", inputURI, errorLocation, 0, line, line, column, column, (Stack<AbstractStackNode<?>>) (Stack<?>) unexpandableNodes, (Stack<AbstractStackNode<?>>) (Stack<?>) unmatchableLeafNodes, (DoubleStack<ArrayList<AbstractStackNode<?>>, AbstractStackNode<?>>) (DoubleStack<?, ?>) unmatchableMidProductionNodes, (DoubleStack<AbstractStackNode<?>, AbstractNode>) (DoubleStack<?, ?>) filteredNodes);
	    }
	    throw new ParseError("Parse error", inputURI, errorLocation, 1, line, line, column, column + 1, (Stack<AbstractStackNode<?>>) (Stack<?>) unexpandableNodes, (Stack<AbstractStackNode<?>>) (Stack<?>) unmatchableLeafNodes, (DoubleStack<ArrayList<AbstractStackNode<?>>, AbstractStackNode<?>>) (DoubleStack<?, ?>) unmatchableMidProductionNodes, (DoubleStack<AbstractStackNode<?>, AbstractNode>) (DoubleStack<?, ?>) filteredNodes);
	  }
	  finally {
	    checkTime("Error handling");
	  }
	}

	private void initTime() {
	  timestamp = System.nanoTime();
	}
	
  private void checkTime(String msg) {
    long newStamp = System.nanoTime();
		long duration = newStamp - timestamp;
		timestamp = newStamp;
		
		if (printTimes) {
		  System.err.println(msg + ": " + duration / (1000 * 1000));
		}
  }

	private static int[] charsToInts(char[] input){
		int[] result = new int[Character.codePointCount(input, 0, input.length)];
		int j = 0;
		
		for(int i = 0; i < input.length; i++){
			if (!Character.isLowSurrogate(input[i])) {
				result[j++] = Character.codePointAt(input, i);
			}
		}
		
		return result;
	}

	/**
	 * Parses with post parse filtering.
	 */
	private T parse(String nonterminal, URI inputURI, int[] input, IActionExecutor<T> actionExecutor, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IRecoverer<P> recoverer, IDebugListener<P> debugListener){
	    AbstractNode result = parse(new NonTerminalStackNode<P>(AbstractStackNode.START_SYMBOL_ID, 0, nonterminal), inputURI, input, recoverer, debugListener);
		return buildResult(result, converter, nodeConstructorFactory, actionExecutor);
	}
	
	public T parse(String nonterminal, URI inputURI, char[] input, IActionExecutor<T> actionExecutor, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IRecoverer<P> recoverer, IDebugListener<P> debugListener){
		return parse(nonterminal, inputURI, charsToInts(input), actionExecutor, converter, nodeConstructorFactory, recoverer, debugListener);
	}
	
	public T parse(String nonterminal, URI inputURI, char[] input, IActionExecutor<T> actionExecutor, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IRecoverer<P> recoverer){
		return parse(nonterminal, inputURI, input, actionExecutor, converter, nodeConstructorFactory, recoverer, null);
	}
	
	public T parse(String nonterminal, URI inputURI, char[] input, IActionExecutor<T> actionExecutor, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IDebugListener<P> debugListener){
		return parse(nonterminal, inputURI, input, actionExecutor, converter, nodeConstructorFactory, null, debugListener);
	}

	public T parse(String nonterminal, URI inputURI, char[] input, IActionExecutor<T> actionExecutor, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory){
		return parse(nonterminal, inputURI, input, actionExecutor, converter, nodeConstructorFactory, null, null);
	}

	/**
	 * Parses without post parse filtering.
	 */
	private T parse(String nonterminal, URI inputURI, int[] input, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IRecoverer<P> recoverer, IDebugListener<P> debugListener){
		AbstractNode result = parse(new NonTerminalStackNode<P>(AbstractStackNode.START_SYMBOL_ID, 0, nonterminal), inputURI, input, recoverer, debugListener);
		return buildResult(result, converter, nodeConstructorFactory, new VoidActionExecutor<T>());
	}
	
	public T parse(String nonterminal, URI inputURI, char[] input, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IRecoverer<P> recoverer, IDebugListener<P> debugListener){
		return parse(nonterminal, inputURI, charsToInts(input), converter, nodeConstructorFactory, recoverer, debugListener);
	}
	
	public T parse(String nonterminal, URI inputURI, char[] input, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IRecoverer<P> recoverer){
		return parse(nonterminal, inputURI, input, converter, nodeConstructorFactory, recoverer, null);
	}

	public T parse(String nonterminal, URI inputURI, char[] input, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IDebugListener<P> debugListener){
		return parse(nonterminal, inputURI, input, converter, nodeConstructorFactory, null, debugListener);
	}
	
	public T parse(String nonterminal, URI inputURI, char[] input, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory) {
		return parse(nonterminal, inputURI, charsToInts(input), converter, nodeConstructorFactory, null, null);
	}
	
	protected T parse(AbstractStackNode<P> startNode, URI inputURI, char[] input, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory) {
	  
		AbstractNode result = parse(startNode, inputURI, charsToInts(input), null, null);
		
		return buildResult(result, converter, nodeConstructorFactory, new VoidActionExecutor<T>());
	}
	
	/**
	 * Constructed the final parse result using the given converter.
	 */
	protected T buildResult(AbstractNode result, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IActionExecutor<T> actionExecutor){
	  initTime();
	  try {
	    FilteringTracker filteringTracker = new FilteringTracker();
	    // Invoke the forest flattener, a.k.a. "the bulldozer".
	    Object rootEnvironment = actionExecutor != null ? actionExecutor.createRootEnvironment() : null;
	    T parseResult = null;
	    try {
	      parseResult = converter.convert(nodeConstructorFactory, result, positionStore, filteringTracker, actionExecutor, rootEnvironment);
	    }
	    finally {
	      actionExecutor.completed(rootEnvironment, (parseResult == null));
	    }
	    if(parseResult != null){
	      return parseResult; // Success.
	    }

	    int offset = filteringTracker.getOffset();
	    int endOffset = filteringTracker.getEndOffset();
	    int length = endOffset - offset;
	    int beginLine = positionStore.findLine(offset);
	    int beginColumn = positionStore.getColumn(offset, beginLine);
	    int endLine = positionStore.findLine(endOffset);
	    int endColumn = positionStore.getColumn(endOffset, endLine);
	    throw new ParseError("All results were filtered", inputURI, offset, length, beginLine, endLine, beginColumn, endColumn);
	  }
	  finally {
	    checkTime("Unbinarizing, post-parse filtering, and mapping to UPTR");
	  }
	}
}
