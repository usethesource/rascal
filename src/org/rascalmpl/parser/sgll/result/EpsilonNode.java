package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public class EpsilonNode implements INode{
	private final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
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
	
	public String toString(){
		return EPSILON_STRING;
	}
	
	public IValue toTerm(IndexedStack<INode> stack, int depth){
		return vf.constructor(Factory.Symbol_Empty);
	}
}
