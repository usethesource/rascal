package org.rascalmpl.parser.gtd.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.gtd.util.specific.PositionStore;
import org.rascalmpl.values.uptr.Factory;

public class AtColumnNode extends AbstractNode{
	private final static String ATCOLUMN = "at-column()";
	
	private final IConstructor result;
	
	public AtColumnNode(int column){
		super();
		
		IConstructor symbol = vf.constructor(Factory.Symbol_AtColumn, vf.integer(column));
		result = vf.constructor(Factory.Tree_Appl, vf.constructor(Factory.Production_Regular, symbol, vf.constructor(Factory.Attributes_NoAttrs)), vf.listWriter().done());
	}
	
	public void addAlternative(IConstructor production, Link children){
		throw new UnsupportedOperationException();
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	public boolean isEmpty(){
		return true;
	}
	
	public boolean isSeparator(){
		return false;
	}
	
	public void setRejected(){
		throw new UnsupportedOperationException();
	}
	
	public boolean isRejected(){
		return false;
	}
	
	public String toString(){
		return ATCOLUMN;
	}
	
	public IConstructor toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, boolean buildErrorTree){
		return result; 
	}
}
