package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;
import org.rascalmpl.values.ValueFactoryFactory;

public abstract class AbstractNode{
	protected final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	public AbstractNode(){
		super();
	}
	
	public boolean isContainer(){
		return (this instanceof ContainerNode);
	}
	
	public abstract boolean isEpsilon();
	
	public abstract boolean isRejected();
	
	public abstract void addAlternative(IConstructor production, Link children);
	
	public abstract IValue toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore);
	
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
}
