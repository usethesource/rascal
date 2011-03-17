package org.rascalmpl.parser.gtd.result.error;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.gtd.util.specific.PositionStore;
import org.rascalmpl.values.uptr.Factory;

public class ExpectedNode extends AbstractNode{
	private final AbstractNode[] mismatchedChildren;
	private final IConstructor symbol;
	
	private final URI input;
	private final int offset;
	private final int endOffset;
	
	private final boolean isSeparator;
	private final boolean isLayout;
	
	private IConstructor cachedResult;
	
	public ExpectedNode(AbstractNode[] mismatchedChildren, IConstructor symbol, URI input, int offset, int endOffset, boolean isSeparator, boolean isLayout){
		super();
		
		this.mismatchedChildren = mismatchedChildren;
		this.symbol = symbol;
		
		this.input = input;
		this.offset = offset;
		this.endOffset = endOffset;
		
		this.isSeparator = isSeparator;
		this.isLayout = isLayout;
	}
	
	public void addAlternative(IConstructor production, Link children){
		throw new UnsupportedOperationException();
	}
	
	public boolean isEmpty(){
		return false;
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	public boolean isRejected(){
		return false;
	}
	
	public boolean isSeparator(){
		return isSeparator;
	}
	
	public void setRejected(){
		throw new UnsupportedOperationException();
	}
	
	public IConstructor toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, boolean buildErrorTree){
		if(cachedResult != null) return cachedResult;
		
		IListWriter childrenListWriter = vf.listWriter(Factory.Tree);
		for(int i = mismatchedChildren.length - 1; i >= 0; --i){
			childrenListWriter.insert(mismatchedChildren[i].toTerm(stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, buildErrorTree));
		}
		
		IConstructor result = vf.constructor(Factory.Tree_Expected, symbol, childrenListWriter.done());
		if(!(isLayout || input == null)){
			int beginLine = positionStore.findLine(offset);
			int endLine = positionStore.findLine(endOffset);
			result = result.setAnnotation(Factory.Location, vf.sourceLocation(input, offset, endOffset - offset, beginLine + 1, endLine + 1, positionStore.getColumn(offset, beginLine), positionStore.getColumn(endOffset, endLine)));
		}
		
		return (cachedResult = result);
	}
}
