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
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredNonTerminalError;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.ListContainerNode;
import org.rascalmpl.parser.gtd.result.SortContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode.FilteringTracker;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.action.IEnvironment;
import org.rascalmpl.parser.gtd.result.action.VoidActionExecutor;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.result.uptr.NodeToUPTR;
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
import org.rascalmpl.parser.gtd.util.LinearIntegerKeyedMap;
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
	private IActionExecutor actionExecutor;
	private final PositionStore positionStore;
	
	private Stack<AbstractStackNode>[] todoLists;
	private int queueIndex;
	
	private final Stack<AbstractStackNode> stacksToExpand;
	private Stack<AbstractStackNode> stacksWithTerminalsToReduce;
	private final DoubleStack<AbstractStackNode, AbstractContainerNode> stacksWithNonTerminalsToReduce;
	
	private final ArrayList<AbstractStackNode[]> lastExpects;
	private final HashMap<String, ArrayList<AbstractStackNode>> cachedEdgesForExpect;
	
	private final IntegerKeyedHashMap<AbstractStackNode> sharedNextNodes;

	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<String, AbstractContainerNode>> resultStoreCache;
	
	private int location;
	private boolean shiftedLevel;
	
	protected char lookAheadChar;
	
	private final HashMap<String, Method> methodCache;
	
	private final LinearIntegerKeyedMap<AbstractStackNode> sharedLastExpects;
	private final LinearIntegerKeyedMap<AbstractStackNode> sharedPrefixNext;
	
	private final LinearIntegerKeyedMap<IntegerList> propagatedPrefixes;
	private final LinearIntegerKeyedMap<IntegerList> propagatedReductions; // Note: we can replace this thing, if we pick a more efficient solution.
	
	// Error reporting.
	private boolean parseErrorOccured;
	private boolean filterErrorOccured;
	
	private final Stack<AbstractStackNode> unexpandableNodes;
	private final Stack<AbstractStackNode> unmatchableNodes;
	private final DoubleStack<AbstractStackNode, AbstractContainerNode> filteredNodes;
	
	// Guard
	private boolean invoked;
	
	public SGTDBF(){
		super();
		
		positionStore = new PositionStore();
		
		stacksToExpand = new Stack<AbstractStackNode>();
		stacksWithNonTerminalsToReduce = new DoubleStack<AbstractStackNode, AbstractContainerNode>();
		
		lastExpects = new ArrayList<AbstractStackNode[]>();
		cachedEdgesForExpect = new HashMap<String, ArrayList<AbstractStackNode>>();
		
		sharedNextNodes = new IntegerKeyedHashMap<AbstractStackNode>();
		
		resultStoreCache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<String, AbstractContainerNode>>();
		
		location = 0;
		shiftedLevel = false;
		
		methodCache = new HashMap<String, Method>();
		
		sharedLastExpects = new LinearIntegerKeyedMap<AbstractStackNode>();
		sharedPrefixNext = new LinearIntegerKeyedMap<AbstractStackNode>();
		
		propagatedPrefixes = new LinearIntegerKeyedMap<IntegerList>();
		propagatedReductions = new LinearIntegerKeyedMap<IntegerList>();
		
		unexpandableNodes = new Stack<AbstractStackNode>();
		unmatchableNodes = new Stack<AbstractStackNode>();
		filteredNodes = new DoubleStack<AbstractStackNode, AbstractContainerNode>();
	}
	
	protected void expect(IConstructor production, AbstractStackNode... symbolsToExpect){
		lastExpects.add(symbolsToExpect);
		
		AbstractStackNode lastNode = symbolsToExpect[symbolsToExpect.length - 1];
		lastNode.setParentProduction(production);
	}
	
	protected void expectReject(IConstructor production, AbstractStackNode... symbolsToExpect){
		lastExpects.add(symbolsToExpect);
		
		AbstractStackNode lastNode = symbolsToExpect[symbolsToExpect.length - 1];
		lastNode.setParentProduction(production);
		lastNode.markAsReject();
	}
	
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
				int errorLocation = (location == Integer.MAX_VALUE ? 0 : location);
				int line = positionStore.findLine(errorLocation);
				int column = positionStore.getColumn(errorLocation, line);
				throw new UndeclaredNonTerminalError(name, VF.sourceLocation(inputURI, errorLocation, 0, line + 1, line + 1, column, column), nsmex);
			}
			methodCache.putUnsafe(name, method);
		}
		
		try{
			method.invoke(this);
		}catch(IllegalAccessException iaex){
			throw new ImplementationError(iaex.getMessage(), iaex);
		}catch(InvocationTargetException itex){
			throw new ImplementationError(itex.getTargetException().getMessage(), itex.getTargetException());
		} 
	}
	
	private AbstractStackNode updateNextNode(AbstractStackNode next, AbstractStackNode node, AbstractNode result){
		AbstractStackNode alternative = sharedNextNodes.get(next.getId());
		if(alternative != null){
			if(result.isEmpty()){
				if(alternative.getId() != node.getId() && !(alternative.isSeparator() || node.isSeparator())){ // (Separated) list cycle fix.
					if(alternative.isMatchable()){
						if(alternative.isEmptyLeafNode()){
							propagateEdgesAndPrefixes(node, result, alternative, alternative.getResult(), node.getEdges().size());
							return alternative;
						}
					}else{
						ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(location);
						AbstractContainerNode nextResult = levelResultStoreMap.get(alternative.getName(), getResultStoreId(alternative.getId()));
						if(nextResult != null){
							propagateEdgesAndPrefixes(node, result, alternative, nextResult, node.getEdges().size());
							return alternative;
						}
					}
				}
			}
			
			alternative.updateNode(node, result);
			
			return alternative;
		}
		
		next = next.getCleanCopy();
		next.setStartLocation(location);
		next.updateNode(node, result);
		
		sharedNextNodes.putUnsafe(next.getId(), next);
		stacksToExpand.push(next);
		
		return next;
	}
	
	private void updateAlternativeNextNode(AbstractStackNode node, AbstractStackNode next, AbstractNode result, LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap, ArrayList<Link>[] prefixesMap){
		int id = next.getId();
		AbstractStackNode alternative = sharedNextNodes.get(id);
		if(alternative != null){
			if(result.isEmpty()){
				if(alternative.getId() != node.getId() && !(alternative.isSeparator() || node.isSeparator())){ // (Separated) list cycle fix.
					if(alternative.isMatchable()){
						if(alternative.isEmptyLeafNode()){
							// Encountered stack 'overtake'.
							propagateAlternativeEdgesAndPrefixes(node, result, alternative, alternative.getResult(), edgesMap.size(), edgesMap, prefixesMap);
							return;
						}
					}else{
						ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(location);
						AbstractContainerNode nextResult = levelResultStoreMap.get(alternative.getName(), getResultStoreId(alternative.getId()));
						if(nextResult != null){
							// Encountered stack 'overtake'.
							propagateAlternativeEdgesAndPrefixes(node, result, alternative, nextResult, edgesMap.size(), edgesMap, prefixesMap);
							return;
						}
					}
				}
			}
			
			alternative.updatePrefixSharedNode(edgesMap, prefixesMap); // Prevent unnecessary overhead; share whenever possible.
		}else{
			next = next.getCleanCopy();
			next.updatePrefixSharedNode(edgesMap, prefixesMap); // Prevent unnecessary overhead; share whenever possible.
			next.setStartLocation(location);
			
			sharedNextNodes.putUnsafe(id, next);
			stacksToExpand.push(next);
		}
	}
	
	private void propagateReductions(AbstractStackNode node, AbstractNode nodeResultStore, AbstractStackNode next, AbstractNode nextResultStore, int potentialNewEdges){
		IntegerList touched = propagatedReductions.findValue(next.getId());
		if(touched == null){
			touched = new IntegerList();
			propagatedReductions.add(next.getId(), touched);
		}
		
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
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
			
			if(touched.contains(startLocation)) continue;
			touched.add(startLocation);
			
			ArrayList<Link> edgePrefixes = new ArrayList<Link>();
			Link prefix = (prefixes != null) ? new Link(prefixes[i], nodeResultStore) : new Link(null, nodeResultStore);
			edgePrefixes.add(prefix);
			
			Link resultLink = new Link(edgePrefixes, nextResultStore);
			
			if(!hasNestingRestrictions){
				handleEdgeList(edgesMap.getValue(i), production, resultLink, startLocation);
			}else{
				handleEdgeListWithPriorities(edgesMap.getValue(i), name, production, resultLink, filteredParents, startLocation);
			}
		}
	}
	
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
			
			// Handle alternative nexts (and prefix sharing).
			ArrayList<AbstractStackNode[]> alternateProds = node.getAlternateProductions();
			if(alternateProds != null){
				sharedPrefixNext.dirtyClear();
				
				sharedPrefixNext.add(next.getId(), next);
				
				LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> nextEdgesMap = next.getEdges();
				ArrayList<Link>[] nextPrefixesMap = next.getPrefixesMap();
				
				for(int i = alternateProds.size() - 1; i >= 0; --i){
					prod = alternateProds.get(i);
					if(nextDot == prod.length) continue;
					AbstractStackNode alternativeNext = prod[nextDot];
					int alternativeNextId = alternativeNext.getId();
					
					AbstractStackNode sharedNext = sharedPrefixNext.findValue(alternativeNextId);
					if(sharedNext == null){
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
						
						sharedPrefixNext.add(alternativeNextId, alternativeNext);
					}
				}
			}
		}
	}
	
	private void propagateAlternativeEdgesAndPrefixes(AbstractStackNode node, AbstractNode nodeResult, AbstractStackNode next, AbstractNode nextResult, int potentialNewEdges, LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap, ArrayList<Link>[] prefixesMap){
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

			// Handle alternative nexts (and prefix sharing).
			ArrayList<AbstractStackNode[]> alternateProds = node.getAlternateProductions();
			if(alternateProds != null){
				sharedPrefixNext.dirtyClear();
				
				sharedPrefixNext.add(next.getId(), next);
				
				LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> nextEdgesMap = next.getEdges();
				ArrayList<Link>[] nextPrefixesMap = next.getPrefixesMap();
				
				for(int i = alternateProds.size() - 1; i >= 0; --i){
					prod = alternateProds.get(i);
					if(nextDot == prod.length) continue;
					AbstractStackNode alternativeNext = prod[nextDot];
					int alternativeNextId = alternativeNext.getId();
					
					AbstractStackNode sharedNext = sharedPrefixNext.findValue(alternativeNextId);
					if(sharedNext == null){
						AbstractStackNode nextNextAltAlternative = sharedNextNodes.get(alternativeNext.getId());
						
						if(nextNextAltAlternative.isEmptyLeafNode()){
							propagateAlternativeEdgesAndPrefixes(next, nextResult, nextNextAltAlternative, nextNextAltAlternative.getResult(), potentialNewEdges, nextEdgesMap, nextPrefixesMap);
						}else{
							AbstractContainerNode nextAltResultStore = levelResultStoreMap.get(nextNextAltAlternative.getName(), getResultStoreId(nextNextAltAlternative.getId()));
							if(nextAltResultStore != null){
								propagateAlternativeEdgesAndPrefixes(next, nextResult, nextNextAltAlternative, nextAltResultStore, potentialNewEdges, nextEdgesMap, nextPrefixesMap);
							}else{
								nextNextAltAlternative.updatePrefixSharedNode(nextEdgesMap, nextPrefixesMap);
							}
						}
						
						sharedPrefixNext.add(alternativeNextId, alternativeNext);
					}
				}
			}
		}
	}
	
	private void updateEdges(AbstractStackNode node, AbstractNode result){
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		IConstructor production = node.getParentProduction();
		String name = edgesMap.getValue(0).get(0).getName();
		
		boolean hasNestingRestrictions = hasNestingRestrictions(name);
		IntegerList filteredParents = null;
		if(hasNestingRestrictions){
			filteredParents = getFilteredParents(node.getId());
		}
		
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			Link resultLink = new Link((prefixesMap != null) ? prefixesMap[i] : null, result);
			
			if(!hasNestingRestrictions){
				handleEdgeList(edgesMap.getValue(i), production, resultLink, edgesMap.getKey(i));
			}else{
				handleEdgeListWithPriorities(edgesMap.getValue(i), name, production, resultLink, filteredParents, edgesMap.getKey(i));
			}
		}
	}
	
	private void updateNullableEdges(AbstractStackNode node, AbstractNode result){
		IntegerList touched = propagatedReductions.findValue(node.getId());
		if(touched == null){
			touched = new IntegerList();
			propagatedReductions.add(node.getId(), touched);
		}
		
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		IConstructor production = node.getParentProduction();
		String name = edgesMap.getValue(0).get(0).getName();
		
		boolean hasNestingRestrictions = hasNestingRestrictions(name);
		IntegerList filteredParents = null;
		if(hasNestingRestrictions){
			filteredParents = getFilteredParents(node.getId());
		}
		
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			int startLocation = edgesMap.getKey(i);
			
			if(touched.contains(startLocation)) continue;
			touched.add(startLocation);
			
			Link resultLink = new Link((prefixesMap != null) ? prefixesMap[i] : null, result);
			
			if(!hasNestingRestrictions){
				handleEdgeList(edgesMap.getValue(i), production, resultLink, startLocation);
			}else{
				handleEdgeListWithPriorities(edgesMap.getValue(i), name, production, resultLink, filteredParents, startLocation);
			}
		}
	}
	
	private void handleEdgeList(ArrayList<AbstractStackNode> edgeList, IConstructor production, Link resultLink, int startLocation){
		ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(startLocation);
		
		if(levelResultStoreMap == null){
			levelResultStoreMap = new ObjectIntegerKeyedHashMap<String, AbstractContainerNode>();
			resultStoreCache.putUnsafe(startLocation, levelResultStoreMap);
		}
		
		AbstractStackNode edge = edgeList.get(0);
		String nodeName = edge.getName();
		
		AbstractContainerNode resultStore = levelResultStoreMap.get(nodeName, DEFAULT_RESULT_STORE_ID);
		if(resultStore != null){
			if(!resultStore.isRejected()) resultStore.addAlternative(production, resultLink);
		}else{
			resultStore = (!edge.isExpandable()) ? new SortContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout()) : new ListContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout());
			levelResultStoreMap.putUnsafe(nodeName, DEFAULT_RESULT_STORE_ID, resultStore);
			resultStore.addAlternative(production, resultLink);
			
			stacksWithNonTerminalsToReduce.push(edge, resultStore);
			
			for(int j = edgeList.size() - 1; j >= 1; --j){
				edge = edgeList.get(j);
				stacksWithNonTerminalsToReduce.push(edge, resultStore);
			}
		}
	}
	
	private void handleEdgeListWithPriorities(ArrayList<AbstractStackNode> edgeList, String name, IConstructor production, Link resultLink, IntegerList filteredParents, int startLocation){
		ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(startLocation);
		
		if(levelResultStoreMap == null){
			levelResultStoreMap = new ObjectIntegerKeyedHashMap<String, AbstractContainerNode>();
			resultStoreCache.putUnsafe(startLocation, levelResultStoreMap);
		}
		
		IntegerKeyedHashMap<AbstractContainerNode> firstTimeReductions = new IntegerKeyedHashMap<AbstractContainerNode>();
		IntegerList firstTimeRegistration = new IntegerList();
		for(int j = edgeList.size() - 1; j >= 0; --j){
			AbstractStackNode edge = edgeList.get(j);
			int resultStoreId = getResultStoreId(edge.getId());
			
			AbstractContainerNode resultStore = firstTimeReductions.get(resultStoreId);
			if(resultStore == null){
				if(firstTimeRegistration.contains(resultStoreId)) continue;
				firstTimeRegistration.add(resultStoreId);
				
				if(filteredParents == null || !filteredParents.contains(edge.getId())){
					resultStore = levelResultStoreMap.get(name, resultStoreId);
					if(resultStore != null){
						if(!resultStore.isRejected()) resultStore.addAlternative(production, resultLink);
					}else{
						resultStore = (!edge.isExpandable()) ? new SortContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout()) : new ListContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout());
						levelResultStoreMap.putUnsafe(name, resultStoreId, resultStore);
						resultStore.addAlternative(production, resultLink);
						
						stacksWithNonTerminalsToReduce.push(edge, resultStore);
						firstTimeReductions.putUnsafe(resultStoreId, resultStore);
					}
				}
			}else{
				stacksWithNonTerminalsToReduce.push(edge, resultStore);
			}
		}
	}
	
	private void updateRejects(AbstractStackNode node){
		IntegerList filteredParents = getFilteredParents(node.getId());
		
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			handleRejectedEdgeListWithPriorities(edgesMap.getValue(i), filteredParents, edgesMap.getKey(i));
		}
	}
	
	private void updateNullableRejects(AbstractStackNode node){
		IntegerList touched = propagatedReductions.findValue(node.getId());
		if(touched == null){
			touched = new IntegerList();
			propagatedReductions.add(node.getId(), touched);
		}

		IntegerList filteredParents = getFilteredParents(node.getId());
		
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			int startLocation = edgesMap.getKey(i);

			if(touched.contains(startLocation)) continue;
			touched.add(startLocation);
			
			handleRejectedEdgeListWithPriorities(edgesMap.getValue(i), filteredParents, startLocation);
		}
	}
	
	private void handleRejectedEdgeListWithPriorities(ArrayList<AbstractStackNode> edgeList, IntegerList filteredParents, int startLocation){
		ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(startLocation);
		
		if(levelResultStoreMap == null){
			levelResultStoreMap = new ObjectIntegerKeyedHashMap<String, AbstractContainerNode>();
			resultStoreCache.putUnsafe(startLocation, levelResultStoreMap);
		}
		
		IntegerKeyedHashMap<AbstractContainerNode> firstTimeReductions = new IntegerKeyedHashMap<AbstractContainerNode>();
		IntegerList firstTimeRegistration = new IntegerList();
		for(int j = edgeList.size() - 1; j >= 0; --j){
			AbstractStackNode edge = edgeList.get(j);
			String nodeName = edge.getName();
			int resultStoreId = getResultStoreId(edge.getId());
			
			AbstractContainerNode resultStore = firstTimeReductions.get(resultStoreId);
			if(resultStore == null){
				if(firstTimeRegistration.contains(resultStoreId)) continue;
				firstTimeRegistration.add(resultStoreId);
				
				if(filteredParents == null || !filteredParents.contains(edge.getId())){
					resultStore = levelResultStoreMap.get(nodeName, resultStoreId);
					if(resultStore != null){
						resultStore.setRejected();
					}else{
						resultStore = (!edge.isExpandable()) ? new SortContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout()) : new ListContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout());
						levelResultStoreMap.putUnsafe(nodeName, resultStoreId, resultStore);
						resultStore.setRejected();
						
						firstTimeReductions.putUnsafe(resultStoreId, resultStore);
					}
					filteredNodes.push(edge, resultStore);
				}
			}
		}
	}
	
	private void moveToNext(AbstractStackNode node, AbstractNode result){
		int nextDot = node.getDot() + 1;

		AbstractStackNode[] prod = node.getProduction();
		AbstractStackNode next = prod[nextDot];
		next.setProduction(prod);
		next = updateNextNode(next, node, result);
		
		ArrayList<AbstractStackNode[]> alternateProds = node.getAlternateProductions();
		if(alternateProds != null){
			int nextNextDot = nextDot + 1;
			
			// Handle alternative nexts (and prefix sharing).
			sharedPrefixNext.dirtyClear();
			
			sharedPrefixNext.add(next.getId(), next);
			
			LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = next.getEdges();
			ArrayList<Link>[] prefixesMap = next.getPrefixesMap();
			
			for(int i = alternateProds.size() - 1; i >= 0; --i){
				prod = alternateProds.get(i);
				if(nextDot == prod.length) continue;
				AbstractStackNode alternativeNext = prod[nextDot];
				int alternativeNextId = alternativeNext.getId();
				
				AbstractStackNode sharedNext = sharedPrefixNext.findValue(alternativeNextId);
				if(sharedNext == null){
					alternativeNext.setProduction(prod);
					updateAlternativeNextNode(node, alternativeNext, result, edgesMap, prefixesMap);
					
					sharedPrefixNext.add(alternativeNextId, alternativeNext);
				}else if(nextNextDot < prod.length){
					if(alternativeNext.isEndNode()){
						sharedNext.markAsEndNode();
						sharedNext.setParentProduction(alternativeNext.getParentProduction());
						sharedNext.setFollowRestriction(alternativeNext.getFollowRestriction());
						sharedNext.setReject(alternativeNext.isReject());
					}
					
					sharedNext.addProduction(prod);
				}
			}
		}
	}
	
	private void move(AbstractStackNode node, AbstractNode result){
		ICompletionFilter[] completionFilters = node.getCompletionFilters();
		if(completionFilters != null){
			int startLocation = node.getStartLocation();
			for(int i = completionFilters.length - 1; i >= 0; --i){
				if(completionFilters[i].isFiltered(input, startLocation, location, positionStore)) return;
			}
		}
		
		if(node.isEndNode()){
			if(!result.isRejected()){
				if(!node.isReject()){
					if(!result.isEmpty() || node.getId() == IExpandableStackNode.DEFAULT_LIST_EPSILON_ID){ // Handle special list case.
						updateEdges(node, result);
					}else{
						updateNullableEdges(node, result);
					}
				}else{
					if(!result.isEmpty() || node.getId() == IExpandableStackNode.DEFAULT_LIST_EPSILON_ID){ // Handle special list case.
						updateRejects(node);
					}else{
						updateNullableRejects(node);
					}
				}
			}
		}
		
		if(node.hasNext()){
			moveToNext(node, result);
		}
	}
	
	private void reduceTerminal(AbstractStackNode terminal){
		move(terminal, terminal.getResult());
	}
	
	private void reduceNonTerminal(AbstractStackNode nonTerminal, AbstractContainerNode result){
		// Filtering
		if(nonTerminal.isReductionFiltered(input, location)){
			filteredNodes.push(nonTerminal, result);
			return;
		}
		
		move(nonTerminal, result);
	}
	
	private void reduce(){
		// Reduce terminals.
		while(!stacksWithTerminalsToReduce.isEmpty()){
			AbstractStackNode terminal = stacksWithTerminalsToReduce.pop();
			reduceTerminal(terminal);
		}
		
		// Reduce non-terminals.
		while(!stacksWithNonTerminalsToReduce.isEmpty()){
			reduceNonTerminal(stacksWithNonTerminalsToReduce.peekFirst(), stacksWithNonTerminalsToReduce.popSecond());
		}
	}
	
	private boolean findFirstStackToReduce(){
		for(int i = 0; i < todoLists.length; ++i){
			Stack<AbstractStackNode> terminalsTodo = todoLists[i];
			if(!(terminalsTodo == null || terminalsTodo.isEmpty())){
				stacksWithTerminalsToReduce = terminalsTodo;
				
				location += i;
				shiftedLevel = (location != 0);
				
				queueIndex = i;
				
				return true;
			}
		}
		return false;
	}
	
	private boolean findStacksToReduce(){
		if(!stacksWithTerminalsToReduce.isEmpty()){
			shiftedLevel = false;
			return true;
		}
		
		int queueDepth = todoLists.length;
		for(int i = 1; i < queueDepth; ++i){
			queueIndex = (queueIndex + 1) % queueDepth;
			
			Stack<AbstractStackNode> terminalsTodo = todoLists[queueIndex];
			if(!(terminalsTodo == null || terminalsTodo.isEmpty())){
				stacksWithTerminalsToReduce = terminalsTodo;
				
				shiftedLevel = true;
				location += i;
				
				return true;
			}
		}
		return false;
	}
	
	private boolean shareListNode(int id, AbstractStackNode stack){
		AbstractStackNode sharedNode = sharedNextNodes.get(id);
		if(sharedNode != null){
			sharedNode.addEdgeWithPrefix(stack, null, location);
			return true;
		}
		return false;
	}
	
	private void handleExpects(AbstractStackNode stackBeingWorkedOn){
		sharedLastExpects.dirtyClear();
		
		ArrayList<AbstractStackNode> cachedEdges = null;
		
		int nrOfExpects = lastExpects.size();
		if(nrOfExpects == 0){ // Error reporting.
			unexpandableNodes.push(stackBeingWorkedOn);
			return;
		}
		
		for(int i = nrOfExpects - 1; i >= 0; --i){
			AbstractStackNode[] expectedNodes = lastExpects.get(i);
			
			AbstractStackNode last = expectedNodes[expectedNodes.length - 1];
			last.markAsEndNode();
			
			AbstractStackNode first = expectedNodes[0];
			
			// Handle prefix sharing.
			int firstId = first.getId();
			AbstractStackNode sharedNode;
			if((sharedNode = sharedLastExpects.findValue(firstId)) != null){
				sharedNode.addProduction(expectedNodes);
				if(expectedNodes.length == 1){
					sharedNode.markAsEndNode();
					sharedNode.setParentProduction(last.getParentProduction());
					sharedNode.setFollowRestriction(last.getFollowRestriction());
					sharedNode.setReject(last.isReject());
				}
				continue;
			}
			
			first = first.getCleanCopy();
			first.setStartLocation(location);
			first.setProduction(expectedNodes);
			first.initEdges();
			
			if(cachedEdges == null){
				cachedEdges = first.addEdge(stackBeingWorkedOn);
			}else{
				first.addEdges(cachedEdges, location);
			}
			
			sharedLastExpects.add(firstId, first);
			
			stacksToExpand.push(first);
		}
		
		cachedEdgesForExpect.put(stackBeingWorkedOn.getName(), cachedEdges);
	}
	
	protected boolean hasNestingRestrictions(String name){
		return false; // Priority and associativity filtering is off by default.
	}
	
	protected IntegerList getFilteredParents(int childId){
		return null; // Default implementation; intended to be overwritten in sub-classes.
	}
	
	protected int getResultStoreId(int parentId){
		return DEFAULT_RESULT_STORE_ID; // Default implementation; intended to be overwritten in sub-classes.
	}
	
	private void expandStack(AbstractStackNode stack){
		IEnterFilter[] enterFilters = stack.getEnterFilters();
		if(enterFilters != null){
			for(int i = enterFilters.length - 1; i >= 0; --i){
				if(enterFilters[i].isFiltered(input, location, positionStore)) return;
			}
		}
		
		if(stack.isMatchable()){
			int length = stack.getLength();
			int endLocation = location + length;
			if(endLocation <= input.length){
				if(!stack.match(input)){
					unmatchableNodes.push(stack);
					return;
				}
				
				// Filtering
				if(stack.isReductionFiltered(input, endLocation)) return;
				
				int queueDepth = todoLists.length;
				if(length >= queueDepth){
					Stack<AbstractStackNode>[] oldTodoLists = todoLists;
					todoLists = new Stack[length + 1];
					System.arraycopy(oldTodoLists, queueIndex, todoLists, 0, queueDepth - queueIndex);
					System.arraycopy(oldTodoLists, 0, todoLists, queueDepth - queueIndex, queueIndex);
					queueDepth = length + 1;
					queueIndex = 0;
				}
				
				int insertLocation = (queueIndex + length) % queueDepth;
				Stack<AbstractStackNode> terminalsTodo = todoLists[insertLocation];
				if(terminalsTodo == null){
					terminalsTodo = new Stack<AbstractStackNode>();
					todoLists[insertLocation] = terminalsTodo;
				}
				terminalsTodo.push(stack);
			}else{
				unexpandableNodes.push(stack);
			}
			
			return;
		}
		
		if(!stack.isExpandable()){
			ArrayList<AbstractStackNode> cachedEdges = cachedEdgesForExpect.get(stack.getName());
			if(cachedEdges != null){
				cachedEdges.add(stack);
				
				ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(location);
				if(levelResultStoreMap != null){
					AbstractContainerNode resultStore = levelResultStoreMap.get(stack.getName(), getResultStoreId(stack.getId()));
					if(resultStore != null){ // Is nullable, add the known results.
						stacksWithNonTerminalsToReduce.push(stack, resultStore);
					}
				}
			}else{
				invokeExpects(stack);
				handleExpects(stack);
			}
		}else{ // List
			AbstractStackNode[] listChildren = stack.getChildren();
			
			for(int i = listChildren.length - 1; i >= 0; --i){
				AbstractStackNode child = listChildren[i];
				int childId = child.getId();
				if(!shareListNode(childId, stack)){
					child = child.getCleanCopy();
					
					sharedNextNodes.putUnsafe(childId, child);
					
					child.setStartLocation(location);
					child.initEdges();
					child.addEdgeWithPrefix(stack, null, location);
					
					stacksToExpand.push(child);
				}
			}
			
			if(stack.canBeEmpty()){ // Star list or optional.
				// This is always epsilon (and unique for this position); so shouldn't be shared.
				AbstractStackNode empty = stack.getEmptyChild().getCleanCopy();
				empty.setStartLocation(location);
				empty.initEdges();
				empty.addEdge(stack);
				
				stacksToExpand.push(empty);
			}
		}
	}
	
	private void expand(){
		while(!stacksToExpand.isEmpty()){
			lastExpects.dirtyClear();
			expandStack(stacksToExpand.pop());
		}
	}
	
	protected boolean isAtEndOfInput(){
		return (location == input.length);
	}
	
	protected boolean isInLookAhead(char[][] ranges, char[] characters){
		if(location == input.length) return false;
		
		for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			if(lookAheadChar >= range[0] && lookAheadChar <= range[1]) return true;
		}
		
		for(int i = characters.length - 1; i >= 0; --i){
			if(lookAheadChar == characters[i]) return true;
		}
		
		return false;
	}
	
	protected AbstractNode parse(AbstractStackNode startNode, URI inputURI, char[] input, IActionExecutor actionExecutor){
		if(invoked){
			throw new RuntimeException("Can only invoke 'parse' once.");
		}
		invoked = true;
		
		// Initialize.
		this.startNode = startNode;
		this.inputURI = inputURI;
		this.input = input;
		this.actionExecutor = actionExecutor;
		
		positionStore.index(input);
		
		todoLists = new Stack[DEFAULT_TODOLIST_CAPACITY];
		
		AbstractStackNode rootNode = startNode.getCleanCopy();
		rootNode.setStartLocation(0);
		rootNode.initEdges();
		stacksToExpand.push(rootNode);
		lookAheadChar = (input.length > 0) ? input[0] : 0;
		expand();
		
		if(findFirstStackToReduce()){
			do{
				lookAheadChar = (location < input.length) ? input[location] : 0;
				if(shiftedLevel){ // Nullable fix.
					sharedNextNodes.clear();
					resultStoreCache.clear();
					cachedEdgesForExpect.clear();
					
					propagatedPrefixes.dirtyClear();
					propagatedReductions.dirtyClear();
					
					unexpandableNodes.dirtyClear();
					unmatchableNodes.dirtyClear();
					filteredNodes.dirtyClear();
				}
				
				do{
					reduce();
					
					expand();
				}while(!stacksWithNonTerminalsToReduce.isEmpty());
			}while(findStacksToReduce());
		}
		
		if(location == input.length){
			ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(0);
			if(levelResultStoreMap != null){
				AbstractContainerNode result = levelResultStoreMap.get(startNode.getName(), getResultStoreId(startNode.getId()));
				if(!(result == null || result.isRejected())){
					return result;
				}
			}
		}
		
		// Parse error.
		parseErrorOccured = true;
		
		int errorLocation = (location == Integer.MAX_VALUE ? 0 : location);
		int line = positionStore.findLine(errorLocation);
		int column = positionStore.getColumn(errorLocation, line);
		throw new SyntaxError("Parse error.", VF.sourceLocation(inputURI, Math.min(errorLocation, input.length - 1), 0, line + 1, line + 1, column, column));
	}
	
	// With post parse filtering.
	public IConstructor parse(String nonterminal, URI inputURI, char[] input, IActionExecutor actionExecutor){
		AbstractNode result = parse(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, nonterminal), inputURI, input, actionExecutor);
		return buildTree(result);
	}
	
	// Without post parse filtering.
	public IConstructor parse(String nonterminal, URI inputURI, char[] input){
		AbstractNode result = parse(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, nonterminal), inputURI, input, new VoidActionExecutor());
		return buildTree(result);
	}
	
	protected IConstructor parse(AbstractStackNode startNode, URI inputURI, char[] input){
		AbstractNode result = parse(startNode, inputURI, input, new VoidActionExecutor());
		return buildTree(result);
	}
	
	protected IConstructor buildTree(AbstractNode result){
		FilteringTracker filteringTracker = new FilteringTracker();
		// Invoke the forest flattener, a.k.a. "the bulldozer".
		IEnvironment rootEnvironment = actionExecutor.createRootEnvironment();
		IConstructor resultTree = null;
		try{
			NodeToUPTR converter = new NodeToUPTR(result, positionStore);
			resultTree = converter.convertToUPTR(filteringTracker, actionExecutor, rootEnvironment);
		}finally{
			actionExecutor.completed(rootEnvironment, (resultTree == null));
		}
		if(resultTree != null){
			return resultTree; // Success.
		}
		
		// Filtering error.
		filterErrorOccured = true;
		
		int line = positionStore.findLine(filteringTracker.offset);
		int column = positionStore.getColumn(filteringTracker.offset, line);
		int endLine = positionStore.findLine(filteringTracker.endOffset);
		int endColumn = positionStore.getColumn(filteringTracker.endOffset, endLine);
		throw new SyntaxError("All trees were filtered.", VF.sourceLocation(inputURI, Math.min(filteringTracker.offset, input.length - 1), (filteringTracker.endOffset - filteringTracker.offset + 1), line + 1, endLine + 1, column, endColumn));
	}
	
	public IConstructor buildErrorTree(){
		if(parseErrorOccured){
			ErrorTreeBuilder errorTreeBuilder = new ErrorTreeBuilder(this, startNode, positionStore, actionExecutor, input, location, inputURI);
			return errorTreeBuilder.buildErrorTree(unexpandableNodes, unmatchableNodes, filteredNodes);
		}
		
		if(filterErrorOccured){
			ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(0);
			AbstractContainerNode result = levelResultStoreMap.get(startNode.getName(), getResultStoreId(startNode.getId()));
			// Invoke "the bulldozer" that constructs error trees while it's flattening the forest.
			IEnvironment rootEnvironment = actionExecutor.createRootEnvironment();
			try{
				NodeToUPTR converter = new NodeToUPTR(result, positionStore);
				return converter.convertToUPTRWithErrors(actionExecutor, rootEnvironment);
			}finally{
				actionExecutor.completed(rootEnvironment, true);
			}
		}
		
		throw new RuntimeException("Cannot build an error tree as no parse error occurred.");
	}
}
