package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;
import org.rascalmpl.values.uptr.Factory;

public class AtColumnNode extends AbstractNode{
	private final static String ATCOLUMN = "at-column()";
	private final IValue result;
	
	public AtColumnNode(int column){
		super();
		
		IValue symbol = vf.constructor(Factory.Symbol_AtColumn, vf.integer(column));
		result = vf.constructor(Factory.Tree_Appl, vf.constructor(Factory.Production_Regular, symbol, vf.constructor(Factory.Attributes_NoAttrs)), vf.listWriter().done());
	}
	
	public void addAlternative(IConstructor production, Link children){
		throw new UnsupportedOperationException();
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	public boolean isRejected(){
		return false;
	}
	
	public String toString(){
		return ATCOLUMN;
	}
	
	public IValue toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore){
		return result; 
	}
}
