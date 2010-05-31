package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.EpsilonNode;
import org.rascalmpl.parser.sgll.result.INode;

public final class EpsilonStackNode extends AbstractStackNode implements IReducableStackNode{
	private final static EpsilonNode result = new EpsilonNode();
	
	public EpsilonStackNode(int id){
		super(id);
	}
	
	private EpsilonStackNode(EpsilonStackNode epsilonParseStackNode){
		super(epsilonParseStackNode);
	}
	
	public String getMethodName(){
		throw new UnsupportedOperationException();
	}
	
	public boolean reduce(char[] input){
		return true;
	}
	
	public boolean reduce(char[] input, int location){
		return true;
	}
	
	public AbstractStackNode getCleanCopy(){
		return new EpsilonStackNode(this);
	}
	
	public AbstractStackNode getCleanCopyWithPrefix(){
		EpsilonStackNode epsn = new EpsilonStackNode(this);
		epsn.prefixes = prefixes;
		epsn.prefixStartLocations = prefixStartLocations;
		return epsn;
	}
	
	public int getLength(){
		return 0;
	}
	
	public void mark(){
		throw new UnsupportedOperationException();
	}
	
	public boolean isMarked(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode[] getChildren(){
		throw new UnsupportedOperationException();
	}
	
	public void addResult(IConstructor production, INode[] children){
		throw new UnsupportedOperationException();
	}
	
	public INode getResult(){
		return result;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
}
