package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.values.uptr.Factory;

public class EndOfLineNode extends AbstractNode{
	private final static String ENDOFLINE = "end-of-line()";
	private final static IValue constantTree = Factory.Tree_Appl.make(vf, Factory.Production_Regular.make(vf, Factory.Symbol_EndOfLine.make(vf), Factory.Attributes_NoAttrs.make(vf)), Factory.Args.make(vf));
	
	public EndOfLineNode(){
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
		return ENDOFLINE;
	}
	
	public IValue toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, LocationStore locationStore){
		return constantTree; 
	}
}
