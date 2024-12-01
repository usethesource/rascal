/*******************************************************************************
 * Copyright (c) 2009-2024 NWO-I Centrum Wiskunde & Informatica (CWI) All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI 
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Pieter Oliver - Pieter.Olivier@swat.engineering
*******************************************************************************/
package org.rascalmpl.parser.uptr.recovery;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Triple;
import org.rascalmpl.parser.gtd.ExpectsProvider;
import org.rascalmpl.parser.gtd.recovery.IRecoverer;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.SkippedNode;
import org.rascalmpl.parser.gtd.stack.AbstractExpandableStackNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.CaseInsensitiveLiteralStackNode;
import org.rascalmpl.parser.gtd.stack.EmptyStackNode;
import org.rascalmpl.parser.gtd.stack.EpsilonStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.stack.RecoveryPointStackNode;
import org.rascalmpl.parser.gtd.stack.SkippingStackNode;
import org.rascalmpl.parser.gtd.stack.StackNodeVisitorAdapter;
import org.rascalmpl.parser.gtd.stack.edge.EdgesSet;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.IdDispenser;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;
import org.rascalmpl.parser.gtd.util.ObjectKeyedIntegerMap;
import org.rascalmpl.parser.gtd.util.Stack;
import org.rascalmpl.parser.uptr.recovery.InputMatcher.MatchResult;
import org.rascalmpl.parser.util.ParseStateVisualizer;
import org.rascalmpl.values.parsetrees.ProductionAdapter;

import io.usethesource.vallang.IConstructor;

public class ToTokenRecoverer implements IRecoverer<IConstructor> {
	private static final boolean VISUALIZE_RECOVERY_NODES = false;

	private URI uri;
	private IdDispenser stackNodeIdDispenser;
	private ExpectsProvider<IConstructor> expectsProvider;

	private Set<Long> processedNodes = new HashSet<>();

	public ToTokenRecoverer(URI uri, ExpectsProvider<IConstructor> expectsProvider, IdDispenser stackNodeIdDispenser) {
		this.uri = uri;
		this.expectsProvider = expectsProvider;
		this.stackNodeIdDispenser = stackNodeIdDispenser;
	}

	@Override
	public DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode> reviveStacks(int[] input, int location,
			Stack<AbstractStackNode<IConstructor>> unexpandableNodes,
			Stack<AbstractStackNode<IConstructor>> unmatchableLeafNodes,
			DoubleStack<DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode>, AbstractStackNode<IConstructor>> unmatchableMidProductionNodes,
			DoubleStack<AbstractStackNode<IConstructor>, AbstractNode> filteredNodes) {

		// For now we ignore unmatchable leaf nodes and filtered nodes. At some point we might use those to
		// improve error recovery.
		
		ArrayList<AbstractStackNode<IConstructor>> failedNodes = new ArrayList<>();
		collectUnexpandableNodes(unexpandableNodes, failedNodes);
		collectUnmatchableMidProductionNodes(location, unmatchableMidProductionNodes, failedNodes);

		return reviveFailedNodes(input, location, failedNodes);
	}

