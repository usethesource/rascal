package org.rascalmpl.parser.sgll.stack;

import java.net.URI;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.AtColumnNode;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;

public class AtColumnStackNode extends AbstractStackNode implements IMatchableStackNode, ILocatableStackNode{
	private final AtColumnNode result;
	
	private final int column;
	
	private PositionStore positionStore;
	
	public AtColumnStackNode(int id, int dot, int column){
		super(id, dot);
		
		this.result = new AtColumnNode(column);
		this.column = column;
	}
	
	private AtColumnStackNode(AtColumnStackNode original){
		super(original);
		
		column = original.column;
		result = original.result;
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public void setPositionStore(PositionStore positionStore){
		this.positionStore = positionStore;
	}
	
	public boolean match(URI inputURI, char[] input){
		return positionStore.isAtColumn(startLocation, column);
	}
	
	public boolean matchWithoutResult(char[] input, int location){
		return positionStore.isAtColumn(location, column);
	}
	
	public AbstractStackNode getCleanCopy(){
		return new AtColumnStackNode(this);
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
