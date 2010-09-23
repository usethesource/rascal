package org.rascalmpl.parser.sgll.stack;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.AbstractContainerNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;

public final class NonTerminalStackNode extends AbstractStackNode{
	private final String expectIdentifier;
	
	private AbstractContainerNode result;
	
	public NonTerminalStackNode(int id, String expectIdentifier){
		super(id);
		
		this.expectIdentifier = expectIdentifier;
	}
	
	public NonTerminalStackNode(int id, IMatchableStackNode[] followRestrictions, String expectIdentifier){
		super(id, followRestrictions);
		
		this.expectIdentifier = expectIdentifier;
	}
	
	private NonTerminalStackNode(NonTerminalStackNode original){
		super(original);
		
		expectIdentifier = original.expectIdentifier;
	}
	
	private NonTerminalStackNode(NonTerminalStackNode original, ArrayList<Link>[] prefixes){
		super(original, prefixes);
		
		expectIdentifier = original.expectIdentifier;
	}
	
	public String getName(){
		return expectIdentifier;
	}
	
	public void setPositionStore(PositionStore positionStore){
		throw new UnsupportedOperationException();
	}
	
	public boolean match(char[] input){
		throw new UnsupportedOperationException();
	}
	
	public boolean isClean(){
		return (result == null);
	}
	
	public AbstractStackNode getCleanCopy(){
		return new NonTerminalStackNode(this);
	}

	public AbstractStackNode getCleanCopyWithPrefix(){
		return new NonTerminalStackNode(this, prefixesMap);
	}
	
	public void setResultStore(AbstractContainerNode resultStore){
		result = resultStore;
	}
	
	public AbstractContainerNode getResultStore(){
		return result;
	}
	
	public int getLength(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode[] getChildren(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractNode getResult(){
		return result;
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
