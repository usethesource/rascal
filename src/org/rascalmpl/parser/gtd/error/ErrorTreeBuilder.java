package org.rascalmpl.parser.gtd.error;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.Stack;

public class ErrorTreeBuilder{
	
	private ErrorTreeBuilder(){
		super();
	}
	
	static IConstructor buildErrorTree(Stack<AbstractStackNode>[] errorStackStacks){
		Stack<AbstractStackNode> errorNodes = new Stack<AbstractStackNode>();
		IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<String, AbstractContainerNode>> errorResultStoreCache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<String,AbstractContainerNode>>();
		
		for(int i = errorStackStacks.length - 1; i >= 0; --i){
			Stack<AbstractStackNode> errorStackStack = errorStackStacks[i];
			while(!errorStackStack.isEmpty()){
				AbstractStackNode errorStackNode = errorStackStack.pop();
	
				// TODO Implement.
			}
		}
		
		while(!errorNodes.isEmpty()){
			AbstractStackNode errorStackNode = errorNodes.pop();

			// TODO Implement.
		}
		
		return null; // Temp.
	}
}
