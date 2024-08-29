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
import java.util.concurrent.atomic.AtomicBoolean;

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
import org.rascalmpl.parser.util.DebugUtil;
import org.rascalmpl.util.visualize.ParseStateVisualizer;
import org.rascalmpl.util.visualize.dot.NodeId;
import org.rascalmpl.values.RascalValueFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

/**
 * This is the core of the parser; it drives the parse process.
 */
public abstract class SGTDBF<P, T, S> implements IGTD<P, T, S> {
	private final static int DEFAULT_TODOLIST_CAPACITY = 16;
	
	private URI inputURI;
	private int[] input;
	private int location;
	protected int lookAheadChar;

	// A mapping between character location and line/column.
	private final PositionStore positionStore;
	
	// Terminals that matched. Circular buffer indexed by length of the terminal. Each entry contains the node to reduce and the result node
	// This is a circular buffer where queueIndex determines the start of the buffer.
	// At each position, a stack is maintained of all terminals to reduce of a certain length.
	// So at queueIndex+3, all terminals of length 3 that need reducing are stored.
	private DoubleStack<AbstractStackNode<P>, AbstractNode>[] todoLists;
	private int queueIndex;
	
	// Stack of non-terminal nodes to expand
	// - Nodes are removed in expand, which pops and expands all stack nodes on this stack
	// - Nodes are added in:
	//   - parse: the root node is pushed
	//   - updateNextNode: next node of the production is pushed
	//   - updateAlternativeNextNode: next node of a prefix-shared production is pushed
	//   - handleExpects: non-matchable first element of each alternative is pushed
	//   - expandStack: when an expandable stack is expanded, all non-matchable children are pushed
	private final Stack<AbstractStackNode<P>> stacksToExpand;

	// The current stack of non-terminals to reduce. Each stack has a container node to accumulate results.
	// - Nodes are removed in `reduce` where all productions are advanced one dot past the non-terminal node
	// - Nodes are added in:
	//   - handleEdgeList: result container node is created and all edges are pushed with the same result container node
	//   - handleEdgeListWithRestrictions: result container node is created and all edges are pushed with the same result container node
	//   - expandStack: non-matchable, non-expandable nodes (and their existing result container node) are added if their name can be found in `cachedEdgesForExpect`.
	//   - expandStack: expandable nodes that are nullable? Might be a cycle thing
	private final DoubleStack<AbstractStackNode<P>, AbstractContainerNode<P>> stacksWithNonTerminalsToReduce;

	// The current stack of non-terminals to reduce: it contains the matchable node with the smallest length from todoLists.
	// - Nodes are removed in `reduce` where all productions are advanced one dot past the matchable node
	// - Variable is assigned in:
	//   - findFirstStacksToReduce: the first non-empty `todoList` is assigned to this variable
	//   - findStacksToReduce: again the first non-empty `todoList` is assigned to this variable
	// - parse: variable is used in main reduce/expand loop to determine when it is time to look for more `stacksToReduce`.
	private DoubleStack<AbstractStackNode<P>, AbstractNode> stacksWithTerminalsToReduce;
	
	private final HashMap<String, EdgesSet<P>> cachedEdgesForExpect;
	
	private final IntegerKeyedDoubleValueHashMap<AbstractStackNode<P>, DoubleArrayList<AbstractStackNode<P>, AbstractNode>> sharedNextNodes;
	
	
	// Reflection is used to get the expects for each non-terminal.
	// This cache is used so the reflection call is only needed once per non-terminal.
	private final HashMap<String, AbstractStackNode<P>[]> expectCache;
	
	private final IntegerObjectList<AbstractStackNode<P>> sharedLastExpects;
	
	// Guard
	private boolean invoked;
	
	// Error reporting
	private final Stack<AbstractStackNode<P>> unexpandableNodes;
	private final Stack<AbstractStackNode<P>> unmatchableLeafNodes; // Leaf nodes (for instance literals) that failed to match
	private final DoubleStack<DoubleArrayList<AbstractStackNode<P>, AbstractNode>, AbstractStackNode<P>> unmatchableMidProductionNodes;
	private final DoubleStack<AbstractStackNode<P>, AbstractNode> filteredNodes;
	
	// Error reporting guards
	private boolean parseErrorOccured;
	
	// Error recovery
	private IRecoverer<P> recoverer;
	
