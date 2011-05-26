package org.rascalmpl.parser.gtd.result.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.result.ListContainerNode;
import org.rascalmpl.parser.gtd.result.LiteralNode;
import org.rascalmpl.parser.gtd.result.SortContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode.CycleMark;
import org.rascalmpl.parser.gtd.result.AbstractNode.FilteringTracker;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.action.IEnvironment;
import org.rascalmpl.parser.gtd.result.error.ErrorListContainerNode;
import org.rascalmpl.parser.gtd.result.error.ErrorSortContainerNode;
import org.rascalmpl.parser.gtd.result.error.ExpectedNode;
import org.rascalmpl.parser.gtd.util.IndexedStack;

// TODO Add 'isInError' parameter passing.
public class NodeToUPTR{
	private final AbstractNode parseTree;
	private final PositionStore positionStore;
	
	private final LiteralNodeConverter literalNodeConverter;
	private final SortContainerNodeConverter sortContainerNodeConverter;
	private final ListContainerNodeConverter listContainerNodeConverter;
	
	public NodeToUPTR(AbstractNode parseTree, PositionStore positionStore){
		super();
		
		this.parseTree = parseTree;
		this.positionStore = positionStore;
		
		literalNodeConverter = new LiteralNodeConverter();
		sortContainerNodeConverter = new SortContainerNodeConverter();
		listContainerNodeConverter = new ListContainerNodeConverter();
	}
	
	protected static class IsInError{
		public boolean inError;
	}
	
	protected IConstructor convert(AbstractNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, IEnvironment environment){
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
	
	protected IConstructor convertWithErrors(AbstractNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor, IEnvironment environment){
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
	
	public IConstructor convertToUPTR(FilteringTracker filteringTracker, IActionExecutor actionExecutor, IEnvironment rootEnvironment){
		return convert(parseTree, new IndexedStack<AbstractNode>(), 0, new CycleMark(), positionStore, filteringTracker, actionExecutor, rootEnvironment);
	}
	
	public IConstructor convertToUPTRWithErrors(IActionExecutor actionExecutor, IEnvironment rootEnvironment){
		return convertWithErrors(parseTree, new IndexedStack<AbstractNode>(), 0, new CycleMark(), positionStore, actionExecutor, rootEnvironment);
	}
}
