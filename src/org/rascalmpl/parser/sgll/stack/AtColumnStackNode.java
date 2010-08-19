package org.rascalmpl.parser.sgll.stack;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.AtColumnNode;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;

public class AtColumnStackNode extends AbstractStackNode implements IReducableStackNode{
	private final AtColumnNode result ;
	
	private final int atLocation;
	
	private boolean isReduced;
	
	public AtColumnStackNode(int id, int column){
		super(id);
		this.result = new AtColumnNode(column);
		this.atLocation = column;
	}
	
	private AtColumnStackNode(AtColumnStackNode original){
		super(original);
		
		atLocation = original.atLocation;
		result = original.result;
	}
	
	private AtColumnStackNode(AtColumnStackNode original, ArrayList<Link>[] prefixes){
		super(original, prefixes);
		
		atLocation = original.atLocation;
		result = original.result;
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public boolean reduce(char[] input){
		isReduced = true;
		return (atLocation == startLocation);
	}
	
	public boolean reduceWithoutResult(char[] input, int location){
		return (atLocation == location);
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