	// Debugging
	private IDebugListener<P> debugListener;
	private ParseStateVisualizer visualizer;
	
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
	 * Return a stack node id that is guaranteed not to be in use. 
	 * The parser generator generates an override for this method as it knows which ids have been dispensed.
	 * Tests that need this should override this method, probably using a common base class.
	 */
	protected int getFreeStackNodeId() {
		throw new UnsupportedOperationException();
	}
	
	@SuppressWarnings("unchecked")
	public AbstractStackNode<P>[] getExpects(String nonTerminal) {
		AbstractStackNode<P>[] expects = expectCache.get(nonTerminal);
		if (expects == null) {
			try{
				Method method = getClass().getMethod(nonTerminal);
				try {
					method.setAccessible(true); // Try to bypass the 'isAccessible' check to save time.
				} catch (SecurityException sex) {
					// Ignore this if it happens.
				}
				
				expects = (AbstractStackNode<P>[]) method.invoke(this);
			} catch (NoSuchMethodException nsmex) {
				throw new UndeclaredNonTerminalException(nonTerminal, getClass());
			} catch (IllegalAccessException iaex) {
				throw new RuntimeException(iaex);
			} catch (InvocationTargetException itex) {
				throw new RuntimeException(itex.getTargetException());
			}
			
			expectCache.putUnsafe(nonTerminal, expects);
		}

		return expects;
	}

