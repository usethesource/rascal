package org.rascalmpl.parser.sgll.stack;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.CharNode;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;

public final class CharStackNode extends AbstractStackNode implements IMatchableStackNode{
	private final char[][] ranges;
	
	private AbstractNode result;
	
	public CharStackNode(int id, int dot, char[][] ranges){
		super(id, dot);

		this.ranges = ranges;
	}
	
	private CharStackNode(CharStackNode original){
		super(original);
		
		ranges = original.ranges;
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public void setPositionStore(PositionStore positionStore){
		throw new UnsupportedOperationException();
	}
	
	public boolean match(char[] input){
		char next = input[startLocation];
		for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			if(next >= range[0] && next <= range[1]){
				result = new CharNode(next);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean matchWithoutResult(char[] input, int location){
		char next = input[location];
		for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			if(next >= range[0] && next <= range[1]){
				return true;
			}
		}
		
		return false;
	}
	
	public AbstractStackNode getCleanCopy(){
		return new CharStackNode(this);
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
		char[] range = ranges[0];
		sb.append(Character.getNumericValue(range[0]));
		sb.append('-');
		sb.append(Character.getNumericValue(range[1]));
		for(int i = ranges.length - 2; i >= 0; --i){
			sb.append(',');
			range = ranges[i];
			sb.append(Character.getNumericValue(range[0]));
			sb.append('-');
			sb.append(Character.getNumericValue(range[1]));
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
