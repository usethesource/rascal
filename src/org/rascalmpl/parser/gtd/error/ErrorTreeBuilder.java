package org.rascalmpl.parser.gtd.error;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.Stack;

public class ErrorTreeBuilder{
	private final DoubleStack<AbstractStackNode, AbstractNode> errorNodes;
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<String, AbstractContainerNode>> errorResultStoreCache;
	
	public ErrorTreeBuilder(){
		super();
		
		errorNodes = new DoubleStack<AbstractStackNode, AbstractNode>();
		errorResultStoreCache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<String,AbstractContainerNode>>();
	}
	
	private void followEdges(AbstractStackNode node, AbstractNode result){
		
	}
	
	private void moveToNext(AbstractStackNode node, AbstractNode result){
		
	}
	
	private void move(AbstractStackNode node, AbstractNode result){
		if(node.isEndNode()){
			if(!result.isRejected()){
				if(!node.isReject()){
					followEdges(node, result);
				}else{
					// Ignore rejects for now.
				}
			}
		}
		
		if(node.hasNext()){
			moveToNext(node, result);
		}
	}
	
	IConstructor buildErrorTree(Stack<AbstractStackNode> unexpandableNodes, Stack<AbstractStackNode> unmatchableNodes, Stack<AbstractStackNode> filteredNodes){
		while(!unexpandableNodes.isEmpty()){
			AbstractStackNode unexpandableNode = unexpandableNodes.pop();

			// TODO Implement.
		}
		
		while(!unmatchableNodes.isEmpty()){
			AbstractStackNode unmatchableNode = unmatchableNodes.pop();

			// TODO Implement.
		}
		
		while(!filteredNodes.isEmpty()){
			AbstractStackNode filteredNode = filteredNodes.pop();

			// TODO Implement.
		}
		
		while(!errorNodes.isEmpty()){
			AbstractStackNode errorStackNode = errorNodes.peekFirst();
			AbstractNode result = errorNodes.popSecond();
			
			move(errorStackNode, result);
		}
		
		return null; // Temp.
	}
}