	/**
	 * Triggers the gathering of alternatives for the given non-terminal.
	 */
	protected AbstractStackNode<P>[] invokeExpects(AbstractStackNode<P> nonTerminal){
		return getExpects(nonTerminal.getName());
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
							EdgesSet<P> nextAlternativeEdgesSet = nextNextAltAlternative.getIncomingEdges();
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
	private void reduceTerminals() {
		// Reduce terminals
		visualize("Reducing terminals", ParseStateVisualizer.TERMINALS_TO_REDUCE_ID);
		while(!stacksWithTerminalsToReduce.isEmpty()){
			move(stacksWithTerminalsToReduce.peekFirst(), stacksWithTerminalsToReduce.popSecond());
		}
	}

	private void reduceNonTerminals() {
		// Reduce non-terminals
		visualize("Reducing non-terminals", ParseStateVisualizer.NON_TERMINALS_TO_REDUCE_ID);
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
			if (debugListener != null) {
				debugListener.reviving(input, location, unexpandableNodes, unmatchableLeafNodes, unmatchableMidProductionNodes, filteredNodes);
			}
			visualize("Recovering", ParseStateVisualizer.ERROR_TRACKING_ID);
			DoubleArrayList<AbstractStackNode<P>, AbstractNode> recoveredNodes = recoverer.reviveStacks(input, location, unexpandableNodes, unmatchableLeafNodes, unmatchableMidProductionNodes, filteredNodes);
			if (debugListener != null) {
				debugListener.revived(recoveredNodes);
			}
			if (recoveredNodes.size() > 0) { // TODO Do something with the revived node. Is this the right location to do this?
				for (int i = 0; i < recoveredNodes.size(); i++) {
					AbstractStackNode<P> recovered = recoveredNodes.getFirst(i);
					queueMatchableNode(recovered, recovered.getLength(), recoveredNodes.getSecond(i));
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
		visualize("Finding stacks to reduce", ParseStateVisualizer.TODO_LISTS_ID);
		int queueDepth = todoLists.length;
		for(int i = 1; i < queueDepth-1; ++i){
			queueIndex = (queueIndex + 1) % queueDepth;
			
			DoubleStack<AbstractStackNode<P>, AbstractNode> terminalsTodo = todoLists[queueIndex];
			if(!(terminalsTodo == null || terminalsTodo.isEmpty())){
				if (ParseStateVisualizer.VISUALIZATION_ENABLED) {
					NodeId reduceNodeId = new NodeId("todo-" + i);
					visualize("Found stack to reduce", reduceNodeId);
				}
		
				stacksWithTerminalsToReduce = terminalsTodo;
				
				location += i;
				
				return true;
			}
		}
		
		if (recoverer != null && location < input.length) {
			if (debugListener != null) {
				debugListener.reviving(input, location, unexpandableNodes, unmatchableLeafNodes, unmatchableMidProductionNodes, filteredNodes);
			}
			visualize("Recovering", ParseStateVisualizer.ERROR_TRACKING_ID);
			DoubleArrayList<AbstractStackNode<P>, AbstractNode> recoveredNodes = recoverer.reviveStacks(input, location, unexpandableNodes, unmatchableLeafNodes, unmatchableMidProductionNodes, filteredNodes);
			if (debugListener != null) {
				debugListener.revived(recoveredNodes);
			}
			if (ParseStateVisualizer.VISUALIZATION_ENABLED && visualizer != null) {
				// Visualize state and include recovered nodes
				visualizer.createGraph(this, "Reviving");
				visualizer.addRecoveredNodes(recoveredNodes);
				visualizer.writeGraph();
				DebugUtil.opportunityToBreak();
			}
			
			if (recoveredNodes.size() > 0) {
				// <PO> was: for (int i = recoveredNodes.size()-1; i>= 0; i--) {
				for (int i = 0; i < recoveredNodes.size(); i++) {
					AbstractStackNode<P> recovered = recoveredNodes.getFirst(i);
					
//					int levelsFromHere = recovered.getLength() - (location - recovered.getStartLocation());
					
					if (debugListener != null) {
						debugListener.reviving(input, location, unexpandableNodes, unmatchableLeafNodes, unmatchableMidProductionNodes, filteredNodes);
					}
					visualize("Queue recovery node", ParseStateVisualizer.getNodeId(recovered));
					queueRecoveryNode(recovered, recovered.getStartLocation(), recovered.getLength(), recoveredNodes.getSecond(i));
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
	private void queueMatchableNode(AbstractStackNode<P> node, int length, AbstractNode result){
		assert result != null;
		
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
     * Inserts a recovery node into the todo-list, and possibly
     * rewinds the parser to an earlier location in the input
     */
    @SuppressWarnings("unchecked")
    private void queueRecoveryNode(AbstractStackNode<P> node, int startPosition, int length, AbstractNode result){
        assert result != null;
        
        int queueDepth = todoLists.length;

        if (startPosition < location) {
            // Have to reset the parser to an earlier location to at least
            // be able to process the new node. Cannot throw away the queue, 
            // because there are possibly already other recovery tokens in the queue.
            // However, we may assume that the queue before the current index is
            // done, based on the way we cycle the queue now. The queue is 
            // looking forward to the future and we never re-use past entries.
            
            int negativeOffset = location - startPosition;
            
            DoubleStack<AbstractStackNode<P>, AbstractNode>[] oldTodoLists = todoLists;
            todoLists = new DoubleStack[negativeOffset + Math.max(queueDepth, length) + 1];
            System.arraycopy(oldTodoLists, queueIndex, todoLists, negativeOffset, queueDepth - queueIndex);
            System.arraycopy(oldTodoLists, 0, todoLists, queueDepth - queueIndex + negativeOffset , queueIndex);
            
            // reset the parser!
            queueIndex = 0;
            location = startPosition;
            
			DoubleStack<AbstractStackNode<P>, AbstractNode> terminalsTodo = todoLists[length];
            if (terminalsTodo == null) {
                terminalsTodo = new DoubleStack<AbstractStackNode<P>, AbstractNode>();
				todoLists[length] = terminalsTodo;
            }
            
            terminalsTodo.push(node, result);
        }
        else if (startPosition == location) {
            // this is the normal case where new matchable nodes are discovered
            // for the current parsing location, so reuse the code for queuing
            queueMatchableNode(node, length, result); 
        }
        else {
            // This would mean we have discovered a recovery node for a location
            // we have not been yet. That would be odd because then there would
            // not have been a parse error and we wouldn't need recovery...
            throw new RuntimeException("discovered a future recovery? " + node);
        }
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
				
				queueMatchableNode(first, length, result);
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
			queueMatchableNode(stack, stack.getLength(), stack.getResult());
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
							queueMatchableNode(child, length, result);
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
		visualize("Expanding", ParseStateVisualizer.STACKS_TO_EXPAND_ID);
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
		
		if (inputURI != null) {
		String query = inputURI.getQuery();
		visualizer = query != null && query.contains("visualize=true") ? new ParseStateVisualizer("Parser") : null;
		}

	    // Initialzed the position store.
	    positionStore.index(input);

	    todoLists = new DoubleStack[DEFAULT_TODOLIST_CAPACITY];

	    // Handle the initial expansion of the root node.
	    AbstractStackNode<P> rootNode = startNode;
	    rootNode.initEdges();
	    stacksToExpand.push(rootNode);
	    lookAheadChar = (input.length > 0) ? input[0] : 0;

	    if(debugListener != null) {
			debugListener.shifting(location, input, positionStore);
		}

	    expand();

	    if(findFirstStacksToReduce()){
	      boolean shiftedLevel = (location != 0);

	      do {
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
	        do {
	          if(debugListener != null) debugListener.iterating();
	
	          reduceTerminals();

			  reduceNonTerminals();

	          expand();
	        } while(!stacksWithNonTerminalsToReduce.isEmpty() || !stacksWithTerminalsToReduce.isEmpty());

	        shiftedLevel = true;
	      } while(findStacksToReduce());
	    }

		visualize("Done", ParseStateVisualizer.PARSER_ID);

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
	      throw new ParseError("Parse error", inputURI, errorLocation, 0, line + 1, line + 1, column, column, (Stack<AbstractStackNode<?>>) (Stack<?>) unexpandableNodes, (Stack<AbstractStackNode<?>>) (Stack<?>) unmatchableLeafNodes, (DoubleStack<ArrayList<AbstractStackNode<?>>, AbstractStackNode<?>>) (DoubleStack<?, ?>) unmatchableMidProductionNodes, (DoubleStack<AbstractStackNode<?>, AbstractNode>) (DoubleStack<?, ?>) filteredNodes);
	    }
	    throw new ParseError("Parse error", inputURI, errorLocation, 1, line + 1, line + 1, column, column + 1, (Stack<AbstractStackNode<?>>) (Stack<?>) unexpandableNodes, (Stack<AbstractStackNode<?>>) (Stack<?>) unmatchableLeafNodes, (DoubleStack<ArrayList<AbstractStackNode<?>>, AbstractStackNode<?>>) (DoubleStack<?, ?>) unmatchableMidProductionNodes, (DoubleStack<AbstractStackNode<?>, AbstractNode>) (DoubleStack<?, ?>) filteredNodes);
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
	    if(parseResult != null) {
			if (recoverer != null) {
				parseResult = fixErrorNodes(parseResult, nodeConstructorFactory);
			}
			return parseResult; // Success.
	    }

	    int offset = filteringTracker.getOffset();
	    int endOffset = filteringTracker.getEndOffset();
	    int length = endOffset - offset;
	    int beginLine = positionStore.findLine(offset);
	    int beginColumn = positionStore.getColumn(offset, beginLine);
	    int endLine = positionStore.findLine(endOffset);
	    int endColumn = positionStore.getColumn(endOffset, endLine);
	    throw new ParseError("All results were filtered", inputURI, offset, length, beginLine + 1, endLine + 1, beginColumn, endColumn);
	  }
	  finally {
	    checkTime("Unbinarizing, post-parse filtering, and mapping to UPTR");
	  }
	}

	/**
	 * Error nodes end up in a n inconvenient form because of the parser algorithm.
	 * This post-processing step transforms the original tree into a more useful form.
	 * In essence, error subtrees look like this after parsing:
	 * `appl(prod(S,[<argtypes>]), [<child1>,<child2>,...,appl(skipped([<chars>]))])`
	 * This method transforms these trees into:
	 * `appl(error(S,prod(S,[<argtypes>]),<dot>), [<child1>,<child2>,...,appl(skipped([<chars>]))])`
	 * This means productions that failed to parse can be recognized at the top level.
	 * Note that this can only be done when we know the actual type of T is IConstructor.
	 */
	@SuppressWarnings("unchecked")
	private T fixErrorNodes(T tree, INodeConstructorFactory<T, S> nodeConstructorFactory) {
		if (!(tree instanceof IConstructor)) {
			return tree;
		}

		return (T) fixErrorNodes((IConstructor) tree, (INodeConstructorFactory<IConstructor, S>) nodeConstructorFactory);
	}

	private IConstructor fixErrorNodes(IConstructor tree, INodeConstructorFactory<IConstructor, S> nodeConstructorFactory) {
		IConstructor result;
		Type type = tree.getConstructorType();
		if (type == RascalValueFactory.Tree_Appl) {
			result = fixErrorAppl(tree, nodeConstructorFactory);
		} else if (type == RascalValueFactory.Tree_Char) {
			result = tree;
		} else if (type == RascalValueFactory.Tree_Amb) {
			result = fixErrorAmb(tree, nodeConstructorFactory);
		} else if (type == RascalValueFactory.Tree_Cycle) {
			result = tree;
		} else {
			throw new RuntimeException("Unrecognized tree type: " + type);
		}

		if (result != tree && tree.asWithKeywordParameters().hasParameter(RascalValueFactory.Location)) {
			IValue loc = tree.asWithKeywordParameters().getParameter(RascalValueFactory.Location);
			result = result.asWithKeywordParameters().setParameter(RascalValueFactory.Location, loc);
		}

		return result;
	}

	private IConstructor fixErrorAppl(IConstructor tree, INodeConstructorFactory<IConstructor, S> nodeConstructorFactory) {
			IValue prod = tree.get(0);
			IList childList = (IList) tree.get(1);
			int childCount = childList.length();

			ArrayList<IConstructor> children = new ArrayList<>(childCount);
			boolean anyChanges = false;
			boolean errorTree = false;
			for (int i=0; i<childCount; i++) {
				if (i == childCount-1) {
					// Last child could be a skipped child
					IConstructor last = (IConstructor) childList.get(childCount-1);
					if (last.getConstructorType() == RascalValueFactory.Tree_Appl) {
						IConstructor lastProd = (IConstructor) last.get(0);
						if (lastProd.getConstructorType() == RascalValueFactory.Production_Skipped) {
							errorTree = true;
							children.add(last);
							break;
						}
					}
				}

				IConstructor child = (IConstructor) childList.get(i);
				IConstructor resultChild = fixErrorNodes(child, nodeConstructorFactory);
				children.add(resultChild);
				if (resultChild != child) {
					anyChanges = true;
				}
			}

			if (errorTree) {
			return nodeConstructorFactory.createErrorNode(children, prod);
			} else if (anyChanges) {
			return nodeConstructorFactory.createSortNode(children, prod);
			}

		return tree;
	}

	private IConstructor fixErrorAmb(IConstructor tree, INodeConstructorFactory<IConstructor, S> nodeConstructorFactory) {
			ISet alternativeSet = (ISet) tree.get(0);
			ArrayList<IConstructor> alternatives = new ArrayList<>(alternativeSet.size());
			final AtomicBoolean anyChanges = new AtomicBoolean(false);
			alternativeSet.forEach(alt -> {
				IConstructor newAlt = fixErrorNodes((IConstructor) alt, nodeConstructorFactory);
				if (newAlt != alt) {
					anyChanges.setPlain(true);
				}
				alternatives.add(newAlt); 
			});

			if (anyChanges.getPlain()) {
			return nodeConstructorFactory.createAmbiguityNode(alternatives);
		}

		return tree;
		}



	/**
	 * Datastructure visualization for debugging purposes
	 */

	 private void visualize(String step, NodeId highlight) {
		// Only visualize when debugging
		if (ParseStateVisualizer.VISUALIZATION_ENABLED && visualizer != null) {
			visualizer.createGraph(this, step);
			if (highlight != null) {
				visualizer.highlight(highlight);
			}
			visualizer.writeGraph();

			DebugUtil.opportunityToBreak();
		}
	 }

	/**
	 * Getters used for graph generation for debugging (see DebugVisualizer)
	 */

	public int[] getInput() {
		return input;
	}

	public int getLocation() {
		return location;
	}

	public int getLookAheadChar() {
		return lookAheadChar;
	}

	public DoubleStack<AbstractStackNode<P>, AbstractNode>[] getTodoLists() {
		return todoLists;
	}

	public int getQueueIndex() {
		return queueIndex;
	}

	public Stack<AbstractStackNode<P>> getStacksToExpand() {
		return stacksToExpand;
	}

	public DoubleStack<AbstractStackNode<P>, AbstractContainerNode<P>> getStacksWithNonTerminalsToReduce() {
		return stacksWithNonTerminalsToReduce;
	}

	public DoubleStack<AbstractStackNode<P>, AbstractNode> getStacksWithTerminalsToReduce() {
		return stacksWithTerminalsToReduce;
	}

	public Stack<AbstractStackNode<P>> getUnexpandableNodes() {
		return unexpandableNodes;
	}

	public Stack<AbstractStackNode<P>> getUnmatchableLeafNodes() {
		return unmatchableLeafNodes;
	}

	public DoubleStack<DoubleArrayList<AbstractStackNode<P>, AbstractNode>, AbstractStackNode<P>> getUnmatchableMidProductionNodes() {
		return unmatchableMidProductionNodes;
	}

	public DoubleStack<AbstractStackNode<P>, AbstractNode> getFilteredNodes() {
		return filteredNodes;
	}
}
