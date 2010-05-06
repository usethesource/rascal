package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;

public class EpsilonNode implements INode{
	private final static String EPSILON_STRING = "";
	
	public EpsilonNode(){
		super();
	}
	
	public void addAlternative(IConstructor production, INode[] children){
		throw new UnsupportedOperationException();
	}
	
	public boolean isEpsilon(){
		return true;
	}
	
	public String toString(){
		return EPSILON_STRING;
	}
}
