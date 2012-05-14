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

public class Recoverer implements IRecoverer{
	// TODO: its a magic constant, and it may clash with other generated constants
	// should generate implementation of static int getLastId() in generated parser to fix this.
	private  int recoveryId = 100000;
	
	private final ObjectKeyedIntegerMap<Object> robust;
	private final int[][] continuations;
	
	public Recoverer(Object[] robustNodes, int[][] continuationCharacters){
		super();
		
		this.robust = new ObjectKeyedIntegerMap<Object>();
		this.continuations = continuationCharacters;
		
		for (int i = robustNodes.length - 1; i >= 0; --i) {
			robust.put(robustNodes[i], i);
		}
	}
	
	private void reviveNodes(DoubleArrayList<AbstractStackNode, AbstractNode> recoveredNodes, int[] input, int location, DoubleArrayList<AbstractStackNode, Object> recoveryNodes){
		for(int i = recoveryNodes.size() - 1; i >= 0; --i) {
			AbstractStackNode recoveryNode = recoveryNodes.getFirst(i);
			Object prod = recoveryNodes.getSecond(i);
			AbstractStackNode continuer = new RecoveryPointStackNode(recoveryId++, prod, recoveryNode);
			int dot = recoveryNode.getDot();
			
			SkippingStackNode recoverLiteral = (SkippingStackNode) new SkippingStackNode(recoveryId++, dot + 1, continuations[robust.get(prod)], input, location, prod);
			recoverLiteral = (SkippingStackNode) recoverLiteral.getCleanCopy(location);
			recoverLiteral.initEdges();
			EdgesSet edges = new EdgesSet(1);
			edges.add(continuer);
			
			recoverLiteral.addEdges(edges, recoverLiteral.getStartLocation());
			
			continuer.setIncomingEdges(edges);
			
			recoveredNodes.add(recoverLiteral, recoverLiteral.getResult());
		}
	}
	
	private void reviveFailedNodes(DoubleArrayList<AbstractStackNode, AbstractNode> recoveredNodes, int[] input, int location, ArrayList<AbstractStackNode> failedNodes) {
		DoubleArrayList<AbstractStackNode, Object> recoveryNodes = new DoubleArrayList<AbstractStackNode, Object>();
		
		for(int i = failedNodes.size() - 1; i >= 0; --i){
			findRecoveryNodes(failedNodes.get(i), recoveryNodes);
		}
		
		reviveNodes(recoveredNodes, input, location, recoveryNodes);
	}
	
	private void collectUnexpandableNodes(Stack<AbstractStackNode> unexpandableNodes, ArrayList<AbstractStackNode> failedNodes) {
		for(int i = unexpandableNodes.getSize() - 1; i >= 0; --i){
			failedNodes.add(unexpandableNodes.get(i));
		}
	}
	
	private void collectUnmatchableMidProductionNodes(int location, DoubleStack<ArrayList<AbstractStackNode>, AbstractStackNode> unmatchableMidProductionNodes, ArrayList<AbstractStackNode> failedNodes){
		for(int i = unmatchableMidProductionNodes.getSize() - 1; i >= 0; --i){
			ArrayList<AbstractStackNode> failedNodePredecessors = unmatchableMidProductionNodes.getFirst(i);
			AbstractStackNode failedNode = unmatchableMidProductionNodes.getSecond(i).getCleanCopy(location); // Clone it to prevent by-reference updates of the static version
			
			// Merge the information on the predecessors into the failed node.
			for(int j = failedNodePredecessors.size() - 1; j >= 0; --j){
				AbstractStackNode predecessor = failedNodePredecessors.get(j);
				failedNode.updateNode(predecessor, predecessor.getResult());
			}
			
			failedNodes.add(failedNode);
		}
	}

	private boolean isRecovering(Object prod) {
		return robust.contains(prod);
	}
	
	/**
	 * This method travels up the graph to find the first nodes that are recoverable.
	 * The graph may split and merge, and even cycle, so we take care of knowing where
	 * we have been and what we still need to do.
	 */
	private void findRecoveryNodes(AbstractStackNode failer, DoubleArrayList<AbstractStackNode, Object> recoveryNodes) {
		ObjectKeyedIntegerMap<AbstractStackNode> visited = new ObjectKeyedIntegerMap<AbstractStackNode>();
		Stack<AbstractStackNode> todo = new Stack<AbstractStackNode>();
		
		todo.push(failer);
		
		while (!todo.isEmpty()) {
			AbstractStackNode node = todo.pop();
			
			visited.put(node, 0);
			
			IntegerObjectList<EdgesSet> toParents = node.getEdges();
			
			for(int i = toParents.size() - 1; i >= 0; --i){
				EdgesSet edges = toParents.getValue(i);

				if (edges != null) {
					for(int j = edges.size() - 1; j >= 0; --j){
						AbstractStackNode parent = edges.get(j);

						Object parentProd = findProduction(node.getProduction());
						
						if (isRecovering(parentProd)) {
							recoveryNodes.add(node, parentProd);
						}else if (!visited.contains(parent)) {
							todo.push(parent);
						}
					}
				}
			}
		}
	}
	
	// What if we are inside a prefix-shared production?
	private Object findProduction(AbstractStackNode[] prod) {
		AbstractStackNode last = prod[prod.length - 1];
		return last.getParentProduction();
	}
	
	public void reviveStacks(DoubleArrayList<AbstractStackNode, AbstractNode> recoveredNodes,
			int[] input,
			int location,
			Stack<AbstractStackNode> unexpandableNodes,
			Stack<AbstractStackNode> unmatchableLeafNodes,
			DoubleStack<ArrayList<AbstractStackNode>, AbstractStackNode> unmatchableMidProductionNodes,
			DoubleStack<AbstractStackNode, AbstractNode> filteredNodes) {
		ArrayList<AbstractStackNode> failedNodes = new ArrayList<AbstractStackNode>();
		collectUnexpandableNodes(unexpandableNodes, failedNodes);
		//collectFilteredNodes(filteredNodes, failedNodes);
		collectUnmatchableMidProductionNodes(location, unmatchableMidProductionNodes, failedNodes);
		
		reviveFailedNodes(recoveredNodes, input, location, failedNodes);
	}
}
