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
	
	private void reviveNodes(DoubleArrayList<AbstractStackNode<P>, AbstractNode> recoveredNodes, int[] input, int location, DoubleArrayList<AbstractStackNode<P>, P> recoveryNodes){
		for(int i = recoveryNodes.size() - 1; i >= 0; --i) {
			AbstractStackNode<P> recoveryNode = recoveryNodes.getFirst(i);
			P prod = recoveryNodes.getSecond(i);
			int start = recoveryNode.getStartLocation();
			AbstractStackNode<P> continuer = new RecoveryPointStackNode<P>(recoveryId++, prod, recoveryNode);
			int dot = recoveryNode.getDot();
			
			SkippingStackNode<P> recoverLiteral = (SkippingStackNode<P>) new SkippingStackNode<P>(recoveryId++, dot + 1, continuations[robust.get(prod)], input, location, start, prod);
			recoverLiteral = (SkippingStackNode<P>) recoverLiteral.getCleanCopy(location);
			recoverLiteral.initEdges();
			EdgesSet<P> edges = new EdgesSet<P>(1);
			edges.add(continuer);
			
			recoverLiteral.addEdges(edges, recoverLiteral.getStartLocation());
			
			continuer.setIncomingEdges(edges);
			
			recoveredNodes.add(recoverLiteral, recoverLiteral.getResult());
		}
	}
	
	private void reviveFailedNodes(DoubleArrayList<AbstractStackNode<P>, AbstractNode> recoveredNodes, int[] input, int location, ArrayList<AbstractStackNode<P>> failedNodes) {
		DoubleArrayList<AbstractStackNode<P>, P> recoveryNodes = new DoubleArrayList<AbstractStackNode<P>, P>();
		
		for(int i = failedNodes.size() - 1; i >= 0; --i){
			findRecoveryNodes(failedNodes.get(i), recoveryNodes);
		}
		
		reviveNodes(recoveredNodes, input, location, recoveryNodes);
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

	private boolean isRecovering(Object prod) {
		return robust.contains(prod);
	}
	
	/**
	 * This method travels up the graph to find the first nodes that are recoverable.
	 * The graph may split and merge, and even cycle, so we take care of knowing where
	 * we have been and what we still need to do.
	 */
	private void findRecoveryNodes(AbstractStackNode<P> failer, DoubleArrayList<AbstractStackNode<P>, P> recoveryNodes) {
		ObjectKeyedIntegerMap<AbstractStackNode<P>> visited = new ObjectKeyedIntegerMap<AbstractStackNode<P>>();
		Stack<AbstractStackNode<P>> todo = new Stack<AbstractStackNode<P>>();
		
		todo.push(failer);
		
		while (!todo.isEmpty()) {
			AbstractStackNode<P> node = todo.pop();
			
			visited.put(node, 0);
			
			IntegerObjectList<EdgesSet<P>> toParents = node.getEdges();
			
			for(int i = toParents.size() - 1; i >= 0; --i){
				EdgesSet<P> edges = toParents.getValue(i);

				if (edges != null) {
					for(int j = edges.size() - 1; j >= 0; --j){
						AbstractStackNode<P> parent = edges.get(j);

						P parentProd = findProduction(node.getProduction());
						
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
	private P findProduction(AbstractStackNode<P>[] prod) {
		AbstractStackNode<P> last = prod[prod.length - 1];
		return last.getParentProduction();
	}
	
	public void reviveStacks(DoubleArrayList<AbstractStackNode<P>, AbstractNode> recoveredNodes,
			int[] input,
			int location,
			Stack<AbstractStackNode<P>> unexpandableNodes,
			Stack<AbstractStackNode<P>> unmatchableLeafNodes,
			DoubleStack<DoubleArrayList<AbstractStackNode<P>, AbstractNode>, AbstractStackNode<P>> unmatchableMidProductionNodes,
			DoubleStack<AbstractStackNode<P>, AbstractNode> filteredNodes) {
		ArrayList<AbstractStackNode<P>> failedNodes = new ArrayList<AbstractStackNode<P>>();
		collectUnexpandableNodes(unexpandableNodes, failedNodes);
		//collectFilteredNodes(filteredNodes, failedNodes);
		collectUnmatchableMidProductionNodes(location, unmatchableMidProductionNodes, failedNodes);
		
		reviveFailedNodes(recoveredNodes, input, location, failedNodes);
	}
}
