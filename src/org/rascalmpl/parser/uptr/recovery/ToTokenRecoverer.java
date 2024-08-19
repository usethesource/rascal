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

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.recovery.IRecoverer;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.SkippedNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.CaseInsensitiveLiteralStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.stack.RecoveryPointStackNode;
import org.rascalmpl.parser.gtd.stack.SeparatedListStackNode;
import org.rascalmpl.parser.gtd.stack.SkippingStackNode;
import org.rascalmpl.parser.gtd.stack.edge.EdgesSet;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.IdDispenser;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;
import org.rascalmpl.parser.gtd.util.ObjectKeyedIntegerMap;
import org.rascalmpl.parser.gtd.util.Stack;
import org.rascalmpl.parser.uptr.recovery.InputMatcher.MatchResult;
import org.rascalmpl.util.visualize.DebugVisualizer;
import org.rascalmpl.values.parsetrees.ProductionAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IValue;

public class ToTokenRecoverer implements IRecoverer<IConstructor> {
	private IdDispenser stackNodeIdDispenser;

	public ToTokenRecoverer(IdDispenser stackNodeIdDispenser) {
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

		DebugVisualizer visualizer = new DebugVisualizer("Recovery");
		visualizer.visualizeRecoveryNodes(recoveryNodes);
		
		for (int i = 0; i<recoveryNodes.size(); i++) {
			AbstractStackNode<IConstructor> recoveryNode = recoveryNodes.getFirst(i);
			ArrayList<IConstructor> prods = recoveryNodes.getSecond(i);

			int dot = recoveryNode.getDot();
			int startLocation = recoveryNode.getStartLocation();

			// Handle every possible continuation associated with the recovery node (there can be more then one because of prefix-sharing).
			for (int j = prods.size() - 1; j >= 0; --j) {
				IConstructor prod = prods.get(j);
				
				List<SkippingStackNode<IConstructor>> skippingNodes = findSkippingNodes(input, recoveryNode, prod, dot, startLocation);
				for (SkippingStackNode<IConstructor> skippingNode : skippingNodes) {
					AbstractStackNode<IConstructor> continuer = new RecoveryPointStackNode<>(stackNodeIdDispenser.dispenseId(), prod, recoveryNode);
				
					EdgesSet<IConstructor> edges = new EdgesSet<>(1);
					edges.add(continuer);
				
					continuer.setIncomingEdges(edges);

					skippingNode.initEdges();
					skippingNode.addEdges(edges, startLocation);
					recoveredNodes.add(skippingNode, skippingNode.getResult());
				}
			}
		}
		
		return recoveredNodes;
	}

	List<SkippingStackNode<IConstructor>> findSkippingNodes(int[] input, AbstractStackNode<IConstructor> recoveryNode, IConstructor prod, int dot, int startLocation) {
		List<SkippingStackNode<IConstructor>> nodes = new java.util.ArrayList<>();

		SkippedNode result;

		// If we are the top-level node, just skip the rest of the input
		if (!recoveryNode.isEndNode() && isTopLevelProduction(recoveryNode)) {
			result = SkippingStackNode.createResultUntilEndOfInput(input, startLocation, prod, dot);
			nodes.add(new SkippingStackNode<>(stackNodeIdDispenser.dispenseId(), prod, result, startLocation));
			return nodes;	// No other nodes would be useful
		}

		// Try to find whitespace to skip to
		/*
		result = SkippingStackNode.createResultUntilCharClass(WHITESPACE, input, startLocation, prod, dot);
		if (result != null) {
			nodes.add(new SkippingStackNode<>(stackNodeIdDispenser.dispenseId(), prod, result, startLocation));
		}*/

		// Do something more clever: find the last token of this production and skip until after that
		List<InputMatcher> endMatchers = findEndMatchers(prod);
		for (InputMatcher endMatcher : endMatchers) {
			MatchResult endMatch = endMatcher.findMatch(input, startLocation);
			if (endMatch != null) {
				result = SkippingStackNode.createResultUntilChar(input, startLocation, endMatch.getEnd(), prod, dot);
				nodes.add(new SkippingStackNode<>(stackNodeIdDispenser.dispenseId(), prod, result, startLocation));
			}
		}

		// Find the first token of the next production and skip until before that
		List<InputMatcher> nextMatchers = findNextMatchers(recoveryNode);
		for (InputMatcher nextMatcher : nextMatchers) {
			MatchResult nextMatch = nextMatcher.findMatch(input, startLocation);
			if (nextMatch != null) {
				result = SkippingStackNode.createResultUntilChar(input, startLocation, nextMatch.getStart(), prod, dot);
				nodes.add(new SkippingStackNode<>(stackNodeIdDispenser.dispenseId(), prod, result, startLocation));
			}
		}

		return nodes;
	}

	// Gather matchers for the last token of a production
	List<InputMatcher> findEndMatchers(IConstructor prod) {
		IList args = (IList) prod.get(1);
		if (args.isEmpty()) {
			return Collections.emptyList();
		}

		IValue last = args.get(args.length()-1);
		if (last instanceof IConstructor) {
			return Collections.singletonList(InputMatcher.createMatcher((IConstructor) last));
		}

		return Collections.emptyList();
	}

