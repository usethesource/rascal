package org.rascalmpl.parser.sgll.stack;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.EndOfLineNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;

public class EndOfLineStackNode extends AbstractStackNode implements IReducableStackNode{
	private final static EndOfLineNode result = new EndOfLineNode();
	
	private boolean isReduced;
	
	public EndOfLineStackNode(int id){
		super(id);
	}
	
	private EndOfLineStackNode(EndOfLineStackNode original){
		super(original);
	}
	
	private EndOfLineStackNode(EndOfLineStackNode original, ArrayList<Link>[] prefixes){
		super(original, prefixes);
	}
	
	public int getLevelId(){
		throw new UnsupportedOperationException();
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public boolean reduce(char[] input){
		isReduced = true;
		// Follow by 'end of file' || Windows or pre-MacOS9 (\r\n, \r) || UNIX (\n)
		return (startLocation == input.length) || (input[startLocation] == '\r') || (input[startLocation] == '\n');
	}
	
	public boolean reduceWithoutResult(char[] input, int location){
		// Follow by 'end of file' || Windows or pre-MacOS9 (\r\n, \r) || UNIX (\n)
		return (location == input.length) || (input[location + 1] == '\r') || (input[location + 1] == '\n');
	}
	
	public boolean isClean(){
		return !isReduced;
	}
	
	public AbstractStackNode getCleanCopy(){
		return new EndOfLineStackNode(this);
	}

	public AbstractStackNode getCleanCopyWithPrefix(){
		return new EndOfLineStackNode(this, prefixesMap);
	}
	
	public void setResultStore(ContainerNode resultStore){
		throw new UnsupportedOperationException();
	}
	
	public ContainerNode getResultStore(){
		throw new UnsupportedOperationException();
	}
	
	public int getLength(){
		return 0;
	}
	
	public AbstractStackNode[] getChildren(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractNode getResult(){
		return result;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append('$');
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
}