	private DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode> reviveNodes(int[] input, int location,
		DoubleArrayList<AbstractStackNode<IConstructor>, ArrayList<IConstructor>> recoveryNodes) {
		DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode> recoveredNodes = new DoubleArrayList<>();

		// Sort nodes by start location
		recoveryNodes.sort((e1, e2) -> Integer.compare(e2.getLeft().getStartLocation(), e1.getLeft().getStartLocation()));

		if (VISUALIZE_RECOVERY_NODES) {
			ParseStateVisualizer visualizer = new ParseStateVisualizer("Recovery");
			visualizer.visualizeRecoveryNodes(recoveryNodes);
		}
		
		//  Keep track of previously added nodes so we can reuse them when we encounter
		// a node with the same constructor, start location, and skip length
		Map<Triple<IConstructor, Integer, Integer>, SkippingStackNode<IConstructor>> addedNodes = new HashMap<>();

		for (int i = 0; i<recoveryNodes.size(); i++) {
			AbstractStackNode<IConstructor> recoveryNode = recoveryNodes.getFirst(i);
			ArrayList<IConstructor> prods = recoveryNodes.getSecond(i);

			int startLocation = recoveryNode.getStartLocation();

			// Handle every possible continuation associated with the recovery node (there can be more then one
			// because of prefix-sharing).
			for (int j = prods.size() - 1; j >= 0; --j) {
				IConstructor prod = prods.get(j);
				
				IConstructor sort = ProductionAdapter.getType(prod);

				List<SkippingStackNode<IConstructor>> skippingNodes = findSkippingNodes(input, location, recoveryNode, prod, startLocation);
				for (SkippingStackNode<IConstructor> skippingNode : skippingNodes) {
					int skipLength = skippingNode.getLength();
					Triple<IConstructor, Integer, Integer> key = Triple.of(sort, startLocation, skipLength);

					// See if we already have added a node with this constructor, start location, and skip length
					SkippingStackNode<IConstructor> previouslyAddedNode = addedNodes.get(key);
					if (previouslyAddedNode == null) {
						// No, add the current node
						addedNodes.put(key, skippingNode);
						skippingNode.initEdges();
						recoveredNodes.add(skippingNode, skippingNode.getResult());
					} else {
						// We already added a similar node, use that instead
						skippingNode = previouslyAddedNode;
					}

					AbstractStackNode<IConstructor> continuer = new RecoveryPointStackNode<>(stackNodeIdDispenser.dispenseId(), prod, recoveryNode);
					EdgesSet<IConstructor> edges = new EdgesSet<>(1);
					edges.add(continuer);
					continuer.setIncomingEdges(edges);

					skippingNode.addEdges(edges, startLocation);
				}
			}
		}
		
		return recoveredNodes;
	}

	// Add a new SkippingStackNode, but only if another node with the same result does not already exist
	private void addSkippingStackNode(List<SkippingStackNode<IConstructor>> nodes, IConstructor prod, int startLocation, SkippedNode result) {
		for (SkippingStackNode<IConstructor> node : nodes) {
			if (((SkippedNode)node.getResult()).getLength() == result.getLength()) {
				// Already added
				return;
			}
		}

		nodes.add(new SkippingStackNode<>(stackNodeIdDispenser.dispenseId(), prod, result, startLocation));
	}

	private List<SkippingStackNode<IConstructor>> findSkippingNodes(int[] input, int location,
		AbstractStackNode<IConstructor> recoveryNode, IConstructor prod, int startLocation) {
		List<SkippingStackNode<IConstructor>> nodes = new java.util.ArrayList<>();

		SkippedNode result;

		// If we are at the end of the input, skip nothing
		if (location >= input.length) {
			result = SkippingStackNode.createResultUntilEndOfInput(uri, input, startLocation);
			addSkippingStackNode(nodes, prod, startLocation, result);
			return nodes; // No other nodes would be useful
		}

		// Find the last token of this production and skip until after that
		List<InputMatcher> endMatchers = findEndMatchers(recoveryNode);
		for (InputMatcher endMatcher : endMatchers) {
			// For now take a very large (basically unlimited) "max match length", experiment with smaller limit later
			MatchResult endMatch = endMatcher.findMatch(input, startLocation, Integer.MAX_VALUE/2);
			if (endMatch != null) {
				result = SkippingStackNode.createResultUntilChar(uri, input, startLocation, endMatch.getEnd());
				addSkippingStackNode(nodes, prod, startLocation, result);
			}
		}

		// Find the first token of the next production and skip until before that
		List<InputMatcher> nextMatchers = findNextMatchers(recoveryNode);
		for (InputMatcher nextMatcher : nextMatchers) {
			// For now take a very large (basically unlimited) "max match length", experiment with smaller limit later
			MatchResult nextMatch = nextMatcher.findMatch(input, startLocation+1, Integer.MAX_VALUE/2);
			if (nextMatch != null) {
				result = SkippingStackNode.createResultUntilChar(uri, input, startLocation, nextMatch.getStart());
				addSkippingStackNode(nodes, prod, startLocation, result);
			}
		}

		return nodes;
	}

	// Find matchers for the last token of the current stack node
	private List<InputMatcher> findEndMatchers(AbstractStackNode<IConstructor> stackNode) {
		final List<InputMatcher> matchers = new java.util.ArrayList<>();

		AbstractStackNode<IConstructor>[] prod = stackNode.getProduction();
		addEndMatchers(prod, prod.length-1, matchers, new HashSet<>());

		IConstructor parentProduction = stackNode.getParentProduction();
		if (parentProduction != null && ProductionAdapter.isContextFree(parentProduction)) {
			matchers.add(new LiteralMatcher("\n"));
		}

		return matchers;
	}
	
