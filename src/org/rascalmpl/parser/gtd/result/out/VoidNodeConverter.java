package org.rascalmpl.parser.gtd.result.out;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;

/**
 * A node converter that doesn't do anything.
 * This may be useful for benchmarking and debugging and such.
 */
public class VoidNodeConverter implements INodeConverter{
	
	public VoidNodeConverter(){
		super();
	}
	
	/**
	 * Returns the given tree.
	 */
	public AbstractNode convert(AbstractNode parseTree, PositionStore positionStore, IActionExecutor actionExecutor, Object rootEnvironment, FilteringTracker filteringTracker){
		return parseTree;
	}
	
	/**
	 * Returns the given tree.
	 */
	public AbstractNode convertWithErrors(AbstractNode parseTree, PositionStore positionStore, IActionExecutor actionExecutor, Object rootEnvironment){
		return parseTree;
	}
}
