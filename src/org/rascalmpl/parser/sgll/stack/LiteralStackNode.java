package org.rascalmpl.parser.sgll.stack;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.LiteralNode;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;

public final class LiteralStackNode extends AbstractStackNode implements IMatchableStackNode{
	private final char[] literal;
	private final IConstructor production;
	
	private LiteralNode result;
	
	public LiteralStackNode(int id, int dot, IConstructor production, char[] literal){
		super(id, dot);
		
		this.literal = literal;
		this.production = production;
	}
	
	public LiteralStackNode(int id, int dot, IConstructor production, IMatchableStackNode[] followRestrictions, char[] literal){
		super(id, dot, followRestrictions);
		
		this.literal = literal;
		this.production = production;
	}
	
	private LiteralStackNode(LiteralStackNode original){
		super(original);
		
		literal = original.literal;
		production = original.production;
		
		result = original.result;
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public void setPositionStore(PositionStore positionStore){
		throw new UnsupportedOperationException();
	}
	
	public boolean match(URI inputURI, char[] input){
		for(int i = literal.length - 1; i >= 0; --i){
			if(literal[i] != input[startLocation + i]) return false; // Did not match.
		}
		
		result = new LiteralNode(inputURI, startLocation, startLocation + literal.length, production, literal);
		
		return true;
	}
	
	public boolean matchWithoutResult(char[] input, int location){
		for(int i = literal.length - 1; i >= 0; --i){
			if(literal[i] != input[location + i]) return false; // Did not match.
		}
		return true;
	}
	
	public AbstractStackNode getCleanCopy(){
		return new LiteralStackNode(this);
	}
	
	public int getLength(){
		return literal.length;
	}
	
	public AbstractStackNode[] getChildren(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractNode getResult(){
		return result;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(new String(literal));
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(',');
		sb.append(startLocation + getLength());
		sb.append(')');
		
		return sb.toString();
	}
}
