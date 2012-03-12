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
package org.rascalmpl.parser.gtd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;

import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.parser.gtd.exception.UndeclaredNonTerminalException;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.ExpandableContainerNode;
import org.rascalmpl.parser.gtd.result.SortContainerNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.action.VoidActionExecutor;
import org.rascalmpl.parser.gtd.result.error.IErrorBuilderHelper;
import org.rascalmpl.parser.gtd.result.out.FilteringTracker;
import org.rascalmpl.parser.gtd.result.out.INodeConverter;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.AbstractExpandableStackNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.EpsilonStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.stack.RecoveryStackNode;
import org.rascalmpl.parser.gtd.stack.edge.EdgesSet;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.HashMap;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;
import org.rascalmpl.parser.gtd.util.ObjectKeyedIntegerMap;
import org.rascalmpl.parser.gtd.util.Stack;

/**
 * This is the core of the parser; it drives the parse process.
 */
public abstract class SGTDBF implements IGTD{
	private final static int DEFAULT_TODOLIST_CAPACITY = 16;
	
	private AbstractStackNode startNode;
	private URI inputURI;
	private int[] input;
	
	private final PositionStore positionStore;
	
	private DoubleStack<AbstractStackNode, AbstractNode>[] todoLists;
	private int queueIndex;
	
	private final Stack<AbstractStackNode> stacksToExpand;
	private final DoubleStack<AbstractStackNode, AbstractContainerNode> stacksWithNonTerminalsToReduce;
	private DoubleStack<AbstractStackNode, AbstractNode> stacksWithTerminalsToReduce;
	
	private final HashMap<String, EdgesSet> cachedEdgesForExpect;
	
	private final IntegerKeyedHashMap<AbstractStackNode> sharedNextNodes;
	
	private int location;
	
	protected int lookAheadChar;
	
	private final HashMap<String, Method> methodCache;
	
	private final IntegerObjectList<AbstractStackNode> sharedLastExpects;
	
	private final IntegerObjectList<IntegerList> propagatedPrefixes;
	private final IntegerObjectList<IntegerList> propagatedReductions; // Note: we can replace this thing, if we pick a more efficient solution.
	
	// Guard
	private boolean invoked;
	
	// Error reporting
	private boolean recovering = false;
	private final Stack<AbstractStackNode> unexpandableNodes;
	private final Stack<AbstractStackNode> unmatchableNodes;
	private final DoubleStack<AbstractStackNode, AbstractNode> filteredNodes;
	
	// Error reporting guards
	private boolean parseErrorOccured;
	private boolean filterErrorOccured;
	
	public SGTDBF(){
		super();
		
		positionStore = new PositionStore();
		
		stacksToExpand = new Stack<AbstractStackNode>();
		stacksWithNonTerminalsToReduce = new DoubleStack<AbstractStackNode, AbstractContainerNode>();
		
		cachedEdgesForExpect = new HashMap<String, EdgesSet>();
		
		sharedNextNodes = new IntegerKeyedHashMap<AbstractStackNode>();
		
		location = 0;
		
		methodCache = new HashMap<String, Method>();
		
		sharedLastExpects = new IntegerObjectList<AbstractStackNode>();
		
		propagatedPrefixes = new IntegerObjectList<IntegerList>();
		propagatedReductions = new IntegerObjectList<IntegerList>();
		
		unexpandableNodes = new Stack<AbstractStackNode>();
		unmatchableNodes = new Stack<AbstractStackNode>();
		filteredNodes = new DoubleStack<AbstractStackNode, AbstractNode>();
	}
	
	public void enableRecovery() {
		recovering = true;
	}
	
	/**
	 * Triggers the gathering of alternatives for the given non-terminal.
	 */
	protected AbstractStackNode[] invokeExpects(AbstractStackNode nonTerminal){
		String name = nonTerminal.getName();
		Method method = methodCache.get(name);
		if(method == null){
			try{
				method = getClass().getMethod(name);
				try{
					method.setAccessible(true); // Try to bypass the 'isAccessible' check to save time.
				}catch(SecurityException sex){
					// Ignore this if it happens.
				}
			}catch(NoSuchMethodException nsmex){
				throw new UndeclaredNonTerminalException(name, getClass());
			}
			methodCache.putUnsafe(name, method);
		}
		
		try{
			return (AbstractStackNode[]) method.invoke(this);
		}catch(IllegalAccessException iaex){
			throw new RuntimeException(iaex);
		}catch(InvocationTargetException itex){
			throw new RuntimeException(itex.getTargetException());
		} 
	}
	
