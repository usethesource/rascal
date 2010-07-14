package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.IndexedStack;

public class EpsilonNode implements INode{
	private final static String EPSILON_STRING = "empty()";
	
	public EpsilonNode(){
		super();
	}
	
	public void addAlternative(IConstructor production, Link children){
		throw new UnsupportedOperationException();
	}
	
	public boolean isEpsilon(){
		return true;
	}
	
	public boolean isRejected(){
		return false;
	}
	
	public String toString(){
		return EPSILON_STRING;
	}
	
	public IValue toTerm(IndexedStack<INode> stack, int depth){
		throw new UnsupportedOperationException(); // This should never be called.
	}
}
