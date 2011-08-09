package org.rascalmpl.parser.gtd.result.out;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;

/**
 * Node converters are intended for the conversion of default binarized parse
 * trees to some other representation.
 */
public interface INodeConverter{
	/**
	 * Convert the given parse tree.
	 */
	Object convert(AbstractNode parseTree, PositionStore positionStore, IActionExecutor actionExecutor, Object rootEnvironment, FilteringTracker filteringTracker);
	
	/**
	 * Convert the given parse tree, taking case of potential errors in the
	 * result. These errors can be either the result of post-parse filtering or
	 * due to the tree being incomplete because of a parse error.
	 */
	Object convertWithErrors(AbstractNode parseTree, PositionStore positionStore, IActionExecutor actionExecutor, Object rootEnvironment);
}
