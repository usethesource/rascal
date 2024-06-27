/*******************************************************************************
 * Copyright (c) 2009-2022 NWO-I Centrum Wiskunde & Informatica (CWI)
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

import org.apache.commons.lang3.tuple.Pair;
import org.rascalmpl.parser.gtd.recovery.IRecoverer;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.RecoveryPointStackNode;
import org.rascalmpl.parser.gtd.stack.SkippingStackNode;
import org.rascalmpl.parser.gtd.stack.edge.EdgesSet;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.IdDispenser;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;
import org.rascalmpl.parser.gtd.util.ObjectKeyedIntegerMap;
import org.rascalmpl.parser.gtd.util.Stack;
import org.rascalmpl.values.parsetrees.ProductionAdapter;

import io.usethesource.vallang.IConstructor;

public class ToNextWhitespaceRecoverer implements IRecoverer<IConstructor> {
	private static final int[] WHITESPACE = {' ', '\r', '\n', '\t' };
	
	private IdDispenser stackNodeIdDispenser;

	public ToNextWhitespaceRecoverer(IdDispenser stackNodeIdDispenser) {
		this.stackNodeIdDispenser = stackNodeIdDispenser;
	}

	@Override
	public DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode> reviveStacks(int[] input,
			int location,
			Stack<AbstractStackNode<IConstructor>> unexpandableNodes,
			Stack<AbstractStackNode<IConstructor>> unmatchableLeafNodes,
			DoubleStack<DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode>, AbstractStackNode<IConstructor>> unmatchableMidProductionNodes,
			DoubleStack<AbstractStackNode<IConstructor>, AbstractNode> filteredNodes) {

		ArrayList<AbstractStackNode<IConstructor>> failedNodes = new ArrayList<AbstractStackNode<IConstructor>>();
		collectUnexpandableNodes(unexpandableNodes, failedNodes);
		collectUnmatchableMidProductionNodes(location, unmatchableMidProductionNodes, failedNodes);
		// TODO: handle unmatchableLeafNodes
		//collectFilteredNodes(filteredNodes, failedNodes);

		return reviveFailedNodes(input, location, failedNodes);
	}

	private DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode> reviveNodes(int[] input, int location, DoubleArrayList<AbstractStackNode<IConstructor>, ArrayList<IConstructor>> recoveryNodes){
		DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode> recoveredNodes = new DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode>();
		
		// <PO> original: for (int i = recoveryNodes.size() - 1; i >= 0; --i) {
		// But this caused problems because recovery nodes with a later position
		// where queued before nodes with an earlier position which the parser cannot handle.

		recoveryNodes.sort((e1, e2) -> Integer.compare(e2.getLeft().getStartLocation(), e1.getLeft().getStartLocation()));

		for (int i = 0; i<recoveryNodes.size(); i++) {
			AbstractStackNode<IConstructor> recoveryNode = recoveryNodes.getFirst(i);
			ArrayList<IConstructor> prods = recoveryNodes.getSecond(i);
			//Pair<AbstractStackNode<IConstructor>, ArrayList<IConstructor>> elem = elems.get(i);
			//AbstractStackNode<IConstructor> recoveryNode = elem.getLeft();
			//ArrayList<IConstructor> prods = elem.getRight();

			// Handle every possible continuation associated with the recovery node (there can be more then one because of prefix-sharing).
			for (int j = prods.size() - 1; j >= 0; --j) {
				IConstructor prod = prods.get(j);
				
				AbstractStackNode<IConstructor> continuer = new RecoveryPointStackNode<>(stackNodeIdDispenser.dispenseId(), prod, recoveryNode);
				
				int startLocation = recoveryNode.getStartLocation();
				
				AbstractStackNode<IConstructor> recoverLiteral = new SkippingStackNode<>(stackNodeIdDispenser.dispenseId(), WHITESPACE, input, startLocation, prod, recoveryNode.getDot());
				recoverLiteral = recoverLiteral.getCleanCopy(startLocation);
				recoverLiteral.initEdges();
				EdgesSet<IConstructor> edges = new EdgesSet<>(1);
				edges.add(continuer);
				
				recoverLiteral.addEdges(edges, startLocation);
				
				continuer.setIncomingEdges(edges);
				
				recoveredNodes.add(recoverLiteral, recoverLiteral.getResult());
			}
		}
		
		return recoveredNodes;
	}
	
	private DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode> reviveFailedNodes(int[] input, int location, ArrayList<AbstractStackNode<IConstructor>> failedNodes) {
		DoubleArrayList<AbstractStackNode<IConstructor>, ArrayList<IConstructor>> recoveryNodes = new DoubleArrayList<AbstractStackNode<IConstructor>, ArrayList<IConstructor>>();
		
		for (int i = failedNodes.size() - 1; i >= 0; --i) {
			findRecoveryNodes(failedNodes.get(i), recoveryNodes);
		}
		
		return reviveNodes(input, location, recoveryNodes);
	}
	
	private static void collectUnexpandableNodes(Stack<AbstractStackNode<IConstructor>> unexpandableNodes, ArrayList<AbstractStackNode<IConstructor>> failedNodes) {
		for (int i = unexpandableNodes.getSize() - 1; i >= 0; --i) {
			failedNodes.add(unexpandableNodes.get(i));
		}
	}
	
	/**
	 * Make a fresh copy of  each unmatchable mid-production node and link in the predecessors of the original node.
	 * The new copies are added to `failedNodes`
	 * @param location the location where the failure occurs
	 * @param unmatchableMidProductionNodes each pair consists of a list of predecessors and a node that failed to match
	 * @param failedNodes the list to which failed nodes must be added
	 */
	private static void collectUnmatchableMidProductionNodes(int location, DoubleStack<DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode>, AbstractStackNode<IConstructor>> unmatchableMidProductionNodes, ArrayList<AbstractStackNode<IConstructor>> failedNodes){
		for (int i = unmatchableMidProductionNodes.getSize() - 1; i >= 0; --i) {
			DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode> failedNodePredecessors = unmatchableMidProductionNodes.getFirst(i);
			AbstractStackNode<IConstructor> failedNode = unmatchableMidProductionNodes.getSecond(i).getCleanCopy(location); // Clone it to prevent by-reference updates of the static version
			
			// Merge the information on the predecessors into the failed node.
			for(int j = failedNodePredecessors.size() - 1; j >= 0; --j) {
				AbstractStackNode<IConstructor> predecessor = failedNodePredecessors.getFirst(j);
				AbstractNode predecessorResult = failedNodePredecessors.getSecond(j);
				failedNode.updateNode(predecessor, predecessorResult);
			}
			
			failedNodes.add(failedNode);
		}
	}

	/**
	 * Travels up the parse graph in an attempt to find the closest recoverable parent nodes.
	 */
	private void findRecoveryNodes(AbstractStackNode<IConstructor> failer, DoubleArrayList<AbstractStackNode<IConstructor>, ArrayList<IConstructor>> recoveryNodes) {
		ObjectKeyedIntegerMap<AbstractStackNode<IConstructor>> visited = new ObjectKeyedIntegerMap<AbstractStackNode<IConstructor>>();
		Stack<AbstractStackNode<IConstructor>> todo = new Stack<AbstractStackNode<IConstructor>>();
		
		todo.push(failer);
		
		while (!todo.isEmpty()) {
			AbstractStackNode<IConstructor> node = todo.pop();
			
			if (visited.contains(node)) {
			    continue; // Don't follow cycles
			}
			
			visited.put(node, 0);
			
			ArrayList<IConstructor> recoveryProductions = new ArrayList<IConstructor>();
			collectProductions(node, recoveryProductions);
			if (recoveryProductions.size() > 0) {
				recoveryNodes.add(node, recoveryProductions);
			}
			
			IntegerObjectList<EdgesSet<IConstructor>> edges = node.getEdges();
			
			for (int i = edges.size() - 1; i >= 0; --i) { // Rewind
				EdgesSet<IConstructor> edgesList = edges.getValue(i);

				if (edgesList != null) {
					for (int j = edgesList.size() - 1; j >= 0; --j) {
						AbstractStackNode<IConstructor> parent = edgesList.get(j);
						
						todo.push(parent);
					}
				}
			}
		}
	}
	
	// Gathers all productions that are marked for recovery (the given node can be part of a prefix shared production)
	private void collectProductions(AbstractStackNode<IConstructor> node, ArrayList<IConstructor> productions) {
	    AbstractStackNode<IConstructor>[] production = node.getProduction();
	    if (production == null) {
	        return; // The root node does not have a production, so ignore it.
	    }

	    int dot = node.getDot();

	    if (node.isEndNode()) {
	        IConstructor parentProduction = node.getParentProduction();
	        if (ProductionAdapter.isContextFree(parentProduction)){
	            productions.add(parentProduction);

	            if (ProductionAdapter.isList(parentProduction)) {
	                return; // Don't follow productions in lists productions, since they are 'cyclic'.
	            }
	        }
	    }

	    for (int i = dot + 1; i < production.length; ++i) {
	        AbstractStackNode<IConstructor> currentNode = production[i];
	        if (currentNode.isEndNode()) {
	            IConstructor parentProduction = currentNode.getParentProduction();
	            if (ProductionAdapter.isContextFree(parentProduction)) {
	                productions.add(parentProduction);
	            }
	        }

	        AbstractStackNode<IConstructor>[][] alternateProductions = currentNode.getAlternateProductions();
	        if (alternateProductions != null) {
	            for (int j = alternateProductions.length - 1; j >= 0; --j) {
	                collectProductions(alternateProductions[j][i], productions);
	            }
	        }
	    }
	}

	
}
