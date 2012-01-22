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

import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.result.error.ErrorListContainerNode;
import org.rascalmpl.parser.gtd.result.error.ErrorSortContainerNode;
import org.rascalmpl.parser.gtd.result.error.ExpectedNode;
import org.rascalmpl.parser.gtd.result.error.IErrorBuilderHelper;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.edge.EdgesSet;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashSet;
import org.rascalmpl.parser.gtd.util.Stack;

/**
 * This builder constructs error parse results. It attempts to deliver a tree
 * that is as complete as possible, using all available information the parser
 * possesses.
 */
public class ErrorResultBuilder{
	private final static CharNode[] NO_CHILDREN = new CharNode[]{};
	
	private final IErrorBuilderHelper errorBuilderHelper;
	
	private final SGTDBF parser;
	private final AbstractStackNode startNode;
	
	private final int[] input;
	private final int location;
	private final URI inputURI;
	
	private final DoubleStack<AbstractStackNode, AbstractNode> errorNodes;
	
	public ErrorResultBuilder(IErrorBuilderHelper errorBuilderHelper, SGTDBF parser, AbstractStackNode startNode, int[] input, int location, URI inputURI){
		super();
		
		this.errorBuilderHelper = errorBuilderHelper;
		
		this.parser = parser;
		this.startNode = startNode;
		
		this.input = input;
		this.location = location;
		this.inputURI = inputURI;
		
		errorNodes = new DoubleStack<AbstractStackNode, AbstractNode>();
	}
	
	/**
	 * Construct the possible future for the given node and continuation.
	 */
	private AbstractStackNode updateNextNode(AbstractStackNode next, AbstractStackNode node, AbstractNode result){
		next = next.getCleanCopy(location);
		next.updateNode(node, result);
		
		Object nextSymbol = findSymbol(next);
		AbstractNode resultStore = new ExpectedNode(NO_CHILDREN, nextSymbol, inputURI, location, location, next.isSeparator(), next.isLayout());
		
		errorNodes.push(next, resultStore);
		
		return next;
	}
	
	/**
	 * Construct the possible future for the given node and continuation.
	 */
	private void updateAlternativeNextNode(AbstractStackNode next, IntegerObjectList<EdgesSet> edgesMap, ArrayList<Link>[] prefixesMap){
		next = next.getCleanCopy(location);
		next.updatePrefixSharedNode(edgesMap, prefixesMap); // Prevent unnecessary overhead; share whenever possible.
		
		Object nextSymbol = findSymbol(next);
		AbstractNode resultStore = new ExpectedNode(NO_CHILDREN, nextSymbol, inputURI, location, location, next.isSeparator(), next.isLayout());
		
		errorNodes.push(next, resultStore);
	}
	
	/**
	 * Construct all possible futures for the given node.
	 */
	private void moveToNext(AbstractStackNode node, AbstractNode result){
		int nextDot = node.getDot() + 1;

		AbstractStackNode[] prod = node.getProduction();
		AbstractStackNode next = prod[nextDot];
		next = updateNextNode(next, node, result);
		
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
	
	/**
	 * Handle the reductions for the given node to complete the tree.
	 * 
	 * NOTE: nesting restrictions have been disabled for scalability reasons.
	 */
	private boolean followEdges(AbstractStackNode node, AbstractNode result){
		Object production = node.getParentProduction();
		
		boolean wasListChild = errorBuilderHelper.isListProduction(production);
		
		//IntegerList filteredParents = parser.getFilteredParents(node.getId());
		
		IntegerObjectList<EdgesSet> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			int startLocation = edgesMap.getKey(i);
			EdgesSet edgeList = edgesMap.getValue(i);
			
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
						resultStore = edgeList.getLastResult(resultStoreId);
						if(resultStore != null){
							resultStore.addAlternative(production, resultLink);
						}else{
							resultStore = (!edge.isExpandable()) ? new ErrorSortContainerNode(inputURI, startLocation, location, edge.isSeparator(), edge.isLayout()) : new ErrorListContainerNode(inputURI, startLocation, location, edge.isSeparator(), edge.isLayout());
							edgeList.setLastResult(resultStore, resultStoreId);
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
	
	/**
	 * Continue the tree for the given node.
	 */
	private void move(AbstractStackNode node, AbstractNode result){
		boolean handleNexts = true;
		if(node.isEndNode()){
			handleNexts = !followEdges(node, result);
		}
		
		if(handleNexts && node.hasNext()){
			moveToNext(node, result);
		}
	}
	
	/**
	 * Find the parent symbol for the given node.
	 */
	private Object getParentSymbol(AbstractStackNode node){
		AbstractStackNode[] production = node.getProduction();
		AbstractStackNode last = production[production.length - 1];
		return errorBuilderHelper.getLHS(last.getParentProduction());
	}
	
	/**
	 * Find the symbol the given stack node is associated with.
	 */
	private Object findSymbol(AbstractStackNode node){
		AbstractStackNode[] production = node.getProduction();
		AbstractStackNode last = production[production.length - 1];
		
		int dot = node.getDot();
		
		return errorBuilderHelper.getSymbol(last.getParentProduction(), dot);
	}
	
	/**
	 * Constructs the error parse result using all available information about
	 * the parse error(s) that occurred.
	 */
	AbstractContainerNode buildErrorTree(Stack<AbstractStackNode> unexpandableNodes, DoubleStack<AbstractStackNode, AbstractNode> filteredNodes){
		// Construct futures for nodes that could not be expanded (if any).
		while(!unexpandableNodes.isEmpty()){
			AbstractStackNode unexpandableNode = unexpandableNodes.pop();
			
			Object symbol = getParentSymbol(unexpandableNode);
			AbstractNode resultStore = new ExpectedNode(NO_CHILDREN, symbol, inputURI, location, location, unexpandableNode.isSeparator(), unexpandableNode.isLayout());
			
			errorNodes.push(unexpandableNode, resultStore);
		}
		
		// Construct futures for nodes that were filtered (if any).
		while(!filteredNodes.isEmpty()){
			AbstractStackNode filteredNode = filteredNodes.peekFirst();
			AbstractNode resultStore = filteredNodes.popSecond();
			
			errorNodes.push(filteredNode, resultStore);
		}
		
		if(errorNodes.isEmpty()) return null; // Can't build an error tree if we don't know what caused the error.
		
		// Build the 'error frontier'.
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
		ErrorSortContainerNode result = (ErrorSortContainerNode) startNode.getIncomingEdges().getLastResult(parser.getResultStoreId(startNode.getId()));
		
		// Update the top node with the rest of the string (this node will always be a sort node).
		result.setUnmatchedInput(rest);
	
		return result;
	}
}
