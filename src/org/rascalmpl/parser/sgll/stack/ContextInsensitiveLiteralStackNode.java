package org.rascalmpl.parser.sgll.stack;

import org.rascalmpl.parser.sgll.result.INode;
import org.rascalmpl.parser.sgll.result.LiteralNode;

public final class ContextInsensitiveLiteralStackNode extends StackNode{
	private final String production;
	private final char[][] ciLiteral;
	
	private LiteralNode result;
	
	public ContextInsensitiveLiteralStackNode(int id, String production, char[] ciLiteral){
		super(id);
		
		this.production = production;
		
		int nrOfCharacters = ciLiteral.length;
		this.ciLiteral = new char[nrOfCharacters][];
		for(int i = nrOfCharacters - 1; i >= 0; i--){
			char character = ciLiteral[i];
			int type = Character.getType(character);
			if(type == Character.LOWERCASE_LETTER){
				this.ciLiteral[i] = new char[]{character, Character.toUpperCase(character)};
			}else if(type == Character.UPPERCASE_LETTER){
				this.ciLiteral[i] = new char[]{character, Character.toLowerCase(character)};
			}else{
				this.ciLiteral[i] = new char[]{character};
			}
		}
	}
	
	private ContextInsensitiveLiteralStackNode(ContextInsensitiveLiteralStackNode contextInsensitiveLiteralParseStackNode){
		super(contextInsensitiveLiteralParseStackNode);
		
		production = contextInsensitiveLiteralParseStackNode.production;
		ciLiteral = contextInsensitiveLiteralParseStackNode.ciLiteral;
		
		result = null;
	}
	
	public boolean isReducable(){
		return true;
	}
	
	public boolean isList(){
		return false;
	}
	
	public String getMethodName(){
		throw new UnsupportedOperationException();
	}
	
	public boolean reduce(char[] input){
		int literalLength = ciLiteral.length;
		char[] resultLiteral = new char[literalLength];
		OUTER : for(int i = literalLength - 1; i >= 0; i--){
			char[] ciLiteralPart = ciLiteral[i];
			for(int j = ciLiteralPart.length - 1; j >= 0; j--){
				char character = ciLiteralPart[j];
				if(character == input[startLocation + i]){
					resultLiteral[i] = character;
					continue OUTER;
				}
			}
			return false; // Did not match.
		}
		
		result = new LiteralNode(production, resultLiteral);
		return true;
	}
	
	public StackNode getCleanCopy(){
		return new ContextInsensitiveLiteralStackNode(this);
	}
	
	public StackNode getCleanCopyWithPrefix(){
		ContextInsensitiveLiteralStackNode cilpsn = new ContextInsensitiveLiteralStackNode(this);
		cilpsn.prefixes = prefixes;
		cilpsn.prefixStartLocations = prefixStartLocations;
		return cilpsn;
	}
	
	public int getLength(){
		return ciLiteral.length;
	}
	
	public void mark(){
		throw new UnsupportedOperationException();
	}
	
	public boolean isMarked(){
		throw new UnsupportedOperationException();
	}
	
	public StackNode[] getChildren(){
		throw new UnsupportedOperationException();
	}
	
	public void addResult(INode[] children){
		throw new UnsupportedOperationException();
	}
	
	public INode getResult(){
		return result;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < ciLiteral.length; i++){
			sb.append(ciLiteral[i][0]);
		}
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(',');
		sb.append(startLocation + getLength());
		sb.append(')');
		
		return sb.toString();
	}
}
