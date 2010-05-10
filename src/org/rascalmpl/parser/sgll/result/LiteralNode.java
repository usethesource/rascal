package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;

public class LiteralNode implements INode{
	private final IConstructor production;
	private final char[] content;
	
	public LiteralNode(IConstructor production, char[] content){
		super();
		
		this.production = production;
		this.content = content;
	}
	
	public void addAlternative(IConstructor production, INode[] children){
		throw new UnsupportedOperationException();
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	private void printCharacter(int character, StringBuilder sb){
		sb.append("appl(prod(char-class([single(");
		sb.append(character);
		sb.append(')');
		sb.append(']');
		sb.append(')');
		sb.append(',');
		sb.append('[');
		sb.append("char(");
		sb.append(character);
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
		printCharacter(Character.getNumericValue(content[0]), sb);
		for(int i = 1; i < content.length; i++){
			sb.append(',');
			printCharacter(Character.getNumericValue(content[i]), sb);
		}
		sb.append(']');
		sb.append(')');
		
		return sb.toString();
	}
}
