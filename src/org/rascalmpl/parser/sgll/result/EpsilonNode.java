package org.rascalmpl.parser.sgll.result;

public class EpsilonNode implements INode{
	private final static String EPSILON_STRING = "";
	
	public EpsilonNode(){
		super();
	}
	
	public void addAlternative(INode[] children){
		throw new UnsupportedOperationException();
	}
	
	public boolean isEpsilon(){
		return true;
	}
	
	public String toString(){
		return EPSILON_STRING;
	}
}