	/**
	 * Moves to the next symbol in the production.
	 */
	private AbstractStackNode updateNextNode(AbstractStackNode next, AbstractStackNode node, AbstractNode result){
		AbstractStackNode alternative = sharedNextNodes.get(next.getId());
		if(alternative != null){ // Sharing check.
			if(result.isEmpty()){
				if(alternative.isMatchable()){
					if(alternative.isEmptyLeafNode()){
						// Encountered a stack 'overtake'.
						propagateEdgesAndPrefixes(node, result, alternative, alternative.getResult(), node.getEdges().size());
						return alternative;
					}
				}else{
					if(alternative.getStartLocation() == location){
						EdgesSet alternativeEdgesSet = alternative.getIncomingEdges();
						int resultStoreId = getResultStoreId(alternative.getId());
						if(alternativeEdgesSet != null && alternativeEdgesSet.getLastVisitedLevel(resultStoreId) == location){
							// Encountered a stack 'overtake'.
							propagateEdgesAndPrefixes(node, result, alternative, alternativeEdgesSet.getLastResult(resultStoreId), node.getEdges().size());
							return alternative;
						}
					}
				}
			}
			
			alternative.updateNode(node, result);
			
			return alternative;
		}
		
		if(next.isMatchable()){ // Eager matching optimization.
			if((location + next.getLength()) > input.length) return null;
			
			AbstractNode nextResult = next.match(input, location);
			if(nextResult == null){
				unmatchableNodes.push(next);
				return null;
			}
			
			next = next.getCleanCopyWithResult(location, nextResult);
		}else{
			next = next.getCleanCopy(location);
		}
		
		if(!node.isMatchable() || result.isEmpty()){
			next.updateNode(node, result);
		}else{ // Non-nullable terminal specific edge set sharing optimization.
			next.updateNodeAfterNonEmptyMatchable(node, result);
		}
		
		sharedNextNodes.putUnsafe(next.getId(), next);
		stacksToExpand.push(next);
		
		return next;
	}
	
	/**
	 * Moves to the next symbol in an alternative continuation of a prefix-shared production.
	 */
	private boolean updateAlternativeNextNode(AbstractStackNode next, AbstractStackNode node, AbstractNode result, IntegerObjectList<EdgesSet> edgesMap, ArrayList<Link>[] prefixesMap){
		int id = next.getId();
		AbstractStackNode alternative = sharedNextNodes.get(id);
		if(alternative != null){ // Sharing check.
			if(result.isEmpty()){
				if(alternative.isMatchable()){
					if(alternative.isEmptyLeafNode()){
						// Encountered a stack 'overtake'.
						propagateAlternativeEdgesAndPrefixes(node, result, alternative, alternative.getResult(), node.getEdges().size(), edgesMap, prefixesMap);
						return true;
					}
				}else{
					EdgesSet alternativeEdgesSet = alternative.getIncomingEdges();
					int resultStoreId = getResultStoreId(alternative.getId());
					if(alternativeEdgesSet != null && alternativeEdgesSet.getLastVisitedLevel(resultStoreId) == location){
						AbstractContainerNode nextResult = alternativeEdgesSet.getLastResult(resultStoreId);
						// Encountered a stack 'overtake'.
						propagateAlternativeEdgesAndPrefixes(node, result, alternative, nextResult, node.getEdges().size(), edgesMap, prefixesMap);
						return true;
					}
				}
			}
			
			alternative.updatePrefixSharedNode(edgesMap, prefixesMap); // Prevent unnecessary overhead; share whenever possible.
			
			return true;
		}
		
		if(next.isMatchable()){ // Eager matching optimization.
			if((location + next.getLength()) > input.length) return false;
			
			AbstractNode nextResult = next.match(input, location);
			if(nextResult == null){
				unmatchableNodes.push(next);
				return false;
			}
			
			next = next.getCleanCopyWithResult(location, nextResult);
		}else{
			next = next.getCleanCopy(location);
		}
		
		next.updatePrefixSharedNode(edgesMap, prefixesMap); // Prevent unnecessary overhead; share whenever possible.
		
		sharedNextNodes.putUnsafe(id, next);
		stacksToExpand.push(next);
		
		return true;
	}
	
	/**
	 * Part of the hidden-right-recursion fix.
	 * Executes absent reductions.
	 */
	private void propagateReductions(AbstractStackNode node, AbstractNode nodeResultStore, AbstractStackNode next, AbstractNode nextResultStore, int potentialNewEdges){
		IntegerList touched = propagatedReductions.findValue(next.getId());
		if(touched == null){
			touched = new IntegerList();
			propagatedReductions.add(next.getId(), touched);
		}
		
		IntegerObjectList<EdgesSet> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixes = node.getPrefixesMap();
		
		Object production = next.getParentProduction();
		String name = edgesMap.getValue(0).get(0).getName();
		
		boolean hasNestingRestrictions = hasNestingRestrictions(name);
		IntegerList filteredParents = null;
		if(hasNestingRestrictions){
			filteredParents = getFilteredParents(next.getId());
		}
		
		int fromIndex = edgesMap.size() - potentialNewEdges;
		for(int i = edgesMap.size() - 1; i >= fromIndex; --i){
			int startLocation = edgesMap.getKey(i);
			
			if(touched.contains(startLocation)) continue; // Prevent duplicate reductions (artifact of the hidden-right-recursion fix).
			touched.add(startLocation);
			
			ArrayList<Link> edgePrefixes = new ArrayList<Link>();
			Link prefix = (prefixes != null) ? new Link(prefixes[i], nodeResultStore) : new Link(null, nodeResultStore);
			edgePrefixes.add(prefix);
			
			Link resultLink = new Link(edgePrefixes, nextResultStore);
			
			if(!hasNestingRestrictions){
				handleEdgeList(edgesMap.getValue(i), name, production, resultLink, startLocation);
			}else{
				handleEdgeListWithRestrictions(edgesMap.getValue(i), name, production, resultLink, startLocation, filteredParents);
			}
		}
	}
	
