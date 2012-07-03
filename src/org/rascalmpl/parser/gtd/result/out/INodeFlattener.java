package org.rascalmpl.parser.gtd.result.out;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.util.IndexedStack;

/**
 * Node converters are intended for the conversion of default binarized parse
 * trees to some other representation.
 */
public interface INodeFlattener<T, P>{
	/**
	 * Convert the given parse result.
	 */
	T convert(INodeConstructorFactory<T, P> nodeConstructorFactory, AbstractNode parseTree, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object rootEnvironment);
	
	T convert(INodeConstructorFactory<T, P> nodeConstructorFactory, AbstractNode parseTree, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object rootEnvironment);
	
	/**
	 * Internal helper structure for cycle detection and handling.
	 */
	static class CycleMark{
		public int depth = Integer.MAX_VALUE;
		
		public CycleMark(){
			super();
		}
		
		/**
		 * Marks the depth at which a cycle was detected.
		 */
		public void setMark(int depth){
			if(depth < this.depth){
				this.depth = depth;
			}
		}
		
		/**
		 * Resets the mark.
		 */
		public void reset(){
			depth = Integer.MAX_VALUE;
		}
	}
}
