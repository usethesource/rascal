package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.values.ValueFactoryFactory;

public abstract class AbstractNode{
	protected final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	protected final static char END_LINE_CHAR = '\n';
	protected final static char CARRIAGE_RETURN_CHAR = '\r';
	
	public AbstractNode(){
		super();
	}
	
	public boolean isContainer(){
		return (this instanceof ContainerNode);
	}
	
	public abstract boolean isEpsilon();
	
	public abstract boolean isRejected();
	
	public abstract void addAlternative(IConstructor production, Link children);
	
	public abstract IValue toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, LocationStore locationStore);
	
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
	
	public static class LocationStore{
		public int line;
		public int column;
		
		public boolean mayBeReturn;
		
		public LocationStore(){
			super();
		}
		
		public LocationStore(LocationStore original){
			super();
			
			line = original.line;
			column = original.column;
			mayBeReturn = original.mayBeReturn;
		}
		
		public void updateTo(LocationStore toCopy){
			line = toCopy.line;
			column = toCopy.column;
			mayBeReturn = toCopy.mayBeReturn;
		}
		
		public void hitCharacter(){
			if(mayBeReturn){
				hitEndLine();
			}
			++column;
		}
		
		public void hitCarriageReturn(){
			mayBeReturn = true;
		}
		
		public void hitEndLine(){
			++line;
			column = 0;
			mayBeReturn = false;
		}
	}
}
