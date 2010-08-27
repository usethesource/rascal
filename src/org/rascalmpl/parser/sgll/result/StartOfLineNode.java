package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.values.uptr.Factory;

public class StartOfLineNode extends AbstractNode{
	private final static String STARTOFLINE = "start-of-line()";
	private final static IValue result = vf.constructor(Factory.Tree_Appl, vf.constructor(Factory.Production_Regular, vf.constructor(Factory.Symbol_StartOfLine), vf.constructor(Factory.Attributes_NoAttrs)), vf.listWriter().done());
	
	public StartOfLineNode(){
		super();
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
		return STARTOFLINE;
	}
	
	public IValue toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, LocationStore locationStore){
		return result; 
	}
}
