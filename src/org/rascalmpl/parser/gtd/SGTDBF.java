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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.parser.gtd.exception.UndeclaredNonTerminalException;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.ExpandableContainerNode;
import org.rascalmpl.parser.gtd.result.SortContainerNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.action.VoidActionExecutor;
import org.rascalmpl.parser.gtd.result.out.FilteringTracker;
import org.rascalmpl.parser.gtd.result.out.INodeConverter;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.IExpandableStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.HashMap;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.Stack;
import org.rascalmpl.values.ValueFactoryFactory;

public abstract class SGTDBF implements IGTD{
	private final static int DEFAULT_RESULT_STORE_ID = -1;
	
	private final static int DEFAULT_TODOLIST_CAPACITY = 16;
	
	protected final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	
	private AbstractStackNode startNode;
	private URI inputURI;
	private char[] input;
	
	private final PositionStore positionStore;
	
	private DoubleStack<AbstractStackNode, AbstractNode>[] todoLists;
	private int queueIndex;
	
	private final Stack<AbstractStackNode> stacksToExpand;
	private final DoubleStack<AbstractStackNode, AbstractContainerNode> stacksWithNonTerminalsToReduce;
	private DoubleStack<AbstractStackNode, AbstractNode> stacksWithTerminalsToReduce;
	
	private final ArrayList<AbstractStackNode> lastExpects;
	private final HashMap<String, ArrayList<AbstractStackNode>> cachedEdgesForExpect;
	
	private final IntegerKeyedHashMap<AbstractStackNode> sharedNextNodes;

	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<String, AbstractContainerNode>> resultStoreCache;
	
	private int location;
	
	protected char lookAheadChar;
	
	private final HashMap<String, Method> methodCache;
	
	private final IntegerObjectList<AbstractStackNode> sharedLastExpects;
	private boolean hasValidAlternatives;
	
	private final IntegerObjectList<IntegerList> propagatedPrefixes;
	private final IntegerObjectList<IntegerList> propagatedReductions; // Note: we can replace this thing, if we pick a more efficient solution.
	
	// Guard
	private boolean invoked;
	
	// Error reporting
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
		
		lastExpects = new ArrayList<AbstractStackNode>();
		cachedEdgesForExpect = new HashMap<String, ArrayList<AbstractStackNode>>();
		
		sharedNextNodes = new IntegerKeyedHashMap<AbstractStackNode>();
		
		resultStoreCache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<String, AbstractContainerNode>>();
		
		location = 0;
		
		methodCache = new HashMap<String, Method>();
		
		sharedLastExpects = new IntegerObjectList<AbstractStackNode>();
		
		propagatedPrefixes = new IntegerObjectList<IntegerList>();
		propagatedReductions = new IntegerObjectList<IntegerList>();
		
