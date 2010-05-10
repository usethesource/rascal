package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.CharNode;
import org.rascalmpl.parser.sgll.result.INode;

public final class CharStackNode extends StackNode{
	private final char[][] ranges;
	private final char[] characters;
	
	private final IConstructor symbol;
	
	private INode result;
	
	public CharStackNode(int id, IConstructor symbol, char[][] ranges, char[] characters){
		super(id);
		
		this.symbol = symbol;

		this.ranges = ranges;
		this.characters = characters;
	}
	
	private CharStackNode(CharStackNode charParseStackNode){
		super(charParseStackNode);
		
		symbol = charParseStackNode.symbol;
		
		ranges = charParseStackNode.ranges;
		characters = charParseStackNode.characters;
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
		if(input.length > startLocation){
			char next = input[startLocation];
			for(int i = ranges.length - 1; i >= 0; i--){
				char[] range = ranges[i];
				if(next >= range[0] && next <= range[1]){
					result = new CharNode(symbol, next);
					return true;
				}
			}
			
			for(int i = characters.length - 1; i >= 0; i--){
				if(next == characters[i]){
					result = new CharNode(symbol, next);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public StackNode getCleanCopy(){
		return new CharStackNode(this);
	}
	
	public StackNode getCleanCopyWithPrefix(){
		CharStackNode cpsn = new CharStackNode(this);
		cpsn.prefixes = prefixes;
		cpsn.prefixStartLocations = prefixStartLocations;
		return cpsn;
	}
	
	public int getLength(){
		return 1;
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
		sb.append(symbol);
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(',');
		sb.append(startLocation + getLength());
		sb.append(')');
		
		return sb.toString();
	}
}