	void forAllParents(AbstractStackNode<IConstructor> stackNode, Consumer<AbstractStackNode<IConstructor>> consumer) {
		IntegerObjectList<EdgesSet<IConstructor>> edges = stackNode.getEdges();
        if (edges != null) {
		    for (int i = edges.size() - 1; i >= 0; --i) {
		        EdgesSet<IConstructor> edgesList = edges.getValue(i);
			    if (edgesList != null) {
				    for (int j = edgesList.size() - 1; j >= 0; --j) {
						consumer.accept(edgesList.get(j));
					}
				}
			}
		}
	}

	AbstractStackNode<IConstructor> getSingleParentStack(AbstractStackNode<IConstructor> stackNode) {
		if (stackNode == null) {
			return null;
		}

		IntegerObjectList<EdgesSet<IConstructor>> edges = stackNode.getEdges();
		if (edges != null) {
			EdgesSet<IConstructor> edgesList = edges.getValue(0);
			if (edgesList != null) {
				return edgesList.get(0);
			}
		}

		return null;
	}

	// Find matchers for the first token after the current stack node
	List<InputMatcher> findNextMatchers(AbstractStackNode<IConstructor> stackNode) {
		DebugVisualizer visualizer = new DebugVisualizer("findNextMatcher");
		visualizer.visualize(stackNode);

		final List<InputMatcher> matchers = new java.util.ArrayList<>();

		AbstractStackNode<IConstructor> parent = getSingleParentStack(stackNode);
		if (parent == null) {
			return matchers;
		}

		AbstractStackNode<IConstructor>[] prod = parent.getProduction();
		if (prod == null) {
			return matchers;
		}

		int nextDot = parent.getDot() + 1;
		if (nextDot >= prod.length) {
			return matchers;
		}

		AbstractStackNode<IConstructor> next = prod[nextDot];
		if (next instanceof NonTerminalStackNode && next.getName().startsWith("layouts_")) {
			// Look "through" layout for now, should be more general to look through any node that can be empty
			nextDot++;
			if (nextDot >= prod.length) {
				return matchers;
			}
			next = prod[nextDot];
		}

		if (next instanceof LiteralStackNode) {
			LiteralStackNode<IConstructor> nextLiteral = (LiteralStackNode<IConstructor>) next;
			matchers.add(new LiteralMatcher(nextLiteral.getLiteral()));
		}

		if (next instanceof CaseInsensitiveLiteralStackNode) {
			CaseInsensitiveLiteralStackNode<IConstructor> nextLiteral = (CaseInsensitiveLiteralStackNode<IConstructor>) next;
			matchers.add(new CaseInsensitiveLiteralMatcher(nextLiteral.getLiteral()));
		}

		if (next instanceof NonTerminalStackNode) {
			NonTerminalStackNode<IConstructor> nextNonTerminal = (NonTerminalStackNode<IConstructor>) next;
			SGTDBF.opportunityToBreak();
		}

		return matchers;
	}

	// Check if a node is a top-level production (i.e., its parent production node has no parents and starts at position -1)
	// As this is experimental code, this method is extremely conservative.
	// Any sharing will result in returning 'false'.
	// We will need to change this strategy in the future to improve error recovery.
	private boolean isTopLevelProduction(AbstractStackNode<IConstructor> node) {
		while (node != null && node.getDot() != 0) {
			node = getSinglePredecessor(node);
		}

		if (node != null) {
			node = getSinglePredecessor(node);
			return node != null && node.getStartLocation() == -1;
		}

		return false;
	}

	private AbstractStackNode<IConstructor> getSinglePredecessor(AbstractStackNode<IConstructor> node) {
		IntegerObjectList<EdgesSet<IConstructor>> edgeMap = node.getEdges();
		if (edgeMap.size() == 1) {
			EdgesSet<IConstructor> edges = edgeMap.getValue(0);
			if (edges.size() == 1) {
				return edges.get(0);
			}
		}

		return null;
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
		
		boolean useNext = true;	// Use this to only use the last active element from a production
		while (!todo.isEmpty()) {
			AbstractStackNode<IConstructor> node = todo.pop();
			
			if (visited.contains(node)) {
			    continue; // Don't follow cycles
			}
			
			visited.put(node, 0);
			
			if (useNext) {
				ArrayList<IConstructor> recoveryProductions = new ArrayList<IConstructor>();
				collectProductions(node, recoveryProductions);
				if (recoveryProductions.size() > 0) {
					addRecoveryNode(node, recoveryProductions, recoveryNodes);
				}
			}
			useNext = node.getDot() == 0;
			
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

	// Only add recovery nodes that are not already present.
	private void addRecoveryNode(AbstractStackNode<IConstructor> node, ArrayList<IConstructor> productions, DoubleArrayList<AbstractStackNode<IConstructor>, ArrayList<IConstructor>> recoveryNodes) {
		for (int i=0; i<recoveryNodes.size(); i++) {
			if (recoveryNodes.getFirst(i) == node) {
				ArrayList<IConstructor> prods = recoveryNodes.getSecond(i);
				if (prods.size() == productions.size()) {
					boolean equal = true;
					for (int j=0; equal && j<prods.size(); j++) {
						if (prods.get(j) != productions.get(j)) {
							equal = false;
						}
					}
					if (equal) {
						return;
					}
				}
			}
		}

		recoveryNodes.add(node, productions);
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
					System.err.println("adding production at " + i + ": " + parentProduction);
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
