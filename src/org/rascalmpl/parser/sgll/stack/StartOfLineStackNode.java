package org.rascalmpl.parser.sgll.stack;

import java.net.URI;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.StartOfLineNode;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;

public class StartOfLineStackNode extends AbstractStackNode implements IMatchableStackNode, ILocatableStackNode{
	private final static StartOfLineNode result = new StartOfLineNode();
	
	private PositionStore positionStore;
	
	public StartOfLineStackNode(int id, int dot){
		super(id, dot);
	}
	
	private StartOfLineStackNode(StartOfLineStackNode original){
		super(original);
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public void setPositionStore(PositionStore positionStore){
		this.positionStore = positionStore;
	}
	
	public boolean match(URI inputURI, char[] input){
		return positionStore.startsLine(startLocation);
	}
	
	public boolean matchWithoutResult(char[] input, int location){
		return positionStore.startsLine(location);
	}
	
	public AbstractStackNode getCleanCopy(){
		return new StartOfLineStackNode(this);
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
		sb.append('^');
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
}
