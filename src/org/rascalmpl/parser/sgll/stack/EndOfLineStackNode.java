package org.rascalmpl.parser.sgll.stack;

import java.net.URI;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.EndOfLineNode;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;

public class EndOfLineStackNode extends AbstractStackNode implements IMatchableStackNode, ILocatableStackNode{
	private final static EndOfLineNode result = new EndOfLineNode();
	
	private PositionStore positionStore;
	
	public EndOfLineStackNode(int id, int dot){
		super(id, dot);
	}
	
	private EndOfLineStackNode(EndOfLineStackNode original){
		super(original);
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public void setPositionStore(PositionStore positionStore){
		this.positionStore = positionStore;
	}
	
	public boolean match(URI inputURI, char[] input){
		return positionStore.endsLine(startLocation);
	}
	
	public boolean matchWithoutResult(char[] input, int location){
		return positionStore.endsLine(location);
	}
	
	public AbstractStackNode getCleanCopy(){
		return new EndOfLineStackNode(this);
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
		sb.append('$');
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
}