	private void addEndMatchers(AbstractStackNode<IConstructor>[] prod, int dot, List<InputMatcher> matchers,
		Set<Integer> visitedNodes) {
		if (prod == null || dot < 0 || dot >= prod.length) {
			return;
		}

		AbstractStackNode<IConstructor> last = prod[dot];
		if (visitedNodes.contains(last.getId())) {
			return;
		}
		visitedNodes.add(last.getId());

		if (isNullable(last)) {
			addEndMatchers(prod, dot-1, matchers, visitedNodes);
		}

		last.accept(new StackNodeVisitorAdapter<IConstructor, Void>() {
			@Override
			public Void visit(LiteralStackNode<IConstructor> literal) {
				matchers.add(new LiteralMatcher(literal.getLiteral()));
				return null;
			}

			@Override
			public Void visit(CaseInsensitiveLiteralStackNode<IConstructor> literal) {
				matchers.add(new CaseInsensitiveLiteralMatcher(literal.getLiteral()));
				return null;
			}

			@Override
			public Void visit(NonTerminalStackNode<IConstructor> nonTerminal) {
				String name = nonTerminal.getName();
				AbstractStackNode<IConstructor>[] alternatives = expectsProvider.getExpects(name);
				for (AbstractStackNode<IConstructor> alternative : alternatives) {
					addEndMatchers(alternative.getProduction(), 0, matchers, visitedNodes);
				}
				return null;
			}
		});
	}

