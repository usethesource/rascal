package org.rascalmpl.parser.sgll.stack;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.AtColumnNode;
import org.rascalmpl.parser.sgll.result.AbstractContainerNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;

public class AtColumnStackNode extends AbstractStackNode implements IMatchableStackNode, ILocatableStackNode{
	private final AtColumnNode result;
	
	private final int column;
	
	private PositionStore positionStore;
	
	private boolean isReduced;
	
	public AtColumnStackNode(int id, int column){
		super(id);
		
		this.result = new AtColumnNode(column);
		this.column = column;
	}
	
	private AtColumnStackNode(AtColumnStackNode original){
		super(original);
		
		column = original.column;
		result = original.result;
	}
	
	private AtColumnStackNode(AtColumnStackNode original, ArrayList<Link>[] prefixes){
		super(original, prefixes);
		
		column = original.column;
		result = original.result;
	}
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public void setPositionStore(PositionStore positionStore){
		this.positionStore = positionStore;
	}
	
	public boolean match(char[] input){
		if(positionStore.isAtColumn(startLocation, column)){
			isReduced = true;
			return true;
		}
		return false;
	}
	
	public boolean matchWithoutResult(char[] input, int location){
		if(positionStore.isAtColumn(location, column)){
			return true;
		}
		return false;
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
	
	public void setResultStore(AbstractContainerNode resultStore){
		throw new UnsupportedOperationException();
	}
	
	public AbstractContainerNode getResultStore(){
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