		unexpandableNodes = new Stack<AbstractStackNode>();
		unmatchableNodes = new Stack<AbstractStackNode>();
		filteredNodes = new DoubleStack<AbstractStackNode, AbstractNode>();
	}
	
	/**
	 * Queue the given node for handling. This node belongs to the symbol we
	 * are currenly expanding.
	 */
	protected void expect(AbstractStackNode symbolToExpect){
		lastExpects.add(symbolToExpect);
	}
	
	/**
	 * Queue the given nodes for handling. These nodes belong to the symbol we
	 * are currently expanding.
	 */
	protected void expect(AbstractStackNode[] symbolsToExpect){
		for(int i = symbolsToExpect.length - 1; i >= 0; --i){
			lastExpects.add(symbolsToExpect[i]);
		}
	}
	
	/**
	 * Triggers the gathering of alternatives for the given non-terminal.
	 */
	protected void invokeExpects(AbstractStackNode nonTerminal){
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
			method.invoke(this);
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
					ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(location);
					AbstractContainerNode nextResult = levelResultStoreMap.get(alternative.getName(), getResultStoreId(alternative.getId()));
					if(nextResult != null){
						// Encountered a stack 'overtake'.
						propagateEdgesAndPrefixes(node, result, alternative, nextResult, node.getEdges().size());
						return alternative;
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
	private boolean updateAlternativeNextNode(AbstractStackNode next, AbstractStackNode node, AbstractNode result, IntegerObjectList<ArrayList<AbstractStackNode>> edgesMap, ArrayList<Link>[] prefixesMap){
		int id = next.getId();
		AbstractStackNode alternative = sharedNextNodes.get(id);
		if(alternative != null){ // Sharing check.
			if(result.isEmpty()){
				if(alternative.isMatchable()){
					if(alternative.isEmptyLeafNode()){
						// Encountered a stack 'overtake'.
						propagateAlternativeEdgesAndPrefixes(node, result, alternative, alternative.getResult(), edgesMap.size(), edgesMap, prefixesMap);
						return true;
					}
				}else{
					ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(location);
					AbstractContainerNode nextResult = levelResultStoreMap.get(alternative.getName(), getResultStoreId(alternative.getId()));
					if(nextResult != null){
						// Encountered a stack 'overtake'.
						propagateAlternativeEdgesAndPrefixes(node, result, alternative, nextResult, edgesMap.size(), edgesMap, prefixesMap);
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
		
		IntegerObjectList<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixes = node.getPrefixesMap();
		
		IConstructor production = next.getParentProduction();
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
			// Proceed with the tail of the production.
			int nextDot = next.getDot() + 1;
			AbstractStackNode[] prod = node.getProduction();
			AbstractStackNode nextNext = prod[nextDot];
			AbstractStackNode nextNextAlternative = sharedNextNodes.get(nextNext.getId());
			if(nextNextAlternative == null) return;
	
			ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(location);
			if(nextNextAlternative.isMatchable()){
				if(nextNextAlternative.isEmptyLeafNode()){
					propagateEdgesAndPrefixes(next, nextResult, nextNextAlternative, nextNextAlternative.getResult(), nrOfAddedEdges);
				}else{
					nextNextAlternative.updateNode(next, nextResult);
				}
			}else{
				AbstractContainerNode nextNextResultStore = levelResultStoreMap.get(nextNextAlternative.getName(), getResultStoreId(nextNextAlternative.getId()));
				if(nextNextResultStore != null){
					propagateEdgesAndPrefixes(next, nextResult, nextNextAlternative, nextNextResultStore, nrOfAddedEdges);
				}else{
					nextNextAlternative.updateNode(next, nextResult);
				}
			}
			
			// Handle alternative continuations (related to prefix sharing).
			AbstractStackNode[][] alternateProds = node.getAlternateProductions();
			if(alternateProds != null){
				IntegerObjectList<ArrayList<AbstractStackNode>> nextEdgesMap = next.getEdges();
				ArrayList<Link>[] nextPrefixesMap = next.getPrefixesMap();
				
				for(int i = alternateProds.length - 1; i >= 0; --i){
					prod = alternateProds[i];
					if(nextDot == prod.length) continue;
					AbstractStackNode alternativeNext = prod[nextDot];
					
					AbstractStackNode nextNextAltAlternative = sharedNextNodes.get(alternativeNext.getId());
					
					AbstractContainerNode nextAltResultStore = levelResultStoreMap.get(nextNextAltAlternative.getName(), getResultStoreId(nextNextAltAlternative.getId()));
					if(nextNextAltAlternative.isMatchable()){
						if(nextNextAltAlternative.isEmptyLeafNode()){
							propagateAlternativeEdgesAndPrefixes(next, nextResult, nextNextAltAlternative, nextAltResultStore, nrOfAddedEdges, nextEdgesMap, nextPrefixesMap);
						}else{
							nextNextAltAlternative.updatePrefixSharedNode(nextEdgesMap, nextPrefixesMap);
						}
					}else{
						if(nextAltResultStore != null){
							propagateAlternativeEdgesAndPrefixes(next, nextResult, nextNextAltAlternative, nextAltResultStore, nrOfAddedEdges, nextEdgesMap, nextPrefixesMap);
						}else{
							nextNextAltAlternative.updatePrefixSharedNode(nextEdgesMap, nextPrefixesMap);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Part of the hidden-right-recursion fix.
	 * Inserts missing prefixes and triggers reductions where necessary (specifically for alternative continuations of prefix-shared productions).
	 */
	private void propagateAlternativeEdgesAndPrefixes(AbstractStackNode node, AbstractNode nodeResult, AbstractStackNode next, AbstractNode nextResult, int potentialNewEdges, IntegerObjectList<ArrayList<AbstractStackNode>> edgesMap, ArrayList<Link>[] prefixesMap){
		next.updatePrefixSharedNode(edgesMap, prefixesMap);
		
		if(next.isEndNode()){
			propagateReductions(node, nodeResult, next, nextResult, potentialNewEdges);
		}
		
		if(potentialNewEdges != 0 && next.hasNext()){
			// Proceed with the tail of the production.
			int nextDot = next.getDot() + 1;
			AbstractStackNode[] prod = node.getProduction();
			AbstractStackNode nextNext = prod[nextDot];
			AbstractStackNode nextNextAlternative = sharedNextNodes.get(nextNext.getId());
			if(nextNextAlternative == null) return;

			ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(location);
			if(nextNextAlternative.isMatchable()){
				if(nextNextAlternative.isEmptyLeafNode()){
					propagateEdgesAndPrefixes(next, nextResult, nextNextAlternative, nextNextAlternative.getResult(), potentialNewEdges);
				}else{
					nextNextAlternative.updateNode(next, nextResult);
				}
			}else{
				AbstractContainerNode nextResultStore = levelResultStoreMap.get(nextNextAlternative.getName(), getResultStoreId(nextNextAlternative.getId()));
				if(nextResultStore != null){
					propagateEdgesAndPrefixes(next, nextResult, nextNextAlternative, nextResultStore, potentialNewEdges);
				}else{
					nextNextAlternative.updateNode(next, nextResult);
				}
			}
			
			// Handle alternative continuations (related to prefix sharing).
			AbstractStackNode[][] alternateProds = node.getAlternateProductions();
			if(alternateProds != null){
				IntegerObjectList<ArrayList<AbstractStackNode>> nextEdgesMap = next.getEdges();
				ArrayList<Link>[] nextPrefixesMap = next.getPrefixesMap();
				
				for(int i = alternateProds.length - 1; i >= 0; --i){
					prod = alternateProds[i];
					if(nextDot == prod.length) continue;
					AbstractStackNode alternativeNext = prod[nextDot];
					
					AbstractStackNode nextNextAltAlternative = sharedNextNodes.get(alternativeNext.getId());
					if(nextNextAlternative.isMatchable()){
						if(nextNextAlternative.isEmptyLeafNode()){
							propagateAlternativeEdgesAndPrefixes(next, nextResult, nextNextAltAlternative, nextNextAltAlternative.getResult(), potentialNewEdges, nextEdgesMap, nextPrefixesMap);
						}else{
							nextNextAltAlternative.updatePrefixSharedNode(nextEdgesMap, nextPrefixesMap);
						}
					}else{
						AbstractContainerNode nextAltResultStore = levelResultStoreMap.get(nextNextAltAlternative.getName(), getResultStoreId(nextNextAltAlternative.getId()));
						if(nextAltResultStore != null){
							propagateAlternativeEdgesAndPrefixes(next, nextResult, nextNextAltAlternative, nextAltResultStore, potentialNewEdges, nextEdgesMap, nextPrefixesMap);
						}else{
							nextNextAltAlternative.updatePrefixSharedNode(nextEdgesMap, nextPrefixesMap);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Initiates the handling of reductions.
	 */
	private void updateEdges(AbstractStackNode node, AbstractNode result){
		IntegerObjectList<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		IConstructor production = node.getParentProduction();
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
		
		IntegerObjectList<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		IConstructor production = node.getParentProduction();
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
	private void handleEdgeList(ArrayList<AbstractStackNode> edgeList, String name, IConstructor production, Link resultLink, int startLocation){
		ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(startLocation);
		
		if(levelResultStoreMap == null){
			levelResultStoreMap = new ObjectIntegerKeyedHashMap<String, AbstractContainerNode>();
			resultStoreCache.putUnsafe(startLocation, levelResultStoreMap);
		}
		
		AbstractStackNode edge = edgeList.get(0);
		
		AbstractContainerNode resultStore = levelResultStoreMap.get(name, DEFAULT_RESULT_STORE_ID);
		if(resultStore != null){
			resultStore.addAlternative(production, resultLink);
		}else{ // Edges visit optimization; only visit the other edges the first time.
			resultStore = (!edge.isExpandable()) ? new SortContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout()) : new ExpandableContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout());
			levelResultStoreMap.putUnsafe(name, DEFAULT_RESULT_STORE_ID, resultStore);
			resultStore.addAlternative(production, resultLink);
			
			stacksWithNonTerminalsToReduce.push(edge, resultStore);
			
			for(int j = edgeList.size() - 1; j >= 1; --j){
				edge = edgeList.get(j);
				stacksWithNonTerminalsToReduce.push(edge, resultStore);
			}
		}
	}
	
	// Reuse these structures.
	private final IntegerList firstTimeRegistration = new IntegerList();
	private final IntegerList firstTimeReductions = new IntegerList();
	
	/**
	 * Handles reductions which may be associated with nesting restrictions.
	 */
	private void handleEdgeListWithRestrictions(ArrayList<AbstractStackNode> edgeList, String name, IConstructor production, Link resultLink, int startLocation, IntegerList filteredParents){
		ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(startLocation);
		
		if(levelResultStoreMap == null){
			levelResultStoreMap = new ObjectIntegerKeyedHashMap<String, AbstractContainerNode>();
			resultStoreCache.putUnsafe(startLocation, levelResultStoreMap);
		}
		
		// Only add the result to each resultstore once.
		// Make sure each edge only gets added to the non-terminal reduction list once per level, by keeping track of them.
		firstTimeRegistration.clear();
		firstTimeReductions.clear();
		for(int j = edgeList.size() - 1; j >= 0; --j){
			AbstractStackNode edge = edgeList.get(j);
			int resultStoreId = getResultStoreId(edge.getId());
			
			if(!firstTimeReductions.contains(resultStoreId)){
				if(firstTimeRegistration.contains(resultStoreId)) continue;
				firstTimeRegistration.add(resultStoreId);
				
				// Check whether or not the nesting is allowed.
				if(filteredParents == null || !filteredParents.contains(edge.getId())){
					AbstractContainerNode resultStore = levelResultStoreMap.get(name, resultStoreId);
					if(resultStore != null){
						resultStore.addAlternative(production, resultLink);
					}else{
						resultStore = (!edge.isExpandable()) ? new SortContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout()) : new ExpandableContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout());
						levelResultStoreMap.putUnsafe(name, resultStoreId, resultStore);
						resultStore.addAlternative(production, resultLink);
						
						stacksWithNonTerminalsToReduce.push(edge, resultStore);
						firstTimeReductions.add(resultStoreId);
					}
				}
			}else{
				AbstractContainerNode resultStore = levelResultStoreMap.get(name, resultStoreId);
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
			IntegerObjectList<ArrayList<AbstractStackNode>> edgesMap = null;
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
			if(!result.isEmpty() || node.getId() == IExpandableStackNode.DEFAULT_LIST_EPSILON_ID){ // Only go into the nullable fix path for nullables (special list epsilons can be ignored as well).
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
		return false;
	}
	
	/**
	 * Inserts a stack bottom into the todo-list.
	 */
	private void addTodo(AbstractStackNode node, int length, AbstractNode result){
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
	 * Handles sharing for children of expandable nodes.
	 */
	private boolean shareExpandableChild(int id, AbstractStackNode stack){
		AbstractStackNode sharedNode = sharedNextNodes.get(id);
		if(sharedNode != null){
			sharedNode.addEdgeWithPrefix(stack, null, location);
			return true;
		}
		return false;
	}
	
	/**
	 * Handles the retrieved alternatives for the given stack.
	 */
	private void handleExpects(AbstractStackNode stackBeingWorkedOn){
		sharedLastExpects.dirtyClear();
		
		ArrayList<AbstractStackNode> cachedEdges = null;
		
		int nrOfExpects = lastExpects.size();
		if(nrOfExpects == 0){ // Error reporting.
			unexpandableNodes.push(stackBeingWorkedOn);
			return;
		}
		
		EXPECTS: for(int i = nrOfExpects - 1; i >= 0; --i){
			AbstractStackNode first = lastExpects.get(i);
			
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
			if(cachedEdges == null){
				cachedEdges = first.addEdge(stackBeingWorkedOn, location);
			}else{
				first.addEdges(cachedEdges, location);
			}
			
			sharedLastExpects.add(first.getId(), first);
			
			hasValidAlternatives = true;
		}
		
		cachedEdgesForExpect.put(stackBeingWorkedOn.getName(), cachedEdges);
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
		return DEFAULT_RESULT_STORE_ID; // Default implementation; intended to be overwritten in sub-classes.
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
			ArrayList<AbstractStackNode> cachedEdges = cachedEdgesForExpect.get(stack.getName());
			if(cachedEdges != null){ // Edge sharing 'expansion' optimization.
				cachedEdges.add(stack);
				
				ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(location);
				if(levelResultStoreMap != null){
					AbstractContainerNode resultStore = levelResultStoreMap.get(stack.getName(), getResultStoreId(stack.getId()));
					if(resultStore != null){ // Has pre-existing nullable results, handle the node.
						stacksWithNonTerminalsToReduce.push(stack, resultStore);
					}
				}
			}else{
				invokeExpects(stack);
				hasValidAlternatives = false;
				handleExpects(stack);
				if(!hasValidAlternatives){
					unexpandableNodes.push(stack);
				}
			}
		}else{ // Expandable
			boolean expanded = false;
			
			AbstractStackNode[] listChildren = stack.getChildren();
			
			// Unfold the expandable stack node.
			CHILDREN: for(int i = listChildren.length - 1; i >= 0; --i){
				AbstractStackNode child = listChildren[i];
				int childId = child.getId();
				if(!shareExpandableChild(childId, stack)){
					if(child.isMatchable()){ // Eager matching optimization.
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
								if(childEnterFilters[i].isFiltered(input, location, positionStore)) continue CHILDREN;
							}
						}
						
						child = child.getCleanCopyWithResult(location, result);
						
						addTodo(child, length, result);
					}else{
						child = child.getCleanCopy(location);
						stacksToExpand.push(child);
					}
					
					sharedNextNodes.putUnsafe(childId, child);
					
					child.initEdges();
					child.addEdgeWithPrefix(stack, null, location);
				}
				
				expanded = true;
			}
			
			if(stack.canBeEmpty()){ // Star list, optional or such.
				// This epsilon is unique for this position, so we don't need to check for sharing.
				AbstractStackNode empty = stack.getEmptyChild().getCleanCopy(location);
				empty.initEdges();
				empty.addEdge(stack, location);
				
				stacksToExpand.push(empty);
				
				expanded = true;
			}
			
			if(!expanded){
				unexpandableNodes.push(stack);
			}
		}
	}
	
	/**
	 * Initiate stack expansion for all queued stacks.
	 */
	private void expand(){
		while(!stacksToExpand.isEmpty()){
			lastExpects.dirtyClear();
			expandStack(stacksToExpand.pop());
		}
	}
	
	/**
	 * Initiates parsing.
	 */
	protected AbstractNode parse(AbstractStackNode startNode, URI inputURI, char[] input){
		if(invoked){
			throw new RuntimeException("Can only invoke 'parse' once.");
		}
		invoked = true;
		
		// Initialize.
		this.startNode = startNode;
		this.inputURI = inputURI;
		this.input = input;
		
		// Initialzed the position store.
		positionStore.index(input);
		
		todoLists = new DoubleStack[DEFAULT_TODOLIST_CAPACITY];
		
		// Handle the initial expansion of the root node.
		AbstractStackNode rootNode = startNode.getCleanCopy(0);
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
					resultStoreCache.clear();
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
			ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(0);
			if(levelResultStoreMap != null){
				AbstractContainerNode result = levelResultStoreMap.get(startNode.getName(), getResultStoreId(startNode.getId()));
				if(result != null){
					// Parsing succeeded.
					return result;
				}
			}
		}
		
		// A parse error occured.
		parseErrorOccured = true;
		
		int errorLocation = (location == Integer.MAX_VALUE ? 0 : location);
		int line = positionStore.findLine(errorLocation);
		int column = positionStore.getColumn(errorLocation, line);
		throw new ParseError("Parse error", inputURI, errorLocation, 0, line, line, column, column, unexpandableNodes, unmatchableNodes, filteredNodes);
	}
	
	/**
	 * Parses with post parse filtering.
	 */
	public Object parse(String nonterminal, URI inputURI, char[] input, IActionExecutor actionExecutor, INodeConverter converter){
		AbstractNode result = parse(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, nonterminal), inputURI, input);
		return buildTree(result, converter, actionExecutor);
	}
	
	/**
	 * Parses without post parse filtering.
	 */
	public Object parse(String nonterminal, URI inputURI, char[] input, INodeConverter converter){
		AbstractNode result = parse(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, nonterminal), inputURI, input);
		return buildTree(result, converter, new VoidActionExecutor());
	}
	
	/**
	 * Parses without post parse filtering.
	 */
	protected Object parse(AbstractStackNode startNode, URI inputURI, char[] input, INodeConverter converter){
		AbstractNode result = parse(startNode, inputURI, input);
		return buildTree(result, converter, new VoidActionExecutor());
	}
	
	/**
	 * Constructed the final parse tree using the given converter.
	 */
	protected Object buildTree(AbstractNode result, INodeConverter converter, IActionExecutor actionExecutor){
		FilteringTracker filteringTracker = new FilteringTracker();
		// Invoke the forest flattener, a.k.a. "the bulldozer".
		Object rootEnvironment = actionExecutor.createRootEnvironment();
		IConstructor resultTree = null;
		try{
			resultTree = (IConstructor) converter.convert(result, positionStore, actionExecutor, rootEnvironment, filteringTracker);
		}finally{
			actionExecutor.completed(rootEnvironment, (resultTree == null));
		}
		if(resultTree != null){
			return resultTree; // Success.
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
		throw new ParseError("All trees were filtered", inputURI, offset, length, beginLine, endLine, beginColumn, endColumn);
	}
	
	/**
	 * Constructed a error parse tree using the given converter.
	 */
	public Object buildErrorTree(INodeConverter converter, IActionExecutor actionExecutor){
		AbstractContainerNode result;
		
		if(parseErrorOccured){
			ErrorTreeBuilder errorTreeBuilder = new ErrorTreeBuilder(this, startNode, input, location, inputURI);
			result = errorTreeBuilder.buildErrorTree(unexpandableNodes, unmatchableNodes, filteredNodes);
		}else if(filterErrorOccured){
			ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(0);
			result = levelResultStoreMap.get(startNode.getName(), getResultStoreId(startNode.getId()));
		}else{
			throw new RuntimeException("Cannot build an error tree as no parse error occurred.");
		}
		
		// Invoke "the bulldozer" that constructs error trees while it's flattening the forest.
		Object rootEnvironment = actionExecutor.createRootEnvironment();
		try{
			return converter.convertWithErrors(result, positionStore, actionExecutor, rootEnvironment);
		}finally{
			actionExecutor.completed(rootEnvironment, true);
		}
	}
}
