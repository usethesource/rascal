package org.rascalmpl.parser.sgll.stack;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.StartOfLineNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;

public class StartOfLineStackNode extends AbstractStackNode implements IMatchableStackNode{
	private final static StartOfLineNode result = new StartOfLineNode();
	
	private boolean isReduced;
	
	public StartOfLineStackNode(int id){
		super(id);
	}
	
	private StartOfLineStackNode(StartOfLineStackNode original){
		super(original);
	}
	
	private StartOfLineStackNode(StartOfLineStackNode original, ArrayList<Link>[] prefixes){
		super(original, prefixes);
	}
	
	public int getLevelId(){
		throw new UnsupportedOperationException();
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public boolean match(char[] input){
		isReduced = true;
		// Preceded by 'start of file' || UNIX / Windows (\n and \r\n) || pre-MacOS9 (\r)
		return (startLocation == 0) || (input[startLocation - 1] == '\n') || (input[startLocation - 1] == '\r');
	}
	
	public boolean matchWithoutResult(char[] input, int location){
		// Preceded by 'start of file' || UNIX / Windows (\n and \r\n) || pre-MacOS9 (\r)
		return (location == 0) || (input[location - 1] == '\n') || (input[location - 1] == '\r');
	}
	
	public boolean isClean(){
		return !isReduced;
	}
	
	public AbstractStackNode getCleanCopy(){
		return new StartOfLineStackNode(this);
	}

	public AbstractStackNode getCleanCopyWithPrefix(){
		return new StartOfLineStackNode(this, prefixesMap);
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
		sb.append('^');
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
}