	private AbstractStackNode<IConstructor> getSingleParentStack(AbstractStackNode<IConstructor> stackNode) {
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
    private List<InputMatcher> findNextMatchers(AbstractStackNode<IConstructor> stackNode) {
		final List<InputMatcher> matchers = new java.util.ArrayList<>();

		// Future improvement: use all parents instead of just one
		AbstractStackNode<IConstructor> parent = getSingleParentStack(stackNode);
		if (parent == null) {
			return matchers;
		}

		addNextMatchers(parent.getProduction(), parent.getDot()+1, matchers, new HashSet<>());

		return matchers;
	}

	private void addNextMatchers(AbstractStackNode<IConstructor>[] prod, int dot, List<InputMatcher> matchers, Set<Integer> visitedNodes) {
		if (prod == null || dot < 0 || dot >= prod.length) {
			return;
		}

		AbstractStackNode<IConstructor> next = prod[dot];
		if (visitedNodes.contains(next.getId())) {
			return;
		}
		visitedNodes.add(next.getId());

		if (isNullable(next)) {
			// In the future, when a node can be empty, we should also consider all prefix-shared alternatives.
			addNextMatchers(prod, dot+1, matchers, visitedNodes);
		}

		next.accept(new StackNodeVisitorAdapter<IConstructor, Void>() {
			@Override
			public Void visit(LiteralStackNode<IConstructor> literal) {
				matchers.add(new LiteralMatcher(literal.getLiteral()));
				return null;
			}

			@Override
			public Void visit(CaseInsensitiveLiteralStackNode<IConstructor> literal) {
				matchers.add(new CaseInsensitiveLiteralMatcher(literal.getLiteral()));
				return null;
			}

			@Override
			public Void visit(NonTerminalStackNode<IConstructor> nonTerminal) {
				String name = nonTerminal.getName();
				AbstractStackNode<IConstructor>[] alternatives = expectsProvider.getExpects(name);
				for (AbstractStackNode<IConstructor> alternative : alternatives) {
					addNextMatchers(alternative.getProduction(), 0, matchers, visitedNodes);
				}

				return null;
		}
		});
	}

	private boolean isNullable(AbstractStackNode<IConstructor> stackNode) {
		if (stackNode instanceof NonTerminalStackNode && stackNode.getName().startsWith("layouts_")) {
			return true;
		}

		if (stackNode instanceof EpsilonStackNode || stackNode instanceof EmptyStackNode) {
			return true;
		}

		if (stackNode instanceof AbstractExpandableStackNode) {
			return stackNode.canBeEmpty();
		}

		return false;
	}

    private DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode> reviveFailedNodes(
		int[] input,
		int location,
		ArrayList<AbstractStackNode<IConstructor>> failedNodes) {
		DoubleArrayList<AbstractStackNode<IConstructor>, ArrayList<IConstructor>> recoveryNodes = new DoubleArrayList<>();

		for (int i = failedNodes.size() - 1; i >= 0; --i) {
			AbstractStackNode<IConstructor> failedNode = failedNodes.get(i);

			// Protect against endless loop
			long id = (long) failedNode.getId() << 32 | failedNode.getStartLocation();
			if (!processedNodes.add(id)) {
				continue;
			}

			findRecoveryNodes(failedNodes.get(i), recoveryNodes);
		}

		return reviveNodes(input, location, recoveryNodes);
	}
	
	private static void collectUnexpandableNodes(Stack<AbstractStackNode<IConstructor>> unexpandableNodes,
		ArrayList<AbstractStackNode<IConstructor>> failedNodes) {
		for (int i = unexpandableNodes.getSize() - 1; i >= 0; --i) {
			failedNodes.add(unexpandableNodes.get(i));
		}
	}
	
	/**
	 * Make a fresh copy of each unmatchable mid-production node and link in the predecessors of the
	 * original node. The new copies are added to `failedNodes`
	 *
	 * @param location the location where the failure occurs
	 * @param unmatchableMidProductionNodes each pair consists of a list of predecessors and a node that
	 *        failed to match
	 * @param failedNodes the list to which failed nodes must be added
	 */
	private static void collectUnmatchableMidProductionNodes(int location,
		DoubleStack<DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode>, AbstractStackNode<IConstructor>> unmatchableMidProductionNodes,
		ArrayList<AbstractStackNode<IConstructor>> failedNodes) {
		for (int i = unmatchableMidProductionNodes.getSize() - 1; i >= 0; --i) {
			DoubleArrayList<AbstractStackNode<IConstructor>, AbstractNode> failedNodePredecessors = unmatchableMidProductionNodes.getFirst(i);
			AbstractStackNode<IConstructor> failedNode =
				unmatchableMidProductionNodes.getSecond(i).getCleanCopy(location); // Clone it to prevent by-reference updates of the static version

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
	private void findRecoveryNodes(AbstractStackNode<IConstructor> failer,
		DoubleArrayList<AbstractStackNode<IConstructor>, ArrayList<IConstructor>> recoveryNodes) {
		ObjectKeyedIntegerMap<AbstractStackNode<IConstructor>> visited = new ObjectKeyedIntegerMap<>();
		Stack<AbstractStackNode<IConstructor>> todo = new Stack<>();
		
		todo.push(failer);
		
		while (!todo.isEmpty()) {
			AbstractStackNode<IConstructor> node = todo.pop();
			
			if (visited.contains(node)) {
			    continue; // Don't follow cycles
			}
			
			visited.put(node, 0);
			
			ArrayList<IConstructor> recoveryProductions = new ArrayList<>();
			collectProductions(node, recoveryProductions);
			if (recoveryProductions.size() > 0) {
				addRecoveryNode(node, recoveryProductions, recoveryNodes);
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

	// Only add recovery nodes that are not already present.
	private void addRecoveryNode(AbstractStackNode<IConstructor> node, ArrayList<IConstructor> productions,
		DoubleArrayList<AbstractStackNode<IConstructor>, ArrayList<IConstructor>> recoveryNodes) {
		for (int i=0; i<recoveryNodes.size(); i++) {
			if (recoveryNodes.getFirst(i) == node && equalProductions(productions, recoveryNodes.getSecond(i))) {
				return;
			}
		}

		recoveryNodes.add(node, productions);
	}

	private boolean equalProductions(ArrayList<IConstructor> prods1, ArrayList<IConstructor> prods2) {
		if (prods1.size() != prods2.size()) {
			return false;
		}

		for (int j = 0; j < prods1.size(); j++) {
			if (prods1.get(j) != prods2.get(j)) {
				return false;
			}
		}

		return true;
	}
	
	// Gathers all productions that are marked for recovery (the given node can be part of a prefix
	// shared production)
	private void collectProductions(AbstractStackNode<IConstructor> node, ArrayList<IConstructor> productions) {
		AbstractStackNode<IConstructor>[] production = node.getProduction();
		if (production == null) {
			return; // The root node does not have a production, so ignore it.
		}

		if (node.isEndNode()) {
			IConstructor parentProduction = node.getParentProduction();
			if (ProductionAdapter.isContextFree(parentProduction)){
				productions.add(parentProduction);

				if (ProductionAdapter.isList(parentProduction)) {
					return; // Don't follow productions in lists productions, since they are 'cyclic'.
				}
			}
		}

		int dot = node.getDot();
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
