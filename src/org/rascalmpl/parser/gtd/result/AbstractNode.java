package org.rascalmpl.parser.gtd.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.gtd.util.specific.PositionStore;
import org.rascalmpl.values.ValueFactoryFactory;

public abstract class AbstractNode{
	protected final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	protected final static String POSITION_ANNNOTATION_LABEL = "loc";
	
	public AbstractNode(){
		super();
	}
	
	public abstract void addAlternative(IConstructor production, Link children);
	
	public boolean isContainer(){
		return (this instanceof SortContainerNode);
	}
	
	public abstract boolean isEpsilon();
	
	public abstract boolean isEmpty();
	
	public abstract boolean isSeparator();
	
	public abstract void setRejected();
	
	public abstract boolean isRejected();
	
	public abstract IConstructor toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, boolean buildErrorTree);
	
	public static class CycleMark{
		public int depth = Integer.MAX_VALUE;
		
		public CycleMark(){
			super();
		}
		
		public void setMark(int depth){
			if(depth < this.depth){
				this.depth = depth;
			}
		}
		
		public void reset(){
			depth = Integer.MAX_VALUE;
		}
	}
	
	public static class FilteringTracker{
		public int offset;
		public int endOffset;
		
		public FilteringTracker(){
			super();
		}
		
		public void setLastFilered(int offset, int endOffset){
			this.offset = offset;
			this.endOffset = endOffset;
		}
	}
}
