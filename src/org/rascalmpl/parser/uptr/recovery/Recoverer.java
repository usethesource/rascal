/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.uptr.recovery;

import org.rascalmpl.parser.gtd.recovery.IRecoverer;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.RecoveryPointStackNode;
import org.rascalmpl.parser.gtd.stack.SkippingStackNode;
import org.rascalmpl.parser.gtd.stack.edge.EdgesSet;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;
import org.rascalmpl.parser.gtd.util.ObjectKeyedIntegerMap;
import org.rascalmpl.parser.gtd.util.Stack;

public class Recoverer<P> implements IRecoverer<P>{
	// TODO: its a magic constant, and it may clash with other generated constants
	// should generate implementation of static int getLastId() in generated parser to fix this.
	private int recoveryId = 100000;
	
	private final int[][] continuationCharactersList;
	
	private final ObjectKeyedIntegerMap<P> robust;
	
	public Recoverer(P[] robustProds, int[][] continuationCharactersList){
		super();
		
		this.continuationCharactersList = continuationCharactersList;
		
		this.robust = new ObjectKeyedIntegerMap<P>();
		
		for (int i = robustProds.length - 1; i >= 0; --i) {
			robust.put(robustProds[i], i);
		}
	}
	
	private DoubleArrayList<AbstractStackNode<P>, AbstractNode> reviveNodes(int[] input, int location, DoubleArrayList<AbstractStackNode<P>, ArrayList<P>> recoveryNodes){
		DoubleArrayList<AbstractStackNode<P>, AbstractNode> recoveredNodes = new DoubleArrayList<AbstractStackNode<P>, AbstractNode>();
		
		for(int i = recoveryNodes.size() - 1; i >= 0; --i) {
			AbstractStackNode<P> recoveryNode = recoveryNodes.getFirst(i);
			ArrayList<P> prods = recoveryNodes.getSecond(i);
			
			// Handle every possible continuation associated with the recovery node (there can be more then one because of prefix-sharing).
			for(int j = prods.size() - 1; j >= 0; --j){
				P prod = prods.get(j);
				
				AbstractStackNode<P> continuer = new RecoveryPointStackNode<P>(recoveryId++, prod, recoveryNode);
				
				int startLocation = recoveryNode.getStartLocation();
				
				int[] until = continuationCharactersList[robust.get(prod)];
				AbstractStackNode<P> recoverLiteral = new SkippingStackNode<P>(recoveryId++, until, input, startLocation, prod);
				recoverLiteral = recoverLiteral.getCleanCopy(startLocation);
				recoverLiteral.initEdges();
				EdgesSet<P> edges = new EdgesSet<P>(1);
				edges.add(continuer);
				
				recoverLiteral.addEdges(edges, startLocation);
				
				continuer.setIncomingEdges(edges);
				
				recoveredNodes.add(recoverLiteral, recoverLiteral.getResult());
			}
		}
		
		return recoveredNodes;
	}
	
	private DoubleArrayList<AbstractStackNode<P>, AbstractNode> reviveFailedNodes(int[] input, int location, ArrayList<AbstractStackNode<P>> failedNodes) {
		DoubleArrayList<AbstractStackNode<P>, ArrayList<P>> recoveryNodes = new DoubleArrayList<AbstractStackNode<P>, ArrayList<P>>();
		
		for(int i = failedNodes.size() - 1; i >= 0; --i){
			findRecoveryNodes(failedNodes.get(i), recoveryNodes);
		}
		
		return reviveNodes(input, location, recoveryNodes);
	}
	
	private void collectUnexpandableNodes(Stack<AbstractStackNode<P>> unexpandableNodes, ArrayList<AbstractStackNode<P>> failedNodes) {
		for(int i = unexpandableNodes.getSize() - 1; i >= 0; --i){
			failedNodes.add(unexpandableNodes.get(i));
		}
	}
	
