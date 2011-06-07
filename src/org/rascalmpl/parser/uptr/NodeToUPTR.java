package org.rascalmpl.parser.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.result.ListContainerNode;
import org.rascalmpl.parser.gtd.result.LiteralNode;
import org.rascalmpl.parser.gtd.result.SortContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode.CycleMark;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.error.ErrorListContainerNode;
import org.rascalmpl.parser.gtd.result.error.ErrorSortContainerNode;
import org.rascalmpl.parser.gtd.result.error.ExpectedNode;
import org.rascalmpl.parser.gtd.result.out.FilteringTracker;
import org.rascalmpl.parser.gtd.result.out.INodeConverter;
import org.rascalmpl.parser.gtd.util.IndexedStack;

public class NodeToUPTR implements INodeConverter{
	private final LiteralNodeConverter literalNodeConverter;
	private final SortContainerNodeConverter sortContainerNodeConverter;
	private final ListContainerNodeConverter listContainerNodeConverter;
	
	public NodeToUPTR(){
		super();
		
		literalNodeConverter = new LiteralNodeConverter();
		sortContainerNodeConverter = new SortContainerNodeConverter();
		listContainerNodeConverter = new ListContainerNodeConverter();
	}
	
	protected static class IsInError{
		public boolean inError;
	}
	
	protected IConstructor convert(AbstractNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		switch(node.getID()){
			case CharNode.ID:
				return CharNodeConverter.convertToUPTR((CharNode) node);
			case LiteralNode.ID:
				return literalNodeConverter.convertToUPTR((LiteralNode) node);
			case SortContainerNode.ID:
				return sortContainerNodeConverter.convertToUPTR(this, (SortContainerNode) node, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			case ListContainerNode.ID:
				return listContainerNodeConverter.convertToUPTR(this, (ListContainerNode) node, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			default:
				throw new RuntimeException("Incorrect result node id: "+node.getID());
		}
	}
	
	protected IConstructor convertWithErrors(AbstractNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor, Object environment){
		switch(node.getID()){
			case CharNode.ID:
				return CharNodeConverter.convertToUPTR((CharNode) node);
			case LiteralNode.ID:
				return literalNodeConverter.convertToUPTR((LiteralNode) node);
			case SortContainerNode.ID:
				return SortContainerNodeInErrorConverter.convertToUPTR(this, (SortContainerNode) node, stack, depth, cycleMark, positionStore, actionExecutor, environment);
			case ListContainerNode.ID:
				return ListContainerNodeInErrorConverter.convertToUPTR(this, (ListContainerNode) node, stack, depth, cycleMark, positionStore, actionExecutor, environment);
			case ErrorSortContainerNode.ID:
				return ErrorSortContainerNodeConverter.convertToUPTR(this, (ErrorSortContainerNode) node, stack, depth, cycleMark, positionStore, actionExecutor, environment);
			case ErrorListContainerNode.ID:
				return ErrorListContainerNodeConverter.convertToUPTR(this, (ErrorListContainerNode) node, stack, depth, cycleMark, positionStore, actionExecutor, environment);
			case ExpectedNode.ID:
				return ExpectedNodeConverter.convertToUPTR(this, (ExpectedNode) node, stack, depth, cycleMark, positionStore, actionExecutor, environment);
			default:
				throw new RuntimeException("Incorrect result node id: "+node.getID());
		}
	}
	
	public IConstructor convert(AbstractNode parseTree, PositionStore positionStore, IActionExecutor actionExecutor, Object rootEnvironment, FilteringTracker filteringTracker){
		return convert(parseTree, new IndexedStack<AbstractNode>(), 0, new CycleMark(), positionStore, filteringTracker, actionExecutor, rootEnvironment);
	}
	
	public IConstructor convertWithErrors(AbstractNode parseTree, PositionStore positionStore, IActionExecutor actionExecutor, Object rootEnvironment){
		return convertWithErrors(parseTree, new IndexedStack<AbstractNode>(), 0, new CycleMark(), positionStore, actionExecutor, rootEnvironment);
	}
}
