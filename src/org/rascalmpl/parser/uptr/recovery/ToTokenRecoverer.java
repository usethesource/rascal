/*******************************************************************************
 * Copyright (c) 2009-2025 NWO-I Centrum Wiskunde & Informatica (CWI) All rights reserved. This
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
import java.util.Iterator;
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
import org.rascalmpl.values.parsetrees.ProductionAdapter;

import io.usethesource.vallang.IConstructor;

public class ToTokenRecoverer implements IRecoverer<IConstructor> {
	private URI uri;
	private IdDispenser stackNodeIdDispenser;
	private ExpectsProvider<IConstructor> expectsProvider;

	private Set<Long> processedNodes = new HashSet<>();

	private static final int DEFAULT_SKIP_LIMIT = 5;
	private static final int DEFAULT_SKIP_WINDOW = 2048;
	private static final int DEFAULT_RECOVERY_LIMIT = 50;
	private static int skipLimit = readEnvVar("RASCAL_RECOVERER_SKIP_LIMIT", DEFAULT_SKIP_LIMIT);
	private static int skipWindow = readEnvVar("RASCAL_RECOVERER_SKIP_WINDOW", DEFAULT_SKIP_WINDOW);
	private static int recoveryLimit = readEnvVar("RASCAL_RECOVERER_RECOVERY_LIMIT", DEFAULT_RECOVERY_LIMIT);

	private int count = 0;

	private static int readEnvVar(String envVar, int defaultValue) {
		String limitSpec = System.getenv(envVar);
		if (limitSpec != null) {
			try {
				return Integer.parseInt(limitSpec);
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

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

		// Cut off error recovery if we have encountered too many errors
		if (count > recoveryLimit) {
			return new DoubleArrayList<>();
		}

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
		
		//  Keep track of previously added nodes so we can reuse them when we encounter
		// a node with the same constructor, start location, and skip length
		Map<Triple<IConstructor, Integer, Integer>, SkippingStackNode<IConstructor>> addedNodes = new HashMap<>();

		//System.err.println("    recoveryNodes.size: " + recoveryNodes.size());
		for (int i = 0; i<recoveryNodes.size(); i++) {
			AbstractStackNode<IConstructor> recoveryNode = recoveryNodes.getFirst(i);
			ArrayList<IConstructor> prods = recoveryNodes.getSecond(i);

			int startLocation = recoveryNode.getStartLocation();

			// Handle every possible continuation associated with the recovery node (there can be more then one
			// because of prefix-sharing).
			//System.err.println("        at " + recoveryNode.getStartLocation() + ", prods.size: " + prods.size() + ", node: " + recoveryNode.toShortString());
			for (int j = prods.size() - 1; j >= 0; --j) {
				IConstructor prod = prods.get(j);
				
				IConstructor sort = ProductionAdapter.getType(prod);

				List<SkippingStackNode<IConstructor>> skippingNodes = findSkippingNodes(input, location, recoveryNode, prod, startLocation);

				//System.err.println("            skippingNodes: " + skippingNodes.size());
				for (SkippingStackNode<IConstructor> skippingNode : skippingNodes) {
					//System.err.println("                skipping " + skippingNode.getLength() + " chars from " + skippingNode.getStartLocation());
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
		//System.err.println("recoveredNodes.size: " + recoveredNodes.size());
		
		return recoveredNodes;
	}

	private List<SkippingStackNode<IConstructor>> findSkippingNodes(int[] input, int location,
		AbstractStackNode<IConstructor> recoveryNode, IConstructor prod, int startLocation) {
		List<SkippingStackNode<IConstructor>> nodes = new java.util.ArrayList<>();

		SkippedNode result;

		// If we are at the end of the input, skip nothing
		if (location >= input.length) {
			result = SkippingStackNode.createResultUntilEndOfInput(uri, input, startLocation);
			nodes.add(new SkippingStackNode<>(stackNodeIdDispenser.dispenseId(), prod, result, startLocation));
			return nodes; // No other nodes would be useful
		}

		// Try to find a match in  limited window starting at the current location
		List<InputMatcher> endMatchers = findEndMatchers(recoveryNode);
		List<InputMatcher> nextMatchers = findNextMatchers(recoveryNode);

		int end = Math.min(location+skipWindow, input.length);
		outer: for (int pos=location; pos<end && nodes.size() < skipLimit; pos++) {
			// Find the last token of this production and skip until after that
			Iterator<InputMatcher> endIter = endMatchers.iterator();
			while (endIter.hasNext()) {
				InputMatcher endMatcher = endIter.next();
				int matchEnd = endMatcher.match(input, pos);
				if (matchEnd > 0) {
					result = SkippingStackNode.createResultUntilChar(uri, input, startLocation, matchEnd);
					nodes.add(new SkippingStackNode<>(stackNodeIdDispenser.dispenseId(), prod, result, startLocation));
					endIter.remove();
					continue outer;
				}
			}

			// Find the first token of the next production and skip until before that
			if (pos > location) {
				Iterator<InputMatcher> nextIter = nextMatchers.iterator();
				while (nextIter.hasNext()) {
					InputMatcher nextMatcher = nextIter.next();
					int matchEnd = nextMatcher.match(input, pos);
					if (matchEnd > 0) {
						result = SkippingStackNode.createResultUntilChar(uri, input, startLocation, pos);
						nodes.add(new SkippingStackNode<>(stackNodeIdDispenser.dispenseId(), prod, result, startLocation));
						nextIter.remove();
						continue outer;
					}
				}
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
			//System.err.println("    Looking before nullable node: " + last.toShortString());
			addEndMatchers(prod, dot-1, matchers, visitedNodes);
			//System.err.println("    Done looking before");
		}

		//System.err.println("    Adding end matchers for: " + last.toShortString());
		last.accept(new StackNodeVisitorAdapter<IConstructor, Void>() {
			@Override
			public Void visit(LiteralStackNode<IConstructor> literal) {
				//System.err.println("      adding literal matcher: " + literal.toShortString());
				matchers.add(new LiteralMatcher(literal.getLiteral()));
				return null;
			}

			@Override
			public Void visit(CaseInsensitiveLiteralStackNode<IConstructor> literal) {
				//System.err.println("      adding case insensitive literal matcher: " + literal.toShortString());
				matchers.add(new CaseInsensitiveLiteralMatcher(literal.getLiteral()));
				return null;
			}

			@Override
			public Void visit(NonTerminalStackNode<IConstructor> nonTerminal) {
				String name = nonTerminal.getName();
				//System.err.println("      adding non terminal matchers for: " + name);
				AbstractStackNode<IConstructor>[] alternatives = expectsProvider.getExpects(name);
				for (AbstractStackNode<IConstructor> alternative : alternatives) {
					addEndMatchers(alternative.getProduction(), 0, matchers, visitedNodes);
				}
				return null;
			}
		});
		//System.err.println("    Done adding end matchers for: " + last.toShortString());
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

		//System.out.println("Reviving " + failedNodes.size() + " nodes");

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
			AbstractStackNode<IConstructor> node = unmatchableMidProductionNodes.getSecond(i);
			AbstractStackNode<IConstructor> failedNode = node.getCleanCopy(location); // Clone it to prevent by-reference updates of the static version

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
