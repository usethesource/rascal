package org.rascalmpl.parser.sgll.stack;

import java.net.URI;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;

public final class NonTerminalStackNode extends AbstractStackNode{
	private final String expectIdentifier;
	
	public NonTerminalStackNode(int id, int dot, String expectIdentifier){
		super(id, dot);
		
		this.expectIdentifier = expectIdentifier;
	}
	
	public NonTerminalStackNode(int id, int dot, IMatchableStackNode[] followRestrictions, String expectIdentifier){
		super(id, dot, followRestrictions);
		
		this.expectIdentifier = expectIdentifier;
	}
	
	private NonTerminalStackNode(NonTerminalStackNode original){
		super(original);
		
		expectIdentifier = original.expectIdentifier;
	}
	
	public String getName(){
		return expectIdentifier;
	}
	
	public void setPositionStore(PositionStore positionStore){
		throw new UnsupportedOperationException();
	}
	
	public boolean match(URI inputURI, char[] input){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode getCleanCopy(){
		return new NonTerminalStackNode(this);
	}
	
	public int getLength(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode[] getChildren(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractNode getResult(){
		throw new UnsupportedOperationException();
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(expectIdentifier);
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(',');
		sb.append('?');
		sb.append(')');
		
		return sb.toString();
	}
}
