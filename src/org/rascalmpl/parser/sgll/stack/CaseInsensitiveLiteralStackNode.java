package org.rascalmpl.parser.sgll.stack;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.LiteralNode;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;

public final class CaseInsensitiveLiteralStackNode extends AbstractStackNode implements IMatchableStackNode{
	private final IConstructor production;
	private final char[][] ciLiteral;
	
	private LiteralNode result;
	
	public CaseInsensitiveLiteralStackNode(int id, int dot, IConstructor production, char[] ciLiteral){
		super(id, dot);
		
		this.production = production;
		
		this.ciLiteral = fill(ciLiteral);
	}
	
	public CaseInsensitiveLiteralStackNode(int id, int dot, IConstructor production, IMatchableStackNode[] followRestrictions, char[] ciLiteral){
		super(id, dot, followRestrictions);
		
		this.production = production;
		
		this.ciLiteral = fill(ciLiteral);
	}
	
	private char[][] fill(char[] ciLiteral){
		int nrOfCharacters = ciLiteral.length;
		char[][] ciLiteralResult = new char[nrOfCharacters][];
		for(int i = nrOfCharacters - 1; i >= 0; --i){
			char character = ciLiteral[i];
			int type = Character.getType(character);
			if(type == Character.LOWERCASE_LETTER){
				ciLiteralResult[i] = new char[]{character, Character.toUpperCase(character)};
			}else if(type == Character.UPPERCASE_LETTER){
				ciLiteralResult[i] = new char[]{character, Character.toLowerCase(character)};
			}else{
				ciLiteralResult[i] = new char[]{character};
			}
		}
		return ciLiteralResult;
	}
	
	private CaseInsensitiveLiteralStackNode(CaseInsensitiveLiteralStackNode original){
		super(original);
		
		production = original.production;
		ciLiteral = original.ciLiteral;
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public void setPositionStore(PositionStore positionStore){
		throw new UnsupportedOperationException();
	}
	
	public boolean match(URI inputURI, char[] input){
		int literalLength = ciLiteral.length;
		char[] resultLiteral = new char[literalLength];
		OUTER : for(int i = literalLength - 1; i >= 0; --i){
			char[] ciLiteralPart = ciLiteral[i];
			for(int j = ciLiteralPart.length - 1; j >= 0; --j){
				char character = ciLiteralPart[j];
				if(character == input[startLocation + i]){
					resultLiteral[i] = character;
					continue OUTER;
				}
			}
			return false; // Did not match.
		}
		
		result = new LiteralNode(inputURI, startLocation, startLocation + literalLength, production, resultLiteral);
		return true;
	}
	
	public boolean matchWithoutResult(char[] input, int location){
		int literalLength = ciLiteral.length;
		OUTER : for(int i = literalLength - 1; i >= 0; --i){
			char[] ciLiteralPart = ciLiteral[i];
			for(int j = ciLiteralPart.length - 1; j >= 0; --j){
				char character = ciLiteralPart[j];
				if(character == input[location + i]){
					continue OUTER;
				}
			}
			return false; // Did not match.
		}
		return true;
	}
	
	public boolean isClean(){
		return true;
	}
	
	public AbstractStackNode getCleanCopy(){
		return new CaseInsensitiveLiteralStackNode(this);
	}
	
	public int getLength(){
		return ciLiteral.length;
	}
	
	public AbstractStackNode[] getChildren(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractNode getResult(){
		return result;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < ciLiteral.length; ++i){
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
