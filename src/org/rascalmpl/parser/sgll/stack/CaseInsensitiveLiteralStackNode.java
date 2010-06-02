package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.INode;
import org.rascalmpl.parser.sgll.result.LiteralNode;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.IntegerList;

public final class CaseInsensitiveLiteralStackNode extends AbstractStackNode implements IReducableStackNode{
	private final IConstructor symbol;
	private final char[][] ciLiteral;
	
	private LiteralNode result;
	
	public CaseInsensitiveLiteralStackNode(int id, IConstructor symbol, char[] ciLiteral){
		super(id);
		
		this.symbol = symbol;
		
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
	
	private CaseInsensitiveLiteralStackNode(CaseInsensitiveLiteralStackNode original){
		super(original);
		
		symbol = original.symbol;
		ciLiteral = original.ciLiteral;
	}
	
	private CaseInsensitiveLiteralStackNode(CaseInsensitiveLiteralStackNode original, ArrayList<INode[]> prefixes, IntegerList prefixStartLocations){
		super(original, prefixes, prefixStartLocations);
		
		symbol = original.symbol;
		ciLiteral = original.ciLiteral;
	}
	
	public String getMethodName(){
		throw new UnsupportedOperationException();
	}
	
	public boolean reduce(char[] input){
		return reduce(input, startLocation);
	}
	
	public boolean reduce(char[] input, int location){
		int literalLength = ciLiteral.length;
		char[] resultLiteral = new char[literalLength];
		OUTER : for(int i = literalLength - 1; i >= 0; i--){
			char[] ciLiteralPart = ciLiteral[i];
			for(int j = ciLiteralPart.length - 1; j >= 0; j--){
				char character = ciLiteralPart[j];
				if(character == input[location + i]){
					resultLiteral[i] = character;
					continue OUTER;
				}
			}
			return false; // Did not match.
		}
		
		result = new LiteralNode(symbol, resultLiteral);
		return true;
	}
	
	public boolean isClean(){
		return true;
	}
	
	public AbstractStackNode getCleanCopy(){
		return new CaseInsensitiveLiteralStackNode(this);
	}
	
	public AbstractStackNode getCleanCopyWithPrefix(){
		return new CaseInsensitiveLiteralStackNode(this, prefixes, prefixStartLocations);
	}
	
	public void initializeResultStore(){
		// Do nothing.
	}
	
	public int getLength(){
		return ciLiteral.length;
	}
	
	public AbstractStackNode[] getChildren(){
		throw new UnsupportedOperationException();
	}
	
	public void addResult(IConstructor production, INode[] children){
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