	/**
	 * Part of the hidden-right-recursion fix.
	 * Propagates absent prefixes.
	 */
	private void propagatePrefixes(AbstractStackNode next, AbstractNode nextResult, int nrOfAddedEdges){
		// Proceed with the tail of the production.
		int nextDot = next.getDot() + 1;
		AbstractStackNode[] prod = next.getProduction();
		AbstractStackNode nextNext = prod[nextDot];
		AbstractStackNode nextNextAlternative = sharedNextNodes.get(nextNext.getId());
		if(nextNextAlternative != null){ // Valid continuation.
			if(nextNextAlternative.isMatchable()){
				if(nextNextAlternative.isEmptyLeafNode()){
					propagateEdgesAndPrefixes(next, nextResult, nextNextAlternative, nextNextAlternative.getResult(), nrOfAddedEdges);
				}else{
					nextNextAlternative.updateNode(next, nextResult);
				}
			}else{
				EdgesSet nextNextAlternativeEdgesSet = nextNextAlternative.getIncomingEdges();
				int resultStoreId = getResultStoreId(nextNextAlternative.getId());
				if(nextNextAlternativeEdgesSet != null && nextNextAlternativeEdgesSet.getLastVisitedLevel(resultStoreId) == location){
					propagateEdgesAndPrefixes(next, nextResult, nextNextAlternative, nextNextAlternativeEdgesSet.getLastResult(resultStoreId), nrOfAddedEdges);
				}else{
					nextNextAlternative.updateNode(next, nextResult);
				}
			}
		}
		
		// Handle alternative continuations (related to prefix sharing).
		AbstractStackNode[][] alternateProds = next.getAlternateProductions();
		if(alternateProds != null){
			if(nextNextAlternative == null){ // If the first continuation has not been initialized yet (it may be a matchable that didn't match), create a dummy version to construct the necessary edges and prefixes.
				if(!nextNext.isMatchable()) return; // Matchable, abort.
				nextNextAlternative = nextNext.getCleanCopy(location);
				nextNextAlternative.updateNode(next, nextResult);
			}
			
			IntegerObjectList<EdgesSet> nextNextEdgesMap = nextNextAlternative.getEdges();
			ArrayList<Link>[] nextNextPrefixesMap = nextNextAlternative.getPrefixesMap();
			
			for(int i = alternateProds.length - 1; i >= 0; --i){
				prod = alternateProds[i];
				if(nextDot == prod.length) continue;
				AbstractStackNode alternativeNext = prod[nextDot];
				
				AbstractStackNode nextNextAltAlternative = sharedNextNodes.get(alternativeNext.getId());
				if(nextNextAltAlternative != null){
					if(nextNextAltAlternative.isMatchable()){
						if(nextNextAltAlternative.isEmptyLeafNode()){
							propagateAlternativeEdgesAndPrefixes(next, nextResult, nextNextAltAlternative, nextNextAltAlternative.getResult(), nrOfAddedEdges, nextNextEdgesMap, nextNextPrefixesMap);
						}else{
							nextNextAltAlternative.updatePrefixSharedNode(nextNextEdgesMap, nextNextPrefixesMap);
						}
					}else{
						EdgesSet nextAlternativeEdgesSet = nextNextAlternative.getIncomingEdges();
						int resultStoreId = getResultStoreId(nextNextAltAlternative.getId());
						if(nextAlternativeEdgesSet != null && nextAlternativeEdgesSet.getLastVisitedLevel(resultStoreId) == location){
							propagateAlternativeEdgesAndPrefixes(next, nextResult, nextNextAltAlternative, nextAlternativeEdgesSet.getLastResult(resultStoreId), nrOfAddedEdges, nextNextEdgesMap, nextNextPrefixesMap);
						}else{
							nextNextAltAlternative.updatePrefixSharedNode(nextNextEdgesMap, nextNextPrefixesMap);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Part of the hidden-right-recursion fix.
	 * Inserts missing prefixes and triggers reductions where necessary.
	 */
	private void propagateEdgesAndPrefixes(AbstractStackNode node, AbstractNode nodeResult, AbstractStackNode next, AbstractNode nextResult, int potentialNewEdges){
		IntegerList touched = propagatedPrefixes.findValue(node.getId());
		if(touched == null){
			touched = new IntegerList();
			propagatedPrefixes.add(node.getId(), touched);
		}
		
		int nrOfAddedEdges = next.updateOvertakenNode(node, nodeResult, potentialNewEdges, touched);
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
	private void propagateAlternativeEdgesAndPrefixes(AbstractStackNode node, AbstractNode nodeResult, AbstractStackNode next, AbstractNode nextResult, int potentialNewEdges, IntegerObjectList<EdgesSet> edgesMap, ArrayList<Link>[] prefixesMap){
		next.updatePrefixSharedNode(edgesMap, prefixesMap);
		
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
	private void updateEdges(AbstractStackNode node, AbstractNode result){
		IntegerObjectList<EdgesSet> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		Object production = node.getParentProduction();
		String name = edgesMap.getValue(0).get(0).getName();
		
		// Check for nesting restrictions.
		boolean hasNestingRestrictions = hasNestingRestrictions(name);
		IntegerList filteredParents = null;
		if(hasNestingRestrictions){
			filteredParents = getFilteredParents(node.getId());
		}
		
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			Link resultLink = new Link((prefixesMap != null) ? prefixesMap[i] : null, result);
			
			if(!hasNestingRestrictions){ // Select the optimized path for handling edge sets that don't have nesting restrictions associated with them.
				handleEdgeList(edgesMap.getValue(i), name, production, resultLink, edgesMap.getKey(i));
			}else{
				handleEdgeListWithRestrictions(edgesMap.getValue(i), name, production, resultLink, edgesMap.getKey(i), filteredParents);
			}
		}
	}
	
	/**
	 * Initiates the handling of reductions for nullable symbols.
	 */
	private void updateNullableEdges(AbstractStackNode node, AbstractNode result){
		IntegerList touched = propagatedReductions.findValue(node.getId());
		if(touched == null){
			touched = new IntegerList();
			propagatedReductions.add(node.getId(), touched);
		}
		
		IntegerObjectList<EdgesSet> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		Object production = node.getParentProduction();
		String name = edgesMap.getValue(0).get(0).getName();
		
		// Check for nesting restrictions.
		boolean hasNestingRestrictions = hasNestingRestrictions(name);
		IntegerList filteredParents = null;
		if(hasNestingRestrictions){
			filteredParents = getFilteredParents(node.getId());
		}
		
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			int startLocation = edgesMap.getKey(i);
			
			if(touched.contains(startLocation)) continue; // Prevent duplicate reductions (artifact of the hidden-right-recursion fix).
			touched.add(startLocation);
			
			Link resultLink = new Link((prefixesMap != null) ? prefixesMap[i] : null, result);
			
			if(!hasNestingRestrictions){ // Select the optimized path for handling edge sets that don't have nesting restrictions associated with them.
				handleEdgeList(edgesMap.getValue(i), name, production, resultLink, startLocation);
			}else{
				handleEdgeListWithRestrictions(edgesMap.getValue(i), name, production, resultLink, startLocation, filteredParents);
			}
		}
	}
	
	/**
	 * Handles reductions.
	 */
	private void handleEdgeList(EdgesSet edgeSet, String name, Object production, Link resultLink, int startLocation){
		AbstractContainerNode resultStore = null;
		int resultStoreId = EdgesSet.DEFAULT_RESULT_STORE_ID;
		if(edgeSet.getLastVisitedLevel(resultStoreId) != location){
			AbstractStackNode edge = edgeSet.get(0);
			
			resultStore = (!edge.isExpandable()) ? new SortContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout()) : new ExpandableContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout());
			
			stacksWithNonTerminalsToReduce.push(edge, resultStore);
			
			for(int j = edgeSet.size() - 1; j >= 1; --j){
				edge = edgeSet.get(j);
				stacksWithNonTerminalsToReduce.push(edge, resultStore);
			}
		
			edgeSet.setLastVisistedLevel(location, resultStoreId);
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
	private void handleEdgeListWithRestrictions(EdgesSet edgeSet, String name, Object production, Link resultLink, int startLocation, IntegerList filteredParents){
		// Only add the result to each resultstore once.
		// Make sure each edge only gets added to the non-terminal reduction list once per level, by keeping track of them.
		firstTimeRegistration.clear();
		firstTimeReductions.clear();
		for(int j = edgeSet.size() - 1; j >= 0; --j){
			AbstractStackNode edge = edgeSet.get(j);
			int resultStoreId = getResultStoreId(edge.getId());
			
			if(!firstTimeReductions.contains(resultStoreId)){
				if(firstTimeRegistration.contains(resultStoreId)) continue;
				firstTimeRegistration.add(resultStoreId);
				
				// Check whether or not the nesting is allowed.
				if(filteredParents == null || !filteredParents.contains(edge.getId())){
					AbstractContainerNode resultStore = null;
					if(edgeSet.getLastVisitedLevel(resultStoreId) == location){
						resultStore = edgeSet.getLastResult(resultStoreId);
					}
					if(resultStore == null){
						resultStore = (!edge.isExpandable()) ? new SortContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout()) : new ExpandableContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout());
						edgeSet.setLastVisistedLevel(location, resultStoreId);
						edgeSet.setLastResult(resultStore, resultStoreId);
						
						stacksWithNonTerminalsToReduce.push(edge, resultStore);
						firstTimeReductions.add(resultStoreId);
					}
					
					resultStore.addAlternative(production, resultLink);
				}
			}else{
				AbstractContainerNode resultStore = edgeSet.getLastResult(resultStoreId);
				stacksWithNonTerminalsToReduce.push(edge, resultStore);
			}
		}
	}
	
	/**
	 * Move to the next symbol(s) in the production.
	 */
	private void moveToNext(AbstractStackNode node, AbstractNode result){
		int nextDot = node.getDot() + 1;

		AbstractStackNode[] prod = node.getProduction();
		AbstractStackNode newNext = prod[nextDot];
		AbstractStackNode next = updateNextNode(newNext, node, result);
		
		// Handle alternative continuations of this production (related to prefix-sharing).
		AbstractStackNode[][] alternateProds = node.getAlternateProductions();
		if(alternateProds != null){
			IntegerObjectList<EdgesSet> edgesMap = null;
			ArrayList<Link>[] prefixesMap = null;
			if(next != null){
				edgesMap = next.getEdges();
				prefixesMap = next.getPrefixesMap();
			}
			
			for(int i = alternateProds.length - 1; i >= 0; --i){
				prod = alternateProds[i];
				if(nextDot == prod.length) continue;
				AbstractStackNode newAlternativeNext = prod[nextDot];
				
				if(edgesMap != null){
					updateAlternativeNextNode(newAlternativeNext, node, result, edgesMap, prefixesMap);
				}else{
					AbstractStackNode alternativeNext = updateNextNode(newAlternativeNext, node, result);
					
					if(alternativeNext != null){
						edgesMap = alternativeNext.getEdges();
						prefixesMap = alternativeNext.getPrefixesMap();
					}
				}
			}
		}
	}
	
	/**
	 * Progess to the next 'states' associated with the given node.
	 * I.e. move to the next symbol(s) in the production (if available) and executed reductions if necessary.
	 */
	private void move(AbstractStackNode node, AbstractNode result){
		// Handle filtering.
		ICompletionFilter[] completionFilters = node.getCompletionFilters();
		if(completionFilters != null){
			int startLocation = node.getStartLocation();
			for(int i = completionFilters.length - 1; i >= 0; --i){
				if(completionFilters[i].isFiltered(input, startLocation, location, positionStore)){
					filteredNodes.push(node, result);
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
			DoubleStack<AbstractStackNode, AbstractNode> terminalsTodo = todoLists[i];
			if(!(terminalsTodo == null || terminalsTodo.isEmpty())){
				stacksWithTerminalsToReduce = terminalsTodo;
				
				location += i;
				
				queueIndex = i;
				
				return true;
			}
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
			
			DoubleStack<AbstractStackNode, AbstractNode> terminalsTodo = todoLists[queueIndex];
			if(!(terminalsTodo == null || terminalsTodo.isEmpty())){
				stacksWithTerminalsToReduce = terminalsTodo;
				
				location += i;
				
				return true;
			}
		}
		
		if (recovering) {
			return findStacksToRevive();
		}
		
		return false;
	}
	
	private boolean findStacksToRevive() {
		boolean unexpandable = reviveFailedNodes(unexpandableNodes);
		boolean unmatchable = reviveFailedNodes(unmatchableNodes);
		boolean filtered = reviveFiltered(filteredNodes);
		
		// don't inline the booleans (short-circuit + side-effects)
		return unexpandable || unmatchable || filtered; 
	}

	/**
	 * Inserts a stack bottom into the todo-list.
	 */
	@SuppressWarnings("unchecked")
	private void addTodo(AbstractStackNode node, int length, AbstractNode result){
		if(result == null) throw new RuntimeException();
		int queueDepth = todoLists.length;
		if(length >= queueDepth){
			DoubleStack<AbstractStackNode, AbstractNode>[] oldTodoLists = todoLists;
			todoLists = new DoubleStack[length + 1];
			System.arraycopy(oldTodoLists, queueIndex, todoLists, 0, queueDepth - queueIndex);
			System.arraycopy(oldTodoLists, 0, todoLists, queueDepth - queueIndex, queueIndex);
			queueDepth = length + 1;
			queueIndex = 0;
		}
		
		int insertLocation = (queueIndex + length) % queueDepth;
		DoubleStack<AbstractStackNode, AbstractNode> terminalsTodo = todoLists[insertLocation];
		if(terminalsTodo == null){
			terminalsTodo = new DoubleStack<AbstractStackNode, AbstractNode>();
			todoLists[insertLocation] = terminalsTodo;
		}
		terminalsTodo.push(node, result);
	}
	
	/**
	 * Handles the retrieved alternatives for the given stack.
	 */
	private boolean handleExpects(AbstractStackNode[] expects, EdgesSet cachedEdges, AbstractStackNode stackBeingWorkedOn){
		boolean hasValidAlternatives = false;
		
		sharedLastExpects.dirtyClear();
		
		EXPECTS: for(int i = expects.length - 1; i >= 0; --i){
			AbstractStackNode first = expects[i];
			
			if(first.isMatchable()){ // Eager matching optimization.
				int length = first.getLength();
				int endLocation = location + length;
				if(endLocation > input.length) continue;
				
				AbstractNode result = first.match(input, location);
				if(result == null){
					unmatchableNodes.push(first);
					continue;
				}
				
				// Handle filtering.
				IEnterFilter[] enterFilters = first.getEnterFilters();
				if(enterFilters != null){
					for(int j = enterFilters.length - 1; j >= 0; --j){
						if(enterFilters[j].isFiltered(input, location, positionStore)) continue EXPECTS;
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
	private void expandStack(AbstractStackNode stack){
		// Handle filtering.
		IEnterFilter[] enterFilters = stack.getEnterFilters();
		if(enterFilters != null){
			for(int i = enterFilters.length - 1; i >= 0; --i){
				if(enterFilters[i].isFiltered(input, location, positionStore)){
					unexpandableNodes.push(stack);
					return;
				}
			}
		}
		
		if(stack.isMatchable()){ // Eager matching optimization related.
			addTodo(stack, stack.getLength(), stack.getResult());
		}else if(!stack.isExpandable()){ // A 'normal' non-terminal.
			EdgesSet cachedEdges = cachedEdgesForExpect.get(stack.getName());
			if(cachedEdges == null){
				cachedEdges = new EdgesSet(1);
				cachedEdgesForExpect.put(stack.getName(), cachedEdges);
				
				AbstractStackNode[] expects = invokeExpects(stack);
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
				}
			}
			
			cachedEdges.add(stack);
			
			stack.setIncomingEdges(cachedEdges);
		}else{ // Expandable
			EdgesSet cachedEdges = cachedEdgesForExpect.get(stack.getName());
			if(cachedEdges == null){
				boolean expanded = false;
				
				cachedEdges = new EdgesSet();
				cachedEdgesForExpect.put(stack.getName(), cachedEdges);
				
				AbstractStackNode[] listChildren = stack.getChildren();
				
				CHILDREN : for(int i = listChildren.length - 1; i >= 0; --i){
					AbstractStackNode child = listChildren[i];
					int childId = child.getId();
					
					AbstractStackNode sharedChild = sharedNextNodes.get(childId);
					if(sharedChild != null){
						sharedChild.setEdgesSetWithPrefix(cachedEdges, null, location);
					}else{
						if(child.isMatchable()){
							int length = child.getLength();
							int endLocation = location + length;
							if(endLocation > input.length) continue;
							
							AbstractNode result = child.match(input, location);
							if(result == null){
								unmatchableNodes.push(child);
								continue;
							}
							
							// Handle filtering
							IEnterFilter[] childEnterFilters = child.getEnterFilters();
							if(childEnterFilters != null){
								for(int j = childEnterFilters.length - 1; j >= 0; --j){
									if(childEnterFilters[j].isFiltered(input, location, positionStore)) { 
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
						
						sharedNextNodes.putUnsafe(childId, child);
					}
					
					expanded = true;
				}
				
				if(stack.canBeEmpty()){ // Star list or optional.
					AbstractStackNode empty = stack.getEmptyChild().getCleanCopyWithResult(location, EpsilonStackNode.EPSILON_RESULT);
					empty.initEdges();
					empty.addEdges(cachedEdges, location);
					
					stacksToExpand.push(empty);
					
					expanded = true;
				}
				
				if(!expanded){
					unexpandableNodes.push(stack);
				}
			}

			int resultStoreId = getResultStoreId(stack.getId());
			if(cachedEdges.getLastVisitedLevel(resultStoreId) == location){ // Is nullable, add the known results.
				stacksWithNonTerminalsToReduce.push(stack, cachedEdges.getLastResult(resultStoreId));
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
	
	/**
	 * Initiates parsing.
	 */
	@SuppressWarnings("unchecked")
	protected AbstractNode parse(AbstractStackNode startNode, URI inputURI, int[] input){
		if(invoked){
			throw new RuntimeException("Can only invoke 'parse' once.");
		}
		invoked = true;
		
		// Initialize.
		this.startNode = startNode.getCleanCopy(0);
		this.inputURI = inputURI;
		this.input = input;
		
		// Initialzed the position store.
		positionStore.index(input);
		
		todoLists = new DoubleStack[DEFAULT_TODOLIST_CAPACITY];
		
		// Handle the initial expansion of the root node.
		AbstractStackNode rootNode = startNode;
		rootNode.initEdges();
		stacksToExpand.push(rootNode);
		lookAheadChar = (input.length > 0) ? input[0] : 0;
		expand();
		
		if(findFirstStacksToReduce()){
			boolean shiftedLevel = (location != 0);
			
			do{
				lookAheadChar = (location < input.length) ? input[location] : 0;
				if(shiftedLevel){ // Nullable fix for the first level.
					sharedNextNodes.clear();
					cachedEdgesForExpect.clear();
					
					propagatedPrefixes.dirtyClear();
					propagatedReductions.dirtyClear();
					
					unexpandableNodes.dirtyClear();
					unmatchableNodes.dirtyClear();
					filteredNodes.dirtyClear();
				}
				
				// Reduce-expand loop.
				do{
					reduce();
					
					expand();
				}while(!stacksWithNonTerminalsToReduce.isEmpty() || !stacksWithTerminalsToReduce.isEmpty());
				shiftedLevel = true;
			}while(findStacksToReduce());
		}
		
		// Check if we were successful.
		if(location == input.length){
			EdgesSet startNodeEdgesSet = startNode.getIncomingEdges();
			int resultStoreId = getResultStoreId(startNode.getId());
			if(startNodeEdgesSet != null && startNodeEdgesSet.getLastVisitedLevel(resultStoreId) == input.length){
				// Parsing succeeded.
				return startNodeEdgesSet.getLastResult(resultStoreId); // Success.
			}
		}
		
		
		// A parse error occured.
		parseErrorOccured = true;

		int errorLocation = (location == Integer.MAX_VALUE ? 0 : location);
		int line = positionStore.findLine(errorLocation);
		int column = positionStore.getColumn(errorLocation, line);
		throw new ParseError("Parse error", inputURI, errorLocation, 0, line, line, column, column, unexpandableNodes, unmatchableNodes, filteredNodes);
	}
	
	private boolean reviveFiltered(DoubleStack<AbstractStackNode, AbstractNode> nodes) {
		// TODO: perhaps this is not really necessary
		return false;
	}

	private boolean reviveFailedNodes(Stack<AbstractStackNode> failedNodes) {
		Stack<AbstractStackNode> recoveryNodes = new Stack<AbstractStackNode>();
		while (!failedNodes.isEmpty()) {
			findRecoveryNodes(failedNodes.pop(), recoveryNodes);
		}
		
		if (recoveryNodes.isEmpty()) {
			return false;
		}
		
		while (!recoveryNodes.isEmpty()) {
			AbstractStackNode recoveryNode = recoveryNodes.pop();
			
			// TODO: skipping to newline here, instead of programmeable follow set
			RecoveryStackNode recoverLiteral = new RecoveryStackNode(0, new int[] {'\n'}, input, location);
			recoverLiteral.initEdges();
			EdgesSet edges = new EdgesSet(1);
			edges.add(recoverLiteral);
			recoverLiteral.addEdges(edges, recoverLiteral.getStartLocation());
			
			recoveryNode.setIncomingEdges(edges);
			
			addTodo(recoverLiteral, recoverLiteral.getLength(), recoverLiteral.getResult());
		}
	
		findFirstStacksToReduce();
		return true;
	}

	/**
	 * This method travels up the graph to find the first nodes that are recoverable.
	 * The graph may split and merge, and even cycle, so we take care of knowing where
	 * we have been and what we still need to do.
	 */
	private void findRecoveryNodes(AbstractStackNode failer, Stack<AbstractStackNode> recoveryNodes) {
		Stack<AbstractStackNode> todo = new Stack<AbstractStackNode>();
		ObjectKeyedIntegerMap<AbstractStackNode> visited = new ObjectKeyedIntegerMap<AbstractStackNode>();
		
		todo.push(failer);
		
		OUTER: while (!todo.isEmpty()) {
			AbstractStackNode node = todo.pop();
			visited.put(node, 0);
			
			if (node.isRecovering()) {
				recoveryNodes.push(node);
				continue;
			}
			
			IntegerObjectList<EdgesSet> toParents = node.getEdges();

			// Terminals don't have edges to parents because they are predicted, but
			// we can safely skip them here, because their parents will be among the failedNodes
			if (toParents == null) { 
				continue;
			}

			for (int i = 0; i < toParents.size(); i++) {
				EdgesSet edges = toParents.getValue(i);

				if (edges != null) {
					for (int j = 0; j < edges.size(); j++) {
						AbstractStackNode parent = edges.get(j);

						if (visited.contains(parent)) {
							continue OUTER;
						}
						else if (parent.isRecovering()) {
							if (!recoveryNodes.contains(parent)) {
								recoveryNodes.push(parent);
							}
						}
						else {
							todo.push(parent);
						}
					}
				}
			}
		}
	}

	private int[] charsToInts(char[] input) {
		int[] result = new int[Character.codePointCount(input, 0, input.length)];
		int j = 0;
		
		for (int i = 0; i < input.length; i++) {
			if (i == 0 || !Character.isHighSurrogate(input[i-1])) {
				result[j++] = Character.codePointAt(input, i);
			}
		}
		
		return result;
	}
	
	/**
	 * Parses with post parse filtering.
	 */
	public Object parse(String nonterminal, URI inputURI, char[] input, IActionExecutor actionExecutor, INodeConverter converter){
		return parse(nonterminal, inputURI, charsToInts(input), actionExecutor, converter);
	}
	
	/**
	 * Parses with post parse filtering.
	 */
	private Object parse(String nonterminal, URI inputURI, int[] input, IActionExecutor actionExecutor, INodeConverter converter){
		AbstractNode result = parse(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, nonterminal), inputURI, input);
		return buildResult(result, converter, actionExecutor);
	}
	
	/**
	 * Parses without post parse filtering.
	 */
	public Object parse(String nonterminal, URI inputURI, char[] input, INodeConverter converter){
		return parse(nonterminal, inputURI, charsToInts(input), converter);
	}
	
	private Object parse(String nonterminal, URI inputURI, int[] input, INodeConverter converter){
		AbstractNode result = parse(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, nonterminal), inputURI, input);
		return buildResult(result, converter, new VoidActionExecutor());
	}
	
	/**
	 * Parses without post parse filtering.
	 */
	protected Object parse(AbstractStackNode startNode, URI inputURI, char[] input, INodeConverter converter){
		return parse(startNode, inputURI, charsToInts(input), converter);
	}
	
	/**
	 * Parses without post parse filtering.
	 */
	protected Object parse(AbstractStackNode startNode, URI inputURI, int[] input, INodeConverter converter){
		AbstractNode result = parse(startNode, inputURI, input);
		return buildResult(result, converter, new VoidActionExecutor());
	}
	
	/**
	 * Constructed the final parse result using the given converter.
	 */
	protected Object buildResult(AbstractNode result, INodeConverter converter, IActionExecutor actionExecutor){
		FilteringTracker filteringTracker = new FilteringTracker();
		// Invoke the forest flattener, a.k.a. "the bulldozer".
		Object rootEnvironment = actionExecutor.createRootEnvironment();
		Object parseResult = null;
		try{
			parseResult = converter.convert(result, positionStore, actionExecutor, rootEnvironment, filteringTracker);
		}finally{
			actionExecutor.completed(rootEnvironment, (parseResult == null));
		}
		if(parseResult != null){
			return parseResult; // Success.
		}
		
		// Filtering error.
		filterErrorOccured = true;
		
		int offset = filteringTracker.getOffset();
		int endOffset = filteringTracker.getEndOffset();
		int length = endOffset - offset;
		int beginLine = positionStore.findLine(offset);
		int beginColumn = positionStore.getColumn(offset, beginLine);
		int endLine = positionStore.findLine(endOffset);
		int endColumn = positionStore.getColumn(endOffset, endLine);
		throw new ParseError("All results were filtered", inputURI, offset, length, beginLine, endLine, beginColumn, endColumn);
	}
	
	/**
	 * Constructs an error parse result, using the given converter and action
	 * executor.
	 */
	private Object buildError(IErrorBuilderHelper errorBuilderHelper, INodeConverter converter, IActionExecutor actionExecutor){
		AbstractContainerNode result;
		
		if(parseErrorOccured){
			ErrorResultBuilder errorTreeBuilder = new ErrorResultBuilder(errorBuilderHelper, this, startNode, input, location, inputURI);
			result = errorTreeBuilder.buildErrorTree(unexpandableNodes, filteredNodes);
			
			if(result == null) return null; // We were unable to construct an error tree.
		}else if(filterErrorOccured){
			EdgesSet rootNodeEdgesSet = startNode.getIncomingEdges();
			int resultStoreId = getResultStoreId(startNode.getId());
			if(rootNodeEdgesSet != null && rootNodeEdgesSet.getLastVisitedLevel(resultStoreId) == input.length){
				result = rootNodeEdgesSet.getLastResult(resultStoreId);
			}else{
				throw new RuntimeException("This can't happen, as filtering errors can't occur on incomplete trees.");
			}
		}else{
			throw new RuntimeException("Cannot build an error result as no parse error occurred.");
		}
		
		// Invoke "the bulldozer" that constructs error results while it's flattening the forest.
		Object rootEnvironment = actionExecutor.createRootEnvironment();
		try{
			return converter.convertWithErrors(result, positionStore, actionExecutor, rootEnvironment);
		}finally{
			actionExecutor.completed(rootEnvironment, true);
		}
	}
	
	/**
	 * Constructed an error parse result, using the given converter and action
	 * executor.
	 */
	public Object buildErrorResult(IErrorBuilderHelper errorBuilderHelper, INodeConverter converter, IActionExecutor actionExecutor){
		return buildError(errorBuilderHelper, converter, actionExecutor);
	}
	
	/**
	 * Constructed a error parse result using the given converter.
	 */
	public Object buildErrorResult(IErrorBuilderHelper errorBuilderHelper, INodeConverter converter){
		return buildError(errorBuilderHelper, converter, new VoidActionExecutor());
	}
}
