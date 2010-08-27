package org.rascalmpl.parser.sgll.stack;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.AtColumnNode;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;

public class AtColumnStackNode extends AbstractStackNode implements IMatchableStackNode{
	private final AtColumnNode result;
	
	private final int atColumn;
	
	private boolean isReduced;
	
	public AtColumnStackNode(int id, int column){
		super(id);
		
		this.result = new AtColumnNode(column);
		this.atColumn = column;
	}
	
	private AtColumnStackNode(AtColumnStackNode original){
		super(original);
		
		atColumn = original.atColumn;
		result = original.result;
	}
	
	private AtColumnStackNode(AtColumnStackNode original, ArrayList<Link>[] prefixes){
		super(original, prefixes);
		
		atColumn = original.atColumn;
		result = original.result;
	}
	
	public int getLevelId(){
		throw new UnsupportedOperationException();
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	// TODO Fix this to use columns instead of locations.
	public boolean match(char[] input){
		isReduced = true;
		return (atColumn == startLocation);
	}
	
	public boolean matchWithoutResult(char[] input, int location){
		return (atColumn == location);
	}
	
	public boolean isClean(){
		return !isReduced;
	}
	
	public AbstractStackNode getCleanCopy(){
		return new AtColumnStackNode(this);
	}

	public AbstractStackNode getCleanCopyWithPrefix(){
		return new AtColumnStackNode(this, prefixesMap);
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
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
}
