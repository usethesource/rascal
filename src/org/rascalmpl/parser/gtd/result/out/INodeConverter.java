package org.rascalmpl.parser.gtd.result.out;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.AbstractNode.FilteringTracker;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;

public interface INodeConverter{
	IConstructor convert(AbstractNode parseTree, PositionStore positionStore, IActionExecutor actionExecutor, Object rootEnvironment, FilteringTracker filteringTracker);
	
	IConstructor convertWithErrors(AbstractNode parseTree, PositionStore positionStore, IActionExecutor actionExecutor, Object rootEnvironment);
}
