package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.AbstractContainerNode;
import org.rascalmpl.parser.sgll.result.LiteralNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;

public final class LiteralStackNode extends AbstractStackNode implements IMatchableStackNode{
	private final char[] literal;
	
	private final LiteralNode result;
	
	public LiteralStackNode(int id, IConstructor production, char[] literal){
		super(id);
		
		this.literal = literal;
		
		result = new LiteralNode(production, literal);
	}
	
	public LiteralStackNode(int id, IConstructor production, IMatchableStackNode[] followRestrictions, char[] literal){
		super(id, followRestrictions);
		
		this.literal = literal;
		
		result = new LiteralNode(production, literal);
	}
	
	private LiteralStackNode(LiteralStackNode original){
		super(original);
		
		literal = original.literal;
		
		result = original.result;
	}
	
	private LiteralStackNode(LiteralStackNode original, ArrayList<Link>[] prefixes){
		super(original, prefixes);
		
		literal = original.literal;
		
		result = original.result;
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public void setPositionStore(PositionStore positionStore){
		throw new UnsupportedOperationException();
	}
	
	public boolean match(char[] input){
		for(int i = literal.length - 1; i >= 0; --i){
			if(literal[i] != input[startLocation + i]) return false; // Did not match.
		}
		return true;
	}
	
	public boolean matchWithoutResult(char[] input, int location){
		for(int i = literal.length - 1; i >= 0; --i){
			if(literal[i] != input[location + i]) return false; // Did not match.
		}
		return true;
	}
	
	public boolean isClean(){
		return true;
	}
	
	public AbstractStackNode getCleanCopy(){
		return new LiteralStackNode(this);
	}

	public AbstractStackNode getCleanCopyWithPrefix(){
		return new LiteralStackNode(this, prefixesMap);
	}
	
	public void setResultStore(AbstractContainerNode resultStore){
		throw new UnsupportedOperationException();
	}
	
	public AbstractContainerNode getResultStore(){
		throw new UnsupportedOperationException();
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
