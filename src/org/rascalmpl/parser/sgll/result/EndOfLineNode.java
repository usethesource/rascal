package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.IActionExecutor;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;
import org.rascalmpl.values.uptr.Factory;

public class EndOfLineNode extends AbstractNode{
	private final static String ENDOFLINE = "end-of-line()";
	private final static IConstructor result = vf.constructor(Factory.Tree_Appl, vf.constructor(Factory.Production_Regular, vf.constructor(Factory.Symbol_EndOfLine), vf.constructor(Factory.Attributes_NoAttrs)), vf.listWriter().done());
	
	public EndOfLineNode(){
		super();
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
		return ENDOFLINE;
	}
	
	public IConstructor toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor){
		return result; 
	}
}
