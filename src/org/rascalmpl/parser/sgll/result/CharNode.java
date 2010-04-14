package org.rascalmpl.parser.sgll.result;

public class CharNode implements INode{
	private final String production;
	private final char character;
	
	public CharNode(String production, char character){
		super();
		
		this.production = production;
		this.character = character;
	}
	
	public void addAlternative(INode[] children){
		throw new UnsupportedOperationException();
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("appl(prod(");
		sb.append(production);
		sb.append(')');
		sb.append(',');
		sb.append('[');
		sb.append("char(");
		sb.append(character);
		sb.append(')');
		sb.append(']');
		sb.append(')');
		
		return sb.toString();
	}
}