	private void collectUnmatchableMidProductionNodes(int location, DoubleStack<DoubleArrayList<AbstractStackNode<P>, AbstractNode>, AbstractStackNode<P>> unmatchableMidProductionNodes, ArrayList<AbstractStackNode<P>> failedNodes){
		for(int i = unmatchableMidProductionNodes.getSize() - 1; i >= 0; --i){
			DoubleArrayList<AbstractStackNode<P>, AbstractNode> failedNodePredecessors = unmatchableMidProductionNodes.getFirst(i);
			AbstractStackNode<P> failedNode = unmatchableMidProductionNodes.getSecond(i).getCleanCopy(location); // Clone it to prevent by-reference updates of the static version
			
			// Merge the information on the predecessors into the failed node.
			for(int j = failedNodePredecessors.size() - 1; j >= 0; --j){
				AbstractStackNode<P> predecessor = failedNodePredecessors.getFirst(j);
				AbstractNode predecessorResult = failedNodePredecessors.getSecond(j);
				failedNode.updateNode(predecessor, predecessorResult);
			}
			
			failedNodes.add(failedNode);
		}
	}

	private boolean isRobust(P prod) {
		return robust.contains(prod);
	}
	
	/**
	 * Travels up the parse graph in an attempt to find the closest recoverable parent nodes.
	 */
	private void findRecoveryNodes(AbstractStackNode<P> failer, DoubleArrayList<AbstractStackNode<P>, ArrayList<P>> recoveryNodes) {
		ObjectKeyedIntegerMap<AbstractStackNode<P>> visited = new ObjectKeyedIntegerMap<AbstractStackNode<P>>();
		Stack<AbstractStackNode<P>> todo = new Stack<AbstractStackNode<P>>();
		
		todo.push(failer);
		
		while (!todo.isEmpty()) {
			AbstractStackNode<P> node = todo.pop();
			if(visited.contains(node)) continue; // Don't follow cycles
			
			visited.put(node, 0);
			
			ArrayList<P> recoveryProductions = new ArrayList<P>();
			collectProductions(node, recoveryProductions);
			
			if(recoveryProductions.size() > 0){
				recoveryNodes.add(node, recoveryProductions);
			}
			
			IntegerObjectList<EdgesSet<P>> edges = node.getEdges();
			
			for(int i = edges.size() - 1; i >= 0; --i){
				EdgesSet<P> edgesList = edges.getValue(i);

				if (edgesList != null) {
					for(int j = edgesList.size() - 1; j >= 0; --j){
						AbstractStackNode<P> parent = edgesList.get(j);
						
						todo.push(parent);
					}
				}
			}
		}
	}
	
	// Gathers all productions that are marked for recovery (the given node can be part of a prefix shared production)
	private void collectProductions(AbstractStackNode<P> node, ArrayList<P> productions) {
		AbstractStackNode<P>[] production = node.getProduction();
		if(production == null) return; // The root node does not have a production, so ignore it.
		
		int dot = node.getDot();
		
		if(node.isEndNode()){
			P parentProduction = node.getParentProduction();
			if(isRobust(parentProduction)){
				productions.add(parentProduction);
			}
		}
		 
		for(int i = dot + 1; i < production.length; ++i){
			AbstractStackNode<P> currentNode = production[i];
			if(currentNode.isEndNode()){
				P parentProduction = currentNode.getParentProduction();
				if(isRobust(parentProduction)){
					productions.add(parentProduction);
				}
			}
			
			AbstractStackNode<P>[][] alternateProductions = currentNode.getAlternateProductions();
			if(alternateProductions != null){
				for(int j = alternateProductions.length - 1; j >= 0; --j){
					collectProductions(alternateProductions[j][i], productions);
				}
			}
		}
	}
	
	public DoubleArrayList<AbstractStackNode<P>, AbstractNode> reviveStacks(int[] input,
			int location,
			Stack<AbstractStackNode<P>> unexpandableNodes,
			Stack<AbstractStackNode<P>> unmatchableLeafNodes,
			DoubleStack<DoubleArrayList<AbstractStackNode<P>, AbstractNode>, AbstractStackNode<P>> unmatchableMidProductionNodes,
			DoubleStack<AbstractStackNode<P>, AbstractNode> filteredNodes) {
		ArrayList<AbstractStackNode<P>> failedNodes = new ArrayList<AbstractStackNode<P>>();
		collectUnexpandableNodes(unexpandableNodes, failedNodes);
		collectUnmatchableMidProductionNodes(location, unmatchableMidProductionNodes, failedNodes);
		//collectFilteredNodes(filteredNodes, failedNodes);
		
		return reviveFailedNodes(input, location, failedNodes);
	}
}
