package org.rascalmpl.parser.gtd.stack;


import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.util.specific.PositionStore;

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
	
	public boolean isEmptyLeafNode(){
		return false;
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
	
	public boolean canBeEmpty(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode getEmptyChild(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractNode getResult(){
		return result;
	}
	
	public static int getNumericCharValue(char character){
		return (character < 128) ? character : Character.getNumericValue(character); // Just ignore the Unicode garbage when possible.
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append('[');
		char[] range = ranges[0];
		sb.append(getNumericCharValue(range[0]));
		sb.append('-');
		sb.append(getNumericCharValue(range[1]));
		for(int i = ranges.length - 2; i >= 0; --i){
			sb.append(',');
			range = ranges[i];
			sb.append(getNumericCharValue(range[0]));
			sb.append('-');
			sb.append(getNumericCharValue(range[1]));
		}
		sb.append(']');
		
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
}
