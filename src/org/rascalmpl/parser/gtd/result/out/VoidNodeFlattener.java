package org.rascalmpl.parser.gtd.result.out;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.util.IndexedStack;

/**
 * A node converter that doesn't do anything.
 * This may be useful for benchmarking and debugging and such.
 */
public class VoidNodeFlattener implements INodeFlattener<AbstractNode, Object>{
	
	public VoidNodeFlattener(){
		super();
	}
	
	/**
	 * Returns the given tree.
	 */
	public AbstractNode convert(INodeConstructorFactory<AbstractNode, Object> nodeConstructorFactory, AbstractNode parseTree, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor<AbstractNode> actionExecutor, Object rootEnvironment){
		return parseTree;
	}
	
	public AbstractNode convert(INodeConstructorFactory<AbstractNode, Object> nodeConstructorFactory, AbstractNode parseTree, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor<AbstractNode> actionExecutor, Object rootEnvironment){
		return parseTree;
	}
	
	/**
	 * Returns the given tree.
	 */
	public AbstractNode convertWithErrors(INodeConstructorFactory<AbstractNode, Object> nodeConstructorFactory, AbstractNode parseTree, PositionStore positionStore, IActionExecutor<AbstractNode> actionExecutor, Object rootEnvironment){
		return parseTree;
	}
}
