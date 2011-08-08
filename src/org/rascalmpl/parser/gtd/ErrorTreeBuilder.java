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

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.result.error.ErrorListContainerNode;
import org.rascalmpl.parser.gtd.result.error.ErrorSortContainerNode;
import org.rascalmpl.parser.gtd.result.error.ExpectedNode;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashSet;
import org.rascalmpl.parser.gtd.util.Stack;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class ErrorTreeBuilder{
	private final static CharNode[] NO_CHILDREN = new CharNode[]{};
	
	private final SGTDBF parser;
	private final AbstractStackNode startNode;
	
	private final char[] input;
	private final int location;
	private final URI inputURI;
	
	private final DoubleStack<AbstractStackNode, AbstractNode> errorNodes;
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<String, AbstractContainerNode>> errorResultStoreCache;
	
	public ErrorTreeBuilder(SGTDBF parser, AbstractStackNode startNode, char[] input, int location, URI inputURI){
		super();
		
		this.parser = parser;
		this.startNode = startNode;
		
		this.input = input;
		this.location = location;
		this.inputURI = inputURI;
		
		errorNodes = new DoubleStack<AbstractStackNode, AbstractNode>();
		errorResultStoreCache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<String,AbstractContainerNode>>();
	}
	
	private AbstractStackNode updateNextNode(AbstractStackNode next, AbstractStackNode node, AbstractNode result){
		next = next.getCleanCopy(location);
		next.updateNode(node, result);
		
		IConstructor nextSymbol = findSymbol(next);
		AbstractNode resultStore = new ExpectedNode(NO_CHILDREN, nextSymbol, inputURI, location, location, next.isSeparator(), next.isLayout());
		
		errorNodes.push(next, resultStore);
		
		return next;
	}
	
	private void updateAlternativeNextNode(AbstractStackNode next, IntegerObjectList<ArrayList<AbstractStackNode>> edgesMap, ArrayList<Link>[] prefixesMap){
		next = next.getCleanCopy(location);
		next.updatePrefixSharedNode(edgesMap, prefixesMap); // Prevent unnecessary overhead; share whenever possible.
		
		IConstructor nextSymbol = findSymbol(next);
		AbstractNode resultStore = new ExpectedNode(NO_CHILDREN, nextSymbol, inputURI, location, location, next.isSeparator(), next.isLayout());
		
		errorNodes.push(next, resultStore);
	}
	
	private void moveToNext(AbstractStackNode node, AbstractNode result){
		int nextDot = node.getDot() + 1;

		AbstractStackNode[] prod = node.getProduction();
		AbstractStackNode next = prod[nextDot];
		next = updateNextNode(next, node, result);
		
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
					updateAlternativeNextNode(newAlternativeNext, edgesMap, prefixesMap);
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
	
	private boolean followEdges(AbstractStackNode node, AbstractNode result){
		IConstructor production = (IConstructor) node.getParentProduction();
		
		boolean wasListChild = ProductionAdapter.isRegular(production);
		
		//IntegerList filteredParents = parser.getFilteredParents(node.getId());
		
		IntegerObjectList<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			int startLocation = edgesMap.getKey(i);
			ArrayList<AbstractStackNode> edgeList = edgesMap.getValue(i);
			
			ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = errorResultStoreCache.get(startLocation);
			
			if(levelResultStoreMap == null){
				levelResultStoreMap = new ObjectIntegerKeyedHashMap<String, AbstractContainerNode>();
				errorResultStoreCache.putUnsafe(startLocation, levelResultStoreMap);
			}
			
			Link resultLink = new Link((prefixesMap != null) ? prefixesMap[i] : null, result);
			
			ObjectIntegerKeyedHashMap<String, AbstractContainerNode> firstTimeReductions = new ObjectIntegerKeyedHashMap<String, AbstractContainerNode>();
			ObjectIntegerKeyedHashSet<String> firstTimeRegistration = new ObjectIntegerKeyedHashSet<String>();
			for(int j = edgeList.size() - 1; j >= 0; --j){
				AbstractStackNode edge = edgeList.get(j);
				String nodeName = edge.getName();
				int resultStoreId = -1;//parser.getResultStoreId(edge.getId());
				
				AbstractContainerNode resultStore = firstTimeReductions.get(nodeName, resultStoreId);
				if(resultStore == null){
					if(firstTimeRegistration.contains(nodeName, resultStoreId)) continue;
					firstTimeRegistration.putUnsafe(nodeName, resultStoreId);
					
					//if(filteredParents == null || !filteredParents.contains(edge.getId())){
						resultStore = levelResultStoreMap.get(nodeName, resultStoreId);
						if(resultStore != null){
							resultStore.addAlternative(production, resultLink);
						}else{
							resultStore = (!edge.isExpandable()) ? new ErrorSortContainerNode(inputURI, startLocation, location, edge.isSeparator(), edge.isLayout()) : new ErrorListContainerNode(inputURI, startLocation, location, edge.isSeparator(), edge.isLayout());
							levelResultStoreMap.putUnsafe(nodeName, resultStoreId, resultStore);
							resultStore.addAlternative(production, resultLink);
							
							errorNodes.push(edge, resultStore);
							firstTimeReductions.putUnsafe(nodeName, resultStoreId, resultStore);
						}
					//}
				}else{
					errorNodes.push(edge, resultStore);
				}
			}
		}
		
		return wasListChild;
	}
	
	private void move(AbstractStackNode node, AbstractNode result){
		boolean handleNexts = true;
		if(node.isEndNode()){
			handleNexts = !followEdges(node, result);
		}
		
		if(handleNexts && node.hasNext()){
			moveToNext(node, result);
		}
	}
	
	private IConstructor getParentSymbol(AbstractStackNode node){
		AbstractStackNode[] production = node.getProduction();
		AbstractStackNode last = production[production.length - 1];
		return ProductionAdapter.getType((IConstructor) last.getParentProduction());
	}
	
	private IConstructor findSymbol(AbstractStackNode node){
		AbstractStackNode[] production = node.getProduction();
		AbstractStackNode last = production[production.length - 1];
		
		int dot = node.getDot();
		
		IConstructor prod = (IConstructor) last.getParentProduction();
		if(!ProductionAdapter.isRegular(prod)){
			IList lhs = ProductionAdapter.getSymbols(prod);
			return (IConstructor) lhs.get(dot);
		}
		
		// Regular
		IConstructor rhs = ProductionAdapter.getType(prod);
		
		if(SymbolAdapter.isOpt(rhs)){
			return (IConstructor) rhs.get("symbol");
		}
		
		if(SymbolAdapter.isAnyList(rhs)){
			IConstructor symbol = (IConstructor) rhs.get("symbol");
			if(dot == 0){
				return symbol;
			}
			
			if(SymbolAdapter.isIterPlusSeps(rhs) || SymbolAdapter.isIterStarSeps(rhs)){
				IList separators = (IList) rhs.get("separators");
				return (IConstructor) separators.get(dot - 1);
			}
		}
		
		if(SymbolAdapter.isSequence(rhs)){
			IList symbols = (IList) rhs.get("symbols");
			return (IConstructor) symbols.get(dot);
		}
		
		if(SymbolAdapter.isAlternative(rhs)){
			throw new RuntimeException("Retrieving the correct symbol from alternatives is not possible.");
		}
		
		throw new RuntimeException("Unknown type of production: "+prod);
	}
	
	AbstractContainerNode buildErrorTree(Stack<AbstractStackNode> unexpandableNodes, Stack<AbstractStackNode> unmatchableNodes, DoubleStack<AbstractStackNode, AbstractNode> filteredNodes){
		while(!unexpandableNodes.isEmpty()){
			AbstractStackNode unexpandableNode = unexpandableNodes.pop();
			
			IConstructor symbol = getParentSymbol(unexpandableNode);
			AbstractNode resultStore = new ExpectedNode(NO_CHILDREN, symbol, inputURI, location, location, unexpandableNode.isSeparator(), unexpandableNode.isLayout());
			
			errorNodes.push(unexpandableNode, resultStore);
		}
		
		while(!unmatchableNodes.isEmpty()){
			AbstractStackNode unmatchableNode = unmatchableNodes.pop();
			
			int length = unmatchableNode.getLength();
			
			CharNode[] children = new CharNode[length];
			for(int i = children.length - 1; i >= 0; --i){
				children[i] = CharNode.createCharNode(input[location + i]);
			}
			
			IConstructor symbol = findSymbol(unmatchableNode);
			
			AbstractNode result = new ExpectedNode(children, symbol, inputURI, location, location + length, unmatchableNode.isSeparator(), unmatchableNode.isLayout());
			
			errorNodes.push(unmatchableNode, result);
		}
		
		while(!filteredNodes.isEmpty()){
			AbstractStackNode filteredNode = filteredNodes.peekFirst();
			AbstractNode resultStore = filteredNodes.popSecond();
			
			errorNodes.push(filteredNode, resultStore);
		}
		
		while(!errorNodes.isEmpty()){
			AbstractStackNode errorStackNode = errorNodes.peekFirst();
			AbstractNode result = errorNodes.popSecond();
			
			move(errorStackNode, result);
		}
		
		// Construct the rest of the input as separate character nodes.
		CharNode[] rest = new CharNode[input.length - location];
		for(int i = input.length - 1; i >= location; --i){
			rest[i - location] = new CharNode(input[i]);
		}
		
		// Find the top node.
		ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = errorResultStoreCache.get(0);
		ErrorSortContainerNode result = (ErrorSortContainerNode) levelResultStoreMap.get(startNode.getName(), parser.getResultStoreId(startNode.getId()));
		
		// Update the top node with the rest of the string (it will always be a sort node).
		result.setUnmatchedInput(rest);
	
		return result;
	}
}
