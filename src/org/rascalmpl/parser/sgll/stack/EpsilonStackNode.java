package org.rascalmpl.parser.sgll.stack;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.AbstractContainerNode;
import org.rascalmpl.parser.sgll.result.EpsilonNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;

public final class EpsilonStackNode extends AbstractStackNode implements IMatchableStackNode{
	private final static EpsilonNode result = new EpsilonNode();
	
	private boolean isReduced;
	
	public EpsilonStackNode(int id){
		super(id);
	}
	
	private EpsilonStackNode(EpsilonStackNode original){
		super(original);
	}
	
	private EpsilonStackNode(EpsilonStackNode original, ArrayList<Link>[] prefixes){
		super(original, prefixes);
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public void setPositionStore(PositionStore positionStore){
		throw new UnsupportedOperationException();
	}
	
	public boolean match(char[] input){
		isReduced = true;
		return true;
	}
	
	public boolean matchWithoutResult(char[] input, int location){
		return true;
	}
	
	public boolean isClean(){
		return !isReduced;
	}
	
	public AbstractStackNode getCleanCopy(){
		return new EpsilonStackNode(this);
	}

	public AbstractStackNode getCleanCopyWithPrefix(){
		return new EpsilonStackNode(this, prefixesMap);
	}
	
	public void setResultStore(AbstractContainerNode resultStore){
		throw new UnsupportedOperationException();
	}
	
	public AbstractContainerNode getResultStore(){
		throw new UnsupportedOperationException();
	}
	
	public int getLength(){
		return 0;
	}
	
	public AbstractStackNode[] getChildren(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractNode getResult(){
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
