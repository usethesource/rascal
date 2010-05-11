package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;

public class CharNode implements INode{
	private final IConstructor symbol;
	private final char character;
	
	public CharNode(IConstructor symbol, char character){
		super();
		
		this.symbol = symbol;
		this.character = character;
	}
	
	public void addAlternative(IConstructor production, INode[] children){
		throw new UnsupportedOperationException();
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("appl(prod(");
		sb.append(symbol);
		sb.append(')');
		sb.append(',');
		sb.append('[');
		sb.append("char(");
		sb.append(getNumericCharValue(character));
		sb.append(')');
		sb.append(']');
		sb.append(')');
		
		return sb.toString();
	}
	
	public static int getNumericCharValue(char character){
		return (character > 127) ? Character.getNumericValue(character) : ((int) character); // Just ignore the Unicode garbage when possible.
	}
}
