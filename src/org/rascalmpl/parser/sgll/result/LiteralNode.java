package org.rascalmpl.parser.sgll.result;

public class LiteralNode implements INode{
	private final String production;
	private final char[] content;
	
	public LiteralNode(String production, char[] content){
		super();
		
		this.production = production;
		this.content = content;
	}
	
	public void addAlternative(INode[] children){
		throw new UnsupportedOperationException();
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	private void printCharacter(char character, StringBuilder sb){
		sb.append("appl(prod(char-class([single(");
		sb.append(Character.getNumericValue(character));
		sb.append(')');
		sb.append(']');
		sb.append(')');
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("appl(prod(");
		sb.append(production);
		sb.append(')');
		sb.append(',');
		sb.append('[');
		for(int i = 0; i < content.length; i++){
			printCharacter(content[i], sb);
		}
		sb.append(']');
		sb.append(')');
		
		return sb.toString();
	}
}
