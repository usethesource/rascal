package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.AbstractContainerNode;
import org.rascalmpl.parser.sgll.result.LiteralNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;

public class MultiCharacterStackNode extends AbstractStackNode implements IMatchableStackNode{
	private final IConstructor production;
	private final char[][] characters;
	
	private AbstractNode result;
	
	public MultiCharacterStackNode(int id, IConstructor production, char[][] characters){
		super(id);
		
		this.production = production;
		this.characters = characters;
	}
	
	private MultiCharacterStackNode(MultiCharacterStackNode original){
		super(original);
		
		production = original.production;
		characters = original.characters;
	}
	
	private MultiCharacterStackNode(MultiCharacterStackNode original, ArrayList<Link>[] prefixes){
		super(original, prefixes);

		production = original.production;
		characters = original.characters;
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public void setPositionStore(PositionStore positionStore){
		throw new UnsupportedOperationException();
	}
	
	public boolean match(char[] input){
		int nrOfCharacters = characters.length;
		char[] resultArray = new char[nrOfCharacters];
		
		OUTER : for(int i = nrOfCharacters - 1; i >= 0; --i){
			char next = input[startLocation + i];
			
			char[] alternatives = characters[i];
			for(int j = alternatives.length - 1; j >= 0; --j){
				char alternative = alternatives[j];
				if(next == alternative){
					resultArray[i] = alternative;
					continue OUTER;
				}
			}
			return false;
		}
		
		result = new LiteralNode(production, resultArray);
		
		return true;
	}
	
	public boolean matchWithoutResult(char[] input, int location){
		int nrOfCharacters = characters.length;
		OUTER : for(int i = nrOfCharacters - 1; i >= 0; --i){
			char next = input[location + i];
			
			char[] alternatives = characters[i];
			for(int j = alternatives.length - 1; j >= 0; --j){
				char alternative = alternatives[j];
				if(next == alternative){
					continue OUTER;
				}
			}
			return false;
		}
		
		return true;
	}
	
	public boolean isClean(){
		return true;
	}
	
	public AbstractStackNode getCleanCopy(){
		return new MultiCharacterStackNode(this);
	}

	public AbstractStackNode getCleanCopyWithPrefix(){
		return new MultiCharacterStackNode(this, prefixesMap);
	}
	
	public void setResultStore(AbstractContainerNode resultStore){
		throw new UnsupportedOperationException();
	}
	
	public AbstractContainerNode getResultStore(){
		throw new UnsupportedOperationException();
	}
	
	public int getLength(){
		return 1;
	}
	
	public AbstractStackNode[] getChildren(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractNode getResult(){
		return result;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append('[');
		for(int i = characters.length - 1; i >= 0; --i){
			char[] range = characters[i];
			sb.append(range[0]);
			sb.append('-');
			sb.append(range[1]);
		}
		sb.append(']');
		
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(',');
		sb.append(startLocation + getLength());
		sb.append(')');
		
		return sb.toString();
	}
}
