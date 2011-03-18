package org.rascalmpl.parser.gtd.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.result.AbstractNode.CycleMark;
import org.rascalmpl.parser.gtd.result.AbstractNode.FilteringTracker;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.gtd.util.specific.PositionStore;

public class ErrorSortBuilder{
	
	private ErrorSortBuilder(){
		super();
	}
	
	public static IConstructor toErrorSortTree(SortContainerNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor){
		// TODO Implement.
		return node.toTree(stack, depth, cycleMark, positionStore, new FilteringTracker(), actionExecutor); // Temp.
	}
}
