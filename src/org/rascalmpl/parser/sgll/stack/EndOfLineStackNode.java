package org.rascalmpl.parser.sgll.stack;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.AbstractContainerNode;
import org.rascalmpl.parser.sgll.result.EndOfLineNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;

public class EndOfLineStackNode extends AbstractStackNode implements IMatchableStackNode, ILocatableStackNode{
	private final static EndOfLineNode result = new EndOfLineNode();
	
	private PositionStore positionStore;
	
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
	
	public String getName(){
		throw new UnsupportedOperationException();
	}
	
	public void setPositionStore(PositionStore positionStore){
		this.positionStore = positionStore;
	}
	
	public boolean match(char[] input){
		if(positionStore.endsLine(startLocation)){
			isReduced = true;
			return true;
		}
		return false;
	}
	
	public boolean matchWithoutResult(char[] input, int location){
		if(positionStore.endsLine(location)){
			return true;
		}
		return false;
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
		sb.append('$');
		sb.append('(');
		sb.append(startLocation);
		sb.append(')');
		
		return sb.toString();
	}
}
