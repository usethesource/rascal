package org.rascalmpl.parser.gtd;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.result.error.ErrorListContainerNode;
import org.rascalmpl.parser.gtd.result.error.ErrorSortContainerNode;
import org.rascalmpl.parser.gtd.result.error.ExpectedNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.Stack;
import org.rascalmpl.values.ValueFactoryFactory;

public class ErrorTreeBuilder{
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	private final static IList EMPTY_LIST = VF.list();
	
	private final SGTDBF parser;
	private final char[] input;
	private final int location;
	private final URI inputURI;
	
	private final DoubleStack<AbstractStackNode, AbstractNode> errorNodes;
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<String, AbstractContainerNode>> errorResultStoreCache;
	
	public ErrorTreeBuilder(SGTDBF parser, char[] input, int location, URI inputURI){
		super();
		
		this.parser = parser;
		this.input = input;
		this.location = location;
		this.inputURI = inputURI;
		
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
			
			// TODO Get the right symbol.
			AbstractNode resultStore = new ExpectedNode(null, inputURI, location, location, unexpandableNode.isSeparator(), unexpandableNode.isLayout());
			
			errorNodes.push(unexpandableNode, resultStore);
		}
		
		while(!unmatchableNodes.isEmpty()){
			AbstractStackNode unmatchableNode = unmatchableNodes.pop();
			
			int startLocation = unmatchableNode.getStartLocation();
			
			AbstractNode[] children = new AbstractNode[location - startLocation];
			for(int i = children.length - 1; i >= 0; --i){
				children[i] = new CharNode(input[startLocation - i]);
			}
			
			// TODO Construct proper node.
			
			errorNodes.push(unmatchableNode, null); // TODO Use the proper result store.
		}
		
		while(!filteredNodes.isEmpty()){
			AbstractStackNode filteredNode = filteredNodes.pop();
			
			AbstractNode resultStore = (!filteredNode.isList()) ? new ErrorSortContainerNode(EMPTY_LIST, inputURI, filteredNode.getStartLocation(), location, filteredNode.isSeparator(), filteredNode.isLayout()) : new ErrorListContainerNode(EMPTY_LIST, inputURI, filteredNode.getStartLocation(), location, filteredNode.isSeparator(), filteredNode.isLayout());
			
			// TODO Set children.
			
			errorNodes.push(filteredNode, resultStore);
		}
		
		while(!errorNodes.isEmpty()){
			AbstractStackNode errorStackNode = errorNodes.peekFirst();
			AbstractNode result = errorNodes.popSecond();
			
			move(errorStackNode, result);
		}
		
		return null; // Temp.
	}
}
