package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.LiteralNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.LinearIntegerKeyedMap;

public final class LiteralStackNode extends AbstractStackNode implements IReducableStackNode{
	private final char[] literal;
	
	private final LiteralNode result;
	
	public LiteralStackNode(int id, IConstructor production, char[] literal){
		super(id);
		
		this.literal = literal;
		
		result = new LiteralNode(production, literal);
	}
	
	private LiteralStackNode(LiteralStackNode original){
		super(original);
		
		literal = original.literal;
		
		result = original.result;
	}
	
	private LiteralStackNode(LiteralStackNode original, LinearIntegerKeyedMap<ArrayList<Link>> prefixes){
		super(original, prefixes);
		
		literal = original.literal;
		
		result = original.result;
	}
	
	public String getMethodName(){
		throw new UnsupportedOperationException();
	}
	
	public boolean reduce(char[] input){
		return reduce(input, startLocation);
	}
	
	public boolean reduce(char[] input, int location){
		for(int i = literal.length - 1; i >= 0; i--){
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
	
	public void setResultStore(ContainerNode resultStore){
		throw new UnsupportedOperationException();
	}
	
	public ContainerNode getResultStore(){
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
