package org.rascalmpl.parser.sgll.stack;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.EpsilonNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;

public class AtLocationStackNode extends AbstractStackNode implements IReducableStackNode{
	private final static EpsilonNode result = new EpsilonNode();
	
	private final int atLocation;
	
	private boolean isReduced;
	
	public AtLocationStackNode(int id, int location){
		super(id);
		
		this.atLocation = location;
	}
	
	private AtLocationStackNode(AtLocationStackNode original){
		super(original);
		
		atLocation = original.atLocation;
	}
	
	private AtLocationStackNode(AtLocationStackNode original, ArrayList<Link>[] prefixes){
		super(original, prefixes);
		
		atLocation = original.atLocation;
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
		return new AtLocationStackNode(this);
	}

	public AbstractStackNode getCleanCopyWithPrefix(){
		return new AtLocationStackNode(this, prefixesMap);
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
